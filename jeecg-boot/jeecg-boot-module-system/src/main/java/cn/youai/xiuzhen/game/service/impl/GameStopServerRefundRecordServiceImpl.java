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
        Set<Integer> gameServerIds = GameServerCache.getInstance().getStopServerRefundServerIds();
        if (gameServerIds.isEmpty()) {
            return;
        }

        // 充值总金额
        Map<Long, GameOrder> playerId2GameOrderMap = gameOrderMapper.sumAmountGroupByPlayerId(gameServerIds).stream()
                .filter(e -> null != e.getTotalAmount() && e.getTotalAmount() > 0)
                .collect(Collectors.toMap(GameOrder::getPlayerId, Function.identity(), (key1, key2) -> key2));
        if (playerId2GameOrderMap.isEmpty()) {
            return;
        }

        // 已处理'停服返还充值'的玩家 GameStopServerRefundRecord.sourcePlayerId
        Set<Long> recordPlayerIds = GameStopServerRefundRecordCache.getInstance().getRecordPlayerIds(gameServerIds);

        // 需要处理'停服返还充值'的玩家 GamePlayer
        LambdaQueryWrapper<GamePlayer> lambdaQuery = Wrappers.<GamePlayer>lambdaQuery()
                .select(GamePlayer::getAccount)
                .in(GamePlayer::getServerId, gameServerIds)
                .in(GamePlayer::getPlayerId, playerId2GameOrderMap.keySet());
        if (!recordPlayerIds.isEmpty()) {
            lambdaQuery.notIn(GamePlayer::getPlayerId, recordPlayerIds);
        }

        Set<String> gamePlayerAccounts = gamePlayerService.list(lambdaQuery).stream().map(GamePlayer::getAccount).collect(Collectors.toSet());
        if (gamePlayerAccounts.isEmpty()) {
            return;
        }

        // 需要处理'停服返还充值'的玩家 Map<account, List<GamePlayer>>
        Map<String, List<GamePlayer>> account2GamePlayerMap = gamePlayerService.list(Wrappers.<GamePlayer>lambdaQuery().in(GamePlayer::getAccount, gamePlayerAccounts))
                .stream().collect(Collectors.groupingBy(GamePlayer::getAccount));

        Date current = DateUtils.now();
        List<GameStopServerRefundRecord> saveRecords = new ArrayList<>(account2GamePlayerMap.size());
        Map<Integer, List<HttpEmail>> httpEmails = new HashMap<>(gameServerIds.size());

        GameSetting ratioGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
        GameSetting emailTitleGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_EMAIL_TITLE);
        GameSetting emailDescribeGameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_EMAIL_DESCRIBE);

        account2GamePlayerMap.forEach((k, v) -> {
            // 需要返还的players
            List<GamePlayer> sourceGamePlayers = v.stream().filter(e -> gameServerIds.contains(e.getServerId())
                    && !recordPlayerIds.contains(e.getPlayerId())).collect(Collectors.toList());
            if (sourceGamePlayers.isEmpty()) {
                return;
            }

            // 返还到该服的playerId
            GamePlayer targetGamePlayer = v.stream().filter(e -> {
                GameServer gameServer = GameServerCache.getInstance().get(e.getServerId());
                return null != gameServer && gameServer.getOutdated() == OutdatedType.NORMAL.getValue() && !gameServerIds.contains(e.getServerId());
            }).min(Comparator.comparing(GamePlayer::getCreateTime)).orElse(null);
            if (null == targetGamePlayer) {
                return;
            }

            sourceGamePlayers.forEach(sourceGamePlayer -> {
                GameOrder gameOrder = playerId2GameOrderMap.get(sourceGamePlayer.getPlayerId());
                if (null == gameOrder || null == gameOrder.getTotalAmount() || gameOrder.getTotalAmount() <= 0) {
                    return;
                }

                GameServer sourceGameServer = GameServerCache.getInstance().get(sourceGamePlayer.getServerId());
                GameServerVersionType versionType = GameServerVersionType.valueOf(sourceGameServer.getVersionType());
                if (null == versionType) {
                    log.error("checkSendStopServerRefund() error, gameServer.versionType is null. versionType={}", sourceGameServer.getVersionType());
                    return;
                }

                if (versionType == GameServerVersionType.BT) {
                    if (null == ratioGameSetting || null == emailTitleGameSetting || null == emailDescribeGameSetting) {
                        log.error("checkSendStopServerRefund() error, bt refund GameSetting is null.");
                        return;
                    }
                }

                List<ItemVO> refundRewards = getRefundRewards(versionType, gameOrder.getTotalAmount());
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
                    httpEmail.setTitle(emailTitleGameSetting.getDictValue()).setDescribe(emailDescribeGameSetting.getDictValue());
                }
                httpEmails.computeIfAbsent(targetGamePlayer.getServerId(), serverId -> new ArrayList<>()).add(httpEmail);
            });
        });

        httpEmails.forEach((k, v) -> gameServerService.gameServerPost(CollUtil.newArrayList(k), sendHttpEmailUrl, v));
        if (!saveRecords.isEmpty()) {
            log.info("add records:{}", JSON.toJSONString(saveRecords));
            saveBatch(saveRecords);
            GameStopServerRefundRecordCache.getInstance().put(saveRecords);
        }
    }

    private List<ItemVO> getRefundRewards(GameServerVersionType versionType, double totalAmount) {
        int itemId = 0;
        long refundNum = 0;
        if (versionType == GameServerVersionType.NORMAL) {
            itemId = 1002;
            refundNum = getNormalRefundNum(totalAmount);
        } else if (versionType == GameServerVersionType.BT) {
            itemId = 1217;
            refundNum = getBtRefundNum(totalAmount);
        }
        return refundNum > 0 ? CollUtil.newArrayList(ItemVO.valueOf(itemId, refundNum)) : null;
    }

    /**
     * 普通服: 1000以内按总金额的1200%换算，超出部分按1500%换算
     *
     * @param totalAmount 总充值金额
     * @return 返还仙玉数量
     */
    private long getNormalRefundNum(double totalAmount) {
        double amountLe1000 = Math.min(totalAmount, 1000);
        long num = (long) (amountLe1000 * 12);

        double amountGt1000 = totalAmount - 1000;
        if (amountGt1000 > 0) {
            num += amountGt1000 * 15;
        }
        return num;
    }

    private long getBtRefundNum(double totalAmount) {
        GameSetting gameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
        if (null == gameSetting) {
            log.error("getBtRefundNum() error, gameSetting is null. key={}", GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
            return 0;
        }
        return (long) (Double.parseDouble(gameSetting.getDictValue()) * 0.01 * totalAmount);
    }
}
