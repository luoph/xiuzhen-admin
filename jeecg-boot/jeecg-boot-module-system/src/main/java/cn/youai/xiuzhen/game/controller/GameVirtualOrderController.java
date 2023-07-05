package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameVirtualOrder;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameVirtualOrderService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
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
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟充值订单
 * @date 2020-09-02
 */
@Slf4j
@RestController
@RequestMapping("game/gameVirtualOrder")
public class GameVirtualOrderController extends JeecgController<GameVirtualOrder, IGameVirtualOrderService> {

    @Autowired
    private IGamePlayerService playerService;

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameRechargeGoodsService rechargeGoodsService;

    @Value("${app.fake-order-url:/order/fake}")
    private String fakeOrderUrl;

    @AutoLog(value = "虚拟充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameVirtualOrder entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameVirtualOrder> pageList = pageList(entity, pageNo, pageSize, req);
        if (pageList.getRecords() != null && pageList.getRecords().size() > 0) {
            Set<Long> playerIds = new HashSet<>(pageList.getRecords().size());
            Set<Integer> goodsIds = new HashSet<>(pageList.getRecords().size());
            pageList.getRecords().forEach(e -> {
                playerIds.add(e.getPlayerId());
                goodsIds.add(e.getGoodsId());
            });

            Map<Long, String> playerNameMap = getPlayerNameMap(playerIds);
            Map<Integer, String> goodsNameMap = getGoodsNameMap(goodsIds);

            pageList.getRecords().forEach(e -> {
                e.setPlayerName(playerNameMap.get(e.getPlayerId()));
                e.setGoodsName(goodsNameMap.get(e.getGoodsId()));
            });
        }
        return Result.ok(pageList);
    }

    @AutoLog(value = "虚拟充值订单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameVirtualOrder entity) {
        if (entity.getPlayerId() == null) {
            return Result.error("请输入玩家id");
        }
        Wrapper<GamePlayer> query = Wrappers.<GamePlayer>lambdaQuery().eq(GamePlayer::getPlayerId, entity.getPlayerId());
        GamePlayer playerInfo = playerService.getOne(query);
        if (playerInfo == null) {
            return Result.error("找不到玩家信息:" + entity.getPlayerId());
        }
        entity.setServerId(playerInfo.getServerId());

        GameServer gameServer = gameServerService.getById(playerInfo.getServerId());
        if (gameServer == null) {
            return Result.error("找不到区服信息:" + playerInfo.getServerId());
        }

        List<Integer> goodsIdList = StringUtils.split2Int(entity.getGoodsIds());
        if (goodsIdList.isEmpty()) {
            return Result.error("订单号为空:" + entity.getGoodsIds());
        }

        goodsIdList.forEach(goodsId -> {
            entity.setId(null).setGoodsId(goodsId);
            ImmutableMap<String, Object> params = ImmutableMap.of("playerId", entity.getPlayerId(), "goodsId", goodsId);
            try {
                Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + fakeOrderUrl, params), Response.class);
                entity.setStatus(response != null && response.isSuccess() ? 1 : 0);
            } catch (Exception e) {
                entity.setStatus(0);
                log.error("create virtual order error, playerId:" + entity.getPlayerId() + ", goodsId:" + goodsId, e);
            }
            super.add(entity);
        });
        return Result.ok("充值成功");
    }

    @AutoLog(value = "虚拟充值订单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameVirtualOrder entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "虚拟充值订单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "虚拟充值订单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "虚拟充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "虚拟充值订单-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameVirtualOrder entity) {
        return super.exportXls(request, entity, GameVirtualOrder.class, "虚拟充值订单");
    }

    @AutoLog(value = "虚拟充值订单-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameVirtualOrder.class);
    }


    private Map<Long, String> getPlayerNameMap(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<GamePlayer> query = Wrappers.lambdaQuery();
        query.select(GamePlayer::getPlayerId, GamePlayer::getNickname);
        query.in(GamePlayer::getPlayerId, playerIds);
        query.groupBy(GamePlayer::getPlayerId);
        List<GamePlayer> list = playerService.list(query);

        return CollectionUtil.isNotEmpty(list) ?
                list.stream().collect(Collectors.toMap(GamePlayer::getPlayerId, GamePlayer::getNickname,
                        (item1, item2) -> item2)) : new HashMap<>(list.size());
    }

    private Map<Integer, String> getGoodsNameMap(Collection<Integer> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<GameRechargeGoods> query = Wrappers.lambdaQuery();
        query.select(GameRechargeGoods::getGoodsId, GameRechargeGoods::getName);
        query.in(GameRechargeGoods::getGoodsId, goodsIds);
        List<GameRechargeGoods> list = rechargeGoodsService.list(query);

        return CollectionUtil.isNotEmpty(list) ?
                list.stream().collect(Collectors.toMap(GameRechargeGoods::getGoodsId, GameRechargeGoods::getName,
                        (item1, item2) -> item2)) : new HashMap<>(list.size());
    }
}
