package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import org.jeecg.modules.game.entity.DailyGiftPackageBuyVO;
import org.jeecg.modules.game.mapper.DailyGiftPackageBuyVOMapper;
import org.jeecg.modules.game.service.IDailyGiftPackageBuyVOService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
@Service
public class DailyGiftPackageBuyVOServiceImpl implements IDailyGiftPackageBuyVOService {

    @Resource
    private DailyGiftPackageBuyVOMapper dailyGiftPackageBuyVOMapper;

    @Override
    public List<DailyGiftPackageBuyVO> queryGiftPackageByDateRange(String createTimeBegin, String createTimeEnd) {

        Date createTimeBeginDate = DateUtils.parseDate(createTimeBegin);
        Date createTimeEndDate = DateUtils.parseDate(createTimeEnd);

        return dailyGiftPackageBuyVOMapper.queryGiftPackageByDateRange(createTimeBeginDate, createTimeEndDate);
    }
}
