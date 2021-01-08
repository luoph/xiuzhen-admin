package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.MilitaryStrengthVO;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IMilitaryStrengthService;
import org.jeecg.modules.player.entity.GameOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huli
 * @Description: MilitaryStrengthListController
 * @date 2021/1/4 11:12
 */

@RestController
@RequestMapping("game/militaryStrength")
public class MilitaryStrengthListController {
    Log log = LogFactory.getLog(this.getClass());
    @Autowired
    IMilitaryStrengthService iMilitaryStrengthService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     *
     */
    @AutoLog(value = "")
    @RequestMapping("/list")
    public Result<?> militaryStrengthList(
                                 String userName,
                                 String rangeDateBegin,
                                 String rangeDateEnd,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "days", defaultValue = "0") Integer days,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = "";
        //服务器空校验
        if (0 == serverId || 0 == channelId) {
            return Result.error("请选择服务器！");
        }else{
            GameChannel gameChannel = gameChannelService.getById(channelId);
            channel = gameChannel.getSimpleName();
        }

        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                return Result.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
                ;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
                ;
            }
        }
        Page<MilitaryStrengthVO> pageVo = new Page<>(pageNo, pageSize);
        List<MilitaryStrengthVO> list = iMilitaryStrengthService.getMilitaryStrengVoAllList(userName,serverId, DateUtils.formatDate( DateUtils.parseDate(rangeDateBegin), DatePattern.NORM_DATE_PATTERN), DateUtils.formatDate( DateUtils.parseDate(rangeDateEnd), DatePattern.NORM_DATE_PATTERN), channel);
//        if(!StringUtils.isEmpty(userName)){
//            Map<String, List<MilitaryStrengthVO>> prodMap= list.stream().collect(Collectors.groupingBy(MilitaryStrengthVO::getUserName));
//            if(null != prodMap.get(userName)){
//                list =  prodMap.get(userName);
//            }else{
//                list = new ArrayList<>() ;
//            }
//        }
        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws IOException {
        String userName = null == jsonObject.getString("userName") ? "":jsonObject.getString("userName");
        String rangeDateBegin = null == jsonObject.getString("rangeDateBegin") ? "":jsonObject.getString("rangeDateBegin");;
        String rangeDateEnd = null == jsonObject.getString("rangeDateEnd") ? "":jsonObject.getString("rangeDateEnd");;
        Integer serverId =  null == jsonObject.getString("serverId") ? 0:Integer.parseInt(jsonObject.getString("serverId"));
        Integer channelId =  null == jsonObject.getString("channelId") ? 0:Integer.parseInt(jsonObject.getString("channelId"));
        Integer days =  null == jsonObject.getString("days") ? 0:Integer.parseInt(jsonObject.getString("days"));
        Integer pageNo =  null == jsonObject.getString("pageNo") ? 1:Integer.parseInt(jsonObject.getString("pageNo"));
        Integer pageSize  =  null == jsonObject.getString("pageSize") ? 20:Integer.parseInt(jsonObject.getString("pageSize"));

        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = "";
        //服务器空校验
        if (0 == serverId || 0 == channelId) {
            log.error("请选择服务器！");
        }else{
            GameChannel gameChannel = gameChannelService.getById(channelId);
            channel = gameChannel.getSimpleName();
        }

        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                log.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
                ;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
                ;
            }
        }

        List<MilitaryStrengthVO> list = iMilitaryStrengthService.getMilitaryStrengVoAllList(userName,serverId, DateUtils.formatDate( DateUtils.parseDate(rangeDateBegin), DatePattern.NORM_DATE_PATTERN), DateUtils.formatDate( DateUtils.parseDate(rangeDateEnd), DatePattern.NORM_DATE_PATTERN), channel);
        if(!StringUtils.isEmpty(userName)){
            Map<String, List<MilitaryStrengthVO>> prodMap= list.stream().collect(Collectors.groupingBy(MilitaryStrengthVO::getUserName));
            if(null != prodMap.get(userName)){
                list =  prodMap.get(userName);
            }else{
                list = new ArrayList<>() ;
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), MilitaryStrengthVO.class).sheet("模板").doWrite(list);
    }

}