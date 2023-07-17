/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 游戏渠道服配置 Mapper 接口
 * </p>
 *
 * @author luopeihuan
 * @since 2021-08-03
 */
public interface GameServerGroupMapper extends BaseMapper<GameServerGroup> {

    IPage<GameServerGroup> queryList(Page<?> page,
                                     @Param("entity") GameServerGroup entity,
                                     @Param("createTimeRange") DateRange createTimeRange,
                                     @Param("crossSettleTimeRange") DateRange crossSettleTimeRange
    );

    List<GameServerGroup> selectByServerId(@Param("serverIds") Collection<Integer> serverIds);

}
