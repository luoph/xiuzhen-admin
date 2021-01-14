package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.utils.ConvertUtils;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GameCampaignType;
import org.jeecg.modules.game.entity.GamePlayMethodsTakePartInVO;
import org.jeecg.modules.game.service.IGamePlayMethodsTakePartInService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;


/**
 * @author huli
 * @Description: GamePlayMethodsTakePartIn
 */

@RestController
@RequestMapping("game/playMethodsTakePart")
public class GamePlayMethodsTakePartInController {
    @Resource
    IGamePlayMethodsTakePartInService gamePlayMethodsTakePartInService;

    /**
     * 玩法参与列表
     * @param grade 玩法对应的等级
     * @param fullTime  玩法对应的满次次数
     * @param playMethodsType 玩法类型
     */
    @RequestMapping("/list")
    public Result<?> playMethodsTakePartList(String rangeDateBegin,
                          String rangeDateEnd,
                          int grade,
                          int fullTime,
                          @RequestParam(name = "serverId", defaultValue = "0") int serverId,
                          @RequestParam(name = "playMethodsType", defaultValue = "") String playMethodsType,
                          @RequestParam(name = "days", defaultValue = "0") int days,
                          @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Date rangeBegin = null;
        Date rangeEnd = null;
        if (StringUtils.isEmpty(playMethodsType)){
            return Result.error("请选择玩法类型!");
        }
        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeBegin = DateUtils.parseDate(rangeDateBegin);
            rangeEnd = DateUtils.parseDate(rangeDateEnd);
        }
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 != days) {
                rangeEnd = DateUtils.now();
                rangeBegin = DateUtils.addDays(DateUtils.now(), days * (-1) + 1);
            }
        }
        Page<GamePlayMethodsTakePartInVO> pageVo = new Page<>(pageNo, pageSize);
        List<GamePlayMethodsTakePartInVO> list = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, rangeBegin, rangeEnd, serverId);

        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,
                                  String rangeDateBegin,
                                  String rangeDateEnd,
                                  int grade,
                                  int fullTime,
                                  @RequestParam(name = "serverId", defaultValue = "0") int serverId,
                                  @RequestParam(name = "playMethodsType", defaultValue = "") String playMethodsType,
                                  @RequestParam(name = "days", defaultValue = "0") int days) {
        Date rangeBegin = null;
        Date rangeEnd = null;
        if (StringUtils.isEmpty(playMethodsType)){
            return null;
        }
        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeBegin = DateUtils.parseDate(rangeDateBegin);
            rangeEnd = DateUtils.parseDate(rangeDateEnd);
        }else{
            if (0 != days) {
                rangeEnd = DateUtils.now();
                rangeBegin = DateUtils.addDays(DateUtils.now(), days * (-1) + 1);
            }
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<GamePlayMethodsTakePartInVO> pageList = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, rangeBegin, rangeEnd, serverId);

        return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), GamePlayMethodsTakePartInVO.class, "玩法参与");
    }

    @RequestMapping("/download")
        public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {
            String rangeDateBegin = ConvertUtils.safeString(jsonObject.getString("rangeDateBegin"));
            String rangeDateEnd = ConvertUtils.safeString(jsonObject.getString("rangeDateEnd"));
            int grade = ConvertUtils.safeInteger(jsonObject.getInteger("grade"));
            int fullTime = ConvertUtils.safeInteger(jsonObject.getInteger("fullTime"));
            int serverId =  ConvertUtils.safeInteger(jsonObject.getInteger("serverId"));
            String playMethodsType =  ConvertUtils.safeString(jsonObject.getString("playMethodsType"));
            int days  = ConvertUtils.safeInteger(jsonObject.getInteger("days"));

        Date rangeBegin = null;
        Date rangeEnd = null;
        if (StringUtils.isEmpty(playMethodsType)){
            throw new Exception("玩法类型不能为空");
        }
        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeBegin = DateUtils.parseDate(rangeDateBegin);
            rangeEnd = DateUtils.parseDate(rangeDateEnd);
        }else{
            if (0 != days) {
                rangeEnd = DateUtils.now();
                rangeBegin = DateUtils.addDays(DateUtils.now(), days * (-1) + 1);
            }
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<GamePlayMethodsTakePartInVO> pageList = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, rangeBegin, rangeEnd, serverId);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("玩法参与", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), GamePlayMethodsTakePartInVO.class).sheet("模板").doWrite(pageList);
        }

}