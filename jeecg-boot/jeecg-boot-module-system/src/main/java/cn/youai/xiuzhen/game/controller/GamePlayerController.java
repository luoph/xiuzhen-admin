package cn.youai.xiuzhen.game.controller;

import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("/player/playerInfo")
public class GamePlayerController extends JeecgController<GamePlayer, IGamePlayerService> {

    @Override
    protected QueryWrapper<GamePlayer> prepareQuery(GamePlayer entity, HttpServletRequest request) {
        QueryWrapper<GamePlayer> queryWrapper = super.prepareQuery(entity, request);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    @AutoLog(value = "玩家信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "玩家信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GamePlayer entity = service.getPlayer(Long.parseLong(id));
        if (entity == null) {
            return Result.error("未找到玩家信息");
        }
        return Result.ok(entity);
    }

    @AutoLog(value = "玩家信息-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GamePlayer entity) {
        return super.exportXls(request, entity, GamePlayer.class, "玩家信息");
    }

}
