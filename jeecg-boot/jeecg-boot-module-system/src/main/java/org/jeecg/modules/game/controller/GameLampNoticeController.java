package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameLampNotice;
import org.jeecg.modules.game.service.IGameLampNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_lamp_notice
 * @date 2020-08-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameLampNotice")
public class GameLampNoticeController extends JeecgController<GameLampNotice, IGameLampNoticeService> {

    @Autowired
    private IGameLampNoticeService gameLampNoticeService;

    /**
     * 分页列表查询
     *
     * @param gameLampNotice 数据实体
     * @param pageNo         页码
     * @param pageSize       分页大小
     * @param req            请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameLampNotice gameLampNotice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameLampNotice> queryWrapper = QueryGenerator.initQueryWrapper(gameLampNotice, req.getParameterMap());
        Page<GameLampNotice> page = new Page<>(pageNo, pageSize);
        IPage<GameLampNotice> pageList = gameLampNoticeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameLampNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameLampNotice gameLampNotice) {
        gameLampNoticeService.save(gameLampNotice);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameLampNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameLampNotice gameLampNotice) {
        gameLampNoticeService.updateById(gameLampNotice);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameLampNoticeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameLampNoticeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameLampNotice gameLampNotice = gameLampNoticeService.getById(id);
        if (gameLampNotice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameLampNotice);
    }

    /**
     * 导出excel
     *
     * @param request        请求
     * @param gameLampNotice 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameLampNotice gameLampNotice) {
        return super.exportXls(request, gameLampNotice, GameLampNotice.class, "game_lamp_notice");
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
        return super.importExcel(request, response, GameLampNotice.class);
    }

}
