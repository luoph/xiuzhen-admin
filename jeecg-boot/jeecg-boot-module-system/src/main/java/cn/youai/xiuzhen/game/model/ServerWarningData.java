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
public class ServerWarningData {

    private String serverIds;

    private List<GameServerStatus> serverStatusList;

}
