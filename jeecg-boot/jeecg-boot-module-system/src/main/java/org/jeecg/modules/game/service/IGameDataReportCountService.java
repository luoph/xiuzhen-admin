package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameDataReportCount;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
public interface IGameDataReportCountService extends IService<GameDataReportCount> {

    /**
     * 查询数据报表
     *
     * @param rangeDateBegin 开始时间
     * @param rangeDateEnd   结束时间
     * @param days           天数
     * @param serverId       服务器id
     * @param channel        游戏渠道
     * @return
     */
    List<GameDataReportCount> queryDataReportByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel);
}
