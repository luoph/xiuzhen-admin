package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailRanking;
import org.jeecg.modules.game.service.IOpenServiceCampaignRankDetailRankingService;
import org.jeecg.modules.game.service.IOpenServiceCampaignRankDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-排行上榜、奖励
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignRankDetailRanking")
public class OpenServiceCampaignRankDetailRankingController extends JeecgController<OpenServiceCampaignRankDetailRanking, IOpenServiceCampaignRankDetailRankingService> {

    @Autowired
    private IOpenServiceCampaignRankDetailService openServiceCampaignRankDetailService;

    @Autowired
    private IOpenServiceCampaignRankDetailRankingService openServiceCampaignRankDetailRankingService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaignRankDetailRanking 数据实体
     * @param pageNo                               页码
     * @param pageSize                             分页大小
     * @param req                                  请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignRankDetailRanking openServiceCampaignRankDetailRanking,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignRankDetailRanking> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaignRankDetailRanking, req.getParameterMap());
        Page<OpenServiceCampaignRankDetailRanking> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignRankDetailRanking> pageList = openServiceCampaignRankDetailRankingService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openServiceCampaignRankDetailRanking 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignRankDetailRanking openServiceCampaignRankDetailRanking) {
        openServiceCampaignRankDetailRankingService.save(openServiceCampaignRankDetailRanking);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openServiceCampaignRankDetailRanking 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignRankDetailRanking openServiceCampaignRankDetailRanking) {
        openServiceCampaignRankDetailRankingService.updateById(openServiceCampaignRankDetailRanking);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        openServiceCampaignRankDetailRankingService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.openServiceCampaignRankDetailRankingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-开服排行-活动明细-排行上榜、奖励-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        OpenServiceCampaignRankDetailRanking openServiceCampaignRankDetailRanking = openServiceCampaignRankDetailRankingService.getById(id);
        if (openServiceCampaignRankDetailRanking == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaignRankDetailRanking);
    }

    /**
     * 导出excel
     *
     * @param request                              请求
     * @param openServiceCampaignRankDetailRanking 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignRankDetailRanking openServiceCampaignRankDetailRanking) {
        return super.exportXls(request, openServiceCampaignRankDetailRanking, OpenServiceCampaignRankDetailRanking.class, "开服活动-开服排行-活动明细-排行上榜、奖励");
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
        return super.importExcel(request, response, OpenServiceCampaignRankDetailRanking.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignRankDetail parent = openServiceCampaignRankDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignRankDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignRankDetailRanking.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignRankDetailRanking> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignRankDetailRanking.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignRankDetailRanking entity : entityList) {
            entity.setId(null);
            entity.setCampaignId(parent.getCampaignId());
            entity.setCampaignTypeId(parent.getCampaignTypeId());
            entity.setRankDetailId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            openServiceCampaignRankDetailRankingService.saveBatch(entityList);
        }
        return Result.ok(vo);
    }

}
