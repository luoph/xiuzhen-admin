package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeLotteryToken;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeLotteryTokenService;
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
 * @Description: 节日活动-夺宝战令
 * @Author: jeecg-boot
 * @Date: 2023-01-17
 * @Version: V1.0
 */
@Api(tags = "节日活动-夺宝战令")
@RestController
@RequestMapping("/game/gameCampaignTypeLotteryToken")
@Slf4j
public class GameCampaignTypeLotteryTokenController extends JeecgController<GameCampaignTypeLotteryToken, IGameCampaignTypeLotteryTokenService> {
    @Autowired
    private IGameCampaignTypeLotteryTokenService gameCampaignTypeLotteryTokenService;

    /**
     * 分页列表查询
     *
     * @param entity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-夺宝战令-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeLotteryToken>> queryPageList(GameCampaignTypeLotteryToken entity,
                                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeLotteryToken> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<GameCampaignTypeLotteryToken> page = new Page<GameCampaignTypeLotteryToken>(pageNo, pageSize);
        IPage<GameCampaignTypeLotteryToken> pageList = gameCampaignTypeLotteryTokenService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-夺宝战令-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_lottery_token:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeLotteryToken entity) {
        gameCampaignTypeLotteryTokenService.save(entity);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-夺宝战令-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_lottery_token:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeLotteryToken entity) {
        gameCampaignTypeLotteryTokenService.updateById(entity);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-夺宝战令-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_lottery_token:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeLotteryTokenService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-夺宝战令-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_lottery_token:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeLotteryTokenService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-夺宝战令-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeLotteryToken> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeLotteryToken gameCampaignTypeLotteryToken = gameCampaignTypeLotteryTokenService.getById(id);
        if (gameCampaignTypeLotteryToken == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeLotteryToken);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entity
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_lottery_token:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeLotteryToken entity) {
        return super.exportXls(request, entity, GameCampaignTypeLotteryToken.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_lottery_token:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeLotteryToken.class);
    }

}
