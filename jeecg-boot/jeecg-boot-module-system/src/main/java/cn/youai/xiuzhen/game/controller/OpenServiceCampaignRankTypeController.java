package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankType;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankTypeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-类型库
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaignRankType")
public class OpenServiceCampaignRankTypeController extends JeecgController<OpenServiceCampaignRankType, IOpenServiceCampaignRankTypeService> {

    @AutoLog(value = "开服活动-开服排行-类型库-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaignRankType entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服活动-开服排行-类型库-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaignRankType entity) {
        return super.add(entity);
    }

    @AutoLog(value = "开服活动-开服排行-类型库-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaignRankType entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动-开服排行-类型库-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动-开服排行-类型库-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动-开服排行-类型库-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaignRankType entity) {
        return super.exportXls(request, entity, OpenServiceCampaignRankType.class, "开服活动-开服排行-类型库");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaignRankType.class);
    }

}
