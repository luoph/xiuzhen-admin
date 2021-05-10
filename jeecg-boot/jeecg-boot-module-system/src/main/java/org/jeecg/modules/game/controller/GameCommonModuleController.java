package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.Rank;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 前端公共组件后端支持
 *
 * @author huli
 */
@RestController
@RequestMapping("game/commonModule")
public class GameCommonModuleController {

    /**
     * 玩法类型前端展示选择列表
     */
    @RequestMapping("/playMethodsTypeShowList")
    public JSONObject list() {
        JSONObject response = new JSONObject();
        JSONObject result = new JSONObject();
        JSONArray record = new JSONArray();
        // 福地夺宝
        record.add(getJsonObject(PlayerLogType.ARENA_BATTLE, 38, 10));
        // 上古遗迹
        record.add(getJsonObject(PlayerLogType.TRAVEL_HILL, 40, 3));
        // 神魔战场
        record.add(getJsonObject(PlayerLogType.GHOST_WAR, 1, 38));
        // 斗灵道场
        // 北冥魔海
        record.add(getJsonObject(PlayerLogType.MAIN_STORY_BOSS, 34, 8));
        // 不死魔巢
        record.add(getJsonObject(PlayerLogType.PET_BOSS, 37, 6));
        // 蛇陵魔窟
        record.add(getJsonObject(PlayerLogType.SPIRITUAL_WORLD_BOSS, 45, 3));
        // 魔王入侵
        record.add(getJsonObject(PlayerLogType.WORLD_BOSS, 33, 1));
        // 仙器秘境
        record.add(getJsonObject(PlayerLogType.TIER_MAP_EXPLORE, 32, 3));
        // 仙兽秘境
        record.add(getJsonObject(PlayerLogType.MAP_EXPLORE, 33, 10));
        // 丹药秘境
        record.add(getJsonObject(PlayerLogType.GOD_ROAD, 35, 3));
        // 符文秘境
        record.add(getJsonObject(PlayerLogType.LINGSHAN_CHECKPOINT, 39, 1));

        // 须弥战境
        record.add(getJsonObject(PlayerLogType.TEAM_BOSS, 59, 3));
        // 结义副本
        record.add(getJsonObject(PlayerLogType.MARRY_BOSS, 1, 3));
        // 双修试练
        record.add(getJsonObject(PlayerLogType.MATE_PRACTICE, 39, 3));
        // 快速挂机
        record.add(getJsonObject(PlayerLogType.MAIN_STORY_TRAVEL_QUICK, 36, 5));
        // 修为秘境
        record.add(getJsonObject(PlayerLogType.PRACTICE_MAP_EXPLORE, 32, 3));
        // 灵石秘境
        record.add(getJsonObject(PlayerLogType.SPIRIT_STONE_MAP_EXPLORE, 0, 3));
        // 炼丹
        record.add(getJsonObject(PlayerLogType.REFINE_DAN, 0, 1));
        // 炼器
        record.add(getJsonObject(PlayerLogType.REFINE_EQUIP, 0, 1));
        // 谪仙台
        record.add(getJsonObject(PlayerLogType.QUALIFYING, 38, 10));
        result.put("record", record);
        response.put("result", result);
        return response;
    }

    private JSONObject getJsonObject(PlayerLogType playerLogType, int grade, int fullTime) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", playerLogType.getType());
        jsonObject.put("name", playerLogType.getName());
        jsonObject.put("grade", grade);
        jsonObject.put("fullTime", fullTime);
        return jsonObject;
    }

    /**
     * 玩法类型前端展示选择列表
     */
    @RequestMapping("/rankListTypeShowList")
    public List<RankListType> rankListTypeShowList() {
        List<RankListType> rankListTypeList = new ArrayList<>();
        // 战力
        rankListTypeList.add(new RankListType().setName(PlayerLogType.COMBAT_POWER.getName()).setType(PlayerLogType.COMBAT_POWER.getType()));
        // 剧情等级
        rankListTypeList.add(new RankListType().setName(PlayerLogType.STORY_LEVEL.getName()).setType(PlayerLogType.STORY_LEVEL.getType()));
        // 仙兽秘境/冒险探索
        rankListTypeList.add(new RankListType().setName(PlayerLogType.MAP_EXPLORE.getName()).setType(PlayerLogType.MAP_EXPLORE.getType()));
        // 符文秘境-小灵山
        rankListTypeList.add(new RankListType().setName(PlayerLogType.LINGSHAN_CHECKPOINT.getName()).setType(PlayerLogType.LINGSHAN_CHECKPOINT.getType()));
        return rankListTypeList;

    }

    @Data
    @Accessors(chain = true)
    private static class RankListType {
        // 类型编号
        private Integer type;
        // 类型名称
        private String name;
    }

}
