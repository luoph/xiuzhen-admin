/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.game.entity.GameStatDayDataCount;

import java.util.List;

/**
 * <p>
 * 每日数据统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
public interface GameDayDataCountMapper extends BaseMapper<GameStatDayDataCount> {
    int updateOrInsert(List<GameStatDayDataCount> list);
}
