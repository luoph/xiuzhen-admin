package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
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

        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        List<PayOrderGiftVO> payOrderGiftVOList = payOrderGiftVOMapper.queryGiftByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
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
