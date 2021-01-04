package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        List<MilitaryStrengthVO> list = iMilitaryStrengthService.getMilitaryStrengVoDujieList(serverId, DateUtils.formatDate( DateUtils.parseDate(rangeDateBegin), DatePattern.NORM_DATE_PATTERN), DateUtils.formatDate( DateUtils.parseDate(rangeDateEnd), DatePattern.NORM_DATE_PATTERN), channel);
        if(!StringUtils.isEmpty(userName)){
            Map<String, List<MilitaryStrengthVO>> prodMap= list.stream().collect(Collectors.groupingBy(MilitaryStrengthVO::getUserName));
            if(null != prodMap.get(userName)){
                list =  prodMap.get(userName);
            }else{
                list = new ArrayList<>() ;
            }
        }
        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

}