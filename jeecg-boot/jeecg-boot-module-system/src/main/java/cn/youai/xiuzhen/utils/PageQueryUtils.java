package cn.youai.xiuzhen.utils;

import cn.hutool.core.date.DateUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.StatDayType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.query.QueryGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class PageQueryUtils {

    private PageQueryUtils() {
    }

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column, Date defaultStart, Date defaultEnd) {
        Date start = defaultStart;
        Date end = defaultEnd;
        String[] createTimeBegin = parameterMap.get(column + QueryGenerator.BEGIN);
        String[] createTimeEnd = parameterMap.get(column + QueryGenerator.END);
        if (createTimeBegin != null && createTimeBegin.length > 0) {
            start = DateUtil.parse(createTimeBegin[0]);
        }
        if (createTimeEnd != null && createTimeEnd.length > 0) {
            end = DateUtil.parse(createTimeEnd[0]);
        }
        return new DateRange(start, end);
    }

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column, int dayType) {
        DateRange dateRange = parseRange(parameterMap, column);
        StatDayType finalDayType = StatDayType.valueOf(dayType);
        if (finalDayType == StatDayType.CUSTOM) {
            return dateRange;
        }

        int days = finalDayType.getValue();
        dateRange.setEnd(DateUtils.todayDate());
        dateRange.setStart(DateUtils.addDays(dateRange.getEnd(), -days));
        return dateRange;
    }

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column) {
        return parseRange(parameterMap, column, null, null);
    }

    public static <T> HashSet<Long> extractIds(List<T> list, ToLongFunction<T> function) {
        HashSet<Long> result = new HashSet<>(list.size());
        list.forEach(e -> result.add(function.applyAsLong(e)));
        return result;
    }

    public static <T> HashSet<Integer> extractIds(List<T> list, ToIntFunction<T> function) {
        HashSet<Integer> result = new HashSet<>(list.size());
        list.forEach(e -> result.add(function.applyAsInt(e)));
        return result;
    }

    public static <T> IPage<T> makePage(List<T> records) {
        IPage<T> pageList = new Page<>();
        pageList.setPages(1).setCurrent(1).setTotal(records.size()).setRecords(records);
        return pageList;
    }

}
