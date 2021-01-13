package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.ConvertUtils;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.MilitaryStrengthVO;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IMilitaryStrengthService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author huli
 * @Description: MilitaryStrengthListController
 * @date 2021/1/4 11:12
 */

@Slf4j
@RestController
@RequestMapping("game/militaryStrength")
public class MilitaryStrengthListController {
    @Resource
    IMilitaryStrengthService militaryStrengthService;

    @Resource
    private IGameChannelService gameChannelService;

    /**
     *
     */
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
        String channel;
        //服务器空校验
        if (0 == serverId || 0 == channelId) {
            return Result.error("请选择服务器！");
        } else {
            GameChannel gameChannel = gameChannelService.getById(channelId);
            channel = gameChannel.getSimpleName();
        }

        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                return Result.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
            }
        }
        Page<MilitaryStrengthVO> pageVo = new Page<>(pageNo, pageSize);
        List<MilitaryStrengthVO> list = militaryStrengthService.getMilitaryStrengVoAllList(userName, serverId, DateUtils.formatDate(DateUtils.parseDate(rangeDateBegin), DatePattern.NORM_DATE_PATTERN), DateUtils.formatDate(DateUtils.parseDate(rangeDateEnd), DatePattern.NORM_DATE_PATTERN), channel);

        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws IOException {
        String userName = ConvertUtils.safeString(jsonObject.getString("userName"));
        String rangeDateBegin = ConvertUtils.safeString(jsonObject.getString("rangeDateBegin"));
        String rangeDateEnd = ConvertUtils.safeString(jsonObject.getString("rangeDateEnd"));
        int serverId = ConvertUtils.safeInteger(jsonObject.getInteger("serverId"));
        int channelId = ConvertUtils.safeInteger(jsonObject.getInteger("channelId"));
        int days = ConvertUtils.safeInteger(jsonObject.getInteger("days"));

        String channel = "";
        // 服务器空校验
        if (0 == serverId || 0 == channelId) {
            log.error("请选择服务器！");
        } else {
            GameChannel gameChannel = gameChannelService.getById(channelId);
            channel = gameChannel.getSimpleName();
        }

        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }

        // 日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                log.error("请选择日期！");
            } else {
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
            }
        }

        List<MilitaryStrengthVO> list = militaryStrengthService.getMilitaryStrengVoAllList(userName, serverId,
                DateUtils.formatDate(DateUtils.parseDate(rangeDateBegin), DatePattern.NORM_DATE_PATTERN),
                DateUtils.formatDate(DateUtils.parseDate(rangeDateEnd), DatePattern.NORM_DATE_PATTERN), channel);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), MilitaryStrengthVO.class).sheet("模板").doWrite(list);
    }

}