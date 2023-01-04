package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeEmailItem;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeEmailItemService;
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
 * @Description: 节日活动-邮件活动-明细
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Slf4j
@RestController
@Api(tags = "节日活动-邮件活动-明细")
@RequestMapping("/game/gameCampaignTypeEmailItem")
public class GameCampaignTypeEmailItemController extends JeecgController<GameCampaignTypeEmailItem, IGameCampaignTypeEmailItemService> {

    @Autowired
    private IGameCampaignTypeEmailItemService gameCampaignTypeEmailItemService;

    /**
     * 分页列表查询
     */
    //@AutoLog(value = "节日活动-邮件活动-明细-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeEmailItem>> queryPageList(GameCampaignTypeEmailItem gameCampaignTypeEmailItem,
                                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeEmailItem> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeEmailItem, req.getParameterMap());
        Page<GameCampaignTypeEmailItem> page = new Page<GameCampaignTypeEmailItem>(pageNo, pageSize);
        IPage<GameCampaignTypeEmailItem> pageList = gameCampaignTypeEmailItemService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "节日活动-邮件活动-明细-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_email_item:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeEmailItem gameCampaignTypeEmailItem) {
        gameCampaignTypeEmailItemService.save(gameCampaignTypeEmailItem);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "节日活动-邮件活动-明细-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_email_item:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeEmailItem gameCampaignTypeEmailItem) {
        gameCampaignTypeEmailItemService.updateById(gameCampaignTypeEmailItem);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "节日活动-邮件活动-明细-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_email_item:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeEmailItemService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "节日活动-邮件活动-明细-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_email_item:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeEmailItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    //@AutoLog(value = "节日活动-邮件活动-明细-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeEmailItem> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeEmailItem gameCampaignTypeEmailItem = gameCampaignTypeEmailItemService.getById(id);
        if (gameCampaignTypeEmailItem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeEmailItem);
    }

    /**
     * 导出excel
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_email_item:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeEmailItem gameCampaignTypeEmailItem) {
        return super.exportXls(request, gameCampaignTypeEmailItem, GameCampaignTypeEmailItem.class, CampaignType.valueOf(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     */
    //@RequiresPermissions("game_campaign_type_email_item:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeEmailItem.class);
    }

}
