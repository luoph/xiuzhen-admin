package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DataResponse;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerTag;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameServerTagService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
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

    private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
    }.getType();

    @Autowired
    private IGameChannelService channelService;

    @Autowired
    private IGameServerTagService serverTagService;

    @Value("${app.update-activity-url:/activity/update}")
    private String updateActivityUrl;

    @Value("${app.update-setting-url:/setting/update}")
    private String updateSettingUrl;

    @Value("${app.start-maintain-url:/game/startMaintain}")
    private String startMaintainUrl;

    @Value("${app.stop-maintain-url:/game/stopMaintain}")
    private String stopMaintainUrl;

    @Value("${app.online-num-url:/game/onlineNum}")
    private String onlineNumUrl;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "游戏服配置-列表查询")
    @ApiOperation(value = "游戏服配置-列表查询", notes = "游戏服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameServer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        List<GameServerTag> serverTags = serverTagService.selectTags();
        Map<Integer, GameServerTag> tagMap = serverTags.stream().collect(Collectors.toMap(GameServerTag::getId, Function.identity(), (key1, key2) -> key2));
        IPage<GameServer> pageList = pageList(entity, pageNo, pageSize, req);
        for (GameServer record : pageList.getRecords()) {
            // 设置标签
            if (record.getTagId() != null) {
                GameServerTag serverTag = tagMap.get(record.getTagId());
                if (serverTag != null) {
                    record.setTag(serverTag.getName());
                }
            }

            // 已废弃服务器不统计在线人数
            if (!onlineStat || record.getOutdated() == 1) {
                record.setOnlineNum(0);
            } else if (record.getOnlineStat() == 1) {
                DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(record.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
                if (response != null) {
                    record.setOnlineNum(response.getData());
                }
            }
        }
        return Result.ok(pageList);
    }

    @AutoLog(value = "游戏服配置-列表查询")
    @ApiOperation(value = "游戏服配置-列表查询", notes = "游戏服配置-列表查询")
    @GetMapping(value = "/all")
    public Result<?> all() {
        Wrapper<GameServer> query = Wrappers.<GameServer>lambdaQuery()
                .select(GameServer::getId, GameServer::getName, GameServer::getGameId,
                        GameServer::getHost, GameServer::getStatus, GameServer::getOpenTime,
                        GameServer::getOnlineTime)
                .orderByAsc(GameServer::getId);
        return Result.ok(service.list(query));
    }

    @AutoLog(value = "游戏服配置-待合服列表查询")
    @ApiOperation(value = "游戏服配置-待合服列表查询", notes = "游戏服配置-待合服列表查询")
    @GetMapping(value = "/mergeServerList")
    public Result<?> mergeServerList(@RequestParam(name = "days", defaultValue = "5") Integer days,
                                     @RequestParam(name = "minAvgPlayers", defaultValue = "50") Integer minAvgPlayers,
                                     @RequestParam(name = "minAvgPayAmount", defaultValue = "200") Double minAvgPayAmount) {
        return Result.ok(service.getMergeServerList(days, minAvgPlayers, minAvgPayAmount));
    }

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "游戏服配置-添加")
    @ApiOperation(value = "游戏服配置-添加", notes = "游戏服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameServer entity) {
        service.save(entity);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "游戏服配置-编辑")
    @ApiOperation(value = "游戏服配置-编辑", notes = "游戏服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameServer entity) {
        service.updateById(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "游戏服配置-通过id删除")
    @ApiOperation(value = "游戏服配置-通过id删除", notes = "游戏服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        service.removeById(id);
        return Result.ok("删除成功!");
    }

//    /**
//     * 批量删除
//     *
//     * @param ids
//     * @return
//     */
//    @AutoLog(value = "游戏服配置-批量删除")
//    @ApiOperation(value = "游戏服配置-批量删除", notes = "游戏服配置-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
//        this.gameServerService.removeByIds(Arrays.asList(ids.split(",")));
//        return Result.ok("批量删除成功！");
//    }

    @AutoLog(value = "游戏服配置-刷新活动配置")
    @ApiOperation(value = "游戏服配置-刷新活动配置", notes = "游戏服配置-刷新活动配置")
    @GetMapping(value = "/updateActivity")
    public Result<?> updateActivity(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = service.gameServerGet(ids, updateActivityUrl);
        log.info("updateActivity response:{}", responseMap);
        return Result.ok("刷新活动配置成功！");
    }

    @AutoLog(value = "游戏服配置-刷新游戏配置")
    @ApiOperation(value = "游戏服配置-刷新游戏配置", notes = "游戏服配置-刷新游戏配置")
    @GetMapping(value = "/updateSetting")
    public Result<?> updateSetting(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = service.gameServerGet(ids, updateSettingUrl);
        log.info("updateSetting response:{}", responseMap);
        return Result.ok("刷新游戏配置成功！");
    }

    @AutoLog(value = "游戏服配置-查询在线人数")
    @ApiOperation(value = "游戏服配置-查询在线人数", notes = "游戏服配置-查询在线人数")
    @GetMapping(value = "/getOnlineNum")
    public Result<?> getOnlineNum(@RequestParam(name = "id") String id) {
        GameServer gameServer = service.getById(id);
        if (gameServer != null) {
            if (gameServer.getOutdated() == 1) {
                return Result.ok(String.valueOf(0));
            }

            DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
            return Result.ok(String.valueOf(response.getData()));
        }
        return Result.ok('-');
    }

    @AutoLog(value = "游戏服配置-开启维护状态")
    @ApiOperation(value = "游戏服配置-开启维护状态", notes = "游戏服配置-开启维护状态")
    @GetMapping(value = "/startMaintain")
    @RequiresPermissions("game:server:admin")
    public Result<?> startMaintain(@RequestParam(name = "ids") String ids) {
        List<Integer> serverIds = StringUtils.split2Int(ids);
        if (CollUtil.isNotEmpty(serverIds)) {
            service.updateGameServerMaintain(serverIds, 1);
        }
        channelService.updateAllChannelConfig();

        Map<Integer, Response> responseMap = service.gameServerGet(ids, startMaintainUrl);
        log.info("startMaintain response:{}", responseMap);
        return Result.ok("开启维护状态成功！");
    }

    @AutoLog(value = "游戏服配置-关闭维护状态")
    @ApiOperation(value = "游戏服配置-关闭维护状态", notes = "游戏服配置-关闭维护状态")
    @RequiresPermissions("game:server:admin")
    @GetMapping(value = "/stopMaintain")
    public Result<?> stopMaintain(@RequestParam(name = "ids") String ids) {
        List<Integer> serverIds = StringUtils.split2Int(ids);
        if (CollUtil.isNotEmpty(serverIds)) {
            service.updateGameServerMaintain(serverIds, 0);
        }
        channelService.updateAllChannelConfig();

        Map<Integer, Response> responseMap = service.gameServerGet(ids, stopMaintainUrl);
        log.info("stopMaintain response:{}", responseMap);
        return Result.ok("关闭维护状态成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "游戏服配置-通过id查询")
    @ApiOperation(value = "游戏服配置-通过id查询", notes = "游戏服配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameServer gameServer = service.getById(id);
        return Result.ok(gameServer);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameServer gameServer) {
        return super.exportXls(request, gameServer, GameServer.class, "游戏服配置");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameServer.class);
    }

}
