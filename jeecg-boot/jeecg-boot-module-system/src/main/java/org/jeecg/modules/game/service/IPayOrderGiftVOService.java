package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.PayOrderGift;
import org.jeecg.modules.game.entity.PayOrderGiftVO;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 消费礼包数据统计
 * @date 2020-09-29
 */
public interface IPayOrderGiftVOService {

    List<PayOrderGiftVO> queryGiftByByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, Integer channel);
}
