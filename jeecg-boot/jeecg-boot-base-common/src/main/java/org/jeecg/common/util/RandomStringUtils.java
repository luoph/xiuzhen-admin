package org.jeecg.common.util;

import cn.hutool.core.util.RandomUtil;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-30.
 */
public class RandomStringUtils {

    private static final int CHECK_CODE_LENGTH = 4;
    private static final int CD_KEY_LENGTH = 8;

    private static final String BASE_CHECK_CODES = "1234567890";

    /**
     * 激活码随机字符串
     */
    private static final String BASE_CD_KEYS = "1234567890QWERTYUPLKJHGFDSAZXCVBNM";


    public static String genCheckCode() {
        return RandomUtil.randomString(BASE_CHECK_CODES, CHECK_CODE_LENGTH);
    }

    public static String genCdKeyCode() {
        return RandomUtil.randomString(BASE_CD_KEYS, CD_KEY_LENGTH);
    }
}
