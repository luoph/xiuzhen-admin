/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameStatDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 每日数据统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
public interface GameDayDataCountMapper extends BaseMapper<GameStatDaily> {
    int updateOrInsert(List<GameStatDaily> list);
}
