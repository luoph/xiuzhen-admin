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
public class ImportTextVO {

    private Integer id;
    private String text;

}
