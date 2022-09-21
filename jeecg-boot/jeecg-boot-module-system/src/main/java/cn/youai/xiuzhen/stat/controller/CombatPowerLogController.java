package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.constant.AttrType;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luopeihuan
 * @date 2022/09/15
 * @Description: CombatPowerLogController
 */

@Slf4j
@RestController
@RequestMapping("game/combatPowerLog")
public class CombatPowerLogController {
    @Autowired
    private ILogPlayerService logPlayerService;

    @RequestMapping("/list")
    public Result<?> list(CombatPowerLog entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        Page<CombatPowerLog> page = new Page<>(pageNo, pageSize);
        if ((entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        IPage<CombatPowerLog> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "战力日志-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, CombatPowerLog entity) {
        Page<CombatPowerLog> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<CombatPowerLog> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), CombatPowerLog.class, "战力日志");
    }

    private IPage<CombatPowerLog> pageList(Page<CombatPowerLog> page, CombatPowerLog entity, HttpServletRequest req) {
        // 默认查询当天
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime", DateUtils.todayDate(), DateUtils.todayDate());
        IPage<CombatPowerLog> pageList = logPlayerService.selectCombatPowerLogList(page, entity, dateRange);
        pageList.getRecords().forEach(t -> {
            AttrType attrType = AttrType.valueOf(t.getAttrType());
            if (attrType != null) {
                t.setAttrName(attrType.getName());
            }
        });
        return pageList;
    }

}