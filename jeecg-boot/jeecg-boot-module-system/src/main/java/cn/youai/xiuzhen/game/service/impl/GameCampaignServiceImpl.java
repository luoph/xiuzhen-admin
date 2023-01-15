package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.CampaignStatus;
import cn.youai.xiuzhen.game.constant.SwitchStatus;
import cn.youai.xiuzhen.game.constant.TimeType;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.mapper.GameCampaignMapper;
import cn.youai.xiuzhen.game.service.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Slf4j
@Service
@DS("master")
public class GameCampaignServiceImpl extends ServiceImpl<GameCampaignMapper, GameCampaign> implements IGameCampaignService {
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

    @Override
    public List<GameCampaignType> getGameCampaignTypeList(GameCampaign gameCampaign) {
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

    @Override
    public void updateCampaign(GameCampaign gameCampaign) {
        GameCampaign dbEntity = getById(gameCampaign.getId());
        Set<String> dbServerIds = StringUtils.split2Set(dbEntity.getServerIds());
        Set<String> newServerIds = StringUtils.split2Set(gameCampaign.getServerIds());

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

        updateById(gameCampaign);

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
    }

    @Override
    public void batchSwitch(GameCampaignServer model) {
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

    private void batchSwitchOff(long campaignId, List<Integer> serverList) {
        if (CollUtil.isNotEmpty(serverList)) {
            Wrapper<GameCampaignSupport> updateWrapper = Wrappers.<GameCampaignSupport>lambdaUpdate()
                    .set(GameCampaignSupport::getStatus, SwitchStatus.OFF.getValue())
                    .eq(GameCampaignSupport::getCampaignId, campaignId)
                    .in(GameCampaignSupport::getServerId, serverList);
            campaignSupportService.update(updateWrapper);
        }
    }

    @Override
    public IPage<GameCampaignServer> serverList(Page<?> page, long campaignId, long typeId, String server) {
        return getBaseMapper().serverList(page, campaignId, typeId, server);
    }

    @Override
    public void syncCampaign(GameCampaign campaign) {
        // 通知游戏服
        Wrapper<GameCampaignSupport> queryWrapper = Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, campaign.getId())
                .groupBy(GameCampaignSupport::getServerId);
        List<GameCampaignSupport> supports = campaignSupportService.list(queryWrapper);
        List<Integer> serverIds = supports.stream().map(GameCampaignSupport::getServerId).collect(Collectors.toList());
        Map<Integer, Response> response = gameServerService.gameServerGet(serverIds, campaignUpdateUrl);
        log.info("sync to server, campaignId:{} response:{}", campaign.getId(), response);

        // 通知跨服
        Map<Long, Response> gameServerGroupResponse = gameServerGroupService.gameServerGroupGetByServerIds(serverIds, campaignUpdateUrl, null);
        log.info("sync to server group, campaignId:{} response:{}", campaign.getId(), gameServerGroupResponse);
    }

    @Override
    public Result<?> removeCompletedServer(GameCampaign gameCampaign) {
        Set<String> serverIds = new HashSet<>(StrUtil.splitTrim(gameCampaign.getServerIds(), ","));
        if (serverIds.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        Wrapper<GameCampaignType> query = Wrappers.<GameCampaignType>lambdaQuery().eq(GameCampaignType::getCampaignId, gameCampaign.getId());
        List<GameCampaignType> campaignTypeList = campaignTypeService.list(query);
        if (campaignTypeList.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        if (campaignTypeList.stream().anyMatch(e -> e.getCross() == 1)) {
            return Result.ok("存在跨服子活动，不支持移除");
        }

        Date current = DateUtils.now();
        if (campaignTypeList.stream().anyMatch(e -> e.getTimeType() == TimeType.TIME_RANGE.getType()
                && null != e.getStartTime() && null != e.getEndTime() && e.getEndTime().after(current))) {
            return Result.ok("没有可移除的区服");
        }

        Set<String> reserveServerIds = new HashSet<>(serverIds.size());
        Set<String> removeServerIds = new HashSet<>(serverIds.size());
        Page<GameServer> page = new Page<>(1, Integer.MAX_VALUE);

        for (GameCampaignType campaignType : campaignTypeList) {
            IPage<GameCampaignServer> pageList = serverList(page, campaignType.getCampaignId(), campaignType.getId(), null);
            for (GameCampaignServer record : pageList.getRecords()) {
                String strServerId = String.valueOf(record.getServerId());
                if (reserveServerIds.contains(strServerId)) {
                    continue;
                }
                setCampaignStatus(record, campaignType, current);
                if (record.getCampaignStatus() == CampaignStatus.COMPLETED.getValue()) {
                    removeServerIds.add(strServerId);
                } else {
                    reserveServerIds.add(strServerId);
                }
            }
        }

        removeServerIds.removeIf(reserveServerIds::contains);
        if (removeServerIds.isEmpty()) {
            return Result.ok("没有可移除的区服");
        }

        if (serverIds.removeIf(removeServerIds::contains)) {
            updateById(new GameCampaign().setId(gameCampaign.getId()).setServerIds(StrUtil.join(",", serverIds)));
        }

        campaignSupportService.remove(Wrappers.<GameCampaignSupport>lambdaQuery().eq(GameCampaignSupport::getCampaignId, gameCampaign.getId()).in(GameCampaignSupport::getServerId, removeServerIds));
        return Result.ok("已移除" + removeServerIds.size() + "个区服");
    }

    private void setCampaignStatus(GameCampaignServer record, GameCampaignType campaignType, Date date) {
        if (record.getStatus() == SwitchStatus.OFF.getValue()) {
            record.setCampaignStatus(CampaignStatus.CLOSED.getValue());
        } else if (record.getStatus() == SwitchStatus.ON.getValue()) {
            Date startTime = null;
            Date endTime = null;
            if (campaignType.getTimeType() == TimeType.TIME_RANGE.getType()) {
                startTime = campaignType.getStartTime();
                endTime = campaignType.getEndTime();
            } else if (campaignType.getTimeType() == TimeType.OPEN_DAY.getType()) {
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
