package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GameDataReportCount;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameDataReportCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameDataReportCount")
public class GameDataReportCountController extends JeecgController<GameDataReportCount, IGameDataReportCountService> {

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "数据报表-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameDataReportCount entity,
                                   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {

        Page<GameDataReportCount> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameDataReportCount> gameDataReportCountList = service.queryDataReportByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        page.setRecords(gameDataReportCountList).setTotal(gameDataReportCountList.size());
        return Result.ok(page);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                  @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                  @RequestParam(name = "days", defaultValue = "0") int days,
                                  @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                  @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                  HttpServletRequest request) {
        // 获取导出数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameDataReportCount> gameDataReportCountList = service.queryDataReportByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        return ExcelUtils.exportXls(sysUser.getRealname(), gameDataReportCountList, request.getParameter("selections"), GameDataReportCount.class, "数据报表");
    }


}
