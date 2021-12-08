package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.server.utils.DateUtils;
import org.jeecg.modules.game.entity.PayOrderGiftVO;
import org.jeecg.modules.game.mapper.PayOrderGiftVOMapper;
import org.jeecg.modules.game.service.IPayOrderGiftVOService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Service
public class PayOrderGiftVOServiceImpl implements IPayOrderGiftVOService {

    @Resource
    private PayOrderGiftVOMapper payOrderGiftVOMapper;

    @Override
    public List<PayOrderGiftVO> queryGiftByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {
        // 如果选择开始时间和结束时间是同一天
        Date payTimeBeginDate;
        Date payTimeEndDate;
        payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        if (payTimeBegin.equals(payTimeEnd)) {
            Date[] dates = DateUtils.dateStartAndEnd(payTimeBeginDate);
            payTimeBeginDate = dates[0];
            payTimeEndDate = dates[1];
        }

        // 查询礼包消费的次数和消费的总金额
        PayOrderGiftVO giftConsume = payOrderGiftVOMapper.queryGiftConsume(payTimeBeginDate, payTimeEndDate, serverId, channel);
        Integer productCount = giftConsume.getProductCount();
        double payAmountSum = giftConsume.getPayAmountSum() != null ? giftConsume.getPayAmountSum().doubleValue() : 0d;
        List<PayOrderGiftVO> payOrderGiftVOList = payOrderGiftVOMapper.queryGiftByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel, productCount, payAmountSum);

        for (PayOrderGiftVO payOrderGiftVO : payOrderGiftVOList) {
            // 数据处理
            BigDecimal productCountRatio = payOrderGiftVO.getProductCountRatio();
            payOrderGiftVO.setProductCountRatio(BigDecimalUtil.dividePercent(productCountRatio.doubleValue()));

            BigDecimal payAmountRatio = payOrderGiftVO.getPayAmountRatio();
            payOrderGiftVO.setPayAmountRatio(BigDecimalUtil.dividePercent(payAmountRatio.doubleValue()));
        }

        return payOrderGiftVOList;
    }
}
