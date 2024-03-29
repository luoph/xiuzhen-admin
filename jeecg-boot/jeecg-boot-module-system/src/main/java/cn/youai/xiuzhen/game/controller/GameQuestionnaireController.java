package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameQuestionnaire;
import cn.youai.xiuzhen.game.service.IGameQuestionnaireService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 问卷调查
 * @date 2021-01-13
 */
@Slf4j
@RestController
@RequestMapping("game/questionnaire")
public class GameQuestionnaireController extends JeecgController<GameQuestionnaire, IGameQuestionnaireService> {
    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.campaign-reload-url:/campaign/reload}")
    private String campaignReloadUrl;

    @AutoLog(value = "问卷调查-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameQuestionnaire entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "问卷调查-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameQuestionnaire entity) {
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        entity.setServerIds(StrUtil.join(",", serverIds));
        return super.add(entity);
    }

    @AutoLog(value = "问卷调查-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameQuestionnaire entity) {
        // 排序区服id
        List<String> serverIds = StringUtils.split2List(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        entity.setServerIds(StrUtil.join(",", serverIds));
        return super.edit(entity);
    }

    /**
     * 同步游戏配置
     *
     * @param id 实体 id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-同步到区服")
    @GetMapping(value = "/sync")
    public Result<?> sync(@RequestParam(name = "id") String id) {
        GameQuestionnaire entity = service.getById(id);
        if (entity == null) {
            return Result.error("找不到对应的配置!");
        }

        List<Integer> lastIds = StringUtils.split2Int(entity.getLastServerIds(), ",");
        List<Integer> currentIds = StringUtils.split2Int(entity.getServerIds(), ",");
        Set<Integer> allIds = new HashSet<>(lastIds);
        allIds.addAll(currentIds);

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", id);
        params.put("name", "Questionnaire");
        Map<Integer, Response> response = gameServerService.getUrl(allIds, campaignReloadUrl, params);
        log.info("sync id:{} response:{}", id, response);

        // 更新已刷新的服务器id
        Collections.sort(currentIds);
        entity.setLastServerIds(StrUtil.join(",", currentIds));

        GameQuestionnaire updateEntity = new GameQuestionnaire();
        updateEntity.setId(entity.getId());
        updateEntity.setLastServerIds(entity.getLastServerIds());
        service.updateById(updateEntity);

        return Result.ok("同步成功!");
    }

    @AutoLog(value = "问卷调查-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "问卷调查-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "问卷调查-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "问卷调查-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameQuestionnaire entity) {
        return super.exportXls(request, entity, GameQuestionnaire.class, "问卷调查");
    }

    @AutoLog(value = "问卷调查-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameQuestionnaire.class, "问卷调查");
    }

}
