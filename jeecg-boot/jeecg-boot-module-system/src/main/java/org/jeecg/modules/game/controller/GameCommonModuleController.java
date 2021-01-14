package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 前端公共组件后端支持
 * @author huli
 */
@RestController
@RequestMapping("game/commonModule")
public class GameCommonModuleController {
    /**
     * 玩法类型前端展示选择列表
     */
    @RequestMapping("/playMethodsTypeShowlist")
    public JSONObject list() {
        JSONObject response = new JSONObject();
        JSONObject result = new JSONObject();
        JSONArray record = new JSONArray();
        //福地夺宝
        JSONObject arenaBattle = new JSONObject();
        arenaBattle.put("type", PlayerLogType.ARENA_BATTLE.getType() + "");
        arenaBattle.put("name", PlayerLogType.ARENA_BATTLE.getName());
        arenaBattle.put("grade", "38");
        arenaBattle.put("fullTime", "10");
        record.add(arenaBattle);
        //上古遗迹
        JSONObject travelHill = new JSONObject();
        travelHill.put("type", PlayerLogType.TRAVEL_HILL.getType() + "");
        travelHill.put("name", PlayerLogType.TRAVEL_HILL.getName());
        travelHill.put("grade", "40");
        travelHill.put("fullTime", "3");
        record.add(travelHill);
        //神魔战场
        JSONObject ghostWar = new JSONObject();
        ghostWar.put("type", PlayerLogType.GHOST_WAR.getType() + "");
        ghostWar.put("name", PlayerLogType.GHOST_WAR.getName());
        ghostWar.put("grade", "1");
        ghostWar.put("fullTime", "38");
        record.add(ghostWar);
        //斗灵道场
        //北冥魔海
        JSONObject mainStoryBoss = new JSONObject();
        mainStoryBoss.put("type", PlayerLogType.MAIN_STORY_BOSS.getType() + "");
        mainStoryBoss.put("name", PlayerLogType.MAIN_STORY_BOSS.getName());
        mainStoryBoss.put("grade", "34");
        mainStoryBoss.put("fullTime", "8");
        record.add(mainStoryBoss);
        //不死魔巢
        JSONObject petBoss = new JSONObject();
        petBoss.put("type", PlayerLogType.PET_BOSS.getType() + "");
        petBoss.put("name", PlayerLogType.PET_BOSS.getName());
        petBoss.put("grade", "37");
        petBoss.put("fullTime", "6");
        record.add(petBoss);
        //蛇陵魔窟
        JSONObject spiritualWorldBoss = new JSONObject();
        spiritualWorldBoss.put("type", PlayerLogType.SPIRITUAL_WORLD_BOSS.getType() + "");
        spiritualWorldBoss.put("name", PlayerLogType.SPIRITUAL_WORLD_BOSS.getName());
        spiritualWorldBoss.put("grade", "45");
        spiritualWorldBoss.put("fullTime", "3");
        record.add(spiritualWorldBoss);
        //魔王入侵
        JSONObject worldBoss = new JSONObject();
        worldBoss.put("type", PlayerLogType.WORLD_BOSS.getType() + "");
        worldBoss.put("name", PlayerLogType.WORLD_BOSS.getName());
        worldBoss.put("grade", "33");
        worldBoss.put("fullTime", "1");
        record.add(worldBoss);
        //仙器秘境
        JSONObject tierMapExplore = new JSONObject();
        tierMapExplore.put("type", PlayerLogType.TIER_MAP_EXPLORE.getType() + "");
        tierMapExplore.put("name", PlayerLogType.TIER_MAP_EXPLORE.getName());
        tierMapExplore.put("grade", "32");
        tierMapExplore.put("fullTime", "3");
        record.add(tierMapExplore);
        //仙兽秘境
        JSONObject mapExplore = new JSONObject();
        mapExplore.put("type", PlayerLogType.MAP_EXPLORE.getType() + "");
        mapExplore.put("name", PlayerLogType.MAP_EXPLORE.getName());
        mapExplore.put("grade", "33");
        mapExplore.put("fullTime", "10");
        record.add(mapExplore);
        //丹药秘境
        JSONObject godRoad = new JSONObject();
        godRoad.put("type", PlayerLogType.GOD_ROAD.getType() + "");
        godRoad.put("name", PlayerLogType.GOD_ROAD.getName());
        godRoad.put("grade", "35");
        godRoad.put("fullTime", "3");
        record.add(godRoad);
        //符文秘境
        JSONObject lingShanCheckpoint = new JSONObject();
        lingShanCheckpoint.put("type", PlayerLogType.LINGSHAN_CHECKPOINT.getType() + "");
        lingShanCheckpoint.put("name", PlayerLogType.LINGSHAN_CHECKPOINT.getName());
        lingShanCheckpoint.put("grade", "39");
        lingShanCheckpoint.put("fullTime", "1");
        record.add(lingShanCheckpoint);
        result.put("record", record);
        response.put("result", result);
        return response;
    }

}
