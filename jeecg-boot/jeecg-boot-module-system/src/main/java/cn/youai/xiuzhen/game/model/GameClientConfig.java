package cn.youai.xiuzhen.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class GameClientConfig {
    private String name;
    private String loginUrl;
    private String serverUrl;
    private String noticeUrl;
    private String roleUrl;
    private String payUrl;
    private String authUrl;
    private String oauthRedirectUrl;
    private String accountRegisterUrl;
    private String accountLoginUrl;
    private Integer offRegisterDay;
    private String weixinReview;
    private String reviewChannel;
}