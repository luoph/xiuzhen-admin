package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * sql注入处理工具类
 *
 * @author zhoujf
 */
@Slf4j
public class SqlInjectionUtil {
    private static final String XSS_STR = "'|and |exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|,";

    private static final String[] XSS_STR_ARR;

    static {
        XSS_STR_ARR = XSS_STR.split("\\|");
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param value
     * @return
     */
    public static void filterContent(String value) {
        detectInject(value);
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param values
     * @return
     */
    public static void filterContent(String[] values) {
        for (String value : values) {
            detectInject(value);
        }
    }

    /**
     * @param value
     * @return
     * @特殊方法(不通用) 仅用于字典条件SQL参数，注入过滤
     */
    @Deprecated
    public static void specialFilterContent(String value) {
        String specialXssStr = "exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|+|";
        detectInject(value, specialXssStr);
    }


    /**
     * @param value
     * @return
     * @特殊方法(不通用) 仅用于Online报表SQL解析，注入过滤
     */
    @Deprecated
    public static void specialFilterContentForOnlineReport(String value) {
        String specialXssStr = "exec |insert |delete |update |drop |chr |mid |master |truncate |char |declare |";
        detectInject(value, specialXssStr);
    }

    private static void detectInject(String value, String filterStr) {
        if (StringUtils.isEmpty(value)) {
            return;
        }

        String[] filterArr = filterStr.split("\\|");
        // 统一转为小写
        value = value.toLowerCase();
        for (String s : filterArr) {
            if (value.contains(s)) {
                log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
            }
        }
    }

    private static void detectInject(String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }

        // 统一转为小写
        value = value.toLowerCase();
        for (String s : XSS_STR_ARR) {
            if (value.contains(s)) {
                log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
            }
        }
    }

}
