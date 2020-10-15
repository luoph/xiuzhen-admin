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
import org.jeecg.modules.game.entity.PayUserRank;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IPayUserRankService;
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
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Slf4j
@RestController
@RequestMapping("game/payUserRank")
public class PayUserRankController extends JeecgController<PayUserRank, IPayUserRankService> {

    @Autowired
    private IPayUserRankService payUserRankService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值用户排行数据统计-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "payTimeBegin", defaultValue = "") String payTimeBegin,
                                   @RequestParam(name = "payTimeEnd", defaultValue = "") String payTimeEnd,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<PayUserRank> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(page);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<PayUserRank> payUserRankList = payUserRankService.queryUserRankByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
        page.setRecords(payUserRankList).setTotal(payUserRankList.size());
        return Result.ok(page);
    }


    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "付费排行-列表查询")
    @GetMapping(value = "/payRank")
    public Result<?> payRank(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                             @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                             @RequestParam(name = "days", defaultValue = "0") int days,
                             @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                             @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<PayUserRank> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<PayUserRank> payUserRankList = payUserRankService.queryPayRankByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);

        page.setRecords(payUserRankList).setTotal(payUserRankList.size());
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<PayUserRank> payUserRankList = payUserRankService.queryPayRankByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        return ExcelUtils.exportXls(sysUser.getRealname(), payUserRankList, request.getParameter("selections"), PayUserRank.class, "付费排行");
    }


}
