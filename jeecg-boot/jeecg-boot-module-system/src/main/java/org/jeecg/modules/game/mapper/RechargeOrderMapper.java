package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.game.entity.RechargeOrder;

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
	              @Param("channel") String channel,
	              @Param("logTable") String logTable);

	/**
	 * 特惠礼包查询
	 * @param rangeDateBeginTime
	 * @param rangeDateEndTime
	 * @return
	 */
	@Select("select * from daily_gift_package where buy_date between STR_TO_DATE(#{rangeDateBeginTime,jdbcType=VARCHAR},'%Y-%m-%d') and STR_TO_DATE(#{rangeDateEndTime,jdbcType=VARCHAR},'%Y-%m-%d')  order by buy_date desc")
	List<Map> queryPreferenceGiftList(@Param("rangeDateBeginTime") String rangeDateBeginTime,
									  @Param("rangeDateEndTime") String rangeDateEndTime);

	/**
	 * 冲榜礼包查询
	 * @param rangeDateBeginTime
	 * @param rangeDateEndTime
	 * @return
	 */
	@Select("select * from seven_day_task_reward where type = 3 and create_time between STR_TO_DATE(#{rangeDateBeginTime,jdbcType=VARCHAR},'%Y-%m-%d') and STR_TO_DATE(#{rangeDateEndTime,jdbcType=VARCHAR},'%Y-%m-%d')  order by create_time desc")
	List<Map> queryAtListGiftList(@Param("rangeDateBeginTime") String rangeDateBeginTime,
									  @Param("rangeDateEndTime") String rangeDateEndTime);

	/**
	 * 0元购查询
	 * @param rangeDateBeginTime
	 * @param rangeDateEndTime
	 * @return
	 */
	@Select("select * from zero_buy where create_time between STR_TO_DATE(#{rangeDateBeginTime,jdbcType=VARCHAR},'%Y-%m-%d') and STR_TO_DATE(#{rangeDateEndTime,jdbcType=VARCHAR},'%Y-%m-%d')  order by create_time desc")
	List<Map> queryZeroBuyGiftList(@Param("rangeDateBeginTime") String rangeDateBeginTime,
								  @Param("rangeDateEndTime") String rangeDateEndTime);
}
