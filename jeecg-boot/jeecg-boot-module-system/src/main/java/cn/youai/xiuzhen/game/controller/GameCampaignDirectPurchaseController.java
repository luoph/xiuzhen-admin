package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameCampaignDirectPurchase;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameCampaignDirectPurchaseService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
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
 * @description 直购礼包
 * @date 2021-04-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignDirectPurchase")
public class GameCampaignDirectPurchaseController extends JeecgController<GameCampaignDirectPurchase, IGameCampaignDirectPurchaseService> {

    private IGameCampaignTypeService gameCampaignTypeService;

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
    @AutoLog(value = "直购礼包-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignDirectPurchase entity,
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
    @AutoLog(value = "直购礼包-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignDirectPurchase entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignDirectPurchase entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-通过id删除")
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
    @AutoLog(value = "直购礼包-批量删除")
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
    @AutoLog(value = "直购礼包-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignDirectPurchase entity) {
        return super.exportXls(request, entity, GameCampaignDirectPurchase.class, "直购礼包");
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
        return super.importExcel(request, response, GameCampaignDirectPurchase.class);
    }


    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = gameCampaignTypeService.getById(vo.getId());
        if (campaignType == null) {
            return Result.error("未找得到对应的子活动配置!");
        }

        String fileName = tempFolder + File.separator + GameCampaignDirectPurchase.class.getSimpleName() + ".xls";

        List<GameCampaignDirectPurchase> directPurchaseList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameCampaignDirectPurchase.class);
        if (CollUtil.isNotEmpty(directPurchaseList)) {
            for (GameCampaignDirectPurchase directPurchase : directPurchaseList) {
                directPurchase.setId(null);
                directPurchase.setCampaignId(campaignType.getCampaignId());
                directPurchase.setTypeId(campaignType.getId());
                directPurchase.setCreateTime(DateUtils.now());
            }
            service.saveBatch(directPurchaseList);
        }
        return Result.ok(vo);
    }
}
