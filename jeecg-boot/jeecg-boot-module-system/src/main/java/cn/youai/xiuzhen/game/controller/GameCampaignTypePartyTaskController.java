package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypePartyTask;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameCampaignTypePartyTaskService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
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
 * @description 节日派对任务
 * @date 2021-03-30
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypePartyTask")
public class GameCampaignTypePartyTaskController extends JeecgController<GameCampaignTypePartyTask, IGameCampaignTypePartyTaskService> {

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "节日派对任务-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypePartyTask entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "节日派对任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypePartyTask entity) {
        return super.add(entity);
    }

    @AutoLog(value = "节日派对任务-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypePartyTask entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "节日派对任务-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日派对任务-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日派对任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypePartyTask entity) {
        return super.exportXls(request, entity, GameCampaignTypePartyTask.class, "节日派对任务");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypePartyTask.class);
    }


    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = gameCampaignTypeService.getById(vo.getId());
        if (campaignType == null) {
            return Result.error("未找得到对应的子活动配置!");
        }

        String fileName = tempFolder + File.separator + GameCampaignTypePartyTask.class.getSimpleName() + ".xls";

        List<GameCampaignTypePartyTask> taskList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameCampaignTypePartyTask.class);
        if (CollUtil.isNotEmpty(taskList)) {
            for (GameCampaignTypePartyTask task : taskList) {
                task.setId(null);
                task.setCampaignId(campaignType.getCampaignId());
                task.setTypeId(campaignType.getId());
                task.setCreateTime(DateUtils.now());
            }
            service.saveBatch(taskList);
        }
        return Result.ok(vo);
    }
}
