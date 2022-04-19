package org.jeecg.modules.game.controller;

import cn.youai.basics.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameAppUpdate;
import org.jeecg.modules.game.service.IGameAppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端版本
 * @date 2021-06-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameAppUpdate")
public class GameAppUpdateController extends JeecgController<GameAppUpdate, IGameAppUpdateService> {

    @Autowired
    private IGameAppUpdateService gameAppUpdateService;

    /**
     * 分页列表查询
     *
     * @param gameAppUpdate 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameAppUpdate gameAppUpdate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameAppUpdate> queryWrapper = QueryGenerator.initQueryWrapper(gameAppUpdate, req.getParameterMap());
        Page<GameAppUpdate> page = new Page<>(pageNo, pageSize);
        IPage<GameAppUpdate> pageList = gameAppUpdateService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameAppUpdate 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameAppUpdate gameAppUpdate) {
        gameAppUpdateService.save(gameAppUpdate);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameAppUpdate 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameAppUpdate gameAppUpdate) {
        gameAppUpdateService.updateById(gameAppUpdate);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameAppUpdateService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameAppUpdateService.removeByIds(StringUtils.split2Set(ids));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameAppUpdate gameAppUpdate = gameAppUpdateService.getById(id);
        if (gameAppUpdate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameAppUpdate);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param gameAppUpdate 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameAppUpdate gameAppUpdate) {
        return super.exportXls(request, gameAppUpdate, GameAppUpdate.class, "客户端版本");
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
        return super.importExcel(request, response, GameAppUpdate.class);
    }

    @AutoLog(value = "客户端版本-刷新版本配置")
    @ApiOperation(value = "客户端版本-刷新版本配置", notes = "客户端版本-刷新版本配置")
    @GetMapping(value = "/updateConfig")
    public Result<?> updateConfig(HttpServletRequest req) {
        try {
            gameAppUpdateService.updateConfig();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
