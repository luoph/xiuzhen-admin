package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface GameOrderMapper extends BaseMapper<GameOrder> {
    /**
     * 计算当天支付金额
     */
    BigDecimal serverPayAmount(@Param("serverId") int serverId, @Param("payDate") Date payDate);

    BigDecimal chanelPayAmount(@Param("channel") String channel, @Param("payDate") Date payDate);

    int serverPayPlayerNum(@Param("serverId") int serverId, @Param("payDate") Date payDate);

    int channelPayPlayerNum(@Param("channel") String channel, @Param("payDate") Date payDate);

    /**
     * 统计日期内，付费玩家数，付费金额
     *
     * @param serverId 服务器id
     * @param dates    日期范围<String[开始日期，结束日期]>
     * @return GameOrder id对应参数date集合下标
     */
    List<GameOrder> getStatPlayerNumAmountByDates(@Param("serverId") int serverId, @Param("dateList") List<JSONObject> dates);

    List<MergeServerVO> getGameOrderRangeDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
