package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import cn.youai.xiuzhen.stat.entity.PlayerBehaviorVO;
import cn.youai.xiuzhen.stat.service.IPlayerStatService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private IPlayerStatService playerStatService;

    @AutoLog(value = "玩家行为分析-查询")
    @GetMapping(value = "/behavior")
    public Result<?> queryBehavior(PlayerBehaviorVO entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        if (!entity.isValidServerId()) {
            return Result.error("请选择服务器！");
        }
        if (!entity.isValidTime()) {
            return Result.error("请选择日期！");
        }

        IPage<PlayerBehavior> page = playerStatService.queryPlayerBehavior(new Page<>(pageNo, pageSize), entity);
        return Result.ok(page);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(PlayerBehaviorVO entity, HttpServletRequest request) {
        if (!entity.isValidServerId()) {
            return new ModelAndView();
        }
        if (!entity.isValidTime()) {
            return new ModelAndView();
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        IPage<PlayerBehavior> page = playerStatService.queryPlayerBehavior(new Page<>(1, Integer.MAX_VALUE), entity);
        return ExcelUtils.exportXls(sysUser.getRealname(), page.getRecords(), request.getParameter("selections"), PlayerBehavior.class, "玩家行为分析");
    }
}