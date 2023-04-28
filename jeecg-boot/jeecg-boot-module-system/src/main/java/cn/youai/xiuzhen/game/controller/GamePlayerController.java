package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.entity.GameVip;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameVipService;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("/game/player")
public class GamePlayerController extends JeecgController<GamePlayer, IGamePlayerService> {

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Autowired
    private IGameOrderService gameOrderService;

    @Autowired
    private IGameVipService gameVipService;

    @Override
    @AutoLog(value = "玩家信息-列表查询")
    @GetMapping(value = "/list")
    @PermissionData(value = "player/GamePlayerList")
    public Result<?> queryPageList(GamePlayer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected void onload(List<GamePlayer> pageList) {
        Map<Long, GamePlayer> playerMap = pageList.stream().collect(Collectors.toMap(GamePlayer::getPlayerId, Function.identity(), (key1, key2) -> key2));

        List<GameOrder> gameOrders = gameOrderService.queryPlayerTotalPayAmount(playerMap.keySet());
        Map<Long, GameOrder> orderMap = gameOrders.stream().collect(Collectors.toMap(GameOrder::getPlayerId, Function.identity(), (key1, key2) -> key2));

        List<GameVip> gameVips = gameVipService.queryVipList(playerMap.keySet());
        Map<Long, GameVip> vipMap = gameVips.stream().collect(Collectors.toMap(GameVip::getPlayerId, Function.identity(), (key1, key2) -> key2));

        playerMap.forEach((k, v) -> {
            GameOrder gameOrder = orderMap.get(k);
            v.setTotalPayAmount(gameOrder != null ? gameOrder.getTotalPayAmount() : BigDecimal.ZERO);

            GameVip gameVip = vipMap.get(k);
            v.setVipId(gameVip != null ? gameVip.getId() : 0L);
        });
    }

    @AutoLog(value = "玩家信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GamePlayer entity = gamePlayerService.queryPlayer(Long.parseLong(id));
        if (entity == null) {
            return Result.error("未找到玩家信息");
        }
        return Result.ok(entity);
    }

    @AutoLog(value = "玩家信息-导出")
    @RequestMapping(value = "/exportXls")
    @PermissionData(value = "player/GamePlayerList")
    public ModelAndView exportXls(HttpServletRequest request, GamePlayer entity) {
        return super.exportXls(request, entity, GamePlayer.class, "玩家信息");
    }
}
