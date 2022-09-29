package cn.youai.xiuzhen.game.controller;

import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏渠道")
@RequestMapping("/game/gameChannel")
public class GameChannelController extends JeecgController<GameChannel, IGameChannelService> {

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @Value("${app.url.chat-server}")
    private String chatServerUrl;

    @AutoLog(value = "游戏渠道-列表查询")
    @ApiOperation(value = "游戏渠道-列表查询", notes = "游戏渠道-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannel entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏渠道-添加")
    @ApiOperation(value = "游戏渠道-添加", notes = "游戏渠道-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannel entity) {
        return super.add(entity);
    }

    @AutoLog(value = "游戏渠道-编辑")
    @ApiOperation(value = "游戏渠道-编辑", notes = "游戏渠道-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannel entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "游戏渠道-通过id删除")
    @ApiOperation(value = "游戏渠道-通过id删除", notes = "游戏渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏渠道-批量删除")
    @ApiOperation(value = "游戏渠道-批量删除", notes = "游戏渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏渠道-通过id查询")
    @ApiOperation(value = "游戏渠道-通过id查询", notes = "游戏渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannel entity) {
        return super.exportXls(request, entity, GameChannel.class, "游戏渠道");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameChannel.class);
    }


    @AutoLog(value = "游戏渠道-刷新所有渠道区服配置")
    @ApiOperation(value = "游戏渠道-刷新所有渠道区服配置", notes = "游戏渠道-刷新所有渠道区服配置")
    @GetMapping(value = "/updateAllServer")
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
    @ApiOperation(value = "游戏渠道-刷新指定渠道区服配置", notes = "游戏渠道-刷新指定渠道区服配置")
    @GetMapping(value = "/updateChannelServer")
    public Result<?> updateChannelServer(@RequestParam(name = "id") String id) {
        reloadServerCache();
        service.updateChannelConfig(Integer.valueOf(id));
        return Result.ok("刷新成功");
    }

    @GetMapping(value = "/updateIpWhitelist")
    public Result<?> updateIpWhitelist(HttpServletRequest req) {
        try {
            service.updateIpWhiteListConfig();
        } catch (Exception e) {
            log.error("updateIpWhitelist error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }

    @GetMapping(value = "/updateServerCache")
    public Result<?> updateServerCache(HttpServletRequest req) {
        reloadServerCache();
        return Result.ok("刷新成功");
    }

    @GetMapping(value = "/updateChatServerCache")
    public Result<?> updateChatServerCache(HttpServletRequest req) {
        OkHttpHelper.get(chatServerUrl + "/gm/cleanCache?className=CrossChatMessageCache");
        return Result.ok("刷新成功");
    }

    private void reloadServerCache() {
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadServer");
        OkHttpHelper.get(gameCenterUrl + "/gm/reloadChannel");
        OkHttpHelper.get(chatServerUrl + "/gm/reloadServerGroup");
    }

}
