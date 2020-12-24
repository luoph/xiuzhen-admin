package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.commons.model.Response;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
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
        // 查询子页签列表
        for (OpenServiceCampaign record : pageList.getRecords()) {
            List<OpenServiceCampaignType> typeList = getCampaignTypeList(record);
            record.setTypeList(typeList);
        }
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
        campaignService.save(model);
        List<String> serverIds = StrUtil.splitTrim(model.getServerIds(), ",");
        // 排序区服id
        Collections.sort(serverIds);
        model.setServerIds(StrUtil.join(",", serverIds));
        updateTypeList(true, model);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param model 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "开服活动(1级)-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenServiceCampaign model) {
        campaignService.updateById(model);
        updateTypeList(false, model);
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

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", id);
        params.put("name", "OpenService");
        Map<String, Response> response = gameServerService.gameServerGet(allIds, campaignReloadUrl, params);
        log.info("sync id:{} response:{}", id, response);

        // 更新已刷新的服务器id
        campaign.setLastServerIds(StrUtil.join(",", currentIds));
        campaignService.updateById(new OpenServiceCampaign().setId(campaign.getId()).setServerIds(campaign.getServerIds()));

        return Result.ok("同步成功!");
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
            campaignTypeService.fillTabDetail(model, true);
        }
        return list;
    }

}
