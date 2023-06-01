/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.entities.HttpEmail;
import cn.youai.enums.OutdatedType;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.server.model.ItemVO;
import cn.youai.server.utils.ConvertUtils;
import cn.youai.server.utils.DBHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.cache.GameServerCache;
import cn.youai.xiuzhen.game.cache.GameSettingCache;
import cn.youai.xiuzhen.game.cache.GameStopServerRefundRecordCache;
import cn.youai.xiuzhen.game.constant.GameServerVersionType;
import cn.youai.xiuzhen.game.constant.GameSettingKey;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.game.mapper.GameStopServerRefundRecordMapper;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 删档返还记录 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2022-12-05
 */
@Slf4j
@Service
public class GameStopServerRefundRecordServiceImpl extends ServiceImpl<GameStopServerRefundRecordMapper, GameStopServerRefundRecord> implements IGameStopServerRefundRecordService {

    @Value("${app.send-httpemail-url}")
    private String sendHttpEmailUrl;

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Override
    public void checkSendStopServerRefund() {
        // 需要处理'停服返还充值'的服务器
        Set<Integer> stopGameServerIds = GameServerCache.getInstance().getStopServerRefundServerIds();
        if (stopGameServerIds.isEmpty()) {
            return;
        }

        log.info("[Refund] start checking...");

        // 玩家充值总金额
        Map<Long, GameOrder> playerId2GameOrderMap = getPlayerId2GameOrderMap(stopGameServerIds);
        if (playerId2GameOrderMap.isEmpty()) {
            return;
        }

        // 已处理'停服返还充值'的玩家 GameStopServerRefundRecord.sourcePlayerId
        Set<Long> refundedPlayerIds = GameStopServerRefundRecordCache.getInstance().getRecordPlayerIds(stopGameServerIds);

        // 需要处理'停服返还充值'的玩家 GamePlayer.account
        Set<String> gamePlayerAccounts = getGamePlayerAccounts(stopGameServerIds, playerId2GameOrderMap.keySet(), refundedPlayerIds);
        if (gamePlayerAccounts.isEmpty()) {
            return;
        }

        // 需要处理'停服返还充值'的玩家 Map<account, List<GamePlayer>>
        Map<String, List<GamePlayer>> account2GamePlayerMap = getAccount2GamePlayerMap(gamePlayerAccounts);

        Date current = DateUtils.now();
        List<GameStopServerRefundRecord> saveRecords = new ArrayList<>(account2GamePlayerMap.size());
        Map<Integer, List<HttpEmail>> httpEmails = new HashMap<>(stopGameServerIds.size());

        account2GamePlayerMap.forEach((k, v) -> {
            // 需要返还的 players
            List<GamePlayer> sourceGamePlayers = getSourceGamePlayers(v, stopGameServerIds, refundedPlayerIds);
            if (sourceGamePlayers.isEmpty()) {
                return;
            }
            // 返还到该服的 playerId
            GamePlayer targetGamePlayer = getTargetGamePlayer(v, stopGameServerIds);
            if (null == targetGamePlayer) {
                return;
            }
            sourceGamePlayers.forEach(sourceGamePlayer -> gamePlayerRefund(playerId2GameOrderMap, sourceGamePlayer, targetGamePlayer, saveRecords, httpEmails, current));
        });

        httpEmails.forEach((k, v) -> gameServerService.gameServerPost(CollUtil.newArrayList(k), sendHttpEmailUrl, v));
        if (!saveRecords.isEmpty()) {
            log.info("add records:{}", JSON.toJSONString(saveRecords));
            DBHelper.saveBatch(saveRecords, getClass());
            GameStopServerRefundRecordCache.getInstance().put(saveRecords);
        }

        log.info("[Refund] finish checking");
    }

    // 玩家充值总金额
    private Map<Long, GameOrder> getPlayerId2GameOrderMap(Set<Integer> gameServerIds) {
        return gameOrderMapper.sumAmountGroupByPlayerId(gameServerIds).stream()
                .filter(e -> null != e.getTotalAmount() && e.getTotalAmount() > 0)
                .collect(Collectors.toMap(GameOrder::getPlayerId, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 需要处理'停服返还充值'的玩家 GamePlayer.account
     *
     * @param gameServerIds     需要处理'停服返还充值'的服务器
     * @param gamePlayerIds     充值玩家
     * @param refundedPlayerIds 已处理'停服返还充值'的玩家 GameStopServerRefundRecord.sourcePlayerId
     * @return Set<GamePlayer.account>
     */
    private Set<String> getGamePlayerAccounts(Set<Integer> gameServerIds, Set<Long> gamePlayerIds, Set<Long> refundedPlayerIds) {
        if (CollUtil.isEmpty(gameServerIds) || CollUtil.isEmpty(gamePlayerIds)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<GamePlayer> lambdaQuery = Wrappers.<GamePlayer>lambdaQuery()
                .select(GamePlayer::getAccount)
                .in(GamePlayer::getServerId, gameServerIds)
                .in(GamePlayer::getPlayerId, gamePlayerIds);
        if (!refundedPlayerIds.isEmpty()) {
            lambdaQuery.notIn(GamePlayer::getPlayerId, refundedPlayerIds);
        }
        return gamePlayerService.list(lambdaQuery).stream().map(GamePlayer::getAccount).collect(Collectors.toSet());
    }

    private Map<String, List<GamePlayer>> getAccount2GamePlayerMap(Set<String> gamePlayerAccounts) {
        if (CollUtil.isEmpty(gamePlayerAccounts)) {
            return Collections.emptyMap();
        }
        return gamePlayerService.list(Wrappers.<GamePlayer>lambdaQuery().in(GamePlayer::getAccount, gamePlayerAccounts))
                .stream().collect(Collectors.groupingBy(GamePlayer::getAccount));
    }

    // 需要返还的 players
    private List<GamePlayer> getSourceGamePlayers(List<GamePlayer> v, Set<Integer> stopGameServerIds, Set<Long> refundedPlayerIds) {
        return v.stream().filter(e -> stopGameServerIds.contains(e.getServerId()) && !refundedPlayerIds.contains(e.getPlayerId())).collect(Collectors.toList());
    }

    // 返还到该服的 playerId
    private GamePlayer getTargetGamePlayer(List<GamePlayer> v, Set<Integer> stopGameServerIds) {
        return v.stream().filter(e -> {
            GameServer gameServer = GameServerCache.getInstance().get(e.getServerId());
            return null != gameServer && gameServer.getOutdated() == OutdatedType.NORMAL.getValue() && !stopGameServerIds.contains(e.getServerId());
        }).min(Comparator.comparing(GamePlayer::getCreateTime)).orElse(null);
    }

    private void gamePlayerRefund(Map<Long, GameOrder> playerId2GameOrderMap,
                                  GamePlayer sourceGamePlayer, GamePlayer targetGamePlayer,
                                  List<GameStopServerRefundRecord> saveRecords, Map<Integer, List<HttpEmail>> httpEmails, Date current) {

        GameOrder gameOrder = playerId2GameOrderMap.get(sourceGamePlayer.getPlayerId());
        if (null == gameOrder || null == gameOrder.getTotalAmount() || gameOrder.getTotalAmount() <= 0) {
            return;
        }

        GameServer sourceGameServer = GameServerCache.getInstance().get(sourceGamePlayer.getServerId());
        GameServerVersionType versionType = GameServerVersionType.valueOf(sourceGameServer.getVersionType());
        if (null == versionType) {
            log.error("[Refund] checkSendStopServerRefund() error, gameServer.versionType is null. versionType={}", sourceGameServer.getVersionType());
            return;
        }

        String title = null, describe = null;
        if (versionType == GameServerVersionType.BT) {
            GameSetting ratioGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
            GameSetting emailTitleGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_EMAIL_TITLE);
            GameSetting emailDescribeGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_EMAIL_DESCRIBE);
            if (null == ratioGameSetting || null == emailTitleGameSetting || null == emailDescribeGameSetting) {
                log.error("[Refund] checkSendStopServerRefund() error, bt refund GameSetting is null.");
                return;
            }
            title = emailTitleGameSetting.getDictValue();
            describe = emailDescribeGameSetting.getDictValue();
        }

        List<ItemVO> refundRewards = getRefundRewards(versionType, gameOrder.getTotalAmount());
        log.info("[Refund] playerId:{} totalAmount:{}, versionType:{} refundRewards:{}",
                sourceGamePlayer.getPlayerId(), gameOrder.getTotalAmount(), versionType, JSON.toJSONString(refundRewards));
        if (CollUtil.isEmpty(refundRewards)) {
            return;
        }
        long refundNum = refundRewards.get(0).getNum();
        if (refundNum <= 0) {
            return;
        }

        saveRecords.add(new GameStopServerRefundRecord()
                .setSourceServerVersionType(versionType.getType())
                .setSourceServerId(sourceGamePlayer.getServerId())
                .setSourcePlayerId(sourceGamePlayer.getPlayerId())
                .setTargetServerId(targetGamePlayer.getServerId())
                .setTargetPlayerId(targetGamePlayer.getPlayerId())
                .setSourceAmount(BigDecimal.valueOf(gameOrder.getTotalAmount()))
                .setTargetNum(refundNum).setCreateTime(current));

        String rewards = JSON.toJSONString(refundRewards);
        Object[] args = new Object[]{gameOrder.getTotalAmount().longValue(), refundNum};
        HttpEmail httpEmail = new HttpEmail().setPlayerId(targetGamePlayer.getPlayerId()).setRewards(rewards)
                .setRuleId(ItemRuleId.GAME_STOP_SERVER_REFUND.getType()).setByteArgs(ConvertUtils.gzipJson(args));
        if (versionType == GameServerVersionType.NORMAL) {
            httpEmail.setMailId(105);
        } else if (versionType == GameServerVersionType.BT) {
            httpEmail.setTitle(title).setDescribe(describe);
        }
        httpEmails.computeIfAbsent(targetGamePlayer.getServerId(), serverId -> new ArrayList<>()).add(httpEmail);
    }

    private List<ItemVO> getRefundRewards(GameServerVersionType versionType, double totalAmount) {
        if (versionType == null) {
            return new ArrayList<>();
        }
        long refundNum = versionType.getFunction().applyAsLong(totalAmount);
        return refundNum > 0 ? CollUtil.newArrayList(ItemVO.valueOf(versionType.getItemId(), refundNum)) : new ArrayList<>();
    }
}
