package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatArpu;
import cn.youai.xiuzhen.stat.service.IGameStatArpuService;
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
 * @description 数据报表
 * @date 2020-10-10
 */
@Slf4j
@RestController
@RequestMapping("game/stat/arpu")
public class GameStatArpuController extends JeecgController<GameStatArpu, IGameStatArpuService> {

    @AutoLog(value = "ARPU统计-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatArpu entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameStatArpu> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStatArpu> pageList = pageList(entity, pageNo, pageSize, req);
        return Result.ok(pageList);
    }


    @GetMapping(value = "/update")
    public Result<?> update(GameStatArpu entity, HttpServletRequest req) {
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
        Date startDate = DateUtils.addDays(endDate, -7);
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate", startDate, endDate);
        Date current = dateRange.getStart();

        while (!current.after(dateRange.getEnd())) {
            if (entity.getServerId() != null && entity.getServerId() > 0) {
                // 按照游戏服维度统计
                service.calcServerArpu(entity.getServerId(), current);
            } else {
                // 按照渠道维度统计
                service.calcChannelArpu(entity.getChannel(), current);
            }
            current = DateUtils.addDays(current, 1);
        }

        return Result.ok("更新成功");
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatArpu entity) {
        return super.exportXls(request, entity, GameStatArpu.class, "ARPU统计");
    }

}
