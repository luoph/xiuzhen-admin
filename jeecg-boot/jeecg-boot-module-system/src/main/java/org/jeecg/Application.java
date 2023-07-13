package org.jeecg;

import cn.youai.server.component.ConfigManager;
import cn.youai.server.event.IServerEvent;
import cn.youai.server.task.TaskExecutor;
import cn.youai.xiuzhen.core.config.GameConfig;
import cn.youai.xiuzhen.game.cache.CacheServiceManager;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.CustomTypeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Slf4j
@EnableAsync
@EnableCaching
@EnableScheduling
@AutoConfigurationPackage
@ServletComponentScan("cn.youai.xiuzhen.filter")
@ComponentScan(basePackages = {"org.jeecg", "cn.youai.server", "cn.youai.xiuzhen"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomTypeFilter.class}))
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, JetCacheAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        // System.setProperty("spring.devtools.restart.enabled", "true");
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = oConvertUtils.getString(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n\t" + "Application Jeecg-Boot is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:" + port + path + "/\n\t" + "External: \thttp://" + ip + ":" + port + path + "/\n\t" + "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" + "----------------------------------------------------------");

    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        // 加载缓存，设置父子依赖关系
        GameConfig.init();
        // 加载配置
        ConfigManager.getInstance().load();
        // 加载缓存
        CacheServiceManager.loadAll();
        // init task
        TaskExecutor.getInstance().start();
        // init IServerEvent
        onServerReady();
    }

    private void onServerReady() {
        Map<String, IServerEvent> beansList = SpringContextUtils.getApplicationContext().getBeansOfType(IServerEvent.class);
        for (IServerEvent controller : beansList.values()) {
            long time = System.currentTimeMillis();
            controller.onServerReady();
            log.info("[GameServer] notify onServerReady:{}, cost:{}ms", controller.getClass().getSimpleName(), (System.currentTimeMillis() - time));
        }
    }
}