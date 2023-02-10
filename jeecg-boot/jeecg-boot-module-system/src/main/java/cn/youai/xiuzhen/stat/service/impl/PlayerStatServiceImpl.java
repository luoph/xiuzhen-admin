package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.stat.constant.PlayerLogType;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import cn.youai.xiuzhen.stat.entity.PlayerBehaviorVO;
import cn.youai.xiuzhen.stat.mapper.GamePlayerMapper;
import cn.youai.xiuzhen.stat.mapper.LogAccountMapper;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import cn.youai.xiuzhen.stat.service.IPlayerStatService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@DS("shardingSphere")
public class PlayerStatServiceImpl extends ServiceImpl<GamePlayerMapper, GamePlayer> implements IPlayerStatService {

    @Autowired
    private ILogAccountService logAccountService;

    @Resource
    private LogAccountMapper logAccountMapper;

    @Override
    public IPage<PlayerBehavior> queryPlayerBehavior(Page<PlayerBehavior> page, PlayerBehaviorVO entity) {
        if (!entity.isValidServerId() && StringUtils.isBlank(entity.getNickname())) {
            return page;
        }

        Integer days = entity.getDaysType();
        Date rangeDateBegin = entity.getRangeDateBegin();
        Date rangeDateEnd = entity.getRangeDateEnd();
        if (null != days && days > 0) {
            rangeDateBegin = DateUtils.addDays(DateUtils.now(), -days);
            rangeDateEnd = DateUtils.now();
        } else {
            if (null == rangeDateBegin || null == rangeDateEnd) {
                return page;
            }
            if (DateUtils.isSameDay(rangeDateBegin, rangeDateEnd)) {
                rangeDateBegin = DateUtil.beginOfDay(rangeDateBegin);
                rangeDateEnd = DateUtil.endOfDay(rangeDateEnd);
            }
        }
        return logAccountMapper.selectBehaviorGroup(page, entity.getServerId(), entity.getNickname(), entity.getPlayerId(), rangeDateBegin, rangeDateEnd);
    }

    @Deprecated
    @Override
    public List<PlayerBehavior> queryPlayerBehavior(Date rangeDateBegin, Date rangeDateEnd, String nickname, Long playerId, int days, int serverId) {
        if (StringUtils.isBlank(nickname) && serverId <= 0) {
            return Collections.emptyList();
        }

        Date rangeDateBeginTime = rangeDateBegin;
        Date rangeDateEndTime = rangeDateEnd;
        if (days <= 0) {
            if (rangeDateBegin == null || rangeDateEnd == null) {
                return Collections.emptyList();
            }
        } else {
            rangeDateBeginTime = DateUtils.addDays(DateUtils.now(), -days);
            rangeDateEndTime = DateUtils.now();
        }

        List<PlayerBehavior> playerBehaviorList = logAccountService.selectBehaviorCount(serverId, nickname, playerId, rangeDateBeginTime, rangeDateEndTime);
        return getPlayerBehaviorList(playerBehaviorList);
    }

    @Deprecated
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
        voList = voList.stream().sorted(Comparator.comparing(PlayerBehavior::getCreateDate).reversed()).collect(Collectors.toList());
        return voList;
    }

    /**
     * 获取数据处理
     */
    @Deprecated
    private void getBehaviorTreating(List<PlayerBehavior> list, PlayerBehavior behavior) {

        // 数据筛选计算
        long practiceYear = list.stream().filter(i -> i.getType() == PlayerLogType.PRACTICE_YEAR.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setPracticeYear(practiceYear);

        long reamLevel = list.stream().filter(i -> i.getType() == PlayerLogType.REAM_LEVEL.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setReamLevel(reamLevel);

        long arenaBattle = list.stream().filter(i -> i.getType() == PlayerLogType.ARENA_BATTLE.getType()).count();
        behavior.setArenaBattle(arenaBattle);

        long mapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.MAP_EXPLORE.getType()).count();
        behavior.setMapExplore(mapExplore);

        long travelHill = list.stream().filter(i -> i.getType() == PlayerLogType.TRAVEL_HILL.getType()).count();
        behavior.setTravelHill(travelHill);

        long travelTimes = list.stream().filter(i -> i.getType() == PlayerLogType.AUTO_EXPLORE.getType()).count();
        behavior.setTravelTimes(travelTimes);

        long mainStoryLevel = list.stream().filter(i -> i.getType() == PlayerLogType.STORY_LEVEL.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setMainStoryLevel(mainStoryLevel);

        long spiritualWorldBoss = list.stream().filter(i -> i.getType() == PlayerLogType.SPIRITUAL_WORLD_BOSS.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setSpiritualWorldBoss(spiritualWorldBoss);

        long worldBoss = list.stream().filter(i -> i.getType() == PlayerLogType.WORLD_BOSS.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setWorldBoss(worldBoss);

        long playerLevel = list.stream().filter(i -> i.getType() == PlayerLogType.PLAYER_LEVEL.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setPlayerLevel(playerLevel);

        long combatPower = list.stream().filter(i -> i.getType() == PlayerLogType.COMBAT_POWER.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setCombatPower(combatPower);

        long mapLevel = list.stream().filter(i -> i.getType() == PlayerLogType.MAP_LEVEL.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setMapLevel(mapLevel);

        long lingShanLevel = list.stream().filter(i -> i.getType() == PlayerLogType.LINGSHAN_CHECKPOINT.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setLingShanLevel(lingShanLevel);

        long practiceValue = list.stream().filter(i -> i.getType() == PlayerLogType.PRACTICE_VALUE.getType()).max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
        behavior.setPracticeValue(practiceValue);


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

        long mateBoss = list.stream().filter(i -> i.getType() == PlayerLogType.MATE_BOSS.getType()).count();
        behavior.setMateBoss(mateBoss);

        long matePractice = list.stream().filter(i -> i.getType() == PlayerLogType.MATE_PRACTICE.getType()).count();
        behavior.setMatePractice(matePractice);

        long teamBoss = list.stream().filter(i -> i.getType() == PlayerLogType.TEAM_BOSS.getType()).count();
        behavior.setTeamBoss(teamBoss);

        long marryBoss = list.stream().filter(i -> i.getType() == PlayerLogType.MARRY_BOSS.getType()).count();
        behavior.setMarryBoss(marryBoss);

        long qualifying = list.stream().filter(i -> i.getType() == PlayerLogType.QUALIFYING.getType()).count();
        behavior.setQualifying(qualifying);

        long spiritStoneMapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.SPIRIT_STONE_MAP_EXPLORE.getType()).count();
        behavior.setSpiritStoneMapExplore(spiritStoneMapExplore);

        long practiceMapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.PRACTICE_MAP_EXPLORE.getType()).count();
        behavior.setPracticeMapExplore(practiceMapExplore);

        long weaponMapExplore = list.stream().filter(i -> i.getType() == PlayerLogType.WEAPON_MAP_EXPLORE.getType()).count();
        behavior.setWeaponMapExplore(weaponMapExplore);

        long immortalHigh = list.stream().filter(i -> i.getType() == PlayerLogType.IMMORTAL_HIGH.getType()).count();
        behavior.setImmortalHigh(immortalHigh);

        long immortalLow = list.stream().filter(i -> i.getType() == PlayerLogType.IMMORTAL_LOW.getType()).count();
        behavior.setImmortalLow(immortalLow);

        long monthCardHigh = list.stream().filter(i -> i.getType() == PlayerLogType.MONTH_CARD_HIGH.getType()).count();
        behavior.setMonthCardHigh(monthCardHigh);

        long monthCardLow = list.stream().filter(i -> i.getType() == PlayerLogType.MONTH_CARD_LOW.getType()).count();
        behavior.setMonthCardLow(monthCardLow);


        behavior.setMatePractice(matePractice);
        long recharge = list.stream().filter(i -> i.getType() == PlayerLogType.RECHARGE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setRecharge(BigDecimalUtils.divide(recharge, 100, 2).doubleValue());

        long consumeMoney = list.stream().filter(i -> i.getType() == PlayerLogType.CONSUME_MONEY.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setConsumeMoney(consumeMoney);

        long experience = list.stream().filter(i -> i.getType() == PlayerLogType.EXPERIENCE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setExperience(experience);

        long onlineTime = list.stream().filter(i -> i.getType() == PlayerLogType.ONLINE_TIME.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setOnlineTime(onlineTime);

        long gainRareStone = list.stream().filter(i -> i.getType() == PlayerLogType.GAIN_RARE_STONE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setGainRareStone(gainRareStone);

        long costRareStone = list.stream().filter(i -> i.getType() == PlayerLogType.COST_RARE_STONE.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setCostRareStone(costRareStone);

        long refineDan = list.stream().filter(i -> i.getType() == PlayerLogType.REFINE_DAN.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setRefineDan(refineDan);

        long refineEquip = list.stream().filter(i -> i.getType() == PlayerLogType.REFINE_EQUIP.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setRefineEquip(refineEquip);

        long spiritStoneMapSweep = list.stream().filter(i -> i.getType() == PlayerLogType.SPIRIT_STONE_MAP_SWEEP.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setSpiritStoneMapSweep(spiritStoneMapSweep);

        long practiceMapSweep = list.stream().filter(i -> i.getType() == PlayerLogType.PRACTICE_MAP_SWEEP.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setPracticeMapSweep(practiceMapSweep);

        long tierMapSweep = list.stream().filter(i -> i.getType() == PlayerLogType.TIER_MAP_SWEEP.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setTierMapSweep(tierMapSweep);

        long weaponMapSweep = list.stream().filter(i -> i.getType() == PlayerLogType.WEAPON_MAP_SWEEP.getType()).mapToLong(PlayerBehavior::getValue).sum();
        behavior.setWeaponMapSweep(weaponMapSweep);
    }
}
