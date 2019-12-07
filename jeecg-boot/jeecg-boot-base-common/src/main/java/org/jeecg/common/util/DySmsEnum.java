package org.jeecg.common.util;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

public enum DySmsEnum {

    LOGIN_TEMPLATE_CODE("SMS_175435174", "JEECG", "code"),
    FORGET_PASSWORD_TEMPLATE_CODE("SMS_175435174", "JEECG", "code"),
    REGISTER_TEMPLATE_CODE("SMS_175430166", "JEECG", "code");

    /**
     * 短信模板编码
     */
    @Getter
    private String templateCode;
    /**
     * 签名
     */
    @Getter
    private String signName;
    /**
     * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
     */
    @Getter
    private String keys;

    private DySmsEnum(String templateCode, String signName, String keys) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.keys = keys;
    }

    public static DySmsEnum toEnum(String templateCode) {
        if (StringUtils.isEmpty(templateCode)) {
            return null;
        }
        for (DySmsEnum item : DySmsEnum.values()) {
            if (item.getTemplateCode().equals(templateCode)) {
                return item;
            }
        }
        return null;
    }
}

