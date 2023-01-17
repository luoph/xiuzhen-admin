/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatConversion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 每日数据统计 Mapper 接口
 * </p>
 *
 * @author luopeihuan
 * @since 2023-01-17
 */
public interface GameStatConversionMapper extends BaseMapper<GameStatConversion> {
    int updateOrInsert(List<GameStatConversion> list);

}
