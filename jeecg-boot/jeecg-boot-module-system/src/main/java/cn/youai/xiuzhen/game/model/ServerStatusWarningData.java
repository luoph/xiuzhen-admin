package cn.youai.xiuzhen.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2023/07/14
 */
@Data
@Accessors(chain = true)
public class ServerStatusWarningData {
    private String profile;
    private String serverIds;
    private String groupIds;
    private String gameServerJobUrl;
    private String crossServerJobUrl;
}
