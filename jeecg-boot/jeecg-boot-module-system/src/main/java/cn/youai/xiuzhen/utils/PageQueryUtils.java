package cn.youai.xiuzhen.utils;

import cn.hutool.core.date.DateUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.query.QueryGenerator;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class PageQueryUtils {

    private PageQueryUtils() {
    }

    public static void addTime(DateRange dateRange) {
        if (dateRange.getStart() != null) {
            dateRange.setStart(DateUtils.startTimeOfDate(dateRange.getStart()));
        }
        if (dateRange.getEnd() != null) {
            dateRange.setEnd(DateUtils.endTimeOfDate(dateRange.getEnd()));
        }
    }

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column, Date defaultStart, Date defaultEnd) {
        Date start = defaultStart;
        Date end = defaultEnd;
        String[] beginValue = parameterMap.get(column + QueryGenerator.BEGIN);
        String[] endValue = parameterMap.get(column + QueryGenerator.END);
        if (beginValue != null && beginValue.length > 0) {
            start = DateUtil.parse(beginValue[0]);
        }
        if (endValue != null && endValue.length > 0) {
            end = DateUtil.parse(endValue[0]);
        }
        return new DateRange(start, end);
    }

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column) {
        return parseRange(parameterMap, column, null, null);
    }

    public static RangeValue<BigDecimal> parseNumberRange(Map<String, String[]> parameterMap, String column, BigDecimal defaultStart, BigDecimal defaultEnd) {
        BigDecimal start = defaultStart;
        BigDecimal end = defaultEnd;
        String[] beginValue = parameterMap.get(column + QueryGenerator.BEGIN);
        String[] endValue = parameterMap.get(column + QueryGenerator.END);
        if (beginValue != null && beginValue.length > 0) {
            start = new BigDecimal(beginValue[0]);
        }
        if (endValue != null && endValue.length > 0) {
            end = new BigDecimal(endValue[0]);
        }
        return new RangeValue<>(start, end);
    }

    public static RangeValue<BigDecimal> parseNumberRange(Map<String, String[]> parameterMap, String column) {
        return parseNumberRange(parameterMap, column, null, null);
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

    public static <T> IPage<T> makePage(List<T> records, int pageNo, int pageSize, long totalCount) {
        IPage<T> pageList = new Page<>();
        int pageNum = (int) ((totalCount + pageSize) / pageSize);
        pageList.setPages(pageNum).setCurrent(pageNo).setTotal(totalCount).setRecords(records);
        return pageList;
    }

    public static <T> IPage<T> makePage(T record) {
        IPage<T> pageList = new Page<>();
        List<T> records = new ArrayList<>();
        if (record != null) {
            records.add(record);
        }
        pageList.setPages(1).setCurrent(1).setTotal(records.size()).setRecords(records);
        return pageList;
    }

}
