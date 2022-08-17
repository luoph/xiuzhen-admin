package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftItem;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftDetailService;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftItemService;
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
 * @description 开服活动-单比好礼-任务明细
 * @date 2020-12-23
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignSingleGiftItem")
public class OpenServiceCampaignSingleGiftItemController extends JeecgController<OpenServiceCampaignSingleGiftItem, IOpenServiceCampaignSingleGiftItemService> {

    @Autowired
    private IOpenServiceCampaignSingleGiftDetailService openServiceCampaignSingleGiftDetailService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignSingleGiftItem entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignSingleGiftItem entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignSingleGiftItem entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignSingleGiftItem entity) {
        return super.exportXls(request, entity, OpenServiceCampaignSingleGiftItem.class, "开服活动-单比好礼-任务明细");
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
        return super.importExcel(request, response, OpenServiceCampaignSingleGiftItem.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaignSingleGiftDetail parent = openServiceCampaignSingleGiftDetailService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaignSingleGiftDetail");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignSingleGiftItem.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignSingleGiftItem> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignSingleGiftItem.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignSingleGiftItem entity : entityList) {
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
