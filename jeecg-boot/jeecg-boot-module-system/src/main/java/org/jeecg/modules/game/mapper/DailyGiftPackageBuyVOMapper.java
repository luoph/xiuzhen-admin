package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.DailyGiftPackageBuyVO;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
public interface DailyGiftPackageBuyVOMapper {

    List<DailyGiftPackageBuyVO> queryGiftPackageByDateRange(@Param("createTimeBeginDate") Date createTimeBeginDate,
                                                            @Param("createTimeEndDate") Date createTimeEndDate);
}
