package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailMessage;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailMessageService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailService;
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
 * @description 开服活动-消耗传闻
 * @date 2020-12-28
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignConsumeDetailMessage")
public class OpenServiceCampaignConsumeDetailMessageController extends JeecgController<OpenServiceCampaignConsumeDetailMessage, IOpenServiceCampaignConsumeDetailMessageService> {

    @Autowired
    private IOpenServiceCampaignConsumeDetailService openServiceCampaignConsumeDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "开服活动-消耗传闻-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignConsumeDetailMessage entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服活动-消耗传闻-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignConsumeDetailMessage entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服活动-消耗传闻-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignConsumeDetailMessage entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动-消耗传闻-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动-消耗传闻-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动-消耗传闻-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "开服活动-消耗传闻-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignConsumeDetailMessage entity) {
        return super.exportXls(request, entity, OpenServiceCampaignConsumeDetailMessage.class, "开服活动-消耗传闻");
    }

    @AutoLog(value = "开服活动-消耗传闻-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignConsumeDetailMessage.class);
    }

    @AutoLog(value = "开服活动-消耗传闻-导入文本")
    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignConsumeDetail parent = openServiceCampaignConsumeDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignConsumeDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignConsumeDetailMessage.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignConsumeDetailMessage> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignConsumeDetailMessage.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignConsumeDetailMessage entity : entityList) {
            entity.setId(null);
            entity.setCampaignId(parent.getCampaignId());
            entity.setCampaignTypeId(parent.getCampaignTypeId());
            entity.setConsumeDetailId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            service.saveBatch(entityList);
        }
        return Result.ok(vo);
    }

}
