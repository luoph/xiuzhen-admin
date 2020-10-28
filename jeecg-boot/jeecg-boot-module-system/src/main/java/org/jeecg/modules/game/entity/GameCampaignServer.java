package org.jeecg.modules.game.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.modules.game.constant.CampaignStatus;
import org.jeecg.modules.game.constant.SwitchStatus;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动与服务器配置关系
 * @date 2020-10-15
 */
@Data
@Accessors(chain = true)
public class GameCampaignServer {

    private static final long serialVersionUID = 1L;

    private String server;

    private Long serverId;
    private String serverName;
    private Date openTime;

    private Long campaignId;
    private Long typeId;

    /**
     * 开关状态
     * {@linkplain SwitchStatus}
     */
    private Integer status;

    /**
     * 活动状态
     * {@linkplain CampaignStatus}
     */
    private Integer campaignStatus;

}
