package org.jeecg.modules.message.handle.enums;

import lombok.Getter;

/**
 * 推送状态枚举
 */
public enum SendMsgStatusEnum {

    // 推送状态 0未推送 1推送成功 2推送失败
    WAIT("0"), SUCCESS("1"), FAIL("2");

    @Getter
    private String code;

    private SendMsgStatusEnum(String code) {
        this.code = code;
    }

}