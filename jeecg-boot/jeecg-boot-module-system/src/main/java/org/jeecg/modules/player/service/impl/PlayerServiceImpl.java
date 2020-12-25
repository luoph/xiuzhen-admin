package org.jeecg.modules.player.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.mapper.PayUserRankMapper;
import org.jeecg.modules.player.entity.GameOrder;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerBehavior;
import org.jeecg.modules.player.entity.PlayerDTO;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.jeecg.modules.player.mapper.GameRegisterInfoMapper;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @ 2019-12-14
 */
@Slf4j
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private PayOrderBillMapper payOrderBillMapper;

    @Resource
    private GameRegisterInfoMapper registerInfoMapper;

    @Resource
    private PayUserRankMapper payUserRankMapper;

    @Value("${app.log.db.table_log_player}")
    private String logTable;

    @Override
    public List<Player> queryForList(PlayerDTO playerDTO) {
        List<Player> list = new ArrayList<Player>();

        // 四个stream流临时转换变量
        List<Player> collect1 = new ArrayList<Player>();
        List<Player> collect2 = new ArrayList<Player>();
        List<Player> collect3 = new ArrayList<Player>();
        List<Player> collect4 = new ArrayList<Player>();

        try {
            // 如果选择开始时间和结束时间是同一天
            String createBegin = playerDTO.getCreateBegin();
            String createEnd = playerDTO.getCreateEnd();
            String loginBegin = playerDTO.getLoginBegin();
            String loginEnd = playerDTO.getLoginEnd();
            // 时间不为空
            // todo 时间为同一天,之后需要把这种时间代码替换成工具类的方法
            if (!StringUtils.isEmpty(createBegin) && !StringUtils.isEmpty(createEnd) && createBegin.equals(createEnd)) {
                createBegin = createBegin + " 00:00:00";
                createEnd = createEnd + " 23:59:59";
            }
            playerDTO.setCreateDateBegin(DateUtils.parseDate(createBegin));
            playerDTO.setCreateDateEnd(DateUtils.parseDate(createEnd));

            if (!StringUtils.isEmpty(loginBegin) && !StringUtils.isEmpty(loginEnd) && loginBegin.equals(loginEnd)) {
                loginBegin = loginBegin + " 00:00:00";
                loginEnd = loginEnd + " 23:59:59";
            }
            playerDTO.setLoginDateBegin(DateUtils.parseDate(loginBegin));
            playerDTO.setLoginDateEnd(DateUtils.parseDate(loginEnd));

            // 获取等级范围
            if (playerDTO.getLevel() != null && !"".equals(playerDTO.getLevel())) {
                String[] level = playerDTO.getLevel().split("-");
                playerDTO.setLevelBegin(Integer.valueOf(level[0]));
                playerDTO.setLevelEnd(Integer.valueOf(level[1]));
            }
            // 获取充值范围
            if (playerDTO.getRecharge() != null && !"".equals(playerDTO.getRecharge())) {
                String[] recharge = playerDTO.getRecharge().split("-");
                playerDTO.setRechargeBegin(Double.valueOf(recharge[0]));
                playerDTO.setRechargeEnd(Double.valueOf(recharge[1]));
            }
            DataSourceHelper.useServerDatabase(playerDTO.getServerId());
            list = playerMapper.queryForList(playerDTO);
            DataSourceHelper.useDefaultDatabase();
            // 通过玩家id获取玩家累充金额
            List<GameOrder> gameOrders = payOrderBillMapper.getPayAmountSum(playerDTO.getServerId());
//            List<Player> playerTimes = payUserRankMapper.getPlayerLastLoginAndRegisterTime(playerDTO.getServerId(), logTable);

            List<Player> playerTimes = new ArrayList<>();
            //查询所有用户注册信息（唯一不重复）
            List<Map> allPlayerInfoList = playerMapper.selectAllPlayerInfo(playerDTO.getServerId());
            //查询所有用户登录信息
            List<Map> allLoginInfoList = playerMapper.selectAllLoginInfo(playerDTO.getServerId());
            //登录信息按照plaer_id分组
            Map<String, List<Map>> allLoginInfoListMap_playerId = allLoginInfoList.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
            for (Map map : allPlayerInfoList) {
                Player player = new Player();
                player.setId(Long.parseLong(map.get("player_id").toString()));
                List<Map> OnePlayerLoginInfoList = allLoginInfoListMap_playerId.get(map.get("player_id").toString()) ;
                player.setRegisterTime(DateUtils.parseDate(map.get("create_time").toString()));
                //注册过不一定登录过
                if(null == OnePlayerLoginInfoList){break;}
                player.setLastLoginTime(DateUtils.parseDate(OnePlayerLoginInfoList.get(0).get("create_time").toString()));
                playerTimes.add(player);
            }




            for (Player player : list) {
                Long playerId = player.getId();
                BigDecimal orderSum = getOrderSum(player, gameOrders);
                // 设置支付总金额
                player.setPayAmountSum(orderSum);
                // 获取玩家注册时间,获取玩家最后登录时间
                for (Player playerTime : playerTimes) {
                    if (playerId.equals(playerTime.getId())) {
                        if (playerTime.getRegisterTime() != null) {
                            player.setRegisterTime(playerTime.getRegisterTime());
                        }
                        if (playerTime.getLastLoginTime() != null) {
                            player.setLastLoginTime(playerTime.getLastLoginTime());
                        }
                        break;
                    }
                }
            }

            if (playerDTO.getCreateDateBegin() != null && playerDTO.getCreateDateEnd() != null && list != null) {

                collect1 = list.stream().filter(t -> t.getRegisterTime() != null && t.getRegisterTime().getTime() >= playerDTO.getCreateDateBegin().getTime()
                        && t.getRegisterTime().getTime() <= playerDTO.getCreateDateEnd().getTime()).collect(Collectors.toList());
            } else {
                collect1 = list;
            }

            if (playerDTO.getLoginDateBegin() != null && playerDTO.getLoginDateEnd() != null && collect1 != null) {
                collect2 = collect1.stream().filter(t -> t.getLastLoginTime() != null && t.getLastLoginTime().getTime() >= playerDTO.getLoginDateBegin().getTime()
                        && t.getRegisterTime().getTime() <= playerDTO.getLoginDateEnd().getTime()).collect(Collectors.toList());
            } else {
                collect2 = collect1;
            }

            if (playerDTO.getRechargeBegin() != null && playerDTO.getRechargeEnd() != null && collect2 != null) {
                collect3 = collect2.stream().filter(t -> t.getPayAmountSum() != null && t.getPayAmountSum().doubleValue() >= playerDTO.getRechargeBegin()
                        && t.getPayAmountSum().doubleValue() <= playerDTO.getRechargeEnd()).collect(Collectors.toList());
            } else {
                collect3 = collect2;
            }

            if (playerDTO.getLevelBegin() != null && playerDTO.getLevelEnd() != null && collect3 != null) {
                collect4 = collect3.stream().filter(t -> t.getLevel() != null && t.getLevel() >= playerDTO.getLevelBegin()
                        && t.getLevel() <= playerDTO.getLevelEnd()).collect(Collectors.toList());
                collect4 = collect3;
            } else {
                collect4 = collect3;
            }

        } catch (Exception e) {
            log.error("切换数据源异常,serverId: " + playerDTO.getServerId(), e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return collect4;
    }

    private BigDecimal getOrderSum(Player player, List<GameOrder> gameOrders) {
        double sum = gameOrders.stream().filter(o -> o.getPlayerId().equals(player.getId())).mapToDouble(o -> o.getPayAmount().doubleValue()).sum();
        return BigDecimal.valueOf(sum);
    }


    @Override
    public String getNameById(Long playerId) {
        return playerMapper.getNameById(playerId);
    }

    @Override
    public List<PlayerBehavior> queryPlayerBehavior(String rangeDateBegin, String rangeDateEnd, String nickname, int days, int serverId) {

        if (StringUtils.isBlank(nickname) && serverId <= 0) {
            return Collections.emptyList();
        }

        Date rangeDateBeginTime = null;
        Date rangeDateEndTime = null;
        if (days <= 0) {
            if (StringUtils.isBlank(rangeDateBegin) || StringUtils.isBlank(rangeDateEnd)) {
                return Collections.emptyList();
            }

            rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
        } else {
            rangeDateBeginTime = DateUtils.addDays(DateUtils.now(), -days);
            rangeDateEndTime = DateUtils.now();
        }

        List<PlayerBehavior> playerBehaviorList = registerInfoMapper.selectBehaviorCount(serverId, nickname, rangeDateBeginTime, rangeDateEndTime, logTable);

        return getPlayerBehaviorList(playerBehaviorList);
    }


    private List<PlayerBehavior> getPlayerBehaviorList(List<PlayerBehavior> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<PlayerBehavior> voList = new ArrayList<>();

        Map<Long, List<PlayerBehavior>> map = list.stream().collect(Collectors.groupingBy(PlayerBehavior::getPlayerId));
        for (Map.Entry<Long, List<PlayerBehavior>> v : map.entrySet()) {
            Long playerId = v.getKey();
            List<PlayerBehavior> behaviorList = v.getValue();
            Map<Date, List<PlayerBehavior>> dateListMap = behaviorList.stream().collect(Collectors.groupingBy(PlayerBehavior::getCreateDate));

            int serverId = behaviorList.get(0).getServerId();
            String nickname = behaviorList.get(0).getNickname();
            // 遍历map
            for (Map.Entry<Date, List<PlayerBehavior>> vo : dateListMap.entrySet()) {
                Date key = vo.getKey();
                List<PlayerBehavior> behaviorListOne = vo.getValue();
                PlayerBehavior playerBehavior = new PlayerBehavior();
                playerBehavior.setPlayerId(playerId);
                // 数据筛选计算
                getBehaviorTreating(behaviorListOne, playerBehavior);
                // 设置日期
                playerBehavior.setCreateDate(key);
                playerBehavior.setServerId(serverId);
                playerBehavior.setPlayerId(playerId);
                playerBehavior.setNickname(nickname);
                voList.add(playerBehavior);
            }
        }
        return voList;
    }

    /**
     * 获取数据处理
     */
    private void getBehaviorTreating(List<PlayerBehavior> list, PlayerBehavior behavior) {

        // 数据筛选计算
        long practiceYear = list.stream().filter(i -> i.getType() == PlayerLogType.PRACTICE_YEAR.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setPracticeYear(practiceYear);

        long reamLevel = list.stream().filter(i -> i.getType() == PlayerLogType.REAM_LEVEL.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setReamLevel(reamLevel);

        long arenaBattle = list.stream().filter(i -> i.getType() == PlayerLogType.ARENA_BATTLE.getType()).count();
        behavior.setArenaBattle(arenaBattle);

        long mapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.MAP_EXPLORE.getType()).count();
        behavior.setMapExplore(mapExplore);

        long travelHill = list.stream().filter(i -> i.getType() == PlayerLogType.TRAVEL_HILL.getType()).count();
        behavior.setTravelHill(travelHill);

        long travelTimes = list.stream().filter(i -> i.getType() == PlayerLogType.AUTO_EXPLORE.getType()).count();
        behavior.setTravelTimes(travelTimes);

        long mainStoryLevel = list.stream().filter(i -> i.getType() == PlayerLogType.STORY_LEVEL.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setMainStoryLevel(mainStoryLevel);

        long spiritualWorldBoss = list.stream().filter(i -> i.getType() == PlayerLogType.SPIRITUAL_WORLD_BOSS.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setSpiritualWorldBoss(spiritualWorldBoss);

        long worldBoss = list.stream().filter(i -> i.getType() == PlayerLogType.WORLD_BOSS.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setWorldBoss(worldBoss);

        long playerLevel = list.stream().filter(i -> i.getType() == PlayerLogType.PLAYER_LEVEL.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setPlayerLevel(playerLevel);

        long combatPower = list.stream().filter(i -> i.getType() == PlayerLogType.COMBAT_POWER.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setCombatPower(combatPower);

        long mapLevel = list.stream().filter(i -> i.getType() == PlayerLogType.MAP_LEVEL.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setMapLevel(mapLevel);

        long lingShanLevel = list.stream().filter(i -> i.getType() == PlayerLogType.LINGSHAN_CHECKPOINT.getType())
                .max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setLingShanLevel(lingShanLevel);

        long mainStoryCheck = list.stream().filter(i -> i.getType() == PlayerLogType.MAIN_STORY_LEVEL.getType()).count();
        behavior.setMainStoryCheck(mainStoryCheck);

        long ghostWar = list.stream().filter(i -> i.getType() == PlayerLogType.GHOST_WAR.getType()).count();
        behavior.setGhostWar(ghostWar);

        long mainStoryBoss = list.stream().filter(i -> i.getType() == PlayerLogType.MAIN_STORY_BOSS.getType()).count();
        behavior.setMainStoryBoss(mainStoryBoss);

        long tierMapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.TIER_MAP_EXPLORE.getType()).count();
        behavior.setTierMapExplore(tierMapExplore);

        long godRoad = list.stream().filter(i -> i.getType() == PlayerLogType.GOD_ROAD.getType()).count();
        behavior.setGodRoad(godRoad);

        long petBoss = list.stream().filter(i -> i.getType() == PlayerLogType.PET_BOSS.getType()).count();
        behavior.setPetBoss(petBoss);

        long factionBanquet = list.stream().filter(i -> i.getType() == PlayerLogType.FACTION_BANQUET.getType()).count();
        behavior.setFactionBanquet(factionBanquet);

        long recharge = list.stream().filter(i -> i.getType() == PlayerLogType.RECHARGE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setRecharge(BigDecimalUtil.divide(recharge, 100, 2).doubleValue());

        long consumeMoney = list.stream().filter(i -> i.getType() == PlayerLogType.CONSUME_MONEY.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setConsumeMoney(consumeMoney);

        long experience = list.stream().filter(i -> i.getType() == PlayerLogType.EXPERIENCE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setExperience(experience);

        long onlineTime = list.stream().filter(i -> i.getType() == PlayerLogType.ONLINE_TIME.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setOnlineTime(onlineTime);
    }
}
