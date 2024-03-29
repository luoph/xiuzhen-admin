package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.enums.OutdatedType;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.cache.GameServerCache;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerTag;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameServerTagService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Slf4j
@Api(tags = "游戏服配置")
@RestController
@RequestMapping("/game/gameServer")
public class GameServerController extends JeecgController<GameServer, IGameServerService> {

    @Value("${app.server.online-stat:true}")
    private boolean onlineStat;

    @Autowired
    private IGameChannelService channelService;

    @Autowired
    private IGameServerTagService serverTagService;

    @Value("${app.update-activity-url:/activity/update}")
    private String updateActivityUrl;

    @Value("${app.update-setting-url:/setting/update}")
    private String updateSettingUrl;

    @Value("${app.sync-player-url:/player/sync}")
    private String syncPlayerUrl;

    @Value("${app.start-maintain-url:/game/startMaintain}")
    private String startMaintainUrl;

    @Value("${app.stop-maintain-url:/game/stopMaintain}")
    private String stopMaintainUrl;

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "游戏服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameServer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        List<GameServerTag> serverTags = serverTagService.selectTags();
        Map<Integer, GameServerTag> tagMap = serverTags.stream().collect(Collectors.toMap(GameServerTag::getId, Function.identity(), (key1, key2) -> key2));
        IPage<GameServer> pageList = pageList(entity, pageNo, pageSize, req);
        updateOnlineNum(pageList.getRecords(), tagMap);
        return Result.ok(pageList);
    }

    @AutoLog(value = "游戏服配置-全部区服")
    @GetMapping(value = "/all")
    public Result<?> all() {
        Wrapper<GameServer> query = Wrappers.<GameServer>lambdaQuery()
                .eq(GameServer::getOutdated, OutdatedType.NORMAL.getValue())
                .select(GameServer::getId, GameServer::getName, GameServer::getGameId,
                        GameServer::getHost, GameServer::getStatus, GameServer::getOpenTime,
                        GameServer::getOnlineTime)
                .orderByAsc(GameServer::getId);
        return Result.ok(service.list(query));
    }

    @AutoLog(value = "游戏服配置-待合服列表查询")
    @GetMapping(value = "/mergeServerList")
    public Result<?> mergeServerList(@RequestParam(name = "days", defaultValue = "5") Integer days,
                                     @RequestParam(name = "minAvgPlayers", defaultValue = "50") Integer minAvgPlayers,
                                     @RequestParam(name = "minAvgPayAmount", defaultValue = "200") Double minAvgPayAmount) {
        return Result.ok(service.getMergeServerList(days, minAvgPlayers, minAvgPayAmount));
    }

    @AutoLog(value = "游戏服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameServer entity) {
        service.save(entity);
        GameServerCache.getInstance().reload(entity.getId());
        return Result.ok("添加成功！");
    }

    @SuppressWarnings("DuplicatedCode")
    @AutoLog(value = "游戏服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameServer entity) {
        Date onlineDate = DateUtils.dateOnly(entity.getOnlineTime());
        Date openDate = DateUtils.dateOnly(entity.getOpenTime());
        if (onlineDate.before(openDate)) {
            return Result.error("上线时间不可早于开服日期!");
        }

        service.applyChange(entity);
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadServer");
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "游戏服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        service.removeById(id);
        GameServerCache.getInstance().remove(Integer.parseInt(id));
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadServer");
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "游戏服配置-刷新活动配置")
    @GetMapping(value = "/updateActivity")
    public Result<?> updateActivity(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = service.getUrl(ids, updateActivityUrl);
        log.info("updateActivity response:{}", responseMap);
        return Result.ok("刷新活动配置成功！");
    }

    @AutoLog(value = "游戏服配置-刷新游戏配置")
    @GetMapping(value = "/updateSetting")
    public Result<?> updateSetting(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = service.getUrl(ids, updateSettingUrl);
        log.info("updateSetting response:{}", responseMap);
        return Result.ok("刷新游戏配置成功！");
    }

    @AutoLog(value = "游戏服配置-同步玩家数据")
    @GetMapping(value = "/syncPlayer")
    @RequiresPermissions("game:server:admin")
    public Result<?> syncPlayer(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = service.getUrl(ids, syncPlayerUrl);
        log.info("syncPlayer response:{}", responseMap);
        return Result.ok("同步玩家数据成功！");
    }

    @AutoLog(value = "游戏服配置-查询在线人数")
    @GetMapping(value = "/getOnlineNum")
    public Result<?> getOnlineNum(@RequestParam(name = "id") String id) {
        GameServer gameServer = service.getById(id);
        if (gameServer != null) {
            if (gameServer.getOutdated() != OutdatedType.NORMAL.getValue()) {
                return Result.ok(String.valueOf(0));
            }

            service.updateOnlineNum(CollUtil.newArrayList(gameServer));
            return Result.ok(String.valueOf(gameServer.getOnlineNum()));
        }
        return Result.ok('-');
    }

    @AutoLog(value = "游戏服配置-开启维护状态")
    @GetMapping(value = "/startMaintain")
    @RequiresPermissions("game:server:admin")
    public Result<?> startMaintain(@RequestParam(name = "ids") String ids) {
        List<Integer> serverIds = StringUtils.split2Int(ids);
        updateServerMaintain(serverIds, 1, startMaintainUrl);
        return Result.ok("开启维护状态成功！");
    }

    @AutoLog(value = "游戏服配置-关闭维护状态")
    @RequiresPermissions("game:server:admin")
    @GetMapping(value = "/stopMaintain")
    public Result<?> stopMaintain(@RequestParam(name = "ids") String ids) {
        List<Integer> serverIds = StringUtils.split2Int(ids);
        updateServerMaintain(serverIds, 0, stopMaintainUrl);
        return Result.ok("关闭维护状态成功！");
    }

    @AutoLog(value = "游戏服配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameServer gameServer = service.getById(id);
        return Result.ok(gameServer);
    }

    @AutoLog(value = "游戏服配置-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameServer gameServer) {
        return super.exportXls(request, gameServer, GameServer.class, "游戏服配置");
    }

    @AutoLog(value = "游戏服配置-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameServer.class);
    }

    private void updateOnlineNum(Collection<GameServer> servers, Map<Integer, GameServerTag> tagMap) {
        if (onlineStat) {
            service.updateOnlineNum(servers);
        }

        // 设置标签
        for (GameServer record : servers) {
            if (record.getTagId() != null) {
                GameServerTag serverTag = tagMap.get(record.getTagId());
                if (serverTag != null) {
                    record.setTag(serverTag.getName());
                }
            }
        }
    }

    private void updateServerMaintain(List<Integer> serverIds, int maintain, String url) {
        List<GameServer> allServers = new ArrayList<>();
        allServers.addAll(service.selectGameServerList(serverIds));
        allServers.addAll(service.selectGameServerByPid(serverIds));

        Map<Integer, GameServer> serverMap = allServers.stream().collect(Collectors.toMap(GameServer::getId, Function.identity(), (key1, key2) -> key2));
        service.updateGameServerMaintain(serverMap.values(), maintain);
        channelService.updateAllChannelConfig();
        GameServerCache.getInstance().loadAll();

        Map<Integer, Response> responseMap = service.getUrl(serverMap.keySet(), url);
        log.info("updateServerMaintain serverIds:{}, url:{}, response:{}", serverIds, url, responseMap);
    }

}
