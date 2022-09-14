package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.RechargeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {

    List<RechargeOrder> queryGiftList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                      @Param("rangeDateEndTime") Date rangeDateEndTime,
                                      @Param("goodsType") int goodsType);

    Long queryDAU(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                  @Param("rangeDateEndTime") Date rangeDateEndTime,
                  @Param("serverId") Integer serverId,
                  @Param("channel") String channel);

    /**
     * 查询时间范围内消耗玉髓明细
     */
    @Select("select player_id, type, config_id, num, price, create_time from fairy_jade_buy_log where type = #{type} and create_time between STR_TO_DATE(#{rangeDateBeginTime,jdbcType=VARCHAR},'%Y-%m-%d %H:%i:%s') and STR_TO_DATE(#{rangeDateEndTime,jdbcType=VARCHAR},'%Y-%m-%d %H:%i:%s')  order by create_time desc")
    List<Map> queryFairyJadeBuyInfo(@Param("rangeDateBeginTime") String rangeDateBeginTime,
                                    @Param("rangeDateEndTime") String rangeDateEndTime,
                                    @Param("type") Integer type);
}
