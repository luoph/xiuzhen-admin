package cn.youai.xiuzhen.game.controller;

import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.core.database.DataSourceHelper;
import cn.youai.xiuzhen.core.database.MongoDataSourceHelper;
import cn.youai.xiuzhen.game.cache.GameServerCache;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.$;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏渠道")
@RequestMapping("/game/channel")
public class GameChannelController extends JeecgController<GameChannel, IGameChannelService> {

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @Value("${app.url.chat-server}")
    private String chatServerUrl;

    @Autowired
    private IGameServerService gameServerService;

    @AutoLog(value = "游戏渠道-列表查询")
    @GetMapping(value = "/list")
    @PermissionData(value = "game/GameChannelList")
    public Result<?> queryPageList(GameChannel entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏渠道-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("game:channel:admin")
    public Result<?> add(@RequestBody GameChannel entity) {
        return super.add(entity.sortIpWhitelist());
    }

    @AutoLog(value = "游戏渠道-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("game:channel:admin")
    public Result<?> edit(@RequestBody GameChannel entity) {
        return super.edit(entity.sortIpWhitelist());
    }

    @AutoLog(value = "游戏渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("game:channel:admin")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @RequiresPermissions("game:channel:admin")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏渠道-导出")
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("game:channel:admin")
    public ModelAndView exportXls(HttpServletRequest request, GameChannel entity) {
        return super.exportXls(request, entity, GameChannel.class, "游戏渠道");
    }

    @AutoLog(value = "游戏渠道-导入")
    @RequiresPermissions("game:channel:admin")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameChannel.class);
    }

    @AutoLog(value = "游戏渠道-全部渠道")
    @GetMapping(value = "/allChannel")
    @PermissionData(value = "game/GameChannelList")
    public Result<?> allChannel() {
        QueryWrapper<GameChannel> query = new QueryWrapper<>();
        query.select("id", "name", "simple_name", "notice_id");
        QueryGenerator.installAuthMplus(query, GameChannel.class);
        return Result.ok(service.list(query));
    }

    @AutoLog(value = "游戏渠道-全部区服")
    @GetMapping(value = "/allServer")
    @PermissionData(value = "game/GameChannelList")
    public Result<?> allServer() {
        String configAuth = QueryGenerator.installAuthJdbc(GameChannel.class);
        List<GameServer> gameServers = gameServerService.selectChannelServerList(configAuth);
        List<GameServerVO> list = $.copy(gameServers, GameServerVO.class);
        return Result.ok(list);
    }

    @AutoLog(value = "游戏渠道-刷新所有渠道区服配置")
    @GetMapping(value = "/updateAllServer")
    @RequiresPermissions("game:channel:admin")
    public Result<?> updateAllServer(HttpServletRequest req) {
        try {
            reloadServerCache();
            service.updateAllChannelConfig();
        } catch (Exception e) {
            log.error("updateAllServer error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }

    @AutoLog(value = "游戏渠道-刷新指定渠道区服配置")
    @GetMapping(value = "/updateChannelServer")
    public Result<?> updateChannelServer(@RequestParam(name = "id") String id) {
        reloadServerCache();
        service.updateChannelConfig(Integer.parseInt(id));
        return Result.ok("刷新成功");
    }

    @AutoLog(value = "游戏渠道-刷新IP白名单")
    @GetMapping(value = "/updateIpWhitelist")
    @RequiresPermissions("game:channel:admin")
    public Result<?> updateIpWhitelist(HttpServletRequest req) {
        try {
            service.updateIpWhiteListConfig();
        } catch (Exception e) {
            log.error("updateIpWhitelist error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }

    @AutoLog(value = "游戏渠道-刷新游戏区服缓存")
    @GetMapping(value = "/updateServerCache")
    @RequiresPermissions("game:channel:admin")
    public Result<?> updateServerCache(HttpServletRequest req) {
        reloadServerCache();
        return Result.ok("刷新成功");
    }

    @AutoLog(value = "游戏渠道-刷新聊天服缓存")
    @GetMapping(value = "/updateChatServerCache")
    @RequiresPermissions("game:channel:admin")
    public Result<?> updateChatServerCache(HttpServletRequest req) {
        OkHttpHelper.get(chatServerUrl + "/gm/cleanCache?className=CrossChatMessageCache");
        return Result.ok("刷新成功");
    }

    private void reloadServerCache() {
        MongoDataSourceHelper.getInstance().reload();
        DataSourceHelper.getInstance().reload();
        GameServerCache.getInstance().clear();
        GameServerCache.getInstance().loadAll();
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadServer");
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadChannel");
        OkHttpHelper.get(chatServerUrl + "/gm/reloadServerGroup");
    }

}
