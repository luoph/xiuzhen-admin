package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import org.jeecg.modules.game.entity.PayOrderGiftVO;
import org.jeecg.modules.game.mapper.PayOrderGiftVOMapper;
import org.jeecg.modules.game.service.IPayOrderGiftVOService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<PayOrderGiftVO> queryGiftByByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {

        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        return payOrderGiftVOMapper.queryGiftByByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
    }
}
