package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetail;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignConsumeDetail")
public class OpenServiceCampaignConsumeDetailController extends JeecgController<OpenServiceCampaignConsumeDetail, IOpenServiceCampaignConsumeDetailService> {

    @Autowired
    private IOpenServiceCampaignConsumeDetailService openServiceCampaignConsumeDetailService;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaignConsumeDetail 数据实体
     * @param pageNo                           页码
     * @param pageSize                         分页大小
     * @param req                              请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignConsumeDetail openServiceCampaignConsumeDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignConsumeDetail> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaignConsumeDetail, req.getParameterMap());
        Page<OpenServiceCampaignConsumeDetail> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignConsumeDetail> pageList = openServiceCampaignConsumeDetailService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openServiceCampaignConsumeDetail 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignConsumeDetail openServiceCampaignConsumeDetail) {
        openServiceCampaignConsumeDetailService.save(openServiceCampaignConsumeDetail);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openServiceCampaignConsumeDetail 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignConsumeDetail openServiceCampaignConsumeDetail) {
        openServiceCampaignConsumeDetailService.updateById(openServiceCampaignConsumeDetail);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        openServiceCampaignConsumeDetailService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.openServiceCampaignConsumeDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服互动消耗配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        OpenServiceCampaignConsumeDetail openServiceCampaignConsumeDetail = openServiceCampaignConsumeDetailService.getById(id);
        if (openServiceCampaignConsumeDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaignConsumeDetail);
    }

    /**
     * 导出excel
     *
     * @param request                          请求
     * @param openServiceCampaignConsumeDetail 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignConsumeDetail openServiceCampaignConsumeDetail) {
        return super.exportXls(request, openServiceCampaignConsumeDetail, OpenServiceCampaignConsumeDetail.class, "开服互动消耗配置");
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
        return super.importExcel(request, response, OpenServiceCampaignConsumeDetail.class);
    }

}