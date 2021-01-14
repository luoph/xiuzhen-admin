package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端公共组件后端支持
 */
@RestController
@RequestMapping("game/commonModule")
public class GameCommonModuleController {
        /**
         *  玩法类型前端展示选择列表
         */
        @AutoLog(value = "")
        @RequestMapping("/playMethodsTypeShowlist")
        public JSONObject List() {
            JSONObject response = new JSONObject();
            JSONObject result = new JSONObject();
            JSONArray record = new JSONArray();
            //福地夺宝
            JSONObject arena_battle = new JSONObject();
            arena_battle.put("type",PlayerLogType.ARENA_BATTLE.getType()+"");
            arena_battle.put("name",PlayerLogType.ARENA_BATTLE.getName());
            arena_battle.put("grade","38");
            arena_battle.put("fullTime","10");
            record.add(arena_battle);
            //上古遗迹
            JSONObject travel_hill = new JSONObject();
            travel_hill.put("type",PlayerLogType.TRAVEL_HILL.getType()+"");
            travel_hill.put("name",PlayerLogType.TRAVEL_HILL.getName());
            travel_hill.put("grade","40");
            travel_hill.put("fullTime","3");
            record.add(travel_hill);
            //神魔战场
            JSONObject ghost_war = new JSONObject();
            ghost_war.put("type",PlayerLogType.GHOST_WAR.getType()+"");
            ghost_war.put("name",PlayerLogType.GHOST_WAR.getName());
            ghost_war.put("grade","1");
            ghost_war.put("fullTime","38");
            record.add(ghost_war);
            //斗灵道场
            //北冥魔海
            JSONObject main_story_boss = new JSONObject();
            main_story_boss.put("type",PlayerLogType.MAIN_STORY_BOSS.getType()+"");
            main_story_boss.put("name",PlayerLogType.MAIN_STORY_BOSS.getName());
            main_story_boss.put("grade","34");
            main_story_boss.put("fullTime","8");
            record.add(main_story_boss);
            //不死魔巢
            JSONObject pet_boss = new JSONObject();
            pet_boss.put("type",PlayerLogType.PET_BOSS.getType()+"");
            pet_boss.put("name",PlayerLogType.PET_BOSS.getName());
            pet_boss.put("grade","37");
            pet_boss.put("fullTime","6");
            record.add(pet_boss);
            //蛇陵魔窟
            JSONObject spiritual_world_boss = new JSONObject();
            spiritual_world_boss.put("type",PlayerLogType.SPIRITUAL_WORLD_BOSS.getType()+"");
            spiritual_world_boss.put("name",PlayerLogType.SPIRITUAL_WORLD_BOSS.getName());
            spiritual_world_boss.put("grade","45");
            spiritual_world_boss.put("fullTime","3");
            record.add(spiritual_world_boss);
            //魔王入侵
            JSONObject world_boss = new JSONObject();
            world_boss.put("type",PlayerLogType.WORLD_BOSS.getType()+"");
            world_boss.put("name",PlayerLogType.WORLD_BOSS.getName());
            world_boss.put("grade","33");
            world_boss.put("fullTime","1");
            record.add(world_boss);
            //仙器秘境
            JSONObject tier_map_explore = new JSONObject();
            tier_map_explore.put("type",PlayerLogType.TIER_MAP_EXPLORE.getType()+"");
            tier_map_explore.put("name",PlayerLogType.TIER_MAP_EXPLORE.getName());
            tier_map_explore.put("grade","32");
            tier_map_explore.put("fullTime","3");
            record.add(tier_map_explore);
            //仙兽秘境
            JSONObject map_explore = new JSONObject();
            map_explore.put("type",PlayerLogType.MAP_EXPLORE.getType()+"");
            map_explore.put("name",PlayerLogType.MAP_EXPLORE.getName());
            map_explore.put("grade","33");
            map_explore.put("fullTime","10");
            record.add(map_explore);
            //丹药秘境
            JSONObject god_road = new JSONObject();
            god_road.put("type",PlayerLogType.GOD_ROAD.getType()+"");
            god_road.put("name",PlayerLogType.GOD_ROAD.getName());
            god_road.put("grade","35");
            god_road.put("fullTime","3");
            record.add(god_road);
            //符文秘境
            JSONObject lingshan_checkpoint = new JSONObject();
            lingshan_checkpoint.put("type",PlayerLogType.LINGSHAN_CHECKPOINT.getType()+"");
            lingshan_checkpoint.put("name",PlayerLogType.LINGSHAN_CHECKPOINT.getName());
            lingshan_checkpoint.put("grade","39");
            lingshan_checkpoint.put("fullTime","1");
            record.add(lingshan_checkpoint);
            result.put("record",record);
            response.put("result",result);
            return response;
        }

}
