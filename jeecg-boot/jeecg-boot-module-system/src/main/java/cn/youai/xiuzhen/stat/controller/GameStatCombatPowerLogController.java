package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.constant.AttrType;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.GameStatArpu;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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
@RequestMapping("game/stat/combatPowerLog")
public class GameStatCombatPowerLogController extends SimplePageController<CombatPowerLog> {
    @Autowired
    private ILogPlayerService logPlayerService;

    @AutoLog(value = "战力日志-列表查询")
    @RequestMapping("/list")
    public Result<?> queryPageList(CombatPowerLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        // 服务器空校验
        Page<GameStatArpu> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0
                && (entity.getPlayerId() == null || entity.getPlayerId() < 0))) {
            return Result.ok(page);
        }
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "战力日志-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, CombatPowerLog entity) {
        return super.exportXls(req, entity, CombatPowerLog.class, "战力日志");
    }

    @Override
    protected IPage<CombatPowerLog> pageList(Page<CombatPowerLog> page, CombatPowerLog entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int serverId = entity.getServerId() != null ? entity.getServerId() : 0;
        long playerId = entity.getPlayerId() != null ? entity.getPlayerId() : 0;

        if (serverId > 0 || playerId > 0) {
            entity.setChannel(null);
        }

        IPage<CombatPowerLog> pageList = logPlayerService.selectCombatPowerLogList(page, entity.getChannel(),
                serverId, playerId, dateRange.getStart(), dateRange.getEnd());
        pageList.getRecords().forEach(t -> {
            AttrType attrType = AttrType.valueOf(t.getAttrType());
            if (attrType != null) {
                t.setAttrName(attrType.getName());
            }
        });
        return pageList;
    }

}