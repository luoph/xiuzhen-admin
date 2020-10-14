package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.PayUserRank;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
public interface IPayUserRankService extends IService<PayUserRank> {

    /**
     * 查询充值用户排行数据统计,取前100
     *
     * @param payTimeBegin 开始时间
     * @param payTimeEnd   结束时间
     * @param serverId     服务器id
     * @param channel      游戏渠道
     * @return
     */
    List<PayUserRank> queryUserRankByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel);

    /**
     * 查询充值用户排行数据统计
     *
     * @param rangeDateBegin 开始时间
     * @param rangeDateEnd   结束时间
     * @param serverId       服务器id
     * @param channel        游戏渠道
     * @return
     */
    List<PayUserRank> queryPayRankByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel);
}
