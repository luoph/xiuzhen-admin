/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameStatRemain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 留存统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface IGameDataRemainService extends IService<GameStatRemain> {
    /**
     * 留存统计
     */
    IPage<GameStatRemain> selectList(Page<GameStatRemain> page, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 留存统计
     */
    GameStatRemain getCountRemain(int serverId, String date, Date statDate);
}
