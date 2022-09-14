package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameOnlineNum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
public interface IGameOnlineNumService extends IService<GameOnlineNum> {

    /**
     * 通过定时任务保存在线人数统计
     */
    void doGameOnlineNumSave();

    /**
     * 获取在线人数统计
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param channel
     * @return
     */
    List<GameOnlineNum> queryGameOnlineNumByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel);

    /**
     * 获取在线人数统计汇总
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param channel
     * @return
     */
    List<GameOnlineNum> queryGameOnlineCollectByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel);
}
