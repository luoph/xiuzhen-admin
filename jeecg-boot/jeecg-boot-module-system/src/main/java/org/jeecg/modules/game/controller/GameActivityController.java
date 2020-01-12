package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameActivity;
import org.jeecg.modules.game.service.IGameActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动
 * @date 2020-01-12
 */
@Slf4j
@RestController
@RequestMapping("game/gameActivity")
public class GameActivityController extends JeecgController<GameActivity, IGameActivityService> {

    @Value("${app.url.gamecenter}")
    private String gameCenterUrl;

    @Autowired
    private IGameActivityService gameActivityService;

    /**
     * 分页列表查询
     *
     * @param gameActivity 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @param req          请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameActivity gameActivity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameActivity> queryWrapper = QueryGenerator.initQueryWrapper(gameActivity, req.getParameterMap());
        Page<GameActivity> page = new Page<>(pageNo, pageSize);
        IPage<GameActivity> pageList = gameActivityService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameActivity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameActivity gameActivity) {
        gameActivityService.save(gameActivity);
        refreshGameActivity();
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameActivity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameActivity gameActivity) {
        gameActivityService.updateById(gameActivity);
        refreshGameActivity();
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameActivityService.removeById(id);
        refreshGameActivity();
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameActivityService.removeByIds(Arrays.asList(ids.split(",")));
        refreshGameActivity();
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameActivity gameActivity = gameActivityService.getById(id);
        if (gameActivity == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameActivity);
    }

    /**
     * 导出excel
     *
     * @param request      请求
     * @param gameActivity 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameActivity gameActivity) {
        return super.exportXls(request, gameActivity, GameActivity.class, "活动");
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
        return super.importExcel(request, response, GameActivity.class);
    }

    /**
     * 刷新中心服缓存
     */
    private void refreshGameActivity() {
        OkHttpHelper.get(gameCenterUrl + "/activity/update");
    }

}
