package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.entities.GamePlayer;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.GameVip;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameVipService;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description VIP
 * @date 2023-01-13
 */
@Slf4j
@RestController
@RequestMapping("game/vip")
public class GameVipController extends JeecgController<GameVip, IGameVipService> {

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @Autowired
    private IGameOrderService orderStatService;

    @Autowired
    private IGamePlayerService playerService;

    @AutoLog(value = "VIP-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameVip entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameVip> page = new Page<>(pageNo, pageSize);
        IPage<GameVip> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "VIP-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameVip entity) {
        return addVip(entity.getPlayerIds());
    }

    @AutoLog(value = "VIP-批量添加")
    @GetMapping(value = "/addVip")
    public Result<?> addVip(@RequestParam(name = "playerIds") String playerIds) {
        Set<Long> playerIdSet = new HashSet<>(StringUtils.split2Long(playerIds));
        if (CollUtil.isEmpty(playerIdSet)) {
            return Result.error("添加失败!");
        }

        List<GameVip> list = service.queryVipList(playerIdSet);
        Map<Long, GameVip> vipMap = list.stream().collect(Collectors.toMap(GameVip::getPlayerId, Function.identity(), (key1, key2) -> key2));

        List<GameVip> addList = new ArrayList<>();
        for (Long playerId : playerIdSet) {
            if (!vipMap.containsKey(playerId)) {
                GamePlayer player = playerService.getPlayer(playerId);
                if (player != null && player.getStatus() == 1) {
                    addList.add(new GameVip().setPlayerId(playerId));
                }
            }
        }

        boolean success = false;
        if (CollUtil.isNotEmpty(addList)) {
            success = service.saveBatch(addList);
        }

        return success ? Result.ok("添加成功") : Result.error("添加失败");
    }

    @AutoLog(value = "VIP-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameVip entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "VIP-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "VIP-刷新缓存")
    @GetMapping(value = "/refresh")
    public Result<?> refresh() {
        OkHttpHelper.get(gameCenterUrl + "/player/refreshVipCache");
        return Result.ok("刷新成功!");
    }

    @AutoLog(value = "VIP-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "VIP-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "VIP-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameVip entity) {
        return super.exportXls(request, entity, GameVip.class, "VIP");
    }

    @AutoLog(value = "VIP-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameVip.class);
    }

    private IPage<GameVip> pageList(Page<GameVip> page, GameVip entity, HttpServletRequest req) {
        Date now = DateUtils.now();
        IPage<GameVip> pageList = service.queryVipList(page, entity);
        Set<Long> orderIdList = pageList.getRecords().stream().filter(t -> t.getOrderId() != null && t.getOrderId() > 0).map(GameVip::getOrderId).collect(Collectors.toSet());
        List<GameOrder> lastOrders = orderStatService.selectByIds(orderIdList);
        Map<Long, GameOrder> orderMap = lastOrders.stream().collect(Collectors.toMap(GameOrder::getId, Function.identity(), (key1, key2) -> key2));

        for (GameVip t : pageList.getRecords()) {
            t.setPlayDays(DateUtils.daysBetween(t.getRegisterTime(), now)).setLastLoginDays(DateUtils.daysBetween(t.getLastLoginTime(), now)).setLastPayDays(DateUtils.daysBetween(t.getLastPayTime(), now));
            GameOrder lastOrder = orderMap.get(t.getOrderId());
            if (lastOrder != null) {
                t.setLastPay(lastOrder.getPayAmount());
            }
        }
        return pageList;
    }

}
