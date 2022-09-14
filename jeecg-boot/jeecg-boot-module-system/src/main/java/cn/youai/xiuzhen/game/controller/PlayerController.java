package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.date.DateUtil;
import cn.youai.entities.GamePlayer;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.PlayerBehavior;
import cn.youai.xiuzhen.game.entity.PlayerBehaviorVO;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家
 * @date 2020-10-19
 */
@Slf4j
@RestController
@RequestMapping("game/player")
public class PlayerController extends JeecgController<GamePlayer, IGamePlayerService> {

    /**
     * 分页列表查询
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家行为分析-查询")
    @GetMapping(value = "/behavior")
    public Result<?> queryBehavior(PlayerBehaviorVO entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {

        //服务器校验
        if (entity.getServerId() == null || entity.getServerId() <= 0) {
            return Result.error("请选择服务器！");
        }

        //日期空校验
        if (entity.getDaysType() == null || entity.getDaysType() <= 0) {
            if (entity.getRangeDateBegin() == null || entity.getRangeDateEnd() == null) {
                return Result.error("请选择日期！");
            }
            if (entity.getRangeDateBegin() != null && entity.getRangeDateEnd() != null) {
                // 如果选择开始时间和结束时间是同一天
                if (DateUtils.isSameDay(entity.getRangeDateBegin(), entity.getRangeDateEnd())) {
                    entity.setRangeDateBegin(DateUtil.beginOfDay(entity.getRangeDateBegin()));
                    entity.setRangeDateEnd(DateUtil.endOfDay(entity.getRangeDateBegin()));
                }
            }
        }

        Page<PlayerBehavior> page = new Page<>(pageNo, pageSize);
        List<PlayerBehavior> list = service.queryPlayerBehavior(entity.getRangeDateBegin(), entity.getRangeDateEnd(),
                entity.getNickname(), entity.getPlayerId(), entity.getDaysType() == null ? 0 : entity.getDaysType(), entity.getServerId());
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(PlayerBehaviorVO playerBehaviorVO,
                                  HttpServletRequest request) {

        //服务器校验
        if (playerBehaviorVO.getServerId() == null || playerBehaviorVO.getServerId() <= 0) {
            return new ModelAndView();
        }

        //日期空校验
        if (playerBehaviorVO.getDaysType() == null || playerBehaviorVO.getDaysType() <= 0) {
            if (playerBehaviorVO.getRangeDateBegin() == null || playerBehaviorVO.getRangeDateEnd() == null) {
                return new ModelAndView();
            }
            if (playerBehaviorVO.getRangeDateBegin() != null && playerBehaviorVO.getRangeDateEnd() != null) {
                // 如果选择开始时间和结束时间是同一天
                if (DateUtils.isSameDay(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd())) {
                    playerBehaviorVO.setRangeDateBegin(DateUtil.beginOfDay(playerBehaviorVO.getRangeDateBegin()));
                    playerBehaviorVO.setRangeDateEnd(DateUtil.endOfDay(playerBehaviorVO.getRangeDateBegin()));
                }
            }
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PlayerBehavior> list = service.queryPlayerBehavior(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd(),
                playerBehaviorVO.getNickname(), playerBehaviorVO.getPlayerId(), playerBehaviorVO.getDaysType() == null ? 0 : playerBehaviorVO.getDaysType(), playerBehaviorVO.getServerId());
        return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), PlayerBehavior.class, "玩家行为分析");
    }
}
















