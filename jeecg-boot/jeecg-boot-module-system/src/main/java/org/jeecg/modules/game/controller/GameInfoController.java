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
import org.jeecg.modules.game.entity.GameInfo;
import org.jeecg.modules.game.service.IGameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Slf4j
@Api(tags = "游戏信息")
@RestController
@RequestMapping("/game/gameInfo")
public class GameInfoController extends JeecgController<GameInfo, IGameInfoService> {

    @Autowired
    private IGameInfoService gameInfoService;

    /**
     * 分页列表查询
     *
     * @param gameInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "游戏信息-列表查询")
    @ApiOperation(value = "游戏信息-列表查询", notes = "游戏信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameInfo gameInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameInfo> queryWrapper = QueryGenerator.initQueryWrapper(gameInfo, req.getParameterMap());
        Page<GameInfo> page = new Page<>(pageNo, pageSize);
        IPage<GameInfo> pageList = gameInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameInfo
     * @return
     */
    @AutoLog(value = "游戏信息-添加")
    @ApiOperation(value = "游戏信息-添加", notes = "游戏信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameInfo gameInfo) {
        gameInfoService.save(gameInfo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameInfo
     * @return
     */
    @AutoLog(value = "游戏信息-编辑")
    @ApiOperation(value = "游戏信息-编辑", notes = "游戏信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameInfo gameInfo) {
        gameInfoService.updateById(gameInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "游戏信息-通过id删除")
    @ApiOperation(value = "游戏信息-通过id删除", notes = "游戏信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gameInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "游戏信息-批量删除")
    @ApiOperation(value = "游戏信息-批量删除", notes = "游戏信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "游戏信息-通过id查询")
    @ApiOperation(value = "游戏信息-通过id查询", notes = "游戏信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GameInfo gameInfo = gameInfoService.getById(id);
        return Result.ok(gameInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameInfo gameInfo) {
        return super.exportXls(request, gameInfo, GameInfo.class, "游戏信息");
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
        return super.importExcel(request, response, GameInfo.class);
    }

}
