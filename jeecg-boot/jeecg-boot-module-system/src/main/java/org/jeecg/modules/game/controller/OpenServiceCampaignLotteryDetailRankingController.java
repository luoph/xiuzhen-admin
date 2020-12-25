package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailRanking;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignLotteryDetailRanking")
public class OpenServiceCampaignLotteryDetailRankingController extends JeecgController<OpenServiceCampaignLotteryDetailRanking, IOpenServiceCampaignLotteryDetailRankingService> {

    @Autowired
    private IOpenServiceCampaignLotteryDetailRankingService openServiceCampaignLotteryDetailRankingService;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaignLotteryDetailRanking 数据实体
     * @param pageNo                                  页码
     * @param pageSize                                分页大小
     * @param req                                     请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignLotteryDetailRanking openServiceCampaignLotteryDetailRanking,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignLotteryDetailRanking> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaignLotteryDetailRanking, req.getParameterMap());
        Page<OpenServiceCampaignLotteryDetailRanking> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignLotteryDetailRanking> pageList = openServiceCampaignLotteryDetailRankingService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openServiceCampaignLotteryDetailRanking 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignLotteryDetailRanking openServiceCampaignLotteryDetailRanking) {
        openServiceCampaignLotteryDetailRankingService.save(openServiceCampaignLotteryDetailRanking);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openServiceCampaignLotteryDetailRanking 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignLotteryDetailRanking openServiceCampaignLotteryDetailRanking) {
        openServiceCampaignLotteryDetailRankingService.updateById(openServiceCampaignLotteryDetailRanking);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        openServiceCampaignLotteryDetailRankingService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.openServiceCampaignLotteryDetailRankingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服夺宝榜单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        OpenServiceCampaignLotteryDetailRanking openServiceCampaignLotteryDetailRanking = openServiceCampaignLotteryDetailRankingService.getById(id);
        if (openServiceCampaignLotteryDetailRanking == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaignLotteryDetailRanking);
    }

    /**
     * 导出excel
     *
     * @param request                                 请求
     * @param openServiceCampaignLotteryDetailRanking 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignLotteryDetailRanking openServiceCampaignLotteryDetailRanking) {
        return super.exportXls(request, openServiceCampaignLotteryDetailRanking, OpenServiceCampaignLotteryDetailRanking.class, "开服夺宝榜单");
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
        return super.importExcel(request, response, OpenServiceCampaignLotteryDetailRanking.class);
    }

}