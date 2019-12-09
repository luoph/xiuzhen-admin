package org.jeecg.modules.message.handle.enums;

import lombok.Getter;
import org.jeecg.common.util.oConvertUtils;

/**
 * 发送消息类型枚举
 */
public enum SendMsgTypeEnum {

    // 推送方式：1短信 2邮件 3微信
    SMS("1", "org.jeecg.modules.message.handle.impl.SmsSendMsgHandle"),
    EMAIL("2", "org.jeecg.modules.message.handle.impl.EmailSendMsgHandle"),
    WX("3", "org.jeecg.modules.message.handle.impl.WxSendMsgHandle");

    @Getter
    private String type;

    @Getter
    private String implClass;

    SendMsgTypeEnum(String type, String implClass) {
        this.type = type;
        this.implClass = implClass;
    }

    public static SendMsgTypeEnum getByType(String type) {
        if (oConvertUtils.isEmpty(type)) {
            return null;
        }
        for (SendMsgTypeEnum val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }
}
