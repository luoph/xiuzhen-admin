package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.IGameCampaignTypePartyTaskService;
import org.jeecg.modules.game.service.IGameCampaignTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
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
    private IGameCampaignTypePartyTaskService gameCampaignTypePartyTaskService;

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypePartyTask 数据实体
     * @param pageNo                    页码
     * @param pageSize                  分页大小
     * @param req                       请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypePartyTask gameCampaignTypePartyTask,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaignTypePartyTask> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypePartyTask, req.getParameterMap());
        Page<GameCampaignTypePartyTask> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaignTypePartyTask> pageList = gameCampaignTypePartyTaskService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypePartyTask 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypePartyTask gameCampaignTypePartyTask) {
        gameCampaignTypePartyTaskService.save(gameCampaignTypePartyTask);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypePartyTask 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypePartyTask gameCampaignTypePartyTask) {
        gameCampaignTypePartyTaskService.updateById(gameCampaignTypePartyTask);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameCampaignTypePartyTaskService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameCampaignTypePartyTaskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaignTypePartyTask gameCampaignTypePartyTask = gameCampaignTypePartyTaskService.getById(id);
        if (gameCampaignTypePartyTask == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaignTypePartyTask);
    }

    /**
     * 导出excel
     *
     * @param request                   请求
     * @param gameCampaignTypePartyTask 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypePartyTask gameCampaignTypePartyTask) {
        return super.exportXls(request, gameCampaignTypePartyTask, GameCampaignTypePartyTask.class, "节日派对任务");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
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
            gameCampaignTypePartyTaskService.saveBatch(taskList);
        }
        return Result.ok(vo);
    }
}
