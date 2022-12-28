package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeTask;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeTaskService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeTask")
public class GameCampaignTypeTaskController extends JeecgController<GameCampaignTypeTask, IGameCampaignTypeTaskService> {

    @AutoLog(value = "任务活动-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeTask entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "任务活动-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeTask entity) {
        return super.add(entity);
    }

    @AutoLog(value = "任务活动-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeTask entity) {
        service.updateById(entity);
        if (entity.getJumpId() == null) {
            GameCampaignTypeTask task = new GameCampaignTypeTask();
            task.setId(entity.getId());
            service.update(Wrappers.lambdaUpdate(task).set(GameCampaignTypeTask::getJumpId, null));
        }
        return Result.ok("编辑成功!");
    }

    @AutoLog(value = "任务活动-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "任务活动-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "任务活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "任务活动-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeTask entity) {
        return super.exportXls(request, entity, GameCampaignTypeTask.class, CampaignType.valueOf(service.getClass()).getName());
    }

    @AutoLog(value = "任务活动-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeTask.class);
    }

}
