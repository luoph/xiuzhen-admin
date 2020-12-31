package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.game.entity.ShopMallLog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 山城购买日志 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
public interface ShopMallLogMapper extends BaseMapper<ShopMallLog> {

    List<ShopMallLog> queryShopMallList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                        @Param("rangeDateEndTime") Date rangeDateEndTime,
                                        @Param("type") int type);

    BigDecimal queryItemSum(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                            @Param("rangeDateEndTime") Date rangeDateEndTime,
                            @Param("type") int type);

    BigDecimal queryItemCount(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                              @Param("rangeDateEndTime") Date rangeDateEndTime,
                              @Param("type") int type,
                              @Param("itemId") int itemId);

    @Select("select player_id, type, item_id, num, total_price, price_item, create_time from shop_mall_log where type = #{type} and state = 0 and create_time >= STR_TO_DATE(#{rangeDateBeginTime},'%Y-%m-%d %H:%i:%s') and create_time < STR_TO_DATE(#{rangeDateEndTime},'%Y-%m-%d %H:%i:%s')")
    List<ShopMallLog> queryShopMallListNew(@Param("rangeDateBeginTime") String rangeDateBeginTime,
                                           @Param("rangeDateEndTime") String rangeDateEndTime,
                                           @Param("type") int type);
}
