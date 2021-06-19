package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日活动-结义排行榜
 * @date 2021-06-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_marry_rank")
public class GameCampaignTypeMarryRank extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeMarryRank() {}

	public GameCampaignTypeMarryRank(GameCampaignTypeMarryRank entity) {
    	this.bigReward = entity.getBigReward();
    	this.bigRewardFight = entity.getBigRewardFight();
    	this.rankNum = entity.getRankNum();
    	this.rankRewardEmail = entity.getRankRewardEmail();
    	this.callOnMessage = entity.getCallOnMessage();
    	this.helpMsg = entity.getHelpMsg();
	}
	
	/**
	 * 大奖展示
	 */
	@Excel(name = "大奖展示", width = 15)
    private java.lang.String bigReward;
	
	/**
	 * 大奖战力
	 */
	@Excel(name = "大奖战力", width = 15)
    private java.lang.Integer bigRewardFight;
	
	/**
	 * 排行玩家数量(上榜人数限制)
	 */
	@Excel(name = "排行玩家数量(上榜人数限制)", width = 15)
    private java.lang.Integer rankNum;
	
	/**
	 * 排名奖励邮件id
	 */
	@Excel(name = "排名奖励邮件id", width = 15)
    private java.lang.Integer rankRewardEmail;
	
	/**
	 * 号召赠酒传闻id
	 */
	@Excel(name = "号召赠酒传闻id", width = 15)
    private java.lang.Integer callOnMessage;
	
	/**
	 * 帮助信息
	 */
	@Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;
	
}
