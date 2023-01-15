package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeStageTask;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeStageTaskService;
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
 * @Description: 节日活动-阶段任务
 * @Author: jeecg-boot
 * @Date: 2023-01-13
 * @Version: V1.0
 */
@Api(tags = "节日活动-阶段任务")
@RestController
@RequestMapping("/game/gameCampaignTypeStageTask")
@Slf4j
public class GameCampaignTypeStageTaskController extends JeecgController<GameCampaignTypeStageTask, IGameCampaignTypeStageTaskService> {
    @Autowired
    private IGameCampaignTypeStageTaskService gameCampaignTypeStageTaskService;

    /**
     * 分页列表查询
     *
     * @param entity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-阶段任务-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeStageTask>> queryPageList(GameCampaignTypeStageTask entity,
                                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeStageTask> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<GameCampaignTypeStageTask> page = new Page<GameCampaignTypeStageTask>(pageNo, pageSize);
        IPage<GameCampaignTypeStageTask> pageList = gameCampaignTypeStageTaskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeStageTask entity) {
        gameCampaignTypeStageTaskService.save(entity);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeStageTask entity) {
        gameCampaignTypeStageTaskService.updateById(entity);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeStageTaskService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-阶段任务-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeStageTaskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-阶段任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeStageTask> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeStageTask gameCampaignTypeStageTask = gameCampaignTypeStageTaskService.getById(id);
        if (gameCampaignTypeStageTask == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeStageTask);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entity
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_stage_task:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeStageTask entity) {
        return super.exportXls(request, entity, GameCampaignTypeStageTask.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_stage_task:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeStageTask.class);
    }

}
