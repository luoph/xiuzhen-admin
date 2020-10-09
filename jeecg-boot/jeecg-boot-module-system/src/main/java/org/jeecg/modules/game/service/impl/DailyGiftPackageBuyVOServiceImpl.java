package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.database.DataSourceHelper;
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
@Slf4j
@Service
public class DailyGiftPackageBuyVOServiceImpl implements IDailyGiftPackageBuyVOService {

    @Resource
    private DailyGiftPackageBuyVOMapper dailyGiftPackageBuyVOMapper;

    @Override
    public List<DailyGiftPackageBuyVO> queryGiftPackageByDateRange(Integer serverId, String createTimeBegin, String createTimeEnd) {

        List<DailyGiftPackageBuyVO> dailyGiftPackageBuyVOList = null;
        Date createTimeBeginDate = DateUtils.parseDate(createTimeBegin);
        Date createTimeEndDate = DateUtils.parseDate(createTimeEnd);
        try {
            //通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            dailyGiftPackageBuyVOList = dailyGiftPackageBuyVOMapper.queryGiftPackageByDateRange(createTimeBeginDate, createTimeEndDate);
        } catch (Exception e){
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return dailyGiftPackageBuyVOList;

    }
}
