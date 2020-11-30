/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.game.entity.GameStatCountOngoing;

import java.util.List;

/**
 * <p>
 * 30天留存和ltv统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
public interface GameCountOngoingMapper extends BaseMapper<GameStatCountOngoing> {
    /**
     * 批量更新或插入
     *
     * @param list
     * @return
     */
    Boolean insertOrUpdateList(List<GameStatCountOngoing> list);
}
