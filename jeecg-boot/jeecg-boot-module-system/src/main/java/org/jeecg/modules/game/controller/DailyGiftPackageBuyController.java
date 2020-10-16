package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.DailyGiftPackageBuy;
import org.jeecg.modules.game.entity.DailyGiftPackageBuyVO;
import org.jeecg.modules.game.service.IDailyGiftPackageBuyService;
import org.jeecg.modules.game.service.IDailyGiftPackageBuyVOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
@Slf4j
@RestController
@RequestMapping("game/dailyGiftPackageBuy")
public class DailyGiftPackageBuyController extends JeecgController<DailyGiftPackageBuy, IDailyGiftPackageBuyService> {

    @Autowired
    private IDailyGiftPackageBuyService dailyGiftPackageBuyService;

    @Autowired
    private IDailyGiftPackageBuyVOService dailyGiftPackageBuyVOService;

    /**
     * 分页列表查询
     *
     * @param dailyGiftPackageBuy 数据实体
     * @param pageNo              页码
     * @param pageSize            分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "daily_gift_package_buy-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DailyGiftPackageBuy dailyGiftPackageBuy,
                                   @RequestParam(name = "createTimeBegin", defaultValue = "") String createTimeBegin,
                                   @RequestParam(name = "createTimeEnd", defaultValue = "") String createTimeEnd,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        // TODO:后面可能会拓展一个channelId可能绑定多个serverId,所以保留serverId字段
        Page<DailyGiftPackageBuyVO> pageVo = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(createTimeBegin) && StringUtils.isEmpty(createTimeEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(pageVo);
        }
	    // 如果选择开始时间和结束时间是同一天
	    if (createTimeBegin.equals(createTimeEnd)){
		    createTimeBegin = createTimeBegin + " 00:00:00";
		    createTimeEnd = createTimeEnd + " 23:59:59";
	    }
        List<DailyGiftPackageBuyVO> dailyGiftPackageBuyVOList = dailyGiftPackageBuyVOService.queryGiftPackageByDateRange(serverId, createTimeBegin, createTimeEnd);
        pageVo.setRecords(dailyGiftPackageBuyVOList).setTotal(dailyGiftPackageBuyVOList.size());
        return Result.ok(pageVo);
    }

}
