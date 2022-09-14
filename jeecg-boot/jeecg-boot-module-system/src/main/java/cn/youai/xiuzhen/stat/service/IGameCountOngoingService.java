/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatOngoing;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 30天留存和ltv统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
public interface IGameCountOngoingService extends IService<GameStatOngoing> {
    /**
     * 查询三十天统计
     *
     * @param page
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param type           1-留存 2-ltv
     * @return
     */
    IPage<GameStatOngoing> selectList(Page<GameStatOngoing> page, int channelId, int serverId, int type, String rangeDateBegin, String rangeDateEnd);
}
