package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeRelicLotteryMessage;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeRelicLotteryMessageService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
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
 * @Description: 节日活动-遗迹夺宝-传闻
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Api(tags = "节日活动-遗迹夺宝-传闻")
@RestController
@RequestMapping("/game/gameCampaignTypeRelicLotteryMessage")
@Slf4j
public class GameCampaignTypeRelicLotteryMessageController extends JeecgController<GameCampaignTypeRelicLotteryMessage, IGameCampaignTypeRelicLotteryMessageService> {
    @Autowired
    private IGameCampaignTypeRelicLotteryMessageService gameCampaignTypeRelicLotteryMessageService;
    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeRelicLotteryMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-遗迹夺宝-传闻-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeRelicLotteryMessage>> queryPageList(GameCampaignTypeRelicLotteryMessage gameCampaignTypeRelicLotteryMessage,
                                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                            HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeRelicLotteryMessage> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeRelicLotteryMessage, req.getParameterMap());
        Page<GameCampaignTypeRelicLotteryMessage> page = new Page<GameCampaignTypeRelicLotteryMessage>(pageNo, pageSize);
        IPage<GameCampaignTypeRelicLotteryMessage> pageList = gameCampaignTypeRelicLotteryMessageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeRelicLotteryMessage
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-传闻-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery_message:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeRelicLotteryMessage gameCampaignTypeRelicLotteryMessage) {
        gameCampaignTypeRelicLotteryMessageService.save(gameCampaignTypeRelicLotteryMessage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeRelicLotteryMessage
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-传闻-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery_message:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeRelicLotteryMessage gameCampaignTypeRelicLotteryMessage) {
        gameCampaignTypeRelicLotteryMessageService.updateById(gameCampaignTypeRelicLotteryMessage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-传闻-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery_message:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeRelicLotteryMessageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-遗迹夺宝-传闻-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery_message:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeRelicLotteryMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-遗迹夺宝-传闻-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeRelicLotteryMessage> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeRelicLotteryMessage gameCampaignTypeRelicLotteryMessage = gameCampaignTypeRelicLotteryMessageService.getById(id);
        if (gameCampaignTypeRelicLotteryMessage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeRelicLotteryMessage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameCampaignTypeRelicLotteryMessage
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_relic_lottery_message:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeRelicLotteryMessage gameCampaignTypeRelicLotteryMessage) {
        return super.exportXls(request, gameCampaignTypeRelicLotteryMessage, GameCampaignTypeRelicLotteryMessage.class, CampaignType.RELIC_LOTTERY.getName() + "-传闻");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_relic_lottery_message:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
        return gameCampaignTypeService.importExcel(campaignId, typeId, request, CampaignType.RELIC_LOTTERY.getName() + "-传闻", service.getClass());
    }

}
