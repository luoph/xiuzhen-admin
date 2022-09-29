package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailScore;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankDetailScoreService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankDetailService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-消耗道具分数
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignRankDetailScore")
public class OpenServiceCampaignRankDetailScoreController extends JeecgController<OpenServiceCampaignRankDetailScore, IOpenServiceCampaignRankDetailScoreService> {

    @Autowired
    private IOpenServiceCampaignRankDetailService openServiceCampaignRankDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignRankDetailScore entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignRankDetailScore entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignRankDetailScore entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignRankDetailScore entity) {
        return super.exportXls(request, entity, OpenServiceCampaignRankDetailScore.class, "开服活动-开服排行-活动明细-消耗道具分数");
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignRankDetailScore.class);
    }

    @AutoLog(value = "开服活动-开服排行-活动明细-消耗道具分数-导入文本")
    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignRankDetail parent = openServiceCampaignRankDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignRankDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignRankDetailScore.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignRankDetailScore> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignRankDetailScore.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignRankDetailScore entity : entityList) {
            entity.setId(null);
            entity.setCampaignId(parent.getCampaignId());
            entity.setCampaignTypeId(parent.getCampaignTypeId());
            entity.setRankDetailId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            service.saveBatch(entityList);
        }
        return Result.ok(vo);
    }

}
