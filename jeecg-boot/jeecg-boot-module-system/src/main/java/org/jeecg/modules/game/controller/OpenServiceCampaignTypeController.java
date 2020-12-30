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
import org.jeecg.modules.game.entity.OpenServiceCampaign;
import org.jeecg.modules.game.entity.OpenServiceCampaignType;
import org.jeecg.modules.game.service.IOpenServiceCampaignService;
import org.jeecg.modules.game.service.IOpenServiceCampaignTypeService;
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
 * @description 开服活动-类型(2级)
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignType")
public class OpenServiceCampaignTypeController extends JeecgController<OpenServiceCampaignType, IOpenServiceCampaignTypeService> {

    @Autowired
    private IOpenServiceCampaignTypeService openServiceCampaignTypeService;

    @Autowired
    private IOpenServiceCampaignService openServiceCampaignService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaignType 数据实体
     * @param pageNo                  页码
     * @param pageSize                分页大小
     * @param req                     请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignType openServiceCampaignType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaignType> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaignType, req.getParameterMap());
        Page<OpenServiceCampaignType> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaignType> pageList = openServiceCampaignTypeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openServiceCampaignType 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignType openServiceCampaignType) {
        openServiceCampaignTypeService.save(openServiceCampaignType);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openServiceCampaignType 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignType openServiceCampaignType) {
        openServiceCampaignTypeService.updateById(openServiceCampaignType);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        openServiceCampaignTypeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.openServiceCampaignTypeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动-类型(2级)-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        OpenServiceCampaignType openServiceCampaignType = openServiceCampaignTypeService.getById(id);
        if (openServiceCampaignType == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaignType);
    }

    /**
     * 导出excel
     *
     * @param request                 请求
     * @param openServiceCampaignType 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignType openServiceCampaignType) {
        return super.exportXls(request, openServiceCampaignType, OpenServiceCampaignType.class, "开服活动-类型(2级)");
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
        return super.importExcel(request, response, OpenServiceCampaignType.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        OpenServiceCampaign parent = openServiceCampaignService.getById(vo.getId());
        if (parent == null) {
            return Result.error("未找得到对应的 OpenServiceCampaign");
        }

        String fileName = tempFolder + File.separator + OpenServiceCampaignType.class.getSimpleName() + ".xls";
        List<OpenServiceCampaignType> entityList = ExcelUtils.importFromExcelText(vo.getText(), fileName, OpenServiceCampaignType.class);
        log.debug("importText vo:{}, list:{}", vo, entityList);
        for (OpenServiceCampaignType entity : entityList) {
            entity.setCampaignId(parent.getId());
            entity.setCreateTime(DateUtils.now());
        }

        if (CollUtil.isNotEmpty(entityList)) {
            openServiceCampaignTypeService.saveBatch(entityList);
        }
        return Result.ok(vo);
    }

}
