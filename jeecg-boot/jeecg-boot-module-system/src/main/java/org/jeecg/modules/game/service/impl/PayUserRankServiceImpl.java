package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.PayUserRank;
import org.jeecg.modules.game.mapper.PayUserRankMapper;
import org.jeecg.modules.game.service.IPayUserRankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Service
public class PayUserRankServiceImpl extends ServiceImpl<PayUserRankMapper, PayUserRank> implements IPayUserRankService {

    @Resource
    private PayUserRankMapper payUserRankMapper;

    @Override
    public List<PayUserRank> queryUserRankByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {

        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        return payUserRankMapper.queryUserRankByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
    }
}
