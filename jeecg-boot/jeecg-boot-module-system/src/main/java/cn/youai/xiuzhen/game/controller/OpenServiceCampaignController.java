package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignType;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignTypeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaign")
public class OpenServiceCampaignController extends JeecgController<OpenServiceCampaign, IOpenServiceCampaignService> {

    @Autowired
    private IOpenServiceCampaignTypeService campaignTypeService;

    @AutoLog(value = "开服活动(1级)-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaign entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服活动(1级)-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaign entity) {
        // 排序区服id
        List<String> serverIds = StringUtils.split2List(entity.getServerIds());
        Collections.sort(serverIds);
        entity.setServerIds(StrUtil.join(",", serverIds));
        return super.add(entity);
    }

    @AutoLog(value = "开服活动(1级)-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaign entity) {
        // 排序区服id
        List<String> serverIds = StringUtils.split2List(entity.getServerIds());
        Collections.sort(serverIds);
        String newServerIds = StrUtil.join(",", serverIds);
        if (StrUtil.equals(newServerIds, entity.getServerIds())) {
            entity.setServerIds(newServerIds);
        }
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动(1级)-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动(1级)-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动(1级)-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "开服活动(1级)-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaign entity) {
        return super.exportXls(request, entity, OpenServiceCampaign.class, "开服活动(1级)");
    }

    @AutoLog(value = "开服活动(1级)-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaign.class);
    }

    /**
     * 同步游戏配置
     *
     * @param id 实体 id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-同步到区服")
    @GetMapping(value = "/sync")
    public Result<?> sync(@RequestParam(name = "id") String id) {
        OpenServiceCampaign campaign = service.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        service.syncCampaign(campaign);
        return Result.ok("同步成功!");
    }

    @AutoLog(value = "活动配置-复制")
    @GetMapping(value = "/duplicate")
    public Result<?> duplicate(@RequestParam(name = "id") String id) {
        OpenServiceCampaign campaign = service.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        OpenServiceCampaign copy = new OpenServiceCampaign(campaign);
        service.save(copy);

        List<OpenServiceCampaignType> typeList = service.getCampaignTypeList(campaign, true);
        if (CollUtil.isNotEmpty(typeList)) {
            for (OpenServiceCampaignType entity : typeList) {
                campaignTypeService.duplicate(entity, copy.getId());
            }
        }

        return Result.ok("复制成功!");
    }

    @AutoLog(value = "开服活动配置-移除已结束活动")
    @GetMapping(value = "/removeCompletedServer")
    public Result<?> removeCompletedServer(@RequestParam(name = "id", defaultValue = "0") String id) {
        long campaignId = Long.parseLong(id);
        OpenServiceCampaign campaign = service.getById(campaignId);
        if (null == campaign) {
            return Result.error("主活动为空");
        }

        return service.removeCompletedServer(campaign);
    }

}
