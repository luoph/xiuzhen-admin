package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GameDataReportCount;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameDataReportCountService;
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
 * @description 数据报表
 * @date 2020-10-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameDataReportCount")
public class GameDataReportCountController extends JeecgController<GameDataReportCount, IGameDataReportCountService> {

    @Autowired
    private IGameDataReportCountService gameDataReportCountService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param gameDataReportCount 数据实体
     * @param pageNo              页码
     * @param pageSize            分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameDataReportCount gameDataReportCount,
                                   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {

        Page<GameDataReportCount> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameDataReportCount> gameDataReportCountList = gameDataReportCountService.queryDataReportByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        page.setRecords(gameDataReportCountList).setTotal(gameDataReportCountList.size());
        return Result.ok(page);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                  @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                  @RequestParam(name = "days", defaultValue = "0") int days,
                                  @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                  @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                  HttpServletRequest request) {
        // 获取导出数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameDataReportCount> gameDataReportCountList = gameDataReportCountService.queryDataReportByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        return ExcelUtils.exportXls(sysUser.getRealname(), gameDataReportCountList, request.getParameter("selections"), GameDataReportCount.class, "数据报表");
    }


    /**
     * 添加
     *
     * @param gameDataReportCount 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameDataReportCount gameDataReportCount) {
        gameDataReportCountService.save(gameDataReportCount);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameDataReportCount 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameDataReportCount gameDataReportCount) {
        gameDataReportCountService.updateById(gameDataReportCount);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameDataReportCountService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameDataReportCountService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameDataReportCount gameDataReportCount = gameDataReportCountService.getById(id);
        if (gameDataReportCount == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameDataReportCount);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param gameDataReportCount 实体
     */
    /*@RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameDataReportCount gameDataReportCount) {
        return super.exportXls(request, gameDataReportCount, GameDataReportCount.class, "数据报表");
    }*/


    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameDataReportCount.class);
    }

}
