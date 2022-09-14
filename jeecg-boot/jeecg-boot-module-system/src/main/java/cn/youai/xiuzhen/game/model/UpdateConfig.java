package cn.youai.xiuzhen.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class UpdateConfig {
    private Integer versionCode;
    private String versionName;
    private Date versionUpdateTime;
}
