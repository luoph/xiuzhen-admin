package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.stat.entity.PayOrderBill;
import cn.youai.xiuzhen.stat.entity.PayOrderBillVO;
import cn.youai.xiuzhen.stat.service.IPayOrderBillService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/payOrderBill")
public class PayOrderBillController extends JeecgController<PayOrderBill, IPayOrderBillService> {

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
    @AutoLog(value = "服务器流水-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PayOrderBill entity,
                                   @RequestParam(name = "payTimeBegin", defaultValue = "") String payTimeBegin,
                                   @RequestParam(name = "payTimeEnd", defaultValue = "") String payTimeEnd,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<PayOrderBillVO> pageVo = new Page<>(pageNo, pageSize);
        // 没有传入查询参数返回空的数据
        if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(pageVo);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        BigDecimal billSum = service.queryBillSumByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
        // 查不到流水总额,设置流水总额为0
        if (billSum == null) {
            billSum = new BigDecimal(0);
        }
        // 封装自定义的list放入Page对象，list中只有一个对象
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
     * @param pageNo   页码
     * @param pageSize 分页大小
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

        // 没有传入查询参数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && StringUtils.isEmpty(payRank)
                && serverId == 0 && channelId == 0 && days == 0) {
            return Result.error("请输入参数！");
        }
        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                return Result.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                ;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
                days = 0;
            }
        } else {
            days = 0;
        }
        String channel = gameChannelService.queryChannelNameById(channelId);

        List<PayOrderBill> list = service.queryForList(payRank, rangeDateBegin, rangeDateEnd, days, serverId, channel);
        Integer sumPayuser = list.stream().mapToInt(payorderBill -> payorderBill.getPayNumSum()).sum();
        Double sumPayuserPayAmount = list.stream().mapToDouble(payorderBill -> payorderBill.getPayAmountSum().doubleValue()).sum();
        for (PayOrderBill payOrderBill : list) {
            if (0 == sumPayuser) {
                //设置人数占比
                payOrderBill.setPayNumSumRate(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                //设置人数占比
                payOrderBill.setPayNumSumRate(BigDecimalUtils.divide(payOrderBill.getPayNumSum() * 100, sumPayuser, 2));//a÷b);
            }
            if (0 == sumPayuserPayAmount) {
                //设置 金额占比
                payOrderBill.setPayAmountSumRate(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                //设置 金额占比
                payOrderBill.setPayAmountSumRate(BigDecimalUtils.divide(payOrderBill.getPayAmountSum().doubleValue() * 100, sumPayuserPayAmount, 2));
            }

        }
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }


    /**
     * 导出excel
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
        PayOrderBill payOrderBill = service.queryPayGradeByDateRange(rangeDateBegin, rangeDateEnd, payRank, days, serverId, channel);
        payOrderBill.setPayRank(payRank);
        list.add(payOrderBill);
        return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), PayOrderBill.class, "付费结构");
    }

    @RequestMapping(value = "/download")
    public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {
        String rangeDateBegin = null == jsonObject.getString("rangeDateBegin") ? "" : jsonObject.getString("rangeDateBegin");
        ;
        String rangeDateEnd = null == jsonObject.getString("rangeDateEnd") ? "" : jsonObject.getString("rangeDateEnd");
        ;
        String payRank = null == jsonObject.getString("payRank") ? "" : jsonObject.getString("payRank");
        Integer days = null == jsonObject.getString("days") ? 0 : Integer.parseInt(jsonObject.getString("days"));
        Integer serverId = null == jsonObject.getString("serverId") ? 0 : Integer.parseInt(jsonObject.getString("serverId"));
        Integer channelId = null == jsonObject.getString("channelId") ? 0 : Integer.parseInt(jsonObject.getString("channelId"));
        Integer pageNo = null == jsonObject.getString("pageNo") ? 1 : Integer.parseInt(jsonObject.getString("pageNo"));
        Integer pageSize = null == jsonObject.getString("pageSize") ? 20 : Integer.parseInt(jsonObject.getString("pageSize"));

        // 没有传入查询参数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && StringUtils.isEmpty(payRank)
                && serverId == 0 && channelId == 0 && days == 0) {
            log.error("请输入参数!");
        }
        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                log.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                ;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
                days = 0;
            }
        } else {
            days = 0;
        }
        String channel = gameChannelService.queryChannelNameById(channelId);

        List<PayOrderBill> list = service.queryForList(payRank, rangeDateBegin, rangeDateEnd, days, serverId, channel);
        Integer sumPayuser = list.stream().mapToInt(payorderBill -> payorderBill.getPayNumSum()).sum();
        Double sumPayuserPayAmount = list.stream().mapToDouble(payorderBill -> payorderBill.getPayAmountSum().doubleValue()).sum();
        for (PayOrderBill payOrderBill : list) {
            if (0 == sumPayuser) {
                //设置人数占比
                payOrderBill.setPayNumSumRate(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                //设置人数占比
                payOrderBill.setPayNumSumRate(BigDecimalUtils.divide(payOrderBill.getPayNumSum() * 100, sumPayuser, 2));//a÷b);
            }
            if (0 == sumPayuserPayAmount) {
                //设置 金额占比
                payOrderBill.setPayAmountSumRate(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                //设置 金额占比
                payOrderBill.setPayAmountSumRate(BigDecimalUtils.divide(payOrderBill.getPayAmountSum().doubleValue() * 100, sumPayuserPayAmount, 2));
            }

        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("excel导出文件名", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), PayOrderBill.class).sheet("模板").doWrite(list);
    }

}
