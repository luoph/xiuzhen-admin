package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameVip;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameVipService;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
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

    /**
     * 玩家vip刷新接口
     */
    @Value("${app.url.player-vip:/player/vip}")
    private String playerVipUrl;

    @Autowired
    private IGameOrderService orderStatService;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Autowired
    private IGameServerService gameServerService;

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
        List<Long> addPlayerIds = new ArrayList<>();
        for (Long playerId : playerIdSet) {
            if (!vipMap.containsKey(playerId)) {
                GamePlayer player = gamePlayerService.queryPlayer(playerId);
                if (player != null && player.getStatus() == 1) {
                    addList.add(new GameVip().setPlayerId(playerId));
                    addPlayerIds.add(playerId);
                }
            }
        }

        boolean success = false;
        if (CollUtil.isNotEmpty(addList)) {
            success = service.saveBatch(addList);
            refreshVipCache();
            notifyVipUpdate(addPlayerIds, true);
        }

        return success ? Result.ok("添加成功") : Result.error("添加失败");
    }

    @AutoLog(value = "VIP-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameVip entity) {
        return Result.error("不支持编辑");
    }

    @AutoLog(value = "VIP-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        GameVip gameVip = service.getById(id);
        Result<?> result = super.delete(id);
        if (gameVip != null) {
            refreshVipCache();
            notifyVipUpdate(CollUtil.newArrayList(gameVip.getPlayerId()), false);
        }
        return result;
    }

    @AutoLog(value = "VIP-刷新缓存")
    @GetMapping(value = "/refresh")
    public Result<?> refresh() {
        boolean result = refreshVipCache();
        return result ? Result.ok("刷新成功!") : Result.error("刷新失败!");
    }

    @AutoLog(value = "VIP-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        List<Long> idList = StringUtils.split2Long(ids);
        List<GameVip> gameVips = service.listByIds(idList);
        Set<Long> playerIds = gameVips.stream().map(GameVip::getPlayerId).collect(Collectors.toSet());
        Result<?> result = super.deleteBatch(ids);
        refreshVipCache();
        notifyVipUpdate(playerIds, false);
        return result;
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

    private boolean refreshVipCache() {
        String response = OkHttpHelper.get(gameCenterUrl + "/game/vip/refresh");
        log.info("refreshVipCache response:{}", response);
        if (response != null) {
            Response result = JSON.parseObject(response, Response.class);
            return result != null && result.isSuccess();
        }
        return false;
    }

    private void notifyVipUpdate(Collection<Long> playerIds, boolean add) {
        Map<Integer, List<GamePlayer>> groupMap = gamePlayerService.groupPlayerByServerId(playerIds);
        CountDownLatch latch = new CountDownLatch(groupMap.size());
        for (Integer serverId : groupMap.keySet()) {
            GameServer gameServer = gameServerService.getById(serverId);
            List<GamePlayer> list = groupMap.get(serverId);
            Set<Long> ids = list.stream().map(GamePlayer::getPlayerId).collect(Collectors.toSet());
            if (GameServer.skipCallGm(gameServer) || CollUtil.isEmpty(ids)) {
                latch.countDown();
                continue;
            }

            // http://127.0.0.1:10082/player/vip?playerIds=1004001620,1004000133&vip=0
            Map<String, Object> params = new HashMap<>();
            params.put("playerIds", StrUtil.join(",", ids));
            params.put("vip", add ? 1 : 0);
            OkHttpHelper.getAsync(gameServer.getGmUrl() + playerVipUrl, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    latch.countDown();
                    log.error("gameServerGet onFailure, url:" + gameServer.getGmUrl() + playerVipUrl, e);
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("gameServerGet error", e);
        }
    }

    private IPage<GameVip> pageList(Page<GameVip> page, GameVip entity, HttpServletRequest req) {
        Date now = DateUtils.now();
        IPage<GameVip> pageList = service.queryVipList(page, entity);
        Set<Long> orderIdList = pageList.getRecords().stream().filter(t -> t.getOrderId() != null && t.getOrderId() > 0)
                .map(GameVip::getOrderId).collect(Collectors.toSet());
        List<GameOrder> lastOrders = orderStatService.queryByIds(orderIdList);
        Map<Long, GameOrder> orderMap = lastOrders.stream().collect(Collectors.toMap(GameOrder::getId, Function.identity(), (key1, key2) -> key2));

        for (GameVip t : pageList.getRecords()) {
            t.setPlayDays(DateUtils.daysBetween(t.getRegisterTime(), now))
                    .setLastLoginDays(DateUtils.daysBetween(t.getLastLoginTime(), now))
                    .setLastPayDays(DateUtils.daysBetween(t.getLastPayTime(), now));
            GameOrder lastOrder = orderMap.get(t.getOrderId());
            if (lastOrder != null) {
                t.setLastPay(lastOrder.getPayAmount());
            }
        }
        return pageList;
    }

}
