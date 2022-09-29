package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailRanking;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailRankingService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailService;
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
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignLotteryDetailRanking")
public class OpenServiceCampaignLotteryDetailRankingController extends JeecgController<OpenServiceCampaignLotteryDetailRanking, IOpenServiceCampaignLotteryDetailRankingService> {

    @Autowired
    private IOpenServiceCampaignLotteryDetailService openServiceCampaignLotteryDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "开服夺宝榜单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignLotteryDetailRanking entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服夺宝榜单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignLotteryDetailRanking entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服夺宝榜单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignLotteryDetailRanking entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服夺宝榜单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服夺宝榜单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服夺宝榜单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "开服夺宝榜单-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignLotteryDetailRanking entity) {
        return super.exportXls(request, entity, OpenServiceCampaignLotteryDetailRanking.class, "开服夺宝榜单");
    }

    @AutoLog(value = "开服夺宝榜单-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignLotteryDetailRanking.class);
    }

    @AutoLog(value = "开服夺宝榜单-导入文本")
    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignLotteryDetail parent = openServiceCampaignLotteryDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignLotteryDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignLotteryDetailRanking.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignLotteryDetailRanking> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignLotteryDetailRanking.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignLotteryDetailRanking entity : entityList) {
            entity.setId(null);
            entity.setCampaignId(parent.getCampaignId());
            entity.setCampaignTypeId(parent.getCampaignTypeId());
            entity.setLotteryDetailId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            service.saveBatch(entityList);
        }
        return Result.ok(vo);
    }
}
