package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSingleDayRechargeJadeRebate;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSingleDayRechargeJadeRebateService;
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
 * @Description: 单日仙玉返利
 * @Author: jeecg-boot
 * @Date: 2023-05-12
 * @Version: V1.0
 */
@Api(tags = "单日仙玉返利")
@RestController
@RequestMapping("/game/gameCampaignTypeSingleDayRechargeJadeRebate")
@Slf4j
public class GameCampaignTypeSingleDayRechargeJadeRebateController extends JeecgController<GameCampaignTypeSingleDayRechargeJadeRebate, IGameCampaignTypeSingleDayRechargeJadeRebateService> {
    @Autowired
    private IGameCampaignTypeSingleDayRechargeJadeRebateService gameCampaignTypeSingleDayRechargeJadeRebateService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeSingleDayRechargeJadeRebate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "单日仙玉返利-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeSingleDayRechargeJadeRebate>> queryPageList(GameCampaignTypeSingleDayRechargeJadeRebate gameCampaignTypeSingleDayRechargeJadeRebate,
                                                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeSingleDayRechargeJadeRebate> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeSingleDayRechargeJadeRebate, req.getParameterMap());
        Page<GameCampaignTypeSingleDayRechargeJadeRebate> page = new Page<GameCampaignTypeSingleDayRechargeJadeRebate>(pageNo, pageSize);
        IPage<GameCampaignTypeSingleDayRechargeJadeRebate> pageList = gameCampaignTypeSingleDayRechargeJadeRebateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeSingleDayRechargeJadeRebate
     * @return
     */
    @AutoLog(value = "单日仙玉返利-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_jade_rebate:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeSingleDayRechargeJadeRebate gameCampaignTypeSingleDayRechargeJadeRebate) {
        gameCampaignTypeSingleDayRechargeJadeRebateService.save(gameCampaignTypeSingleDayRechargeJadeRebate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeSingleDayRechargeJadeRebate
     * @return
     */
    @AutoLog(value = "单日仙玉返利-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_jade_rebate:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeSingleDayRechargeJadeRebate gameCampaignTypeSingleDayRechargeJadeRebate) {
        gameCampaignTypeSingleDayRechargeJadeRebateService.updateById(gameCampaignTypeSingleDayRechargeJadeRebate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "单日仙玉返利-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_jade_rebate:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeSingleDayRechargeJadeRebateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "单日仙玉返利-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_jade_rebate:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeSingleDayRechargeJadeRebateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "单日仙玉返利-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeSingleDayRechargeJadeRebate> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeSingleDayRechargeJadeRebate gameCampaignTypeSingleDayRechargeJadeRebate = gameCampaignTypeSingleDayRechargeJadeRebateService.getById(id);
        if (gameCampaignTypeSingleDayRechargeJadeRebate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeSingleDayRechargeJadeRebate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameCampaignTypeSingleDayRechargeJadeRebate
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_single_day_recharge_jade_rebate:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSingleDayRechargeJadeRebate gameCampaignTypeSingleDayRechargeJadeRebate) {
        return super.exportXls(request, gameCampaignTypeSingleDayRechargeJadeRebate, GameCampaignTypeSingleDayRechargeJadeRebate.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_single_day_recharge_jade_rebate:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeSingleDayRechargeJadeRebate.class);
    }

}
