package org.jeecg.modules.player.controller;

import cn.youai.commons.model.ResponseCode;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.constant.ItemId;
import org.jeecg.modules.game.constant.ItemReduce;
import org.jeecg.modules.game.constant.ItemRuleId;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.GamePlayerItemLog;
import org.jeecg.modules.player.service.BackpackLogService;
import org.jeecg.modules.player.service.IGamePlayerItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@RestController
@RequestMapping("player/playerItemLog")
public class PlayerItemLogController extends JeecgController<GamePlayerItemLog, IGamePlayerItemLogService> {

    @Autowired
    private IGamePlayerItemLogService playerItemLogService;
    @Autowired
    private IGameServerService gameServerService;
    @Autowired
    private BackpackLogService backpackLogService;

    /**
     * 分页列表查询
     *
     * @param playerItemLog 数据实体
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_player_item_log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayerItemLog playerItemLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) throws UnsupportedEncodingException {

        if (playerItemLog.getServerId() == null || playerItemLog.getServerId() <= 0) {
            return Result.error("请选择服务器！");
        }
        ItemId itemId = new ItemId();
        //道具map
        Map<String, String> itemNameToIdMap = itemId.getItemNameToIdMap();
        Map<String, String> itemIdToNameMap = itemId.getItemIdToNameMap();
        //根据前端输入的道具名称模糊匹配到的对应id集合
        List<Integer> wayList = new ArrayList<>();
        String regex = null == playerItemLog.getItemIdName() ? "":playerItemLog.getItemIdName();
        for (String s : itemNameToIdMap.keySet()) {
            if(s.contains(regex) && !"".equals(regex)){
                wayList.add(Integer.parseInt(itemNameToIdMap.get(s)));
            }
        }

        try {
            DataSourceHelper.useServerDatabase(playerItemLog.getServerId());

            List<GamePlayerItemLog> recordList = new ArrayList<>();
            //对模糊匹配的所有道具id进行查询，并将结果放入recordList中
            if(wayList.size() > 0 ){
                String itemIdStr = "";
                for (int i = 0; i < wayList.size(); i++) {
                    if(i == wayList.size()-1){
                        itemIdStr= itemIdStr +wayList.get(i);
                    }else{
                        itemIdStr= itemIdStr +wayList.get(i)+",";
                    }

                }
                LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = getQueryWrapper(playerItemLog, itemIdStr);
                Page<GamePlayerItemLog> page = new Page<>(pageNo, 1000);
                IPage<GamePlayerItemLog> pageList0= playerItemLogService.page(page, queryWrapper);

                for (GamePlayerItemLog record : pageList0.getRecords()) {

                    //道具名称
                    if(null != itemIdToNameMap.get(record.getItemId().toString())){
                        record.setItemIdName(itemIdToNameMap.get(record.getItemId().toString()));
                    }
                    //途径名称
                    if(1 == record.getType()){
                        //获得
                        record.setWayName(ItemRuleId.valueOf(record.getWay()).getName());
                    }else if(2 == record.getType()){
                        //使用
                        record.setWayName(ItemReduce.valueOf(record.getWay()).getName());
                    }
                    recordList.add(record);
                }
//
//                IPage<GamePlayerItemLog> pageList = new Page<>();
//                pageList.setRecords(recordList).setTotal(recordList.size());
                return Result.ok(pageList0);
            }else{
                LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = getQueryWrapper(playerItemLog,"");
                Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
                IPage<GamePlayerItemLog> pageList = playerItemLogService.page(page, queryWrapper);
                for (GamePlayerItemLog record : pageList.getRecords()) {
                    //道具名称
                    if(null != itemIdToNameMap.get(record.getItemId().toString())){
                        record.setItemIdName(itemIdToNameMap.get(record.getItemId().toString()));
                    }
                    //途径名称
                    if(1 == record.getType()){
                        //获得
                        record.setWayName(ItemRuleId.valueOf(record.getWay()).getName());
                    }else if(2 == record.getType()){
                        //使用
                        record.setWayName(ItemReduce.valueOf(record.getWay()).getName());
                    }

                }
                return Result.ok(pageList);
            }

        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
    }


    private LambdaQueryWrapper<GamePlayerItemLog> getQueryWrapper(GamePlayerItemLog playerItemLog,String itemIdStr) {
        LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = Wrappers.lambdaQuery();
        if (playerItemLog.getPlayerId() != null && playerItemLog.getPlayerId() > 0) {
            queryWrapper.eq(GamePlayerItemLog::getPlayerId, playerItemLog.getPlayerId());
        }

        if (!StringUtils.isEmpty(itemIdStr)) {
            queryWrapper.in(GamePlayerItemLog::getItemId, itemIdStr.split(","));
        }
        if(!StringUtils.isEmpty(playerItemLog.getWayName()) && !",".equals(playerItemLog.getWayName())){
            String[] strArr = playerItemLog.getWayName().split(",");
            Integer[] intArr = new Integer[strArr.length];
            for(int a=0;a<strArr.length;a++){
                intArr[a] = Integer.valueOf(strArr[a]);
            }
            queryWrapper.in(GamePlayerItemLog::getWay, intArr);
        }

//        if (playerItemLog.getWay() != null && playerItemLog.getWay() > 0) {
//            queryWrapper.eq(GamePlayerItemLog::getWay, playerItemLog.getWay());
//        }

        if (playerItemLog.getType() != null && playerItemLog.getType() > 0) {
            queryWrapper.eq(GamePlayerItemLog::getType, playerItemLog.getType());
        }

        if (!StringUtils.isAllBlank(playerItemLog.getStartDate(), playerItemLog.getEndDate())) {
            queryWrapper.between(GamePlayerItemLog::getCreateTime, DateUtils.parseDate(playerItemLog.getStartDate()),
                    DateUtils.parseDate(playerItemLog.getEndDate())).orderByDesc(GamePlayerItemLog::getCreateTime);
        }else{
            queryWrapper.orderByDesc(GamePlayerItemLog::getCreateTime);
        }
        return queryWrapper;
    }

    /**
     * 编辑
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "手动同步日志-编辑")
    @GetMapping(value = "/sync")
    public Result<?> edit(BackpackLog backpackLog,
                          @RequestParam(name = "syncTimeBegin") String syncTimeBegin,
                          @RequestParam(name = "syncTimeEnd") String syncTimeEnd,
                          @RequestParam(name = "serverId") Integer serverId,
                          @RequestParam(name = "playerId", defaultValue = "0", required = false) Long playerId,
                          HttpServletRequest req
    ) {
        GameServer gameServer = gameServerService.getOne(Wrappers.<GameServer>lambdaQuery().eq(GameServer::getId, serverId));
        if (gameServer == null) {
            return Result.error("所选服务器不存在！");
        }
        ResponseCode responseCode = backpackLogService.syncBackpackLog(backpackLog, playerId, serverId, req.getParameterMap(), syncTimeBegin, syncTimeEnd);
        if (responseCode.isSuccess()) {
            return Result.ok("同步成功!");
        } else {
            return Result.error(responseCode.getDesc());
        }
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "货币产销-列表查询")
    @GetMapping(value = "/currencyPayIncomeList")
    public Result<?> currencyPayIncomeList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                           @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                           @RequestParam(name = "days", defaultValue = "0") int days,
                                           @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                           @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0 && itemId == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryCurrencyPayIncomeList(rangeDateBegin, rangeDateEnd, days, serverId, itemId);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }


    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "途径分布-列表查询")
    @GetMapping(value = "/wayDistributeList")
    public Result<?> wayDistributeList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                       @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                       @RequestParam(name = "days", defaultValue = "0") int days,
                                       @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                       @RequestParam(name = "type", defaultValue = "0") int type,
                                       @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                       @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0
                && channelId == 0 && days == 0 && itemId == 0 && type == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryWayDistributeList(rangeDateBegin, rangeDateEnd, days, serverId, itemId, type);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "物品流水-列表查询")
    @GetMapping(value = "/itemBillList")
    public Result<?> itemBillList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                  @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                  @RequestParam(name = "way", defaultValue = "0") int way,
                                  @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                  @RequestParam(name = "type", defaultValue = "0") int type,
                                  @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                  @RequestParam(name = "playerId", defaultValue = "0") Long playerId,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0
                && playerId == 0 && way == 0 && itemId == 0 && type == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryItemBillList(rangeDateBegin, rangeDateEnd, way, serverId, itemId, type, playerId);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }


    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(
            HttpServletRequest request, GamePlayerItemLog gamePlayerItemLog) {
        if (gamePlayerItemLog.getServerId() == null || gamePlayerItemLog.getServerId() <= 0) {
            return new ModelAndView();
        }
        LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = getQueryWrapper(gamePlayerItemLog,"");
        try {
            DataSourceHelper.useServerDatabase(gamePlayerItemLog.getServerId());
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            List<GamePlayerItemLog> list = playerItemLogService.list(queryWrapper);
            return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), GamePlayerItemLog.class, "玩家道具产销日志");
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
    }

        @RequestMapping("/download")
            public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {

                Integer pageNo =  null == jsonObject.getString("pageNo") ? 1:Integer.parseInt(jsonObject.getString("pageNo"));
//                Integer pageSize  =  null == jsonObject.getString("pageSize") ? 20:Integer.parseInt(jsonObject.getString("pageSize"));

                GamePlayerItemLog playerItemLog = JSON.parseObject(jsonObject.toJSONString(),GamePlayerItemLog.class);

                if (playerItemLog.getServerId() == null || playerItemLog.getServerId() <= 0) {
                    log.error("请选择服务器！");
                }
                LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = getQueryWrapper(playerItemLog,"");

                Page<GamePlayerItemLog> page = new Page<>(pageNo, 1999999999);
                List<GamePlayerItemLog> list = new ArrayList<>();
                try {
                    DataSourceHelper.useServerDatabase(playerItemLog.getServerId());
                    IPage<GamePlayerItemLog> pageList = playerItemLogService.page(page, queryWrapper);
                    list = pageList.getRecords();
                } finally {
                    DataSourceHelper.useDefaultDatabase();
                }

                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode("测试", "UTF-8");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

                EasyExcel.write(response.getOutputStream(), GamePlayerItemLog.class).sheet("模板").doWrite(list);
            }

}
