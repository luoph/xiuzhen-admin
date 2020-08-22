package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameChannelServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏渠道服配置")
@RequestMapping("/game/gameChannelServer")
public class GameChannelServerController extends JeecgController<GameChannelServer, IGameChannelServerService> {

    @Autowired
    private IGameChannelServerService gameChannelServerService;

    /**
     * 分页列表查询
     *
     * @param gameChannelServer 数据实体
     * @param pageNo            页码
     * @param pageSize          分页大小
     * @param req               请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-列表查询")
    @ApiOperation(value = "游戏渠道服配置-列表查询", notes = "游戏渠道服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannelServer gameChannelServer,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameChannelServer> queryWrapper = QueryGenerator.initQueryWrapper(gameChannelServer, req.getParameterMap());
        Page<GameChannelServer> page = new Page<>(pageNo, pageSize);
        IPage<GameChannelServer> pageList = gameChannelServerService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameChannelServer 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-添加")
    @ApiOperation(value = "游戏渠道服配置-添加", notes = "游戏渠道服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannelServer gameChannelServer) {
        gameChannelServerService.save(gameChannelServer);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameChannelServer 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-编辑")
    @ApiOperation(value = "游戏渠道服配置-编辑", notes = "游戏渠道服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannelServer gameChannelServer) {
        gameChannelServerService.updateById(gameChannelServer);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-通过id删除")
    @ApiOperation(value = "游戏渠道服配置-通过id删除", notes = "游戏渠道服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameChannelServerService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-批量删除")
    @ApiOperation(value = "游戏渠道服配置-批量删除", notes = "游戏渠道服配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameChannelServerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-通过id查询")
    @ApiOperation(value = "游戏渠道服配置-通过id查询", notes = "游戏渠道服配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameChannelServer gameChannelServer = gameChannelServerService.getById(id);
        return Result.ok(gameChannelServer);
    }

    /**
     * 导出excel
     *
     * @param request           请求
     * @param gameChannelServer 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannelServer gameChannelServer) {
        return super.exportXls(request, gameChannelServer, GameChannelServer.class, "游戏渠道服配置");
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
        return super.importExcel(request, response, GameChannelServer.class);
    }

    /**
     * 通过渠道id 查询关联的服务器
     *
     * @param channelId 渠道id
     * @return
     */
    @RequestMapping(value = "channelWithServer")
    public Result<?> channelWithServer(@RequestParam(name = "channelId") Integer channelId) {
        List<GameServer> gameServers = gameChannelServerService.gameServerByChannelId(channelId);
        return Result.ok(gameServers);
    }
}
