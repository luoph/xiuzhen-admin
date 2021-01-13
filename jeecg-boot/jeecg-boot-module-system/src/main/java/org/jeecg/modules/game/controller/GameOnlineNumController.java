package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.GameOnlineNum;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameOnlineNumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameOnlineNum")
public class GameOnlineNumController extends JeecgController<GameOnlineNum, IGameOnlineNumService> {

    @Resource
    private IGameOnlineNumService gameOnlineNumService;

    @Resource
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     * @return {@linkplain Result}
     */
    @AutoLog(value = "在线情况-列表查询")
    @GetMapping(value = "/list")
    public JSONObject queryPageList(
                                   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId
    ) {
        if (0 == serverId) {
            JSONObject res = new JSONObject();
            res.put("message", "服务器id不能为空！");
            res.put("success", false);
            return res;
        }
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                JSONObject res = new JSONObject();
                res.put("message", "时间或就近天数不能同时为空！");
                res.put("success", false);
                return res;
            }
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameOnlineNum> gameOnlineNumList = gameOnlineNumService.queryGameOnlineNumByRangDate(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        JSONObject jsonObject = new JSONObject();
        //所有
        jsonObject.put("gameOnlineNumListAll", gameOnlineNumList);
        //分钟排序
        Map<String, List<GameOnlineNum>> gameOnlineNumListSeconds = gameOnlineNumList.stream().collect(Collectors.groupingBy( g -> DateUtils.formatDate(g.getCreateTime(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)));
        jsonObject.put("gameOnlineNumListSeconds", sortMap(gameOnlineNumListSeconds));
        //小时排序
        Map<String, List<GameOnlineNum>> gameOnlineNumListHours = gameOnlineNumList.stream().collect(Collectors.groupingBy( g -> DateUtils.formatDate(g.getCreateTime(), "yyyy-MM-dd HH")));
        jsonObject.put("gameOnlineNumListHours", sortMap(gameOnlineNumListHours));
        //天排序
        Map<String, List<GameOnlineNum>> gameOnlineNumListDay= gameOnlineNumList.stream().collect(Collectors.groupingBy( g -> DateUtils.formatDate(g.getCreateTime(), DatePattern.NORM_DATE_PATTERN)));
        jsonObject.put("gameOnlineNumListDays", sortMap(gameOnlineNumListDay));
        JSONObject res = new JSONObject();
        res.put("result", jsonObject);
        res.put("success", true);
        return res;
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "在线情况统计-列表查询")
    @GetMapping(value = "/collectList")
    public Result<?> collectList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                 @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                 @RequestParam(name = "days", defaultValue = "0") int days,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameOnlineNum> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameOnlineNum> gameOnlineNumList = gameOnlineNumService.queryGameOnlineCollectByRangDate(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        page.setRecords(gameOnlineNumList).setTotal(gameOnlineNumList.size());
        return Result.ok(page);
    }

    /**
     * 对集合排序，并封装成List<JSONObject>
     */
    private List<JSONObject> sortMap(Map<String, List<GameOnlineNum>> gameOnlineNumListSeconds) {
        List<JSONObject> gameOnlineNumList = new ArrayList<>();
        Map<String, List<GameOnlineNum>> sortMapDescDaySeconds = gameOnlineNumListSeconds.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
        for (String s : sortMapDescDaySeconds.keySet()) {
            JSONObject gameOnlineNumListSecondsJsonObject = new JSONObject();
            gameOnlineNumListSecondsJsonObject.put("onlineNum", gameOnlineNumListSeconds.get(s).size());
            gameOnlineNumListSecondsJsonObject.put("getTime", s);
            gameOnlineNumList.add(gameOnlineNumListSecondsJsonObject);
        }
        return gameOnlineNumList;
    }


}
