package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.database.DataSourceHelper;
import cn.youai.xiuzhen.stat.entity.ShopMallLog;
import cn.youai.xiuzhen.stat.mapper.ShopMallLogMapper;
import cn.youai.xiuzhen.stat.service.IShopMallLogService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import cn.youai.xiuzhen.utils.ParamUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * <p>
 * 山城购买日志 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
@Service
@Slf4j
public class ShopMallLogServiceImpl extends ServiceImpl<ShopMallLogMapper, ShopMallLog> implements IShopMallLogService {

    @Override
    public List<ShopMallLog> queryShopMallList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int type) {
        List<ShopMallLog> list = null;
        try {
            Date rangeDateBeginTime;
            Date rangeDateEndTime;
            if (days == 0) {
                rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
                rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            } else {
                rangeDateEndTime = DateUtils.dateOnly(new Date());
                rangeDateBeginTime = DateUtils.dateOnly(DateUtils.addDays(rangeDateEndTime, days * (-1)));
            }
            DataSourceHelper.useServerDatabase(serverId);

            // 全途径下的道具次数总和
            BigDecimal itemNumSum = getBaseMapper().queryItemSum(rangeDateBeginTime, rangeDateEndTime, type);

            list = getBaseMapper().queryShopMallList(rangeDateBeginTime, rangeDateEndTime, type);

            for (ShopMallLog shopMallLog : list) {

                Integer itemId = shopMallLog.getItemId();
                // 次数
                BigDecimal itemCount = getBaseMapper().queryItemCount(rangeDateBeginTime, rangeDateEndTime, shopMallLog.getType(), itemId);

                BigDecimal itemNum = shopMallLog.getItemNum();
                shopMallLog.setItemCount(itemCount);
                shopMallLog.setItemNumRate(BigDecimalUtils.divideFour(itemNum.doubleValue(), itemNumSum.doubleValue(), true));
                // 设置道具名字
                ItemReduce itemReduce = ItemReduce.valueOf(shopMallLog.getItemId());
                shopMallLog.setWayName(itemReduce.getName());
            }

        } catch (Exception e) {
            log.error("数据源切换异常,serverId:" + serverId, e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        return list;
    }

    @Override
    public List<ShopMallLog> queryShopMallListNew(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int type) {
        //读取道具json文件
        InputStream in = this.getClass().getResourceAsStream("/json/item.json");
        String file1 = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        String dest;
        Pattern p = compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(file1);
        dest = m.replaceAll("");
        file1 = dest;
        JSONArray jsonArray = JSON.parseArray(file1);
        //道具id和名称map
        Map<String, String> wayNameMap = new HashMap<>(16);
        for (int i = 0; i < jsonArray.size(); i++) {
            wayNameMap.put(jsonArray.getJSONObject(i).getString("item_id"), jsonArray.getJSONObject(i).getString("name"));
        }
        List<ShopMallLog> list = null;
        try {
            DataSourceHelper.useServerDatabase(serverId);
            //时间段内某商店类型中所有的购买信息
            list = getBaseMapper().queryShopMallListNew(rangeDateBegin, rangeDateEnd, type);
        } catch (Exception e) {
            log.error("数据源切换异常,serverId:" + serverId, e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        List<ShopMallLog> shopMallLogVO = new ArrayList<>();
        if (null == list) {
            return shopMallLogVO;
        }
        //购买信息按日分组
        Map<String, List<ShopMallLog>> shopMallLogListMapCreateTime = list.stream().collect(Collectors.groupingBy(shopmalllog -> DateUtils.formatDate(DateUtils.dateOnly(shopmalllog.getCreateTime()), DatePattern.NORM_DATE_PATTERN)));
        int dateRangeBetween = ParamUtils.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dayStringYmd = DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i), DatePattern.NORM_DATE_PATTERN);
            //获取当前日期的购买信息
            List<ShopMallLog> shopMallLogListOneDay = shopMallLogListMapCreateTime.get(dayStringYmd);
            if (null == shopMallLogListOneDay) {
                continue;
            }
            System.out.println(i);
            //道具名称收集
            Map<Integer, List<ShopMallLog>> shopMallLogListOneDayMapWayName = shopMallLogListOneDay.stream().collect(Collectors.groupingBy(ShopMallLog::getItemId));
            for (Integer s : shopMallLogListOneDayMapWayName.keySet()) {
                List<ShopMallLog> shopMallLogListOneDayMapWayNameOneWayName = shopMallLogListOneDayMapWayName.get(s);
                ShopMallLog shopMallLog = new ShopMallLog();
                //道具名称
                shopMallLog.setWayName(wayNameMap.get(s.toString()));
                int sum = shopMallLogListOneDayMapWayNameOneWayName.stream().mapToInt(shopMallLog1 -> shopMallLog1.getTotalPrice().intValue()).sum();
                //货币数量
                shopMallLog.setItemNum(new BigDecimal(sum));
                Map<Long, List<ShopMallLog>> shopMallLogListOneDayMapWayNameOneWayNameMapPlayerId = shopMallLogListOneDayMapWayNameOneWayName.stream().collect(Collectors.groupingBy(ShopMallLog::getPlayerId));
                //人数
                shopMallLog.setPlayerNum(new BigDecimal(shopMallLogListOneDayMapWayNameOneWayNameMapPlayerId.size()));
                //次数
                shopMallLog.setItemCount(new BigDecimal(shopMallLogListOneDayMapWayNameOneWayName.size()));
                //时间
                shopMallLog.setCreateTimeString(dayStringYmd);
                shopMallLogVO.add(shopMallLog);
            }

        }

        return shopMallLogVO;
    }
}
