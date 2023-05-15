package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSingleDayRechargeItemRebate;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSingleDayRechargeItemRebateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 单日道具返利
 * @Author: jeecg-boot
 * @Date: 2023-05-12
 * @Version: V1.0
 */
@Api(tags = "单日道具返利")
@RestController
@RequestMapping("/game/gameCampaignTypeSingleDayRechargeItemRebate")
@Slf4j
public class GameCampaignTypeSingleDayRechargeItemRebateController extends JeecgController<GameCampaignTypeSingleDayRechargeItemRebate, IGameCampaignTypeSingleDayRechargeItemRebateService> {
    @Autowired
    private IGameCampaignTypeSingleDayRechargeItemRebateService gameCampaignTypeSingleDayRechargeItemRebateService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeSingleDayRechargeItemRebate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "单日道具返利-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeSingleDayRechargeItemRebate>> queryPageList(GameCampaignTypeSingleDayRechargeItemRebate gameCampaignTypeSingleDayRechargeItemRebate,
                                                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeSingleDayRechargeItemRebate> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeSingleDayRechargeItemRebate, req.getParameterMap());
        Page<GameCampaignTypeSingleDayRechargeItemRebate> page = new Page<GameCampaignTypeSingleDayRechargeItemRebate>(pageNo, pageSize);
        IPage<GameCampaignTypeSingleDayRechargeItemRebate> pageList = gameCampaignTypeSingleDayRechargeItemRebateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeSingleDayRechargeItemRebate
     * @return
     */
    @AutoLog(value = "单日道具返利-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_item_rebate:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeSingleDayRechargeItemRebate gameCampaignTypeSingleDayRechargeItemRebate) {
        gameCampaignTypeSingleDayRechargeItemRebateService.save(gameCampaignTypeSingleDayRechargeItemRebate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeSingleDayRechargeItemRebate
     * @return
     */
    @AutoLog(value = "单日道具返利-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_item_rebate:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeSingleDayRechargeItemRebate gameCampaignTypeSingleDayRechargeItemRebate) {
        gameCampaignTypeSingleDayRechargeItemRebateService.updateById(gameCampaignTypeSingleDayRechargeItemRebate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "单日道具返利-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_item_rebate:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeSingleDayRechargeItemRebateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "单日道具返利-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_item_rebate:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeSingleDayRechargeItemRebateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "单日道具返利-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeSingleDayRechargeItemRebate> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeSingleDayRechargeItemRebate gameCampaignTypeSingleDayRechargeItemRebate = gameCampaignTypeSingleDayRechargeItemRebateService.getById(id);
        if (gameCampaignTypeSingleDayRechargeItemRebate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeSingleDayRechargeItemRebate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameCampaignTypeSingleDayRechargeItemRebate
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_item_rebate:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSingleDayRechargeItemRebate gameCampaignTypeSingleDayRechargeItemRebate) {
        return super.exportXls(request, gameCampaignTypeSingleDayRechargeItemRebate, GameCampaignTypeSingleDayRechargeItemRebate.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_single_day_recharge_item_rebate:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeSingleDayRechargeItemRebate.class);
    }

}
