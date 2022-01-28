package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 邮件接受者类型
 *
 * @author luopeihuan
 * @date 2022/01/11
 */
@Getter
public enum EmailReceiver {

    /**
     * 邮件接受者类型, 1-单个玩家 2-全服
     */
    PLAYER(1),
    SERVER(2);

    private final int type;

    EmailReceiver(int type) {
        this.type = type;
    }

    public static EmailReceiver valueOf(int value) {
        for (EmailReceiver emailType : EmailReceiver.values()) {
            if (emailType.type == value) {
                return emailType;
            }
        }
        return null;
    }
}
