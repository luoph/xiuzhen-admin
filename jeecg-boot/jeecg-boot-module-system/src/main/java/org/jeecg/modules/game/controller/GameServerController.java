package org.jeecg.modules.game.controller;

import cn.youai.commons.model.DataResponse;
import cn.youai.commons.model.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Map;

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

    private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
    }.getType();

    @Autowired
    private IGameServerService gameServerService;

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
     *
     * @param gameServer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "游戏服配置-列表查询")
    @ApiOperation(value = "游戏服配置-列表查询", notes = "游戏服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameServer gameServer,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameServer> queryWrapper = QueryGenerator.initQueryWrapper(gameServer, req.getParameterMap());
        Page<GameServer> page = new Page<>(pageNo, pageSize);
        IPage<GameServer> pageList = gameServerService.page(page, queryWrapper);
        for (GameServer record : pageList.getRecords()) {
            // 增加在线人数统计
            if (record.getOnlineStat() == 1) {
                DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(record.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
                if (response != null) {
                    record.setOnlineNum(response.getData());
                }
            }
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameServer
     * @return
     */
    @AutoLog(value = "游戏服配置-添加")
    @ApiOperation(value = "游戏服配置-添加", notes = "游戏服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameServer gameServer) {
        gameServerService.save(gameServer);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameServer
     * @return
     */
    @AutoLog(value = "游戏服配置-编辑")
    @ApiOperation(value = "游戏服配置-编辑", notes = "游戏服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameServer gameServer) {
        gameServerService.updateById(gameServer);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "游戏服配置-通过id删除")
    @ApiOperation(value = "游戏服配置-通过id删除", notes = "游戏服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameServerService.removeById(id);
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
        Map<Integer, Response> responseMap = gameServerService.gameServerGet(ids, updateActivityUrl);
        log.info("updateActivity response:{}", responseMap);
        return Result.ok("刷新活动配置成功！");
    }

    @AutoLog(value = "游戏服配置-刷新游戏配置")
    @ApiOperation(value = "游戏服配置-刷新游戏配置", notes = "游戏服配置-刷新游戏配置")
    @GetMapping(value = "/updateSetting")
    public Result<?> updateSetting(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = gameServerService.gameServerGet(ids, updateSettingUrl);
        log.info("updateSetting response:{}", responseMap);
        return Result.ok("刷新游戏配置成功！");
    }

    @AutoLog(value = "游戏服配置-查询在线人数")
    @ApiOperation(value = "游戏服配置-查询在线人数", notes = "游戏服配置-查询在线人数")
    @GetMapping(value = "/getOnlineNum")
    public Result<?> getOnlineNum(@RequestParam(name = "id") String id) {
        GameServer gameServer = gameServerService.getById(id);
        if (gameServer != null) {
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
        Map<Integer, Response> responseMap = gameServerService.gameServerGet(ids, startMaintainUrl);
        log.info("startMaintain response:{}", responseMap);
        return Result.ok("开启维护状态成功！");
    }

    @AutoLog(value = "游戏服配置-关闭维护状态")
    @ApiOperation(value = "游戏服配置-关闭维护状态", notes = "游戏服配置-关闭维护状态")
    @RequiresPermissions("game:server:admin")
    @GetMapping(value = "/stopMaintain")
    public Result<?> stopMaintain(@RequestParam(name = "ids") String ids) {
        Map<Integer, Response> responseMap = gameServerService.gameServerGet(ids, stopMaintainUrl);
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
        GameServer gameServer = gameServerService.getById(id);
        return Result.ok(gameServer);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameServer
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameServer gameServer) {
        return super.exportXls(request, gameServer, GameServer.class, "游戏服配置");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameServer.class);
    }

}
