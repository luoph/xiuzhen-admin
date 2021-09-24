package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.commons.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.OpenServiceCampaign;
import org.jeecg.modules.game.entity.OpenServiceCampaignType;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IOpenServiceCampaignService;
import org.jeecg.modules.game.service.IOpenServiceCampaignTypeService;
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
    private IOpenServiceCampaignService campaignService;

    @Autowired
    private IOpenServiceCampaignTypeService campaignTypeService;

    @Value("${app.campaign-reload-url:/campaign/reload}")
    private String campaignReloadUrl;

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    /**
     * 分页列表查询
     *
     * @param openServiceCampaign 数据实体
     * @param pageNo              页码
     * @param pageSize            分页大小
     * @param req                 请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenServiceCampaign openServiceCampaign,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OpenServiceCampaign> queryWrapper = QueryGenerator.initQueryWrapper(openServiceCampaign, req.getParameterMap());
        Page<OpenServiceCampaign> page = new Page<>(pageNo, pageSize);
        IPage<OpenServiceCampaign> pageList = campaignService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param model 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenServiceCampaign model) {
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(model.getServerIds() != null ? model.getServerIds() : "", ",");
        Collections.sort(serverIds);
        model.setServerIds(StrUtil.join(",", serverIds));
        campaignService.save(model);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
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
        campaignService.updateById(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        campaignService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.campaignService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        OpenServiceCampaign openServiceCampaign = campaignService.getById(id);
        if (openServiceCampaign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(openServiceCampaign);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param model   实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OpenServiceCampaign model) {
        return super.exportXls(request, model, OpenServiceCampaign.class, "开服活动(1级)");
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
        OpenServiceCampaign campaign = campaignService.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        List<String> lastIds = StrUtil.splitTrim(campaign.getLastServerIds(), ",");
        List<String> currentIds = StrUtil.splitTrim(campaign.getServerIds(), ",");
        Set<String> allIds = new HashSet<>(lastIds);
        allIds.addAll(currentIds);

        StopWatch stopWatch = new StopWatch("开服活动同步");
        stopWatch.start("通知中心服重新加载");
        // 通知中心服重新加载
        OkHttpHelper.getAsync(gameCenterUrl + "/openServiceCampaign/reloadId?id=" + id, null);
        stopWatch.stop();

        stopWatch.start("通知各游戏服重新加载");
        // 通知各游戏服重新加载
        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", id);
        params.put("name", "OpenService");
        Map<String, Response> response = gameServerService.gameServerGet(allIds, campaignReloadUrl, params);
        log.info("sync id:{} response:{}", id, response);
        stopWatch.stop();

        stopWatch.start("更新已刷新的服务器id");
        // 更新已刷新的服务器id
        Collections.sort(currentIds);
        campaign.setLastServerIds(StrUtil.join(",", currentIds));
        campaignService.updateById(new OpenServiceCampaign().setId(campaign.getId()).setLastServerIds(campaign.getLastServerIds()));
        stopWatch.stop();

        log.error(stopWatch.prettyPrint());
        return Result.ok("同步成功!");
    }

    @AutoLog(value = "活动配置-复制")
    @GetMapping(value = "/duplicate")
    public Result<?> duplicate(@RequestParam(name = "id") String id) {
        OpenServiceCampaign campaign = campaignService.getById(id);
        if (campaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        OpenServiceCampaign copy = new OpenServiceCampaign(campaign);
        campaignService.save(copy);

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
        LambdaQueryWrapper<OpenServiceCampaignType> query = Wrappers.<OpenServiceCampaignType>lambdaQuery()
                .eq(OpenServiceCampaignType::getCampaignId, campaignId)
                .orderByAsc(OpenServiceCampaignType::getSort);

        List<OpenServiceCampaignType> list = campaignTypeService.list(query);
        for (OpenServiceCampaignType model : list) {
            campaignTypeService.fillTabDetail(model);
        }
        return list;
    }

}
