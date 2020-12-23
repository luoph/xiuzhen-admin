package org.jeecg.modules.game.controller;


import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameChalcedonyOrder;
import org.jeecg.modules.game.entity.GameRemainStatistisc;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.jeecg.modules.game.service.IRemainStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huli
 * @description 留存率查询
 * @date 2020-12-21
 */
@Slf4j
@RestController
@RequestMapping("game/remainStatistisc")
public class RemainStatisticsController extends JeecgController<RechargeOrder, IRechargeOrderService> {

    /**
     * 礼包类型
     */
    private static final int[] GOODS_TYPE = {0, 1, 2, 3, 4,};

    @Autowired
    private IRemainStatisticsService remainStatisticsService;
    @Autowired
    IGameChannelService gameChannelService;
    @Value("${app.log.db.table}")
    private String logTable;
    /**
     * 新增留存查询
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "新增留存-列表查询")
    @GetMapping(value = "/newUserlist")
    public Result<?> queryPageList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameRemainStatistisc> page = new Page<>(pageNo, pageSize);
        //时间相关参数校验和转换
        if(StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)){
            if(days == 0){
                return Result.error("时间不能为空！");
            }else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
            }
        }
        String channelName = gameChannelService.queryChannelNameById(channelId);
        //查询并计算新增留存
        List<GameRemainStatistisc> gameRemainStatistiscList1 = remainStatisticsService.queryRemainStatistiscOfNewUserlListB(rangeDateBegin, rangeDateEnd, logTable, serverId, channelName);
        //统计
        List<GameRemainStatistisc> gameRemainStatistiscList2 = new ArrayList<>();
        GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
        gameRemainStatistisc.setCountDate("汇总");
        Long registerNum = gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getRegisterNum).sum();
        gameRemainStatistisc.setRegisterNum(registerNum);
        gameRemainStatistisc.setC2(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC2).sum());
        gameRemainStatistisc.setC3(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC3).sum());
        gameRemainStatistisc.setC4(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC4).sum());
        gameRemainStatistisc.setC5(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC5).sum());
        gameRemainStatistisc.setC6(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC6).sum());
        gameRemainStatistisc.setC7(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC7).sum());
        gameRemainStatistisc.setC15(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC15).sum());
        gameRemainStatistisc.setC30(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC30).sum());
        gameRemainStatistisc.setC60(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC60).sum());
        gameRemainStatistisc.setC90(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC90).sum());
        gameRemainStatistisc.setC120(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC120).sum());
        gameRemainStatistiscList2.add(gameRemainStatistisc);
        gameRemainStatistiscList2.addAll(gameRemainStatistiscList1);
        page.setRecords(gameRemainStatistiscList2).setTotal(gameRemainStatistiscList2.size());
        return Result.ok(page);
    }

    /**
     * 首付留存查询
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "首付留存查询-列表查询")
    @GetMapping(value = "/downPayment")
    public Result<?> downPayment(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameRemainStatistisc> page = new Page<>(pageNo, pageSize);
        //时间相关参数校验和转换
        if(StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)){
            if(days == 0){
                return Result.error("时间不能为空！");
            }else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
            }
        }
        String channelName = gameChannelService.queryChannelNameById(channelId);
        //查询并计算新增留存
        List<GameRemainStatistisc> gameRemainStatistiscList1 = remainStatisticsService.queryRemainStatistiscOfDownPaymentList(rangeDateBegin, rangeDateEnd, logTable, serverId, channelName);
        //统计
        List<GameRemainStatistisc> gameRemainStatistiscList2 = new ArrayList<>();
        GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
        gameRemainStatistisc.setCountDate("汇总");
        Long registerNum = gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getRegisterNum).sum();
        gameRemainStatistisc.setRegisterNum(registerNum);
        gameRemainStatistisc.setC2(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC2).sum());
        gameRemainStatistisc.setC3(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC3).sum());
        gameRemainStatistisc.setC4(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC4).sum());
        gameRemainStatistisc.setC5(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC5).sum());
        gameRemainStatistisc.setC6(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC6).sum());
        gameRemainStatistisc.setC7(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC7).sum());
        gameRemainStatistisc.setC15(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC15).sum());
        gameRemainStatistisc.setC30(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC30).sum());
        gameRemainStatistisc.setC60(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC60).sum());
        gameRemainStatistisc.setC90(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC90).sum());
        gameRemainStatistisc.setC120(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC120).sum());
        gameRemainStatistiscList2.add(gameRemainStatistisc);
        gameRemainStatistiscList2.addAll(gameRemainStatistiscList1);
        page.setRecords(gameRemainStatistiscList2).setTotal(gameRemainStatistiscList2.size());
        return Result.ok(page);
    }

    /**
     * 免费留存查询
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "免费留存-列表查询")
    @GetMapping(value = "/free")
    public Result<?> free(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                 @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                 @RequestParam(name = "days", defaultValue = "0") int days,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameRemainStatistisc> page = new Page<>(pageNo, pageSize);
        //时间相关参数校验和转换
        if(StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)){
            if(days == 0){
                return Result.error("时间不能为空！");
            }else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
            }
        }
        String channelName = gameChannelService.queryChannelNameById(channelId);
        //查询并计算新增留存
        List<GameRemainStatistisc> gameRemainStatistiscList1 = remainStatisticsService.queryRemainStatistiscOfFreeList(rangeDateBegin, rangeDateEnd, logTable, serverId, channelName);
        //统计
        List<GameRemainStatistisc> gameRemainStatistiscList2 = new ArrayList<>();
        GameRemainStatistisc gameRemainStatistisc = new GameRemainStatistisc();
        gameRemainStatistisc.setCountDate("汇总");
        Long registerNum = gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getRegisterNum).sum();
        gameRemainStatistisc.setRegisterNum(registerNum);
        gameRemainStatistisc.setC2(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC2).sum());
        gameRemainStatistisc.setC3(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC3).sum());
        gameRemainStatistisc.setC4(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC4).sum());
        gameRemainStatistisc.setC5(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC5).sum());
        gameRemainStatistisc.setC6(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC6).sum());
        gameRemainStatistisc.setC7(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC7).sum());
        gameRemainStatistisc.setC15(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC15).sum());
        gameRemainStatistisc.setC30(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC30).sum());
        gameRemainStatistisc.setC60(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC60).sum());
        gameRemainStatistisc.setC90(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC90).sum());
        gameRemainStatistisc.setC120(gameRemainStatistiscList1.stream().mapToLong(GameRemainStatistisc::getC120).sum());
        gameRemainStatistiscList2.add(gameRemainStatistisc);
        gameRemainStatistiscList2.addAll(gameRemainStatistiscList1);
        page.setRecords(gameRemainStatistiscList2).setTotal(gameRemainStatistiscList2.size());
        return Result.ok(page);
    }

    /**
     * 分档留存
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "分档留存-列表查询")
    @GetMapping(value = "/grade")
    public Result<?> grade(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                          @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                          @RequestParam(name = "days", defaultValue = "0") int days,
                          @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                          @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameRemainStatistisc> page = new Page<>(pageNo, pageSize);
        //时间相关参数校验和转换
        if(StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)){
            if(days == 0){
                return Result.error("时间不能为空！");
            }else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
            }
        }
        if(!rangeDateBegin.equals(rangeDateEnd)){return  Result.error("请选择同一天的时间");}
        String channelName = gameChannelService.queryChannelNameById(channelId);
        //查询并计算新增留存
        List<GameRemainStatistisc> gameRemainStatistiscList1 = remainStatisticsService.queryRemainStatistiscOfGradeList(rangeDateBegin, rangeDateEnd, logTable, serverId, channelName);
        page.setRecords(gameRemainStatistiscList1).setTotal(gameRemainStatistiscList1.size());
        return Result.ok(page);
    }

}