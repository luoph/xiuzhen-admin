package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.cache.GameServerCache;
import cn.youai.xiuzhen.game.constant.OpenServiceType;
import cn.youai.xiuzhen.game.constant.TimeType;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignType;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignMapper;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignTypeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

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
@Service
@DS("master")
public class OpenServiceCampaignServiceImpl extends ServiceImpl<GameOpenServiceCampaignMapper, OpenServiceCampaign> implements IOpenServiceCampaignService {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IOpenServiceCampaignTypeService campaignTypeService;

    @Autowired
    private IGameServerGroupService gameServerGroupService;

    @Value("${app.campaign-reload-url:/campaign/reload}")
    private String campaignReloadUrl;

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @Override
    public void duplicate(OpenServiceCampaign other) {
    }

    @Override
    public List<OpenServiceCampaignType> getCampaignTypeList(OpenServiceCampaign openServiceCampaign, boolean isLoadDetailExt) {
        long campaignId = openServiceCampaign.getId();
        LambdaQueryWrapper<OpenServiceCampaignType> query = Wrappers.<OpenServiceCampaignType>lambdaQuery()
                .eq(OpenServiceCampaignType::getCampaignId, campaignId).orderByAsc(OpenServiceCampaignType::getSort);
        List<OpenServiceCampaignType> list = campaignTypeService.list(query);
        for (OpenServiceCampaignType model : list) {
            campaignTypeService.fillTabDetail(model, isLoadDetailExt);
        }
        return list;
    }

    @Override
    public Result<?> removeCompletedServer(OpenServiceCampaign campaign) {
        if (campaign.getCross() == 1) {
            return Result.ok("跨服活动，不支持移除");
        }

        Set<String> serverIds = StringUtils.split2Set(campaign.getServerIds());
        if (serverIds.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        List<OpenServiceCampaignType> campaignTypeList = getCampaignTypeList(campaign, false);
        if (campaignTypeList.isEmpty()) {
            return Result.ok("该活动没有支持的区服");
        }

        Date current = DateUtils.now();
        Set<String> reserveServerIds = new HashSet<>(serverIds.size());
        Set<String> removeServerIds = new HashSet<>(serverIds.size());

        for (String serverId : serverIds) {
            GameServer gameServer = GameServerCache.getInstance().get(Integer.parseInt(serverId));
            if (null == gameServer) {
                continue;
            }

            for (OpenServiceCampaignType campaignType : campaignTypeList) {
                OpenServiceType openServiceType = OpenServiceType.valueOf(campaignType.getType());
                if (null == openServiceType || CollUtil.isEmpty(campaignType.getDetails())) {
                    continue;
                }

                List<? extends OpenServiceCampaignDetail> details = (List<? extends OpenServiceCampaignDetail>) campaignType.getDetails();
                if (details.stream().anyMatch(e -> e.getTimeType() == TimeType.TIME_RANGE.getType()
                        && null != e.getStartTime() && null != e.getEndTime() && e.getEndTime().after(current))) {
                    return Result.ok("没有可移除的区服");
                }

                int maxDay = details.stream().filter(e -> e.getTimeType() == TimeType.OPEN_DAY.getType()).mapToInt(e -> Math.max(e.getStartDay() + e.getDuration() - 1, 0)).max().orElse(0);
                Date maxEndTime = DateUtils.endTimeOfDate(DateUtils.addDays(gameServer.getOpenTime(), maxDay));
                boolean isCompleted = maxEndTime.before(current);
                if (isCompleted) {
                    removeServerIds.add(serverId);
                } else {
                    reserveServerIds.add(serverId);
                    break;
                }
            }
        }

        if (reserveServerIds.containsAll(serverIds)) {
            return Result.ok("没有可移除的区服");
        }

        removeServerIds.removeIf(reserveServerIds::contains);
        if (removeServerIds.isEmpty()) {
            return Result.ok("没有可移除的区服");
        }

        if (serverIds.removeIf(removeServerIds::contains)) {
            ArrayList<String> serverIdList = new ArrayList<>(serverIds);
            Collections.sort(serverIdList);
            updateById(new OpenServiceCampaign().setId(campaign.getId())
                    .setServerIds(StrUtil.join(",", serverIdList)));
        }
        return Result.ok("已移除" + removeServerIds.size() + "个区服");
    }

    @Override
    public void syncCampaign(OpenServiceCampaign campaign) {
        List<String> lastIds = StrUtil.splitTrim(campaign.getLastServerIds(), ",");
        List<String> currentIds = StrUtil.splitTrim(campaign.getServerIds(), ",");
        Set<String> allIds = new HashSet<>(lastIds);
        allIds.addAll(currentIds);

        StopWatch stopWatch = new StopWatch("开服活动同步");
        // 1.通知中心服
        stopWatch.start("通知中心服重新加载");
        OkHttpHelper.get(gameCenterUrl + "/openServiceCampaign/reloadId?id=" + campaign.getId());
        stopWatch.stop();

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", campaign.getId());
        params.put("name", "OpenService");

        // 2.通知游戏服
        stopWatch.start("通知各游戏服重新加载");
        Map<String, Response> response = gameServerService.gameServerGet(allIds, campaignReloadUrl, params);
        log.info("sync id:{} response:{}", campaign.getId(), response);
        stopWatch.stop();

        // 3.通知跨服
        Map<Long, Response> gameServerGroupResponse = gameServerGroupService.gameServerGroupGetByServerIds(allIds.stream().map(Integer::parseInt)
                .collect(Collectors.toSet()), campaignReloadUrl, params);
        log.info("sync id:{} response:{}", campaign.getId(), gameServerGroupResponse);

        // 4.更新已刷新的服务器id
        stopWatch.start("更新已刷新的服务器id");
        Collections.sort(currentIds);
        campaign.setLastServerIds(StrUtil.join(",", currentIds));
        updateById(new OpenServiceCampaign().setId(campaign.getId()).setLastServerIds(campaign.getLastServerIds()));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
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
                List<OpenServiceCampaignType> typeList = getCampaignTypeList(model, true);
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

}
