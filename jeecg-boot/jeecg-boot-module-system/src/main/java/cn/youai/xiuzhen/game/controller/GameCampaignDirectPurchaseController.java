package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignDirectPurchase;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameCampaignDirectPurchaseService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
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
 * @description 直购礼包
 * @date 2021-04-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignDirectPurchase")
public class GameCampaignDirectPurchaseController extends JeecgController<GameCampaignDirectPurchase, IGameCampaignDirectPurchaseService> {

    @Autowired
    private IGameCampaignTypeService campaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "直购礼包-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignDirectPurchase entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        return super.queryPageList(entity, pageNo, pageSize, req);
        IPage<GameCampaignDirectPurchase> page = pageList(entity, pageNo, pageSize, req);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            GameCampaignType gameCampaignType = campaignTypeService.getOne(Wrappers.<GameCampaignType>lambdaQuery().eq(GameCampaignType::getId, entity.getTypeId()));
            if (null != gameCampaignType) {
                page.getRecords().forEach(e -> e.setCampaignType(gameCampaignType.getType()));
            }
        }
        return Result.ok(page);
    }

    @AutoLog(value = "直购礼包-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignDirectPurchase entity) {
        return super.add(entity);
    }

    @AutoLog(value = "直购礼包-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignDirectPurchase entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "直购礼包-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "直购礼包-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "直购礼包-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "直购礼包-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignDirectPurchase entity) {
        return super.exportXls(request, entity, GameCampaignDirectPurchase.class, CampaignType.valueOf(entity.getCampaignType()).getName());
    }

    @AutoLog(value = "直购礼包-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignDirectPurchase.class);
    }

    @AutoLog(value = "直购礼包-导入文本")
    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = campaignTypeService.getById(vo.getId());
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
