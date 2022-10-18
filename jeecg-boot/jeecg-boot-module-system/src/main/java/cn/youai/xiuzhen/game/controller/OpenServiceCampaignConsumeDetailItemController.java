package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailItem;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailItemService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
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
 * @description 开服活动-消耗道具
 * @date 2020-12-28
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignConsumeDetailItem")
public class OpenServiceCampaignConsumeDetailItemController extends JeecgController<OpenServiceCampaignConsumeDetailItem, IOpenServiceCampaignConsumeDetailItemService> {

    @Autowired
    private IOpenServiceCampaignConsumeDetailService openServiceCampaignConsumeDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "开服活动-消耗道具-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignConsumeDetailItem entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignConsumeDetailItem> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<OpenServiceCampaignConsumeDetailItem> page = new Page<>(pageNo, pageSize);
        queryWrapper.orderByAsc("sort", "id");
        IPage<OpenServiceCampaignConsumeDetailItem> pageList = super.pageList(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "开服活动-消耗道具-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignConsumeDetailItem entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服活动-消耗道具-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignConsumeDetailItem entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动-消耗道具-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动-消耗道具-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动-消耗道具-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "开服活动-消耗道具-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignConsumeDetailItem entity) {
        return super.exportXls(request, entity, OpenServiceCampaignConsumeDetailItem.class, "开服活动-消耗道具");
    }

    @AutoLog(value = "开服活动-消耗道具-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignConsumeDetailItem.class);
    }

    @AutoLog(value = "开服活动-消耗道具-导入文本")

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignConsumeDetail parent = openServiceCampaignConsumeDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignConsumeDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignConsumeDetailItem.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignConsumeDetailItem> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignConsumeDetailItem.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignConsumeDetailItem entity : entityList) {
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
