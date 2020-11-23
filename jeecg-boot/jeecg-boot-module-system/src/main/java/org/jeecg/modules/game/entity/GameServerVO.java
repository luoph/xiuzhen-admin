package org.jeecg.modules.game.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Data
@Accessors(chain = true)
public class GameServerVO {

    private Integer id;
    private Integer gameId;
    private String name;
    private String host;
    private String loginUrl;
    private Integer status;
    private Integer recommend;
    private String warning;
    private Integer minVersion;
    private Integer maxVersion;
    private Long openTime;
}