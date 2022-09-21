package cn.youai.xiuzhen.utils;

import cn.hutool.core.date.DateUtil;
import cn.youai.basics.model.DateRange;
import org.jeecg.common.system.query.QueryGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;

public final class QueryUtils {

    private QueryUtils() {
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

    public static DateRange parseRange(Map<String, String[]> parameterMap, String column) {
        return parseRange(parameterMap, column, null, null);
    }

    public static <T> HashSet<Long> extractPlayerIds(List<T> list, ToLongFunction<T> function) {
        HashSet<Long> playerIds = new HashSet<>(list.size());
        list.forEach(e -> playerIds.add(function.applyAsLong(e)));
        return playerIds;
    }

}
