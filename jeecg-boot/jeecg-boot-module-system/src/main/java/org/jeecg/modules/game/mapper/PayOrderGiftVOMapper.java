package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.PayOrderGiftVO;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
public interface PayOrderGiftVOMapper extends BaseMapper<PayOrderGiftVO> {

    List<PayOrderGiftVO> queryGiftByDateRange(@Param("payTimeBeginDate") Date payTimeBeginDate,
                                              @Param("payTimeEndDate") Date payTimeEndDate,
                                              @Param("serverId") Integer serverId,
                                              @Param("channel") String channel,
                                              @Param("productCount") Integer productCount,
                                              @Param("payAmountSum") double payAmountSum);

    PayOrderGiftVO queryGiftConsume(@Param("payTimeBeginDate") Date payTimeBeginDate,
                                    @Param("payTimeEndDate") Date payTimeEndDate,
                                    @Param("serverId") Integer serverId,
                                    @Param("channel") String channel);

}
