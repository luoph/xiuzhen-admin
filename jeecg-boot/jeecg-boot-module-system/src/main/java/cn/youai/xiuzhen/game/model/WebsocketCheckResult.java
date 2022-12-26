package cn.youai.xiuzhen.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebsocketCheckResult {
    private Integer serverId;
    private String url;
    private Integer failed;
    private Integer success;
}
