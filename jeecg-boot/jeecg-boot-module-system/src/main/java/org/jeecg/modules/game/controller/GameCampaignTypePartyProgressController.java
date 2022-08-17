package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.GameCampaignType;
import org.jeecg.modules.game.entity.GameCampaignTypePartyProgress;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.service.IGameCampaignTypePartyProgressService;
import org.jeecg.modules.game.service.IGameCampaignTypeService;
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
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypePartyProgress")
public class GameCampaignTypePartyProgressController extends JeecgController<GameCampaignTypePartyProgress, IGameCampaignTypePartyProgressService> {

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypePartyProgress entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypePartyProgress entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypePartyProgress entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日派对进度任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypePartyProgress entity) {
        return super.exportXls(request, entity, GameCampaignTypePartyProgress.class, "节日派对进度任务");
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
        return super.importExcel(request, response, GameCampaignTypePartyProgress.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = gameCampaignTypeService.getById(vo.getId());
        if (campaignType == null) {
            return Result.error("未找得到对应的子活动配置!");
        }

        String fileName = tempFolder + File.separator + GameCampaignTypePartyProgress.class.getSimpleName() + ".xls";

        List<GameCampaignTypePartyProgress> progressList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameCampaignTypePartyProgress.class);
        if (CollUtil.isNotEmpty(progressList)) {
            for (GameCampaignTypePartyProgress progress : progressList) {
                progress.setId(null);
                progress.setCampaignId(campaignType.getCampaignId());
                progress.setTypeId(campaignType.getId());
                progress.setCreateTime(DateUtils.now());
            }
            service.saveBatch(progressList);
        }
        return Result.ok(vo);
    }

}
