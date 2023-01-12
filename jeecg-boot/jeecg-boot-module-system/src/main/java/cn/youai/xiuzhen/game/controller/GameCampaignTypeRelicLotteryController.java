package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeRelicLottery;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeRelicLotteryService;
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
 * @Description: 节日活动-遗迹夺宝
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Api(tags = "节日活动-遗迹夺宝")
@RestController
@RequestMapping("/game/gameCampaignTypeRelicLottery")
@Slf4j
public class GameCampaignTypeRelicLotteryController extends JeecgController<GameCampaignTypeRelicLottery, IGameCampaignTypeRelicLotteryService> {
    @Autowired
    private IGameCampaignTypeRelicLotteryService gameCampaignTypeRelicLotteryService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeRelicLottery
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-遗迹夺宝-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeRelicLottery>> queryPageList(GameCampaignTypeRelicLottery gameCampaignTypeRelicLottery,
                                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeRelicLottery> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeRelicLottery, req.getParameterMap());
        Page<GameCampaignTypeRelicLottery> page = new Page<GameCampaignTypeRelicLottery>(pageNo, pageSize);
        IPage<GameCampaignTypeRelicLottery> pageList = gameCampaignTypeRelicLotteryService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeRelicLottery
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeRelicLottery gameCampaignTypeRelicLottery) {
        gameCampaignTypeRelicLotteryService.save(gameCampaignTypeRelicLottery);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeRelicLottery
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeRelicLottery gameCampaignTypeRelicLottery) {
        gameCampaignTypeRelicLotteryService.updateById(gameCampaignTypeRelicLottery);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeRelicLotteryService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeRelicLotteryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-遗迹夺宝-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeRelicLottery> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeRelicLottery gameCampaignTypeRelicLottery = gameCampaignTypeRelicLotteryService.getById(id);
        if (gameCampaignTypeRelicLottery == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeRelicLottery);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameCampaignTypeRelicLottery
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeRelicLottery gameCampaignTypeRelicLottery) {
        return super.exportXls(request, gameCampaignTypeRelicLottery, GameCampaignTypeRelicLottery.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_relic_lottery:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeRelicLottery.class);
    }

}
