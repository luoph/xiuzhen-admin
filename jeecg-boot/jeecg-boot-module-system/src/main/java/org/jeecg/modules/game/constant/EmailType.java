package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 邮件分类 1- 有附件， 2-没有附件
 *
 * @author ocean
 * @since 202012221724
 */
@Getter
public enum EmailType {

    /**
     * 邮件分类 1- 有附件， 2-没有附件
     */
    ATTACHMENT(1),
    NO_ATTACHMENT(2);

    private final int type;

    EmailType(int type) {
        this.type = type;
    }

    public static EmailType valueOf(int value) {
        for (EmailType emailType : EmailType.values()) {
            if (emailType.type == value) {
                return emailType;
            }
        }
        return null;
    }
}
