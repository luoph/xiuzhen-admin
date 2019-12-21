package org.jeecg.modules.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class GameConfig {
    private String name;
    private String loginUrl;
    private String serverUrl;
    private String noticeUrl;
}
