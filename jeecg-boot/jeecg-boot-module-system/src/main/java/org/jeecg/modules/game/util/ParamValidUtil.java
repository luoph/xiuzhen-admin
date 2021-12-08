package org.jeecg.modules.game.util;

import cn.hutool.core.date.DatePattern;
import cn.youai.basics.model.DateRange;
import cn.youai.basics.model.ResponseCode;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.ErrorCode;

import java.util.Date;

/**
 * @ClassName ParamValidUtil
 * @Description 参数验证
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-28 18:18
 */
public final class ParamValidUtil {

    /**
     * 统计日期最大的日期跨度天数
     */
    private static final int MAX_DATE_RANGE = 30;

    private ParamValidUtil() {
    }

    public static boolean isParamInValid(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        return channelId <= 0 || serverId <= 0 || StringUtils.isBlank(rangeDateBegin) || StringUtils.isBlank(rangeDateEnd);
    }

    public static ResponseCode dateRangeValid(String rangeDateBegin, String rangeDateEnd) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        int daysBetween = dateRangeBetween(dateBegin, dateEnd);
        if (daysBetween > MAX_DATE_RANGE) {
            return new ErrorCode(1001, "查询日期只能在" + MAX_DATE_RANGE + "天之内");
        }
        return ErrorCode.SUCCESS;
    }

    public static int dateRangeBetween(Date dateBegin, Date dateEnd) {
        Date[] dates = dateBegin(dateBegin, dateEnd);
        return DateUtils.daysBetween(dates[0], dates[1]);
    }

    public static Date[] dateBegin(Date dateBegin, Date dateEnd) {
        Date[] dates = new Date[2];
        Date tmp;
        if (dateEnd.before(dateBegin)) {
            tmp = dateBegin;
            dates[0] = dateEnd;
            dates[1] = tmp;
        } else {
            dates[0] = dateBegin;
            dates[1] = dateEnd;
        }
        return dates;
    }

    @Deprecated
    public static String[] dateParamValid(String rangeDateBegin, String rangeDateEnd, int days) {
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                return null;
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
                return new String[]{rangeDateBegin, rangeDateEnd};
            }
        }
        return null;
    }

    public static Date[] convertDateParam(String startDate, String endDate, Integer days) {
        boolean isValidRangeDate = !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate);
        if (!isValidRangeDate && (null == days || days <= 0)) {
            return null;
        }

        Date startTime;
        Date endTime;
        if (isValidRangeDate) {
            startTime = DateUtils.parseDate(startDate);
            endTime = DateUtils.parseDate(endDate);
        } else {
            Date current = DateUtils.now();
            startTime = DateUtils.addDays(current, -days + 1);
            endTime = DateUtils.endTimeOfDate(current);
        }
        return new Date[]{DateUtils.startTimeOfDate(startTime), DateUtils.endTimeOfDate(endTime)};
    }

    public static DateRange getDateRange(String startDate, String endDate, Integer days) {
        Date[] dates = convertDateParam(startDate, endDate, days);
        return null != dates ? new DateRange(dates[0], dates[1]) : null;
    }

    /**
     * 获取日期里的整点结束时间，例如:输入2021-01-12 1 得到 2021-01-12 01:59:59
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date getHourEnd(Date date, Integer hour) {
        if (hour > 23 || hour < 0) {
            return null;
        }
        String hourStr;
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = hour + "";
        }
        return DateUtils.parseDate(DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN) + " " + hourStr + ":59:59");
    }

}
