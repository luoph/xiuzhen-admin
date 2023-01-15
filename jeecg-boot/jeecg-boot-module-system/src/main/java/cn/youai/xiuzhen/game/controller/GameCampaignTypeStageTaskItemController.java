package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeStageTaskItem;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeStageTaskItemService;
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
 * @Description: 节日活动-阶段任务-任务
 * @Author: jeecg-boot
 * @Date: 2023-01-13
 * @Version: V1.0
 */
@Api(tags = "节日活动-阶段任务-任务")
@RestController
@RequestMapping("/game/gameCampaignTypeStageTaskItem")
@Slf4j
public class GameCampaignTypeStageTaskItemController extends JeecgController<GameCampaignTypeStageTaskItem, IGameCampaignTypeStageTaskItemService> {
    @Autowired
    private IGameCampaignTypeStageTaskItemService gameCampaignTypeStageTaskItemService;

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    /**
     * 分页列表查询
     *
     * @param entity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-阶段任务-任务-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeStageTaskItem>> queryPageList(GameCampaignTypeStageTaskItem entity,
                                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeStageTaskItem> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<GameCampaignTypeStageTaskItem> page = new Page<GameCampaignTypeStageTaskItem>(pageNo, pageSize);
        IPage<GameCampaignTypeStageTaskItem> pageList = gameCampaignTypeStageTaskItemService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-任务-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task_item:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeStageTaskItem entity) {
        gameCampaignTypeStageTaskItemService.save(entity);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-任务-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task_item:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeStageTaskItem entity) {
        gameCampaignTypeStageTaskItemService.updateById(entity);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-任务-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task_item:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeStageTaskItemService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-任务-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task_item:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeStageTaskItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-阶段任务-任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeStageTaskItem> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeStageTaskItem gameCampaignTypeStageTaskItem = gameCampaignTypeStageTaskItemService.getById(id);
        if (gameCampaignTypeStageTaskItem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeStageTaskItem);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entity
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task_item:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeStageTaskItem entity) {
        return super.exportXls(request, entity, GameCampaignTypeStageTaskItem.class, CampaignType.STAGE_TASK.getName() + "-任务");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_stage_task_item:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
        return gameCampaignTypeService.importExcel(campaignId, typeId, request, CampaignType.STAGE_TASK.getName() + "-任务", service.getClass());
    }

}
