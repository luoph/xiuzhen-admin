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
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.entity.PayOrderBillVO;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IPayOrderBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/payOrderBill")
public class PayOrderBillController extends JeecgController<PayOrderBill, IPayOrderBillService> {

    @Autowired
    private IPayOrderBillService payOrderBillService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param payOrderBill 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "服务器流水-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PayOrderBill payOrderBill,
                                   @RequestParam(name = "payTimeBegin", defaultValue = "") String payTimeBegin,
                                   @RequestParam(name = "payTimeEnd", defaultValue = "") String payTimeEnd,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
                                    ) {
        Page<PayOrderBillVO> pageVo = new Page<>(pageNo, pageSize);
        //没有传入查询参数返回空的数据
        if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(pageVo);
        }
        String channel= gameChannelService.queryChannelNameById(channelId);
        BigDecimal billSum = payOrderBillService.queryBillSumByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
        //查不到流水总额,设置流水总额为0
        if (billSum == null){
            billSum = new BigDecimal(0);
        }
        //封装自定义的list放入Page对象，list中只有一个对象
        List<PayOrderBillVO> listVo = new ArrayList<>();
        PayOrderBillVO payOrderBillVo = new PayOrderBillVO();
        payOrderBillVo.setBillSum(billSum).setBeginTime(payTimeBegin).setEndTime(payTimeEnd);
        listVo.add(payOrderBillVo);
        pageVo.setRecords(listVo).setTotal(listVo.size());
        return Result.ok(pageVo);
    }


    /**
     * 分页列表查询
     *
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "付费结构-列表查询")
    @GetMapping(value = "/payConstruction")
    public Result<?> queryPayConstruction(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                           @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                           @RequestParam(name = "payRank", defaultValue = "") String payRank,
                                           @RequestParam(name = "days", defaultValue = "0") int days,
                                           @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
                                    ) {
        Page<PayOrderBill> page = new Page<>(pageNo, pageSize);
        List<PayOrderBill> list = new ArrayList<>();
        //没有传入查询参数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(page);
        }
        String channel= gameChannelService.queryChannelNameById(channelId);
        PayOrderBill payOrderBill = payOrderBillService.queryPaygGradeByDateRange(rangeDateBegin, rangeDateEnd, payRank, days, serverId, channel);
        payOrderBill.setPayRank(payRank);
        list.add(payOrderBill);
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }


    /**
     * 导出excel
     *
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "payRank", defaultValue = "") String payRank,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest request
                                    ) {
        // 获取导出数据
        List<PayOrderBill> list = new ArrayList<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String channel = gameChannelService.queryChannelNameById(channelId);
        PayOrderBill payOrderBill = payOrderBillService.queryPaygGradeByDateRange(rangeDateBegin, rangeDateEnd, payRank, days, serverId, channel);
        payOrderBill.setPayRank(payRank);
        list.add(payOrderBill);
        return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), PayOrderBill.class, "付费结构");
    }


}
