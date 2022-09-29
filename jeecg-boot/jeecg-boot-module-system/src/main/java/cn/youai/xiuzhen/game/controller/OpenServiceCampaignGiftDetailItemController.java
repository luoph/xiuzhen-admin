package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignGiftDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignGiftDetailItem;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignGiftDetailItemService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignGiftDetailService;
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
 * @description 开服活动-开服开服礼包-礼包明细
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignGiftDetailItem")
public class OpenServiceCampaignGiftDetailItemController extends JeecgController<OpenServiceCampaignGiftDetailItem, IOpenServiceCampaignGiftDetailItemService> {

    @Autowired
    private IOpenServiceCampaignGiftDetailService openServiceCampaignGiftDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignGiftDetailItem entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignGiftDetailItem> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        queryWrapper.orderByAsc("sort");
        Page<OpenServiceCampaignGiftDetailItem> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignGiftDetailItem> pageList = pageList(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignGiftDetailItem entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignGiftDetailItem entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动-开服开服礼包-礼包明细-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignGiftDetailItem entity) {
        return super.exportXls(request, entity, OpenServiceCampaignGiftDetailItem.class, "开服活动-开服开服礼包-礼包明细");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignGiftDetailItem.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignGiftDetail parent = openServiceCampaignGiftDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignGiftDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignGiftDetailItem.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignGiftDetailItem> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignGiftDetailItem.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignGiftDetailItem entity : entityList) {
            entity.setId(null);
            entity.setCampaignId(parent.getCampaignId());
            entity.setCampaignTypeId(parent.getCampaignTypeId());
            entity.setGiftDetailId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            service.saveBatch(entityList);
        }
        return Result.ok(vo);
    }

}
