package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.youai.basics.model.Response;
import cn.youai.entities.GamePlayer;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameVirtualOrder;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    @Value("${app.fake-order-url:/order/fake}")
    private String fakeOrderUrl;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "虚拟充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameVirtualOrder entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameVirtualOrder> pageList = pageList(entity, pageNo, pageSize, req);
        if (pageList.getRecords() != null && pageList.getRecords().size() > 0) {
            HashSet<Long> playerIds = new HashSet<>(pageList.getRecords().size());
            pageList.getRecords().forEach(e -> playerIds.add(e.getPlayerId()));

            LambdaQueryWrapper<GamePlayer> query = Wrappers.lambdaQuery();
            query.select(GamePlayer::getPlayerId, GamePlayer::getNickname);
            query.in(GamePlayer::getPlayerId, playerIds);
            query.groupBy(GamePlayer::getPlayerId);
            List<GamePlayer> list = playerService.list(query);

            Map<Long, String> nameMap = CollectionUtil.isNotEmpty(list) ?
                    list.stream().collect(Collectors.toMap(GamePlayer::getPlayerId, GamePlayer::getNickname,
                            (item1, item2) -> item2)) : new HashMap<>(list.size());

            pageList.getRecords().forEach(e -> e.setPlayerName(nameMap.get(e.getPlayerId())));
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
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

        ImmutableMap<String, Object> params = ImmutableMap.of("playerId", entity.getPlayerId(),
                "goodsId", entity.getGoodsId());
        try {
            Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + fakeOrderUrl, params), Response.class);
            if (response != null && response.isSuccess()) {
                entity.setStatus(1);
            } else {
                entity.setStatus(0);
            }
        } catch (Exception e) {
            entity.setStatus(0);
            log.error("create virtual order error, playerId:" + entity.getPlayerId() + ", goodsId:" + entity.getGoodsId(), e);
        }

        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "虚拟充值订单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameVirtualOrder entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "虚拟充值订单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "虚拟充值订单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "虚拟充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameVirtualOrder entity) {
        return super.exportXls(request, entity, GameVirtualOrder.class, "虚拟充值订单");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameVirtualOrder.class);
    }

}
