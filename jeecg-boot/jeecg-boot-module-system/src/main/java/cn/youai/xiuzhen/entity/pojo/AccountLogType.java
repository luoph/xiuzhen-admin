package cn.youai.xiuzhen.entity.pojo;


import lombok.Getter;

/**
 * 账号统计类型
 *
 * @author luopeihuan
 */
@Getter
public enum AccountLogType {
    /**
     * 背包类型
     */
    REGISTER(1, " 注册"),
    LOGIN(2, "登录"),
    LOGOUT(3, "登出");

    /**
     * 名称
     */
    private final String name;

    /**
     * 类型
     */
    private final int type;

    AccountLogType(int type, String name) {
        this.type = type;
        this.name = name;
    }
}

