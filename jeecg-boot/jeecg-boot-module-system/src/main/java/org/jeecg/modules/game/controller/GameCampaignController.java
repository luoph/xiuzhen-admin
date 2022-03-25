package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
import org.jeecg.modules.game.constant.CampaignStatus;
import org.jeecg.modules.game.constant.SwitchStatus;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaign")
public class GameCampaignController extends JeecgController<GameCampaign, IGameCampaignService> {

    @Autowired
    private IGameCampaignService campaignService;

    @Autowired
    private IGameCampaignTypeService campaignTypeService;

    @Autowired
    private IGameCampaignSupportService campaignSupportService;

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameServerGroupService gameServerGroupService;

    @Value("${app.campaign-update-url:/campaign/update}")
    private String campaignUpdateUrl;

    /**
     * 分页列表查询
     *
     * @param gameCampaign 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @param req          请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaign gameCampaign,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaign> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaign, req.getParameterMap());
        Page<GameCampaign> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaign> pageList = campaignService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @GetMapping(value = "/serverList")
    public Result<?> queryServerList(GameCampaignServer campaignServer,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        if (campaignServer.getTypeId() == null) {
            return Result.ok();
        }

        Page<GameServer> page = new Page<>(pageNo, pageSize);
        Date now = DateUtils.now();

        GameCampaignType campaignType = campaignTypeService.getById(campaignServer.getTypeId());
        IPage<GameCampaignServer> pageList = campaignService.serverList(page, campaignServer.getCampaignId(),
                campaignServer.getTypeId(), campaignServer.getServer());
        for (GameCampaignServer record : pageList.getRecords()) {
            if (record.getStatus() == SwitchStatus.OFF.getValue()) {
                record.setCampaignStatus(CampaignStatus.CLOSED.getValue());
            } else if (record.getStatus() == SwitchStatus.ON.getValue()) {
                Date startTime = null;
                Date endTime = null;
                if (campaignType.getTimeType() == 1) {
                    startTime = campaignType.getStartTime();
                    endTime = campaignType.getEndTime();
                } else if (campaignType.getTimeType() == 2) {
                    startTime = DateUtils.startTimeOfDate(cn.youai.server.utils.DateUtils.addDays(record.getOpenTime(), campaignType.getStartDay()));
                    endTime = DateUtils.endTimeOfDate(cn.youai.server.utils.DateUtils.addDays(record.getOpenTime(),
                            Math.max(campaignType.getStartDay() + campaignType.getDuration() - 1, 0)));
                }
                record.setCampaignStatus(now.before(startTime) ? CampaignStatus.NOT_STARTED.getValue()
                        : now.after(endTime) ? CampaignStatus.COMPLETED.getValue()
                        : CampaignStatus.IN_PROGRESS.getValue());
            } else {
                record.setCampaignStatus(CampaignStatus.NONE.getValue());
            }
        }
        return Result.ok(pageList);
    }

    @GetMapping(value = "/serverSwitch")
    public Result<?> switchServer(GameCampaignServer model, HttpServletRequest req) {
        log.debug("switchServer {}", model);

        Wrapper<GameCampaignSupport> query = Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, model.getCampaignId())
                .eq(GameCampaignSupport::getTypeId, model.getTypeId())
                .eq(GameCampaignSupport::getServerId, model.getServerId());

        GameCampaignSupport campaignSupport = campaignSupportService.getOne(query);
        if (campaignSupport == null) {
            campaignSupport = new GameCampaignSupport()
                    .setStatus(model.getStatus())
                    .setCampaignId(model.getCampaignId())
                    .setTypeId(model.getTypeId())
                    .setServerId(model.getServerId());
            campaignSupportService.save(campaignSupport);
        } else {
            if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                campaignSupport.setStatus(model.getStatus());
                campaignSupportService.updateById(campaignSupport);
            }
        }
        return Result.ok("修改成功！");
    }

    @GetMapping(value = "/switchBatch")
    public Result<?> batchSwitch(GameCampaignServer model, HttpServletRequest req) {
        batchSwitch(model);
        return Result.ok("切换成功！");
    }

    /**
     * 添加
     *
     * @param gameCampaign 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaign gameCampaign) {
        campaignService.save(gameCampaign);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaign 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaign gameCampaign) {
        GameCampaign dbEntity = campaignService.getById(gameCampaign.getId());
        Set<String> dbServerIds = new HashSet<>(StrUtil.splitTrim(dbEntity.getServerIds(), ","));
        Set<String> newServerIds = new HashSet<>(StrUtil.splitTrim(gameCampaign.getServerIds(), ","));

        // 去重处理
        gameCampaign.setServerIds(StrUtil.join(",", newServerIds));
        List<Integer> addList = new ArrayList<>();
        List<Integer> removeList = new ArrayList<>();
        for (String serverId : newServerIds) {
            if (!dbServerIds.contains(serverId)) {
                addList.add(Integer.valueOf(serverId));
            }
        }

        for (String serverId : dbServerIds) {
            if (!newServerIds.contains(serverId)) {
                removeList.add(Integer.valueOf(serverId));
            }
        }

        campaignService.updateById(gameCampaign);

        // 批量关闭
        batchSwitchOff(gameCampaign.getId(), removeList);

        // 处理新增区服id
        if (CollUtil.isNotEmpty(newServerIds)) {
            String serverIds = StrUtil.join(",", newServerIds);
            List<GameCampaignType> typeList = getGameCampaignTypeList(gameCampaign);
            for (GameCampaignType model : typeList) {
                GameCampaignServer campaignServer = new GameCampaignServer()
                        .setCampaignId(gameCampaign.getId())
                        .setServer(serverIds)
                        .setTypeId(model.getId())
                        .setStatus(SwitchStatus.ON.getValue());
                batchSwitch(campaignServer);
            }
        }

        return Result.ok("编辑成功!");
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
        GameCampaign gameCampaign = campaignService.getById(id);
        if (gameCampaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        // 通知游戏服
        List<GameCampaignSupport> supports = campaignSupportService.list(Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, id)
                .groupBy(GameCampaignSupport::getServerId));
        List<Integer> serverIds = supports.stream().map(GameCampaignSupport::getServerId).collect(Collectors.toList());
        Map<Integer, Response> response = gameServerService.gameServerGet(serverIds, campaignUpdateUrl);
        log.info("sync id:{} response:{}", id, response);

        // 通知跨服
        Map<Long, Response> gameServerGroupResponse = gameServerGroupService.gameServerGroupGetByServerIds(serverIds, campaignUpdateUrl, null);
        log.info("sync id:{} response:{}", id, gameServerGroupResponse);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-通过id删除")
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
    @AutoLog(value = "活动配置-批量删除")
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
    @AutoLog(value = "活动配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = campaignService.getById(id);
        if (gameCampaign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaign);
    }

    /**
     * 导出excel
     *
     * @param request      请求
     * @param gameCampaign 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaign gameCampaign) {
        return super.exportXls(request, gameCampaign, GameCampaign.class, "活动配置");
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
        return super.importExcel(request, response, GameCampaign.class);
    }

    private void batchSwitchOff(long campaignId, List<Integer> serverList) {
        if (CollUtil.isNotEmpty(serverList)) {
            Wrapper<GameCampaignSupport> updateWrapper = Wrappers.<GameCampaignSupport>lambdaUpdate()
                    .set(GameCampaignSupport::getStatus, SwitchStatus.OFF.getValue())
                    .eq(GameCampaignSupport::getCampaignId, campaignId)
                    .in(GameCampaignSupport::getServerId, serverList);
            campaignSupportService.update(updateWrapper);
        }
    }

    private void batchSwitch(GameCampaignServer model) {
        int[] ids = StrUtil.splitToInt(model.getServer(), ",");
        List<GameCampaignSupport> addList = new ArrayList<>();
        List<GameCampaignSupport> updateList = new ArrayList<>();

        List<GameCampaignSupport> list = campaignSupportService.list(Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, model.getCampaignId())
                .eq(GameCampaignSupport::getTypeId, model.getTypeId()));
        for (int serverId : ids) {
            GameCampaignSupport campaignSupport = list.stream().filter(e -> e.getServerId() == serverId).findFirst().orElse(null);
            if (campaignSupport == null) {
                campaignSupport = new GameCampaignSupport()
                        .setStatus(model.getStatus())
                        .setCampaignId(model.getCampaignId())
                        .setTypeId(model.getTypeId())
                        .setServerId(serverId);
                addList.add(campaignSupport);
            } else {
                if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                    campaignSupport.setStatus(model.getStatus());
                    updateList.add(campaignSupport);
                }
            }
        }

        if (CollUtil.isNotEmpty(addList)) {
            campaignSupportService.saveBatch(addList);
        }

        if (CollUtil.isNotEmpty(updateList)) {
            campaignSupportService.updateBatchById(updateList);
        }
    }

    private List<GameCampaignType> getGameCampaignTypeList(GameCampaign gameCampaign) {
        long campaignId = gameCampaign.getId();
        LambdaQueryWrapper<GameCampaignType> query = Wrappers.<GameCampaignType>lambdaQuery()
                .eq(GameCampaignType::getCampaignId, campaignId)
                .orderByAsc(GameCampaignType::getSort);

        List<GameCampaignType> list = campaignTypeService.list(query);
        for (GameCampaignType model : list) {
            campaignTypeService.fillTabDetail(model, true);
        }
        gameCampaign.setTypeList(list);
        return list;
    }


    @AutoLog(value = "节日活动配置-复制")
    @GetMapping(value = "/duplicate")
    public Result<?> duplicate(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = campaignService.getById(id);
        if (gameCampaign == null) {
            return Result.error("找不到对应活动配置!");
        }
        GameCampaign copy = new GameCampaign(gameCampaign);
        if (campaignService.save(copy)) {
            List<GameCampaignType> campaignTypeList = campaignTypeService.list(Wrappers.<GameCampaignType>lambdaQuery()
                    .eq(GameCampaignType::getCampaignId, gameCampaign.getId()).orderByAsc(GameCampaignType::getSort));
            for (GameCampaignType gameCampaignType : campaignTypeList) {
                campaignTypeService.duplicate(gameCampaignType, copy.getId());
            }
            return Result.ok("复制成功!");
        }
        return Result.error("复制失败!");
    }

    @GetMapping(value = "/removeCompletedServer")
    public Result<?> removeCompletedServer(@RequestParam(name = "id", defaultValue = "0") String id) {
        long campaignId = Long.parseLong(id);
        GameCampaign gameCampaign = campaignService.getById(campaignId);
        if (null == gameCampaign) {
            return Result.error("主活动为空");
        }

        Set<String> serverIds = new HashSet<>(StrUtil.splitTrim(gameCampaign.getServerIds(), ","));
        if (serverIds.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        List<GameCampaignType> campaignTypeList = campaignTypeService.list(Wrappers.<GameCampaignType>lambdaQuery().eq(GameCampaignType::getCampaignId, campaignId));
        if (campaignTypeList.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        Date now = DateUtils.now();
        Set<String> reserveServerIds = new HashSet<>(serverIds.size());
        Set<String> removeServerIds = new HashSet<>(serverIds.size());
        Page<GameServer> page = new Page<>(1, Integer.MAX_VALUE);
        for (GameCampaignType campaignType : campaignTypeList) {
            IPage<GameCampaignServer> pageList = campaignService.serverList(page, campaignType.getCampaignId(), campaignType.getId(), null);
            for (GameCampaignServer record : pageList.getRecords()) {
                String strServerId = String.valueOf(record.getServerId());
                if (reserveServerIds.contains(strServerId)) {
                    continue;
                }
                setCampaignStatus(record, campaignType, now);
                if (record.getCampaignStatus() == CampaignStatus.COMPLETED.getValue()) {
                    removeServerIds.add(strServerId);
                } else {
                    reserveServerIds.add(strServerId);
                }
            }
        }

        removeServerIds.removeIf(reserveServerIds::contains);
        removeServerIds.addAll(serverIds.stream().filter(serverId -> !reserveServerIds.contains(serverId)).collect(Collectors.toSet()));
        if (removeServerIds.isEmpty()) {
            return Result.ok("没有可移除的区服");
        }

        campaignService.updateById(new GameCampaign().setId(campaignId).setServerIds(StrUtil.join(",", reserveServerIds)));
        campaignSupportService.remove(Wrappers.<GameCampaignSupport>lambdaQuery().eq(GameCampaignSupport::getCampaignId, campaignId).in(GameCampaignSupport::getServerId, removeServerIds));
        return Result.ok("已移除" + removeServerIds.size() + "个区服");
    }

    private void setCampaignStatus(GameCampaignServer record, GameCampaignType campaignType, Date date) {
        if (record.getStatus() == SwitchStatus.OFF.getValue()) {
            record.setCampaignStatus(CampaignStatus.CLOSED.getValue());
        } else if (record.getStatus() == SwitchStatus.ON.getValue()) {
            Date startTime = null;
            Date endTime = null;
            if (campaignType.getTimeType() == 1) {
                startTime = campaignType.getStartTime();
                endTime = campaignType.getEndTime();
            } else if (campaignType.getTimeType() == 2) {
                startTime = DateUtils.startTimeOfDate(DateUtils.addDays(record.getOpenTime(), campaignType.getStartDay()));
                endTime = DateUtils.endTimeOfDate(DateUtils.addDays(record.getOpenTime(),
                        Math.max(campaignType.getStartDay() + campaignType.getDuration() - 1, 0)));
            }
            record.setCampaignStatus(date.before(startTime) ? CampaignStatus.NOT_STARTED.getValue()
                    : date.after(endTime) ? CampaignStatus.COMPLETED.getValue()
                    : CampaignStatus.IN_PROGRESS.getValue());
        } else {
            record.setCampaignStatus(CampaignStatus.NONE.getValue());
        }
    }
}
