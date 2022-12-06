package cn.youai.xiuzhen.stat.task;

import cn.youai.server.event.IServerEvent;
import cn.youai.server.task.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-31.
 */
@Slf4j
@Component
public class GameTask implements IServerEvent {

    private final Map<String, ITask> taskMap = new ConcurrentHashMap<>();

    // 获取任务
    @SuppressWarnings("rawtypes")
    public ITask getTask(Class requiredType) {
        return taskMap.get(requiredType.getCanonicalName());
    }

    // 获取任务
    public void addTask(ITask task) {
        taskMap.put(task.getClass().getCanonicalName(), task);
    }

    @Override
    public void onServerReady() {
        try {
            addTask(new GameStopServerRefundWork());

            for (ITask task : taskMap.values()) {
                try {
                    long time = System.currentTimeMillis();
                    task.setup();
                    log.info("setup GameTask class:{}, cost:{}ms", task.getClass().getSimpleName(), (System.currentTimeMillis() - time));
                } catch (Exception e) {
                    log.error("setup GameTask error, class:" + task.getClass().getSimpleName(), e);
                }
            }
        } catch (Exception e) {
            log.error("GameTask onServerReady error", e);
        }
    }
}
