package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameImage;
import org.jeecg.modules.game.service.IGameImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏图片
 * @date 2020-11-02
 */
@Slf4j
@RestController
@RequestMapping("game/gameImage")
public class GameImageController extends JeecgController<GameImage, IGameImageService> {

    @Autowired
    private IGameImageService gameImageService;

    /**
     * 分页列表查询
     *
     * @param gameImage 数据实体
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param req       请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameImage gameImage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameImage> queryWrapper = QueryGenerator.initQueryWrapper(gameImage, req.getParameterMap());
        Page<GameImage> page = new Page<>(pageNo, pageSize);
        IPage<GameImage> pageList = gameImageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameImage 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameImage gameImage) {
        gameImageService.save(gameImage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameImage 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameImage gameImage) {
        gameImageService.updateById(gameImage);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameImageService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameImageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameImage gameImage = gameImageService.getById(id);
        if (gameImage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameImage);
    }

    /**
     * 导出excel
     *
     * @param request   请求
     * @param gameImage 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameImage gameImage) {
        return super.exportXls(request, gameImage, GameImage.class, "游戏图片");
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
        return super.importExcel(request, response, GameImage.class);
    }

}
