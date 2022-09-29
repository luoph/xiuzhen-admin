package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignType;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/openServiceCampaign")
public class OpenServiceCampaignController extends JeecgController<OpenServiceCampaign, IOpenServiceCampaignService> {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameServerGroupService gameServerGroupService;

    @Autowired
    private IOpenServiceCampaignTypeService campaignTypeService;

    @Value("${app.campaign-reload-url:/campaign/reload}")
    private String campaignReloadUrl;

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @AutoLog(value = "开服活动(1级)-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaign entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "开服活动(1级)-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaign entity) {
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        entity.setServerIds(StrUtil.join(",", serverIds));
        return super.add(entity);
    }

    @AutoLog(value = "开服活动(1级)-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaign entity) {
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        String newServerIds = StrUtil.join(",", serverIds);
        if (StringUtils.equals(newServerIds, entity.getServerIds())) {
            entity.setServerIds(newServerIds);
        }
        return super.edit(entity);
    }

    @AutoLog(value = "开服活动(1级)-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "开服活动(1级)-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "开服活动(1级)-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaign entity) {
        return super.exportXls(request, entity, OpenServiceCampaign.class, "开服活动(1级)");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OpenServiceCampaign.class);
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
        OpenServiceCampaign campaign = service.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        List<String> lastIds = StrUtil.splitTrim(campaign.getLastServerIds(), ",");
        List<String> currentIds = StrUtil.splitTrim(campaign.getServerIds(), ",");
        Set<String> allIds = new HashSet<>(lastIds);
        allIds.addAll(currentIds);

        StopWatch stopWatch = new StopWatch("开服活动同步");
        // 1.通知中心服
        stopWatch.start("通知中心服重新加载");
        OkHttpHelper.get(gameCenterUrl + "/openServiceCampaign/reloadId?id=" + id);
        stopWatch.stop();

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", id);
        params.put("name", "OpenService");

        // 2.通知游戏服
        stopWatch.start("通知各游戏服重新加载");
        Map<String, Response> response = gameServerService.gameServerGet(allIds, campaignReloadUrl, params);
        log.info("sync id:{} response:{}", id, response);
        stopWatch.stop();

        // 3.通知跨服
        Map<Long, Response> gameServerGroupResponse = gameServerGroupService.gameServerGroupGetByServerIds(allIds.stream().map(Integer::parseInt).collect(Collectors.toSet()), campaignReloadUrl, params);
        log.info("sync id:{} response:{}", id, gameServerGroupResponse);

        // 4.更新已刷新的服务器id
        stopWatch.start("更新已刷新的服务器id");
        Collections.sort(currentIds);
        campaign.setLastServerIds(StrUtil.join(",", currentIds));
        service.updateById(new OpenServiceCampaign().setId(campaign.getId()).setLastServerIds(campaign.getLastServerIds()));
        stopWatch.stop();

        log.error(stopWatch.prettyPrint());
        return Result.ok("同步成功!");
    }

    @AutoLog(value = "活动配置-复制")
    @GetMapping(value = "/duplicate")
    public Result<?> duplicate(@RequestParam(name = "id") String id) {
        OpenServiceCampaign campaign = service.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        OpenServiceCampaign copy = new OpenServiceCampaign(campaign);
        service.save(copy);

        List<OpenServiceCampaignType> typeList = getCampaignTypeList(campaign);
        if (CollUtil.isNotEmpty(typeList)) {
            for (OpenServiceCampaignType entity : typeList) {
                campaignTypeService.duplicate(entity, copy.getId());
            }
        }

        return Result.ok("复制成功!");
    }

    private void updateTypeList(boolean isAdd, OpenServiceCampaign model) {
        // 过滤空的新增页签
        List<OpenServiceCampaignType> nowList = model.getTypeList().stream().filter(t -> t.getType() != null).collect(Collectors.toList());
        for (OpenServiceCampaignType item : nowList) {
            item.setCampaignId(model.getId());
        }

        List<OpenServiceCampaignType> addList = new ArrayList<>();
        List<OpenServiceCampaignType> updateList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        if (isAdd) {
            addList.addAll(nowList);
        } else {
            for (OpenServiceCampaignType item : nowList) {
                if (item.getId() == null) {
                    addList.add(item);
                }
            }

            nowList.removeAll(addList);
            updateList.addAll(nowList);

            if (!nowList.isEmpty()) {
                Map<Long, OpenServiceCampaignType> typeMap = nowList.stream().collect(Collectors.toMap(OpenServiceCampaignType::getId, Function.identity()));

                // 更新子页签
                List<OpenServiceCampaignType> typeList = getCampaignTypeList(model);
                model.setTypeList(typeList);

                for (OpenServiceCampaignType item : typeList) {
                    if (typeMap.containsKey(item.getId())) {
                        updateList.remove(item);
                    } else {
                        removeList.add(item.getId());
                    }
                }
            }
        }

        log.debug("addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        updateCampaignTypeList(addList, updateList, removeList);
    }

    private void updateCampaignTypeList(List<OpenServiceCampaignType> addList, List<OpenServiceCampaignType> updateList, List<Long> removeList) {
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeService.saveBatch(addList);
        }

        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeService.updateBatchById(updateList);
        }

        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeService.removeByIds(removeList);
        }
    }

    private List<OpenServiceCampaignType> getCampaignTypeList(OpenServiceCampaign openServiceCampaign) {
        long campaignId = openServiceCampaign.getId();
        LambdaQueryWrapper<OpenServiceCampaignType> query = Wrappers.<OpenServiceCampaignType>lambdaQuery().eq(OpenServiceCampaignType::getCampaignId, campaignId).orderByAsc(OpenServiceCampaignType::getSort);

        List<OpenServiceCampaignType> list = campaignTypeService.list(query);
        for (OpenServiceCampaignType model : list) {
            campaignTypeService.fillTabDetail(model);
        }
        return list;
    }

}
