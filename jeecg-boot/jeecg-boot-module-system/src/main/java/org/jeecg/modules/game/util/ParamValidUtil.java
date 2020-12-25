package org.jeecg.modules.game.util;

import cn.youai.commons.model.ResponseCode;
import cn.youai.xiuzhen.utils.DateUtils;
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

    public static boolean isParamInValidCheck(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
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
}
