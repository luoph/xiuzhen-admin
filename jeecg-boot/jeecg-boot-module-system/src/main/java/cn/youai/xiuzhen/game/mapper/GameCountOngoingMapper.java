/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameStatOngoing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 30天留存和ltv统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
public interface GameCountOngoingMapper extends BaseMapper<GameStatOngoing> {
    /**
     * 批量更新或插入
     *
     * @param list
     * @return
     */
    Boolean insertOrUpdateList(List<GameStatOngoing> list);
}
