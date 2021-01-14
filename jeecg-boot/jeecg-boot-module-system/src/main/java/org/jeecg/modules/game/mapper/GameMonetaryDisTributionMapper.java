package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.player.entity.BackpackLog;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: GameMonetaryDisTributionMapper
 * @date 2020/12/29 10:32
 */

public interface GameMonetaryDisTributionMapper {

    @Select("select player_id, item_id, num, way, type, create_time from backpack_log where item_id = #{productAndMarketTyep} and type = #{quantityType} and create_time >= STR_TO_DATE(#{rangeTimeBegin},'%Y-%m-%d %H:%i:%s') and create_time < STR_TO_DATE(#{rangeTimeEnd},'%Y-%m-%d %H:%i:%s') order by create_time desc")
    List<Map> selectAllBackPackByTime(@Param("rangeTimeBegin") String rangeTimeBegin, @Param("rangeTimeEnd") String rangeTimeEnd, @Param("productAndMarketTyep") int productAndMarketTyep, @Param("quantityType") int quantityType);

    @Select("select player_id, item_id, num, way, type, create_time, create_date from backpack_log " +
            "where item_id = #{productAndMarketType} and type = #{quantityType} and create_time >= #{rangeTimeBegin} and create_time < #{rangeTimeEnd} order by create_time desc")
    List<BackpackLog> selectAllBackPackByTime2(@Param("rangeTimeBegin") Date rangeTimeBegin, @Param("rangeTimeEnd") Date rangeTimeEnd, @Param("productAndMarketType") int productAndMarketType, @Param("quantityType") int quantityType);
}