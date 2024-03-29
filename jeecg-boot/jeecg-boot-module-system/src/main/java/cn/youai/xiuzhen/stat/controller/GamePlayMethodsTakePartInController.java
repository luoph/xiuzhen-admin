package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.ConvertUtils;
import cn.youai.xiuzhen.stat.entity.GamePlayMethodsTakePartInVO;
import cn.youai.xiuzhen.stat.service.IGamePlayMethodsTakePartInService;
import cn.youai.xiuzhen.utils.ParamUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
     *
     * @param grade           玩法对应的等级
     * @param fullTime        玩法对应的满次次数
     * @param playMethodsType 玩法类型
     */
    @RequestMapping("/list")
    public Result<?> playMethodsTakePartList(String rangeDateBegin,
                                             String rangeDateEnd,
                                             @RequestParam(name = "grade", defaultValue = "0") int grade,
                                             @RequestParam(name = "fullTime", defaultValue = "0") int fullTime,
                                             @RequestParam(name = "serverId", defaultValue = "0") int serverId,
                                             @RequestParam(name = "playMethodsType", defaultValue = "") String playMethodsType,
                                             @RequestParam(name = "days", defaultValue = "0") int days,
                                             @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        if (StringUtils.isEmpty(playMethodsType)) {
            return Result.error("请选择玩法类型!");
        }
        // 日期空校验
        DateRange dateRange = ParamUtils.getDateRange(rangeDateBegin, rangeDateEnd, days);
        if (null == dateRange) {
            return Result.error("请选择日期！");
        }
        Page<GamePlayMethodsTakePartInVO> pageVo = new Page<>(pageNo, pageSize);
        List<GamePlayMethodsTakePartInVO> list = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, dateRange.getStart(), dateRange.getEnd(), serverId);

        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

    @AutoLog(value = "XXX-导出") // TODO
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,
                                  String rangeDateBegin,
                                  String rangeDateEnd,
                                  int grade,
                                  int fullTime,
                                  @RequestParam(name = "serverId", defaultValue = "0") int serverId,
                                  @RequestParam(name = "playMethodsType", defaultValue = "") String playMethodsType,
                                  @RequestParam(name = "days", defaultValue = "0") int days) {

        if (StringUtils.isEmpty(playMethodsType)) {
            return null;
        }
        // 日期空校验
        DateRange dateRange = ParamUtils.getDateRange(rangeDateBegin, rangeDateEnd, days);
        if (null == dateRange) {
            return null;
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<GamePlayMethodsTakePartInVO> pageList = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, dateRange.getStart(), dateRange.getEnd(), serverId);

        return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), GamePlayMethodsTakePartInVO.class, "玩法参与");
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {
        String rangeDateBegin = ConvertUtils.safeString(jsonObject.getString("rangeDateBegin"));
        String rangeDateEnd = ConvertUtils.safeString(jsonObject.getString("rangeDateEnd"));
        int grade = ConvertUtils.safeInteger(jsonObject.getInteger("grade"));
        int fullTime = ConvertUtils.safeInteger(jsonObject.getInteger("fullTime"));
        int serverId = ConvertUtils.safeInteger(jsonObject.getInteger("serverId"));
        String playMethodsType = ConvertUtils.safeString(jsonObject.getString("playMethodsType"));
        int days = ConvertUtils.safeInteger(jsonObject.getInteger("days"));

        if (StringUtils.isEmpty(playMethodsType)) {
            throw new Exception("玩法类型不能为空");
        }
        // 日期空校验
        DateRange dateRange = ParamUtils.getDateRange(rangeDateBegin, rangeDateEnd, days);
        if (null == dateRange) {
            throw new Exception("请选择日期！");
        }
        List<GamePlayMethodsTakePartInVO> pageList = gamePlayMethodsTakePartInService.playMethodsTakePartList(fullTime, grade, playMethodsType, dateRange.getStart(), dateRange.getEnd(), serverId);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("玩法参与", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), GamePlayMethodsTakePartInVO.class).sheet("模板").doWrite(pageList);
    }

}