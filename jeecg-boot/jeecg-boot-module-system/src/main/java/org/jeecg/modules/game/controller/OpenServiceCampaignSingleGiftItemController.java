package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftItem;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
    private IOpenServiceCampaignSingleGiftItemService openServiceCampaignSingleGiftItemService;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaignSingleGiftItem 数据实体
     * @param pageNo                            页码
     * @param pageSize                          分页大小
     * @param req                               请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignSingleGiftItem openServiceCampaignSingleGiftItem,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignSingleGiftItem> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaignSingleGiftItem, req.getParameterMap());
        Page<OpenServiceCampaignSingleGiftItem> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignSingleGiftItem> pageList = openServiceCampaignSingleGiftItemService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openServiceCampaignSingleGiftItem 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignSingleGiftItem openServiceCampaignSingleGiftItem) {
        openServiceCampaignSingleGiftItemService.save(openServiceCampaignSingleGiftItem);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openServiceCampaignSingleGiftItem 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-单比好礼-任务明细-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignSingleGiftItem openServiceCampaignSingleGiftItem) {
        openServiceCampaignSingleGiftItemService.updateById(openServiceCampaignSingleGiftItem);
        return Result.ok("编辑成功!");
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
        openServiceCampaignSingleGiftItemService.removeById(id);
        return Result.ok("删除成功!");
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
        this.openServiceCampaignSingleGiftItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
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
        OpenServiceCampaignSingleGiftItem openServiceCampaignSingleGiftItem = openServiceCampaignSingleGiftItemService.getById(id);
        if (openServiceCampaignSingleGiftItem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaignSingleGiftItem);
    }

    /**
     * 导出excel
     *
     * @param request                           请求
     * @param openServiceCampaignSingleGiftItem 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignSingleGiftItem openServiceCampaignSingleGiftItem) {
        return super.exportXls(request, openServiceCampaignSingleGiftItem, OpenServiceCampaignSingleGiftItem.class, "开服活动-单比好礼-任务明细");
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

}
