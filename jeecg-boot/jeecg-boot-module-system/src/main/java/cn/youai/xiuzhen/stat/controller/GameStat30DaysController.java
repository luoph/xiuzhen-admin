package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.stat.entity.GameStat30Days;
import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.service.IGameStatLtvDetailService;
import cn.youai.xiuzhen.stat.service.IGameStatRemainDetailService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * @ClassName GameDataCountController
 * @Description 游戏数据统计
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-20 17:31
 */
@Slf4j
@RestController
@Api(tags = "30日数据")
@RequestMapping("game/stat/30days")
public class GameStat30DaysController {

    @Autowired
    private IGameStatRemainDetailService remainDetailService;

    @Autowired
    private IGameStatLtvDetailService ltvDetailService;

    @RequestMapping("/list")
    public Result<?> list(GameStat30Days entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        Page<GameStat30Days> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStat30Days> pageList = pageList(pageNo, pageSize, entity, req);
        return Result.ok(pageList);
    }

    @SuppressWarnings("DuplicatedCode")
    private IPage<GameStat30Days> pageList(int pageNo, int pageSize, GameStat30Days entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int type = entity.getType() != null ? entity.getType() : 1;
        if (type == 1) {
            LambdaQueryWrapper<GameStatRemainDetail> query = Wrappers.<GameStatRemainDetail>lambdaQuery()
                    .eq(GameStatRemainDetail::getRoleType, RoleType.ALL.getValue());
            if (entity.getServerId() != null) {
                query.eq(GameStatRemainDetail::getServerId, entity.getServerId());
            }
            if (StringUtils.isNotBlank(entity.getChannel())) {
                query.eq(GameStatRemainDetail::getChannel, entity.getChannel());
            }
            if (dateRange.getStart() != null) {
                query.ge(GameStatRemainDetail::getCountDate, dateRange.getStart());
            }
            if (dateRange.getEnd() != null) {
                query.le(GameStatRemainDetail::getCountDate, dateRange.getEnd());
            }

            Page<GameStatRemainDetail> page = new Page<>(pageNo, pageSize);
            Page<GameStatRemainDetail> pageList = remainDetailService.page(page, query);
            return convertRemainPage(pageList);
        } else if (type == 2) {
            LambdaQueryWrapper<GameStatLtvDetail> query = lambdaQuery();
            if (entity.getServerId() != null) {
                query.eq(GameStatLtvDetail::getServerId, entity.getServerId());
            }
            if (StringUtils.isNotBlank(entity.getChannel())) {
                query.eq(GameStatLtvDetail::getChannel, entity.getChannel());
            }
            if (dateRange.getStart() != null) {
                query.ge(GameStatLtvDetail::getCountDate, dateRange.getStart());
            }
            if (dateRange.getEnd() != null) {
                query.le(GameStatLtvDetail::getCountDate, dateRange.getEnd());
            }

            Page<GameStatLtvDetail> page = new Page<>(pageNo, pageSize);
            Page<GameStatLtvDetail> pageList = ltvDetailService.page(page, query);
            return convertLtvPage(pageList);
        }
        return null;
    }

    @SuppressWarnings("DuplicatedCode")
    private Page<GameStat30Days> convertRemainPage(Page<GameStatRemainDetail> page) {
        Page<GameStat30Days> pageList = new Page<>();
        pageList.setTotal(page.getTotal());
        pageList.setSize(page.getSize());
        pageList.setPages(page.getPages());
        pageList.setCurrent(page.getCurrent());

        List<GameStat30Days> records = new ArrayList<>(page.getRecords().size());
        page.getRecords().forEach(t -> records.add(GameStat30Days.from(t)));
        pageList.setRecords(records);
        return pageList;
    }

    @SuppressWarnings("DuplicatedCode")
    private Page<GameStat30Days> convertLtvPage(Page<GameStatLtvDetail> page) {
        Page<GameStat30Days> pageList = new Page<>();
        pageList.setTotal(page.getTotal());
        pageList.setSize(page.getSize());
        pageList.setPages(page.getPages());
        pageList.setCurrent(page.getCurrent());

        List<GameStat30Days> records = new ArrayList<>(page.getRecords().size());
        page.getRecords().forEach(t -> records.add(GameStat30Days.from(t)));
        pageList.setRecords(records);
        return pageList;
    }

}
