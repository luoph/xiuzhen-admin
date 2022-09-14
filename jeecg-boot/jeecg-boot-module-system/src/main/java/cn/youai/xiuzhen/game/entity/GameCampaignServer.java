package cn.youai.xiuzhen.game.entity;

import cn.youai.xiuzhen.game.constant.CampaignStatus;
import cn.youai.xiuzhen.game.constant.SwitchStatus;
import lombok.Data;
import lombok.experimental.Accessors;

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

    private Long id;

    private String server;

    private Integer serverId;
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
