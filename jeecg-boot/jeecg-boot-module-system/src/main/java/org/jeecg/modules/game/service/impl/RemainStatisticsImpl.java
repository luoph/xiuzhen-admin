package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.server.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.game.entity.GameRemainStatistisc;
import org.jeecg.modules.game.mapper.RemainStatisticsMapper;
import org.jeecg.modules.game.service.IRemainStatisticsService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 留存统计实现类
 *
 * @author huli
 */
@Service
public class RemainStatisticsImpl extends ServiceImpl<RemainStatisticsMapper, GameRemainStatistisc> implements IRemainStatisticsService {

    /**
     * 查询时间时间跨度
     */
    private final int[] DAY = {0, 1, 2, 3, 4, 5, 6, 14, 29, 59, 89, 119};
    /**
     * 查询档位跨度
     */
    private static final String[] GRADE = {"6-6", "7-29", "30-67", "68-97", "98-197", "198-327", "328-647", "648-9999"};
    @Resource
    RemainStatisticsMapper remainStatisticsMapper;

    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListA(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {

        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //注册日期
            Date queryDateBegin = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin + " 00:00:00"), i);
            Date queryDateEnd = DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), i);
            String registerBeginTime = DateUtils.formatDate(queryDateBegin, DatePattern.NORM_DATETIME_PATTERN);
            String registerEndTime = DateUtils.formatDate(queryDateEnd, DatePattern.NORM_DATETIME_PATTERN);
            //根据查询日期查询 这个日期注册的的用户 在某段时间内登陆过游戏的 人数
            //第DAY[j]+1天数据统计
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //登录日期
                String loginBeginTime = DateUtils.formatDate(DateUtils.addDays(queryDateBegin, DAY[j]), DatePattern.NORM_DATETIME_PATTERN);
                String loginEndTime = DateUtils.formatDate(DateUtils.addDays(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin + " 23:59:59"), i), DAY[j]), DatePattern.NORM_DATETIME_PATTERN);
                String ramainSum = remainStatisticsMapper.selectnNwUserRemainSum(registerBeginTime, registerEndTime, channelName, serverId, loginBeginTime, loginEndTime);
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum(Long.parseLong(ramainSum));
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2(Long.parseLong(ramainSum));
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3(Long.parseLong(ramainSum));
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4(Long.parseLong(ramainSum));
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5(Long.parseLong(ramainSum));
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6(Long.parseLong(ramainSum));
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7(Long.parseLong(ramainSum));
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15(Long.parseLong(ramainSum));
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30(Long.parseLong(ramainSum));
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60(Long.parseLong(ramainSum));
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90(Long.parseLong(ramainSum));
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120(Long.parseLong(ramainSum));
                }
            }
            gameRemainStatistisc.setCountDate(DateUtils.formatDate(queryDateBegin, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListB(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //查询时间段内所有注册信息
        List<Map> allRegisterUserMap = remainStatisticsMapper.selectAllRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATE_PATTERN));
        //查询120天后所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集注册信息
        Map<String, List<Map>> prodMap1 = allRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //注册日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取注册日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    continue;
                }
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数，并收集第一天新增的用户
                int num = 0;
                JSONArray jsonArray = new JSONArray();
                for (Map map : userMap1) {
                    if (null != userMap3.get(map.get("player_id").toString())) {
                        if (0 == j) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("player_id", map.get("player_id").toString());
                            jsonObject.put("createTime", map.get("create_date").toString());
                            jsonArray.add(jsonObject);
                        }
                        num++;
                    }
                }
                if (0 == j) {
                    gameRemainStatistisc.setUserJsonArray(jsonArray);
                }
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistisc.setCountDate(DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    /**
     * 查询新增留存数---返回List<JSONObject>
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param tableName
     * @param serverId
     * @param channelName
     */
    @Override
    public List<JSONObject> queryRemainStatistiscOfNewUserlListJsonObjectList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName, String daysRange) {
        List<Integer> dayList = new ArrayList<Integer>();
        if (!StringUtils.isEmpty(daysRange)) {
            dayList.add(0);
            String[] daysRangeArrayAll = daysRange.split(",");
            for (String s : daysRangeArrayAll) {
                String[] daysRangeArray = s.split("-");
                int daysRangeBegain = Integer.parseInt(daysRangeArray[0]);
                int daysRangeEnd = Integer.parseInt(daysRangeArray[1]);
                for (int i = daysRangeBegain; i <= daysRangeEnd; i++) {
                    System.out.println(i);
                    dayList.add(i - 1);
                }
            }
        } else {//使用Array.asList()会有bug
            dayList.add(0);
            dayList.add(1);
            dayList.add(2);
            dayList.add(3);
            dayList.add(4);
            dayList.add(5);
            dayList.add(6);
            dayList.add(14);
            dayList.add(29);
            dayList.add(59);
            dayList.add(89);
            dayList.add(119);
        }
        List<JSONObject> gameRemainStatistiscJsonObjectList = new ArrayList<>();

        //查询时间段内所有注册信息
        List<Map> allRegisterUserMap = remainStatisticsMapper.selectAllRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATE_PATTERN));
        //查询120天后所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集注册信息
        Map<String, List<Map>> prodMap1 = allRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //注册日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);

            JSONObject gameRemainStatistiscJsonObject;
            //对放入的数据排序
            Map<String, Object> gameRemainStatistiscJsonObjectMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.split("c").length >= 2 && o2.split("c").length >= 2) {
                        try {
                            int a = Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                        return Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                    }
                    return 1;
                }

            });

            for (int j = 0; j < dayList.size(); j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, dayList.get(j));
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取注册日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {
                    if (0 == dayList.get(j)) {
                        gameRemainStatistiscJsonObjectMap.put("registerNum", 0);
                    } else {
                        gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), 0);
                    }
                    continue;
                }
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数，并收集第一天新增的用户
                int num = 0;
                JSONArray jsonArray = new JSONArray();
                for (Map map : userMap1) {
                    if (null != userMap3.get(map.get("player_id").toString())) {
                        if (0 == j) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("player_id", map.get("player_id").toString());
                            jsonObject.put("createTime", map.get("create_date").toString());
                            jsonArray.add(jsonObject);
                        }
                        num++;
                    }
                }
                if (0 == dayList.get(j)) {
                    gameRemainStatistiscJsonObjectMap.put("userJsonArray", jsonArray);
                    gameRemainStatistiscJsonObjectMap.put("registerNum", num);
                } else {
                    gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), num);
                }
            }
            gameRemainStatistiscJsonObjectMap.put("countDate", DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistiscJsonObjectMap.put("channel", channelName);
            gameRemainStatistiscJsonObjectMap.put("serverId", serverId);
            String gameRemainStatistiscJsonObjectMapJsonString = JSON.toJSONString(gameRemainStatistiscJsonObjectMap);
            gameRemainStatistiscJsonObject = JSONObject.parseObject(gameRemainStatistiscJsonObjectMapJsonString);
            gameRemainStatistiscJsonObjectList.add(gameRemainStatistiscJsonObject);
        }
        return gameRemainStatistiscJsonObjectList;
    }

    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfDownPaymentList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //查询到开服时间所有支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, "2000-01-01 00:00:00", DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATETIME_PATTERN));
        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //按create_time收集支付信息
        Map<String, List<Map>> prodMap1 = allPayUserMap.stream().collect(Collectors.groupingBy(item -> item.get("create_time").toString().substring(0, 10)));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //支付日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取支付日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    continue;
                }

                //获取支付日期里所有用户player_id(去重)
                List<Map> userPayMapEquels = userMap1.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("player_id").toString()))), ArrayList::new));
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数
                int num = 0;
                JSONArray jsonArray = new JSONArray();
                for (Map map : userPayMapEquels) {
                    //判断当前日期之前的日期用户是否已经购买过，如果购买过，说明不是首付，需要去除
                    List<Map> hasPayMap = payMapByPlayId.get(map.get("player_id").toString());
                    boolean ynCanNum = true;
                    if (hasPayMap.size() > 1) {
                        for (Map map1 : hasPayMap) {
                            //存在当前日期下，之前是否购买过，购买过break
                            if (DateUtils.parseDate(map1.get("create_time").toString()).before(DateUtils.parseDate(registerTimeString))) {
                                ynCanNum = false;
                                break;
                            }
                        }
                    }
                    //累加计数
                    if (null != userMap3.get(map.get("player_id").toString()) && ynCanNum) {
                        if (0 == j) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("player_id", map.get("player_id").toString());
                            jsonObject.put("createTime", map.get("create_time").toString());
                            jsonArray.add(jsonObject);
                        }
                        num++;
                    }
                }
                if (0 == j) {
                    gameRemainStatistisc.setUserJsonArray(jsonArray);
                }
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistisc.setCountDate(DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    /**
     * 查询首付留存数---返回List<JSONObject>
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param tableName
     * @param serverId
     * @param channelName
     * @param daysRange
     */
    @Override
    public List<JSONObject> queryRemainStatistiscOfDownPaymentListJsonObjectList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName, String daysRange) {
        List<Integer> dayList = new ArrayList<Integer>();
        if (!StringUtils.isEmpty(daysRange)) {
            dayList.add(0);
            String[] daysRangeArrayAll = daysRange.split(",");
            for (String s : daysRangeArrayAll) {
                String[] daysRangeArray = s.split("-");
                int daysRangeBegain = Integer.parseInt(daysRangeArray[0]);
                int daysRangeEnd = Integer.parseInt(daysRangeArray[1]);
                for (int i = daysRangeBegain; i <= daysRangeEnd; i++) {
                    System.out.println(i);
                    dayList.add(i - 1);
                }
            }
        } else {//使用Array.asList()会有bug
            dayList.add(0);
            dayList.add(1);
            dayList.add(2);
            dayList.add(3);
            dayList.add(4);
            dayList.add(5);
            dayList.add(6);
            dayList.add(14);
            dayList.add(29);
            dayList.add(59);
            dayList.add(89);
            dayList.add(119);
        }
        List<JSONObject> gameRemainStatistiscJsonObjectList = new ArrayList<>();

        //查询到开服时间所有支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, "2000-01-01 00:00:00", DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATETIME_PATTERN));
        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //按create_time收集支付信息
        Map<String, List<Map>> prodMap1 = allPayUserMap.stream().collect(Collectors.groupingBy(item -> item.get("create_time").toString().substring(0, 10)));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //支付日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);

            JSONObject gameRemainStatistiscJsonObject = new JSONObject();
            //对放入的数据排序
            Map<String, Object> gameRemainStatistiscJsonObjectMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.split("c").length >= 2 && o2.split("c").length >= 2) {
                        try {
                            int a = Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                        return Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                    }
                    return 1;
                }
            });

            for (int j = 0; j < dayList.size(); j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, dayList.get(j));
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取支付日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {

                    if (0 == dayList.get(j)) {
                        gameRemainStatistiscJsonObjectMap.put("registerNum", 0);
                    } else {
                        gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), 0);
                    }
                    continue;

                }
                //获取支付日期里所有用户player_id(去重)
                List<Map> userPayMapEquels = userMap1.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("player_id").toString()))), ArrayList::new));

                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数
                int num = 0;
                JSONArray jsonArray = new JSONArray();
                for (Map map : userPayMapEquels) {
                    //判断当前日期之前的日期用户是否已经购买过，如果购买过，说明不是首付，需要去除
                    List<Map> hasPayMap = payMapByPlayId.get(map.get("player_id").toString());
                    boolean ynCanNum = true;
                    if (hasPayMap.size() > 1) {
                        for (Map map1 : hasPayMap) {
                            //存在当前日期下，之前是否购买过，购买过break
                            if (DateUtils.parseDate(map1.get("create_time").toString()).before(DateUtils.parseDate(registerTimeString))) {
                                ynCanNum = false;
                                break;
                            }
                        }
                    }
                    //累加计数
                    if (null != userMap3.get(map.get("player_id").toString()) && ynCanNum) {
                        if (0 == j) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("player_id", map.get("player_id").toString());
                            jsonObject.put("createTime", map.get("create_time").toString());
                            jsonArray.add(jsonObject);
                        }
                        num++;
                    }
                }

                if (0 == dayList.get(j)) {
                    gameRemainStatistiscJsonObjectMap.put("userJsonArray", jsonArray);
                    gameRemainStatistiscJsonObjectMap.put("registerNum", num);
                } else {
                    gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), num);
                }
            }
            gameRemainStatistiscJsonObjectMap.put("countDate", DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistiscJsonObjectMap.put("channel", channelName);
            gameRemainStatistiscJsonObjectMap.put("serverId", serverId);
            // WTF???
            String gameRemainStatistiscJsonObjectMapJsonString = JSON.toJSONString(gameRemainStatistiscJsonObjectMap);
            gameRemainStatistiscJsonObject = JSONObject.parseObject(gameRemainStatistiscJsonObjectMapJsonString);
            gameRemainStatistiscJsonObjectList.add(gameRemainStatistiscJsonObject);
        }
        return gameRemainStatistiscJsonObjectList;
    }

    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfFreeList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //查询到开服时间所有支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, "2000-01-01 00:00:00", DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATETIME_PATTERN));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));

        //查询时间段内所有注册信息
        List<Map> allRegisterUserMap = remainStatisticsMapper.selectAllRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd), 0), DatePattern.NORM_DATE_PATTERN));
        //查询120天后所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集注册信息
        Map<String, List<Map>> prodMap1 = allRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //注册日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取注册日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    ;
                    continue;
                }

                //获取支付日期里所有用户player_id(去重)
                List<Map> userPayMapEquels = userMap1.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("player_id").toString()))), ArrayList::new));
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数
                int num = 0;
                for (Map map : userPayMapEquels) {
                    //判断是否有过支付记录，如果有，说明不是免费用户，需要去除
                    List<Map> hasPayMap = payMapByPlayId.get(map.get("player_id").toString());
                    boolean ynCanNum = true;
                    if (null != hasPayMap && hasPayMap.size() > 0) {
                        ynCanNum = false;
                        break;
                    }
                    if (ynCanNum && null != userMap3.get(map.get("player_id").toString())) {
                        num++;
                    }
                }
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistisc.setCountDate(DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfFreeListB(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) throws Exception {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //新增留存
        List<GameRemainStatistisc> remainStatistiscOfNewUserlList = queryRemainStatistiscOfNewUserlListB(rangeDateBegin, rangeDateEnd, serverId, channelName);
        //首付留存
        List<GameRemainStatistisc> remainStatistiscOfDownPaymentList = queryRemainStatistiscOfDownPaymentList(rangeDateBegin, rangeDateEnd, serverId, channelName);
        JSONArray jsonArrayOfNewUser = new JSONArray();
        JSONArray jsonArrayOfDownPayMent = new JSONArray();
        if (null != remainStatistiscOfNewUserlList.get(0).getUserJsonArray()) {
            jsonArrayOfNewUser = remainStatistiscOfNewUserlList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfNewUser.add(jsonObject);
        }
        if (null != remainStatistiscOfDownPaymentList.get(0).getUserJsonArray()) {
            jsonArrayOfDownPayMent = remainStatistiscOfDownPaymentList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfDownPayMent.add(jsonObject);
        }

        JSONArray jsonArrayOfFree = new JSONArray();
        //新增留存-首付留存=免费留存用户
        for (int i = 0; i < jsonArrayOfNewUser.size(); i++) {
            for (int i1 = 0; i1 < jsonArrayOfDownPayMent.size(); i1++) {
                if (jsonArrayOfNewUser.getJSONObject(i).getString("player_id").equals(jsonArrayOfDownPayMent.getJSONObject(i1).getString("player_id"))) {
                    break;
                }
                if (i1 == jsonArrayOfDownPayMent.size() - 1) {
                    jsonArrayOfFree.add(jsonArrayOfNewUser.getJSONObject(i));
                }
            }
        }
        //免费留存按日期分类
        List<Map> freeList = new ArrayList<>();
        for (int i = 0; i < jsonArrayOfFree.size(); i++) {
            Map<String, String> freeMap = new HashMap<>();
            freeMap.put("createTime", jsonArrayOfFree.getJSONObject(i).getString("createTime"));
            freeMap.put("player_id", jsonArrayOfFree.getJSONObject(i).getString("player_id"));
            freeList.add(freeMap);
        }
        Map<String, List<Map>> freeMap_createDate = freeList.stream().collect(Collectors.groupingBy(map -> map.get("createTime").toString().substring(0, 10)));

        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));


        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //用户注册日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                //注册日期内免费用户
                List<Map> prodMap3 = freeMap_createDate.get(registerTimeString);
                if (null == userMap2 || null == prodMap3) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    continue;
                }

                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));

                //当前登录日期下计数
                int num = 0;
                System.out.println(j);
                for (Map map : prodMap3) {
                    //累加计数
                    if (null != userMap3.get(map.get("player_id").toString())) {
                        num++;
                    }
                }
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistisc.setCountDate(DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }

        return gameRemainStatistiscList;
    }

    /**
     * 免费留存---返回List<JSONObject>
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param tableName
     * @param serverId
     * @param channelName
     * @param daysRange
     */
    @Override
    public List<JSONObject> queryRemainStatistiscOfFreeListBJsonObjectList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName, String daysRange) throws Exception {
        List<Integer> dayList = new ArrayList<Integer>();
        if (!StringUtils.isEmpty(daysRange)) {
            dayList.add(0);
            String[] daysRangeArrayAll = daysRange.split(",");
            for (String s : daysRangeArrayAll) {
                String[] daysRangeArray = s.split("-");
                int daysRangeBegain = Integer.parseInt(daysRangeArray[0]);
                int daysRangeEnd = Integer.parseInt(daysRangeArray[1]);
                for (int i = daysRangeBegain; i <= daysRangeEnd; i++) {
                    System.out.println(i);
                    dayList.add(i - 1);
                }
            }
        } else {//使用Array.asList()会有bug
            dayList.add(0);
            dayList.add(1);
            dayList.add(2);
            dayList.add(3);
            dayList.add(4);
            dayList.add(5);
            dayList.add(6);
            dayList.add(14);
            dayList.add(29);
            dayList.add(59);
            dayList.add(89);
            dayList.add(119);
        }
        List<JSONObject> gameRemainStatistiscJsonObjectList = new ArrayList<>();

        //新增留存
        List<GameRemainStatistisc> remainStatistiscOfNewUserlList = queryRemainStatistiscOfNewUserlListB(rangeDateBegin, rangeDateEnd, serverId, channelName);
        //首付留存
        List<GameRemainStatistisc> remainStatistiscOfDownPaymentList = queryRemainStatistiscOfDownPaymentList(rangeDateBegin, rangeDateEnd, serverId, channelName);
        JSONArray jsonArrayOfNewUser = new JSONArray();
        JSONArray jsonArrayOfDownPayMent = new JSONArray();
        if (null != remainStatistiscOfNewUserlList.get(0).getUserJsonArray()) {
            jsonArrayOfNewUser = remainStatistiscOfNewUserlList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfNewUser.add(jsonObject);
        }
        if (null != remainStatistiscOfDownPaymentList.get(0).getUserJsonArray()) {
            jsonArrayOfDownPayMent = remainStatistiscOfDownPaymentList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfDownPayMent.add(jsonObject);
        }

        JSONArray jsonArrayOfFree = new JSONArray();
        //新增留存-首付留存=免费留存用户
        for (int i = 0; i < jsonArrayOfNewUser.size(); i++) {
            for (int i1 = 0; i1 < jsonArrayOfDownPayMent.size(); i1++) {
                if (jsonArrayOfNewUser.getJSONObject(i).getString("player_id").equals(jsonArrayOfDownPayMent.getJSONObject(i1).getString("player_id"))) {
                    break;
                }
                if (i1 == jsonArrayOfDownPayMent.size() - 1) {
                    jsonArrayOfFree.add(jsonArrayOfNewUser.getJSONObject(i));
                }
            }
        }
        //免费留存按日期分类
        List<Map> freeList = new ArrayList<>();
        for (int i = 0; i < jsonArrayOfFree.size(); i++) {
            Map<String, String> freeMap = new HashMap<>();
            freeMap.put("createTime", jsonArrayOfFree.getJSONObject(i).getString("createTime"));
            freeMap.put("player_id", jsonArrayOfFree.getJSONObject(i).getString("player_id"));
            freeList.add(freeMap);
        }
        Map<String, List<Map>> freeMap_createDate = freeList.stream().collect(Collectors.groupingBy(map -> map.get("createTime").toString().substring(0, 10)));

        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));


        //计算开始时间和结束时间的相差天数
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        //根据相差天数来决定返回的总行数
        for (int i = dateRangeBetween; i >= 0; i--) {
            //用户注册日期
            Date registerTime = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);

            //对放入的数据排序
            Map<String, Object> gameRemainStatistiscJsonObjectMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.split("c").length >= 2 && o2.split("c").length >= 2) {
                        try {
                            int a = Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                        return Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                    }
                    return 1;
                }

            });

            for (int j = 0; j < dayList.size(); j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, dayList.get(j));
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                //注册日期内免费用户
                List<Map> prodMap3 = freeMap_createDate.get(registerTimeString);
                if (null == userMap2 || null == prodMap3) {
                    if (0 == dayList.get(j)) {
                        gameRemainStatistiscJsonObjectMap.put("registerNum", 0);
                    } else {
                        gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), 0);
                    }
                    continue;
                }

                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));

                //当前登录日期下计数
                int num = 0;
                for (Map map : prodMap3) {
                    //累加计数
                    if (null != userMap3.get(map.get("player_id").toString())) {
                        num++;
                    }
                }
                if (0 == dayList.get(j)) {
                    gameRemainStatistiscJsonObjectMap.put("registerNum", num);
                } else {
                    gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), num);
                }
            }
            gameRemainStatistiscJsonObjectMap.put("countDate", DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN));
            gameRemainStatistiscJsonObjectMap.put("channel", channelName);
            gameRemainStatistiscJsonObjectMap.put("serverId", serverId);
            String gameRemainStatistiscJsonObjectMapJsonString = JSON.toJSONString(gameRemainStatistiscJsonObjectMap);
            JSONObject gameRemainStatistiscJsonObject = JSONObject.parseObject(gameRemainStatistiscJsonObjectMapJsonString);
            gameRemainStatistiscJsonObjectList.add(gameRemainStatistiscJsonObject);
        }
        return gameRemainStatistiscJsonObjectList;
    }

    /**
     * rangeDateBegin和rangeDateEnd一定是相等的，档位查询只能一天一天查询
     */
    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfGradeList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //查询到开服时间所有支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, "2000-01-01 00:00:00", DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), 0), DatePattern.NORM_DATETIME_PATTERN));
        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //按create_time收集支付信息
        Map<String, List<Map>> prodMap1 = allPayUserMap.stream().collect(Collectors.groupingBy(item -> item.get("create_time").toString().substring(0, 10)));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));

        //根据档位等级来决定返回数据条数
        for (int i = 0; i < GRADE.length; i++) {
            //支付日期
            Date registerTime = DateUtils.parseDate(rangeDateBegin);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取支付日期里所有用户
                List<Map> userMap1 = prodMap1.get(registerTimeString);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap1 || null == userMap2) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    ;
                    continue;
                }

                //获取支付日期里所有用户player_id(去重)
                List<Map> userPayMapEquels = userMap1.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("player_id").toString()))), ArrayList::new));
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                //当前登录日期下计数
                int num = 0;
                for (Map map : userPayMapEquels) {
                    //判断当前日期之前的日期用户是否已经购买过，如果购买过，说明不是首付，需要去除
                    List<Map> hasPayMap = payMapByPlayId.get(map.get("player_id").toString());
                    boolean ynCanNum = true;
                    if (hasPayMap.size() > 1) {
                        for (Map map1 : hasPayMap) {
                            //存在当前日期下，之前是否购买过，购买过break
                            if (DateUtils.parseDate(map1.get("create_time").toString()).before(DateUtils.parseDate(registerTimeString))) {
                                ynCanNum = false;
                                break;
                            }
                        }
                    }
                    //累加计数
                    if (null != userMap3.get(map.get("player_id").toString()) && ynCanNum) {
                        //统计当前日期下首付用户的总充值金额，如果处于档位内则累加
                        Double payAmountSum = userMap1.stream().mapToDouble(usermap -> {
                            return Double.parseDouble(usermap.get("pay_amount").toString());
                        }).sum();
                        String[] gradeBeginAndEnd = GRADE[i].split("-");
                        //档位前后相等情况
                        if (Integer.parseInt(gradeBeginAndEnd[0]) == Integer.parseInt(gradeBeginAndEnd[1])) {
                            //判断是否处于档位内
                            if (Integer.parseInt(gradeBeginAndEnd[0]) == payAmountSum) {
                                num++;
                            }
                        } else {
                            //判断是否处于档位内
                            if (Integer.parseInt(gradeBeginAndEnd[0]) <= payAmountSum && payAmountSum < Integer.parseInt(gradeBeginAndEnd[1])) {
                                num++;
                            }
                        }

                    }
                }
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistisc.setCountDate(GRADE[i]);
            gameRemainStatistisc.setChannel(channelName);
            gameRemainStatistisc.setServerId(serverId);
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    /**
     * rangeDateBegin和rangeDateEnd一定是相等的，档位查询只能一天一天查询
     */
    @Override
    public List<GameRemainStatistisc> queryRemainStatistiscOfGradeListB(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName) {
        List<GameRemainStatistisc> gameRemainStatistiscList = new ArrayList<>();
        //首付留存
        List<GameRemainStatistisc> remainStatistiscOfDownPaymentList = queryRemainStatistiscOfDownPaymentList(rangeDateBegin, rangeDateEnd, serverId, channelName);
        //首付留存的用户(唯一)
        JSONArray jsonArrayOfDownPayMent = new JSONArray();
        if (null != remainStatistiscOfDownPaymentList.get(0).getUserJsonArray()) {
            jsonArrayOfDownPayMent = remainStatistiscOfDownPaymentList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfDownPayMent.add(jsonObject);
        }

        //查询首付日期支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, DateUtils.formatDate(DateUtils.parseDate(rangeDateBegin + " 00:00:00"), DatePattern.NORM_DATETIME_PATTERN), DateUtils.formatDate(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), DatePattern.NORM_DATETIME_PATTERN));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));
        //首付留存的用户转成List<Map>
        List<Map> downPayMentList = new ArrayList<>();
        for (int i = 0; i < jsonArrayOfDownPayMent.size(); i++) {
            Map<String, String> freeMap = new HashMap<>();
            freeMap.put("createTime", jsonArrayOfDownPayMent.getJSONObject(i).getString("createTime"));
            freeMap.put("player_id", jsonArrayOfDownPayMent.getJSONObject(i).getString("player_id"));
            downPayMentList.add(freeMap);
        }
//        //首付留存用户按player_id分组
//        Map<String, List<Map>> downPayMentMap_playerId= downPayMentList.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString().substring(0,10)));

        //首付留存用户按档位分类
        Map<String, List<String>> grade_downPaymentMap = new HashMap<>();
        //根据档位等级来决定返回数据条数
        for (int i = 0; i < GRADE.length; i++) {
            List<String> grade_downPaymentList = new ArrayList<>();
            for (Map map : downPayMentList) {
                List<Map> someoneOnedayPayInfo = payMapByPlayId.get(map.get("player_id").toString());
                //统计该用户充值总金额
                Double payAmountSum = 0.0;
                payAmountSum = someoneOnedayPayInfo.stream().mapToDouble(usermap -> {
                    return Double.parseDouble(usermap.get("pay_amount").toString());
                }).sum();
                String[] gradeBeginAndEnd = GRADE[i].split("-");
                //档位前后相等情况
                if (Integer.parseInt(gradeBeginAndEnd[0]) == Integer.parseInt(gradeBeginAndEnd[1])) {
                    //判断是否处于档位内
                    if (Integer.parseInt(gradeBeginAndEnd[0]) == payAmountSum) {
                        grade_downPaymentList.add(map.get("player_id").toString());
                    }
                } else {
                    //判断是否处于档位内
                    if (Integer.parseInt(gradeBeginAndEnd[0]) <= payAmountSum && payAmountSum <= Integer.parseInt(gradeBeginAndEnd[1])) {
                        grade_downPaymentList.add(map.get("player_id").toString());
                    }
                }

            }
            grade_downPaymentMap.put(GRADE[i], grade_downPaymentList);

        }

        for (String s : grade_downPaymentMap.keySet()) {
            GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
            //设置档位
            gameRemainStatistisc.setCountDate(s);
            //新增用户注册日期
            Date registerTime = DateUtils.parseDate(rangeDateBegin);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);
            for (int j = 0; j < DAY.length; j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, DAY[j]);
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap2) {
                    if (0 == DAY[j]) {
                        gameRemainStatistisc.setRegisterNum((long) 0);
                    }
                    if (1 == DAY[j]) {
                        gameRemainStatistisc.setC2((long) 0);
                    }
                    if (2 == DAY[j]) {
                        gameRemainStatistisc.setC3((long) 0);
                    }
                    if (3 == DAY[j]) {
                        gameRemainStatistisc.setC4((long) 0);
                    }
                    if (4 == DAY[j]) {
                        gameRemainStatistisc.setC5((long) 0);
                    }
                    if (5 == DAY[j]) {
                        gameRemainStatistisc.setC6((long) 0);
                    }
                    if (6 == DAY[j]) {
                        gameRemainStatistisc.setC7((long) 0);
                    }
                    if (14 == DAY[j]) {
                        gameRemainStatistisc.setC15((long) 0);
                    }
                    if (29 == DAY[j]) {
                        gameRemainStatistisc.setC30((long) 0);
                    }
                    if (59 == DAY[j]) {
                        gameRemainStatistisc.setC60((long) 0);
                    }
                    if (89 == DAY[j]) {
                        gameRemainStatistisc.setC90((long) 0);
                    }
                    if (119 == DAY[j]) {
                        gameRemainStatistisc.setC120((long) 0);
                    }
                    continue;
                }
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                List<String> g = grade_downPaymentMap.get(s);
                int num = 0;
                for (String s1 : g) {
                    if (null != userMap3.get(s1)) {
                        num += 1;
                    }
                }
                //设置登录留存数
                if (0 == DAY[j]) {
                    gameRemainStatistisc.setRegisterNum((long) num);
                }
                if (1 == DAY[j]) {
                    gameRemainStatistisc.setC2((long) num);
                }
                if (2 == DAY[j]) {
                    gameRemainStatistisc.setC3((long) num);
                }
                if (3 == DAY[j]) {
                    gameRemainStatistisc.setC4((long) num);
                }
                if (4 == DAY[j]) {
                    gameRemainStatistisc.setC5((long) num);
                }
                if (5 == DAY[j]) {
                    gameRemainStatistisc.setC6((long) num);
                }
                if (6 == DAY[j]) {
                    gameRemainStatistisc.setC7((long) num);
                }
                if (14 == DAY[j]) {
                    gameRemainStatistisc.setC15((long) num);
                }
                if (29 == DAY[j]) {
                    gameRemainStatistisc.setC30((long) num);
                }
                if (59 == DAY[j]) {
                    gameRemainStatistisc.setC60((long) num);
                }
                if (89 == DAY[j]) {
                    gameRemainStatistisc.setC90((long) num);
                }
                if (119 == DAY[j]) {
                    gameRemainStatistisc.setC120((long) num);
                }
            }
            gameRemainStatistiscList.add(gameRemainStatistisc);
        }
        return gameRemainStatistiscList;
    }

    /**
     * 分档位留存
     */
    @Override
    public List<JSONObject> queryRemainStatistiscOfGradeListBJsonObjectList(String rangeDateBegin, String rangeDateEnd, int serverId, String channelName, String daysRange, String grade) {
        //档位自定义
        List<String> gradeList = new ArrayList<>();
        if (!StringUtils.isEmpty(grade)) {
            String[] daysRangeArrayAll = grade.split(",");
            for (String s : daysRangeArrayAll) {
                gradeList.add(s);
            }
        } else {//使用Array.asList()会有bug
            gradeList.add("1-6");
            gradeList.add("7-29");
            gradeList.add("30-67");
            gradeList.add("68-97");
            gradeList.add("98-197");
            gradeList.add("198-327");
            gradeList.add("328-647");
            gradeList.add("648-9999");
        }
        //天数自定义
        List<Integer> dayList = new ArrayList<Integer>();
        if (!StringUtils.isEmpty(daysRange)) {
            dayList.add(0);
            String[] daysRangeArrayAll = daysRange.split(",");
            for (String s : daysRangeArrayAll) {
                String[] daysRangeArray = s.split("-");
                int daysRangeBegain = Integer.parseInt(daysRangeArray[0]);
                int daysRangeEnd = Integer.parseInt(daysRangeArray[1]);
                for (int i = daysRangeBegain; i <= daysRangeEnd; i++) {
                    System.out.println(i);
                    dayList.add(i - 1);
                }
            }
        } else {//使用Array.asList()会有bug
            dayList.add(0);
            dayList.add(1);
            dayList.add(2);
            dayList.add(3);
            dayList.add(4);
            dayList.add(5);
            dayList.add(6);
            dayList.add(14);
            dayList.add(29);
            dayList.add(59);
            dayList.add(89);
            dayList.add(119);
        }
        List<JSONObject> gameRemainStatistiscJsonObjectList = new ArrayList<>();

        //首付留存
        List<GameRemainStatistisc> remainStatistiscOfDownPaymentList = queryRemainStatistiscOfDownPaymentList(rangeDateBegin, rangeDateEnd, serverId, channelName);
        //首付留存的用户(唯一)
        JSONArray jsonArrayOfDownPayMent = new JSONArray();
        if (null != remainStatistiscOfDownPaymentList.get(0).getUserJsonArray()) {
            jsonArrayOfDownPayMent = remainStatistiscOfDownPaymentList.get(0).getUserJsonArray();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("player_id", "");
            jsonObject.put("createTime", "");
            jsonArrayOfDownPayMent.add(jsonObject);
        }

        //查询首付日期支付信息
        List<Map> allPayUserMap = remainStatisticsMapper.selectAllOrderHasPayList(channelName, serverId, DateUtils.formatDate(DateUtils.parseDate(rangeDateBegin + " 00:00:00"), DatePattern.NORM_DATETIME_PATTERN), DateUtils.formatDate(DateUtils.parseDate(rangeDateEnd + " 23:59:59"), DatePattern.NORM_DATETIME_PATTERN));
        //按play_id收集支付信息
        Map<String, List<Map>> payMapByPlayId = allPayUserMap.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
        //查询120天后到现在的所有登陆信息（120是由页面显示展示决定的）
        List<Map> allLoginAndRegisterUserMap = remainStatisticsMapper.selectAllLoginAndRegisterUser(channelName, serverId, rangeDateBegin, DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), 120), DatePattern.NORM_DATE_PATTERN));
        //收集登陆信息
        Map<String, List<Map>> prodMap2 = allLoginAndRegisterUserMap.stream().collect(Collectors.groupingBy(a -> a.get("create_date").toString()));
        //首付留存的用户转成List<Map>
        List<Map> downPayMentList = new ArrayList<>();
        for (int i = 0; i < jsonArrayOfDownPayMent.size(); i++) {
            Map<String, String> freeMap = new HashMap<>();
            freeMap.put("createTime", jsonArrayOfDownPayMent.getJSONObject(i).getString("createTime"));
            freeMap.put("player_id", jsonArrayOfDownPayMent.getJSONObject(i).getString("player_id"));
            downPayMentList.add(freeMap);
        }
//        //首付留存用户按player_id分组
//        Map<String, List<Map>> downPayMentMap_playerId= downPayMentList.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString().substring(0,10)));

        //首付留存用户按档位分类
        Map<String, List<String>> grade_downPaymentMap = new HashMap<>();
        //根据档位等级来决定返回数据条数
        for (int i = 0; i < gradeList.size(); i++) {
            List<String> grade_downPaymentList = new ArrayList<>();
            for (Map map : downPayMentList) {
                List<Map> someoneOnedayPayInfo = payMapByPlayId.get(map.get("player_id").toString());
                //统计该用户充值总金额
                Double payAmountSum = 0.0;
                payAmountSum = someoneOnedayPayInfo.stream().mapToDouble(usermap -> {
                    return Double.parseDouble(usermap.get("pay_amount").toString());
                }).sum();
                String[] gradeBeginAndEnd = gradeList.get(i).split("-");
                //档位前后相等情况
                if (Integer.parseInt(gradeBeginAndEnd[0]) == Integer.parseInt(gradeBeginAndEnd[1])) {
                    //判断是否处于档位内
                    if (Integer.parseInt(gradeBeginAndEnd[0]) == payAmountSum) {
                        grade_downPaymentList.add(map.get("player_id").toString());
                    }
                } else {
                    //判断是否处于档位内
                    if (Integer.parseInt(gradeBeginAndEnd[0]) <= payAmountSum && payAmountSum <= Integer.parseInt(gradeBeginAndEnd[1])) {
                        grade_downPaymentList.add(map.get("player_id").toString());
                    }
                }

            }
            grade_downPaymentMap.put(gradeList.get(i), grade_downPaymentList);

        }

        for (String s : grade_downPaymentMap.keySet()) {
            //新增用户注册日期
            Date registerTime = DateUtils.parseDate(rangeDateBegin);
            String registerTimeString = DateUtils.formatDate(registerTime, DatePattern.NORM_DATE_PATTERN);

            JSONObject gameRemainStatistiscJsonObject = new JSONObject();
            //对放入的数据排序
            Map<String, Object> gameRemainStatistiscJsonObjectMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.split("c").length >= 2 && o2.split("c").length >= 2) {
                        try {
                            int a = Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                        return Integer.parseInt(o1.split("c")[1]) - Integer.parseInt(o2.split("c")[1]);
                    }
                    return 1;
                }

            });

            for (int j = 0; j < dayList.size(); j++) {
                //需要判断的登录日期
                Date loginTime = DateUtils.addDays(registerTime, dayList.get(j));
                String loginTimeString = DateUtils.formatDate(loginTime, DatePattern.NORM_DATE_PATTERN);
                //获取登录日期里所有用户
                List<Map> userMap2 = prodMap2.get(loginTimeString);
                if (null == userMap2) {
                    if (0 == dayList.get(j)) {
                        gameRemainStatistiscJsonObjectMap.put("registerNum", 0);
                    } else {
                        gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), 0);
                    }
                    continue;
                }
                //按player_id收集登录日期里的登录信息
                Map<String, List<Map>> userMap3 = userMap2.stream().collect(Collectors.groupingBy(a -> a.get("player_id").toString()));
                List<String> g = grade_downPaymentMap.get(s);
                int num = 0;
                for (String s1 : g) {
                    if (null != userMap3.get(s1)) {
                        num += 1;
                    }
                }
                //设置登录留存数
                if (0 == dayList.get(j)) {
                    gameRemainStatistiscJsonObjectMap.put("registerNum", num);
                } else {
                    gameRemainStatistiscJsonObjectMap.put("c" + (dayList.get(j) + 1), num);
                }
            }

            gameRemainStatistiscJsonObjectMap.put("countDate", s);
            String gameRemainStatistiscJsonObjectMapJsonString = JSON.toJSONString(gameRemainStatistiscJsonObjectMap);
            gameRemainStatistiscJsonObject = JSONObject.parseObject(gameRemainStatistiscJsonObjectMapJsonString);
            gameRemainStatistiscJsonObjectList.add(gameRemainStatistiscJsonObject);
        }
        return gameRemainStatistiscJsonObjectList;
    }


}