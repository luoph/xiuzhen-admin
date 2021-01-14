package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    // TODO url命名错误(小驼峰)
    @RequestMapping("/playMethodsTypeShowlist")
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

}
