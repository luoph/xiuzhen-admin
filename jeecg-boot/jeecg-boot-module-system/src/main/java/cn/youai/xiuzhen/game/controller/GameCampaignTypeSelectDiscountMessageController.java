package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSelectDiscountMessage;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSelectDiscountMessageService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日活动-自选特惠-传闻部分
 * @date 2021-09-09
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeSelectDiscountMessage")
public class GameCampaignTypeSelectDiscountMessageController extends JeecgController<GameCampaignTypeSelectDiscountMessage, IGameCampaignTypeSelectDiscountMessageService> {

    @Autowired
    private IGameCampaignService gameCampaignService;
    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @AutoLog(value = "节日活动-自选特惠-传闻部分-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeSelectDiscountMessage entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeSelectDiscountMessage entity) {
        return super.add(entity);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeSelectDiscountMessage entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSelectDiscountMessage entity) {
        return super.exportXls(request, entity, GameCampaignTypeSelectDiscountMessage.class, CampaignType.SELECT_DISCOUNT_ITEM.getName() + "-传闻");
    }

    @AutoLog(value = "节日活动-自选特惠-传闻部分-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (null == gameCampaign) {
            return Result.error("找不到主活动配置");
        }
        return gameCampaignTypeService.importExcel(gameCampaign, typeId, request, CampaignType.SELECT_DISCOUNT_ITEM.getName() + "-传闻", service.getClass());
    }

}
