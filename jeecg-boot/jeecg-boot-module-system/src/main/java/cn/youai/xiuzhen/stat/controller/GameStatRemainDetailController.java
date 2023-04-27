package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.service.IGameStatRemainDetailService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 留存详细统计
 * @date 2021/12/22
 */
@Slf4j
@RestController
@RequestMapping("/game/stat/remainDetail")
public class GameStatRemainDetailController extends JeecgController<GameStatRemainDetail, IGameStatRemainDetailService> {

    @Override
    @AutoLog(value = "留存明细-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatRemainDetail entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        Page<GameStatRemainDetail> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStatRemainDetail> pageList = pageList(entity, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "留存明细-刷新")
    @GetMapping(value = "/update")
    public Result<?> update(GameStatRemainDetail entity, HttpServletRequest req) {
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.error("请选择渠道或者区服id");
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        // 刷新统计数据
        Date endDate = DateUtils.todayDate();
        Date startDate = DateUtils.addDays(endDate, -2);
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate", startDate, endDate);

        Date current = dateRange.getEnd();
        Date todayDate = DateUtils.todayDate();
        while (!current.before(dateRange.getStart())) {
            int days = DateUtils.daysBetweenNatural(current, todayDate);
            if (entity.getServerId() != null && entity.getServerId() > 0) {
                // 按照游戏服维度统计
                if (entity.getRoleType() != null) {
                    service.calcServerRemain(entity.getServerId(), RoleType.valueOf(entity.getRoleType()), current, days, true);
                } else {
                    service.calcServerRemain(entity.getServerId(), RoleType.ALL, current, days, true);
                    service.calcServerRemain(entity.getServerId(), RoleType.PAID, current, days, true);
                    service.calcServerRemain(entity.getServerId(), RoleType.FREE, current, days, true);
                }
            } else {
                // 按照渠道维度统计
                if (entity.getRoleType() != null) {
                    service.calcChannelRemain(entity.getChannel(), RoleType.valueOf(entity.getRoleType()), current, days, true);
                } else {
                    service.calcChannelRemain(entity.getChannel(), RoleType.ALL, current, days, true);
                    service.calcChannelRemain(entity.getChannel(), RoleType.PAID, current, days, true);
                    service.calcChannelRemain(entity.getChannel(), RoleType.FREE, current, days, true);
                }
            }
            current = DateUtils.addDays(current, -1);
        }

        return Result.ok("更新成功");
    }

    @AutoLog(value = "留存明细-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatRemainDetail entity) {
        return super.exportXls(request, entity, GameStatRemainDetail.class, "留存明细统计");
    }

}
