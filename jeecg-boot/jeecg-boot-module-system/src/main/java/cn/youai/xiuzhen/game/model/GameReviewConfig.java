package cn.youai.xiuzhen.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameReviewConfig {

    /**
     * sdk渠道
     */
    private String sdkChannel;

    /**
     * 版本
     */
    private String version;

    private String profile;
}
