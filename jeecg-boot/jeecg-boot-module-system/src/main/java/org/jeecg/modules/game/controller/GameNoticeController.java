package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.JsonFileUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameNotice;
import org.jeecg.modules.game.model.NoticeConfig;
import org.jeecg.modules.game.service.IGameNoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏公告
 * @date 2019-12-13
 */
@Slf4j
@RestController
@Api(tags = "游戏公告")
@RequestMapping("/game/gameNotice")
public class GameNoticeController extends JeecgController<GameNotice, IGameNoticeService> {

    @Value("${app.folder.notice}")
    private String noticeFolder;

    @Autowired
    private IGameNoticeService gameNoticeService;

    /**
     * 分页列表查询
     *
     * @param gameNotice 数据实体
     * @param pageNo     页码
     * @param pageSize   分页大小
     * @param req        请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-列表查询")
    @ApiOperation(value = "游戏公告-列表查询", notes = "游戏公告-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameNotice gameNotice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameNotice> queryWrapper = QueryGenerator.initQueryWrapper(gameNotice, req.getParameterMap());
        Page<GameNotice> page = new Page<>(pageNo, pageSize);
        IPage<GameNotice> pageList = gameNoticeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-添加")
    @ApiOperation(value = "游戏公告-添加", notes = "游戏公告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameNotice gameNotice) {
        gameNoticeService.save(gameNotice);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-编辑")
    @ApiOperation(value = "游戏公告-编辑", notes = "游戏公告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameNotice gameNotice) {
        gameNoticeService.updateById(gameNotice);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-通过id删除")
    @ApiOperation(value = "游戏公告-通过id删除", notes = "游戏公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameNoticeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-批量删除")
    @ApiOperation(value = "游戏公告-批量删除", notes = "游戏公告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameNoticeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-通过id查询")
    @ApiOperation(value = "游戏公告-通过id查询", notes = "游戏公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameNotice gameNotice = gameNoticeService.getById(id);
        return Result.ok(gameNotice);
    }

    /**
     * 导出excel
     *
     * @param request    请求
     * @param gameNotice 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameNotice gameNotice) {
        return super.exportXls(request, gameNotice, GameNotice.class, "游戏公告");
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
        return super.importExcel(request, response, GameNotice.class);
    }

    @GetMapping(value = "/updateNoticeConfig")
    public Result<?> updateServerConfig(HttpServletRequest req) {
        try {
            List<GameNotice> noticeList = gameNoticeService.list();
            for (GameNotice gameNotice : noticeList) {
                NoticeConfig notice = new NoticeConfig();
                BeanUtils.copyProperties(gameNotice, notice);
                if (gameNotice.getStatus() == 1) {
                    JsonFileUtils.writeJsonFile(notice, noticeFolder, String.valueOf(gameNotice.getId()));
                } else {
                    JsonFileUtils.deleteJsonFile(noticeFolder, String.valueOf(gameNotice.getId()));
                }
            }
        } catch (Exception e) {
            log.error("updateServerConfig error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
