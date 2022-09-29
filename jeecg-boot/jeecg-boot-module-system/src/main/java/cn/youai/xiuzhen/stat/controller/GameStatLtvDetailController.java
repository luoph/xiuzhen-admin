package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.stat.service.IGameStatLtvDetailService;
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
 * @description ltv详细统计
 * @date 2021/12/28
 */
@Slf4j
@RestController
@RequestMapping("/game/stat/ltvDetail")
public class GameStatLtvDetailController extends JeecgController<GameStatLtvDetail, IGameStatLtvDetailService> {

    @Override
    @AutoLog(value = "LTV详细-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatLtvDetail entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameStatLtvDetail> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStatLtvDetail> pageList = pageList(entity, pageNo, pageSize, req);
        return Result.ok(pageList);
    }


    @AutoLog(value = "LTV详细-更新")
    @GetMapping(value = "/update")
    public Result<?> update(GameStatLtvDetail entity, HttpServletRequest req) {
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
        Date current = dateRange.getStart();

        Date todayDate = DateUtils.todayDate();
        while (!current.after(dateRange.getEnd())) {
            int days = DateUtils.daysBetweenNatural(current, todayDate);
            if (entity.getServerId() != null && entity.getServerId() > 0) {
                // 按照游戏服维度统计
                service.calcServerLtvDetail(entity.getServerId(), current, days, true);
            } else {
                // 按照渠道维度统计
                service.calcChannelLtvDetail(entity.getChannel(), current, days, true);
            }
            current = DateUtils.addDays(current, 1);
        }

        return Result.ok("更新成功");
    }

    @AutoLog(value = "LTV详细-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatLtvDetail entity) {
        return super.exportXls(request, entity, GameStatLtvDetail.class, "LTV详细统计");
    }

}
