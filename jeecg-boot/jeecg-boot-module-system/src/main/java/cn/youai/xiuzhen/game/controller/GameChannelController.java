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

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-列表查询")
    @ApiOperation(value = "游戏渠道-列表查询", notes = "游戏渠道-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannel entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-添加")
    @ApiOperation(value = "游戏渠道-添加", notes = "游戏渠道-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannel entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-编辑")
    @ApiOperation(value = "游戏渠道-编辑", notes = "游戏渠道-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannel entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-通过id删除")
    @ApiOperation(value = "游戏渠道-通过id删除", notes = "游戏渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-批量删除")
    @ApiOperation(value = "游戏渠道-批量删除", notes = "游戏渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-通过id查询")
    @ApiOperation(value = "游戏渠道-通过id查询", notes = "游戏渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannel entity) {
        return super.exportXls(request, entity, GameChannel.class, "游戏渠道");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
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
