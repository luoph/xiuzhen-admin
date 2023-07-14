package cn.youai.xiuzhen.game.model;

import cn.youai.entities.GameServerStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2022/12/26
 */
@Data
@Accessors(chain = true)
public class ServerSocketWarningData {
    private String profile;
    private String serverIds;
    private String jenkinsJobUrl;
    private List<WebsocketCheckResult> resultList;
    private List<GameServerStatus> serverStatusList;
}
