package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.xiuzhen.constant.OperationType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.base.PlayerDataSourceController;
import org.jeecg.modules.game.entity.GamePlayerItemLog;
import org.jeecg.modules.game.service.IGamePlayerItemLogService;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@RestController
@RequestMapping("player/playerItemLog")
public class PlayerItemLogController extends PlayerDataSourceController<GamePlayerItemLog, IGamePlayerItemLogService> {

    @Override
    protected QueryWrapper<GamePlayerItemLog> prepareQuery(GamePlayerItemLog entity, HttpServletRequest req) {
        QueryWrapper<GamePlayerItemLog> queryWrapper = super.prepareQuery(entity, req);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    protected IPage<GamePlayerItemLog> pageList(Page<GamePlayerItemLog> page, QueryWrapper<GamePlayerItemLog> queryWrapper) {
        IPage<GamePlayerItemLog> pageList = super.pageList(page, queryWrapper);
        if (CollUtil.isNotEmpty(pageList.getRecords())) {
            pageList.getRecords().forEach(t -> {
                ConfItem confItem = GameConfigUtils.getItemById(t.getItemId());
                t.setItemName(confItem != null ? confItem.getName() : "");

                // 设置产销点名字
                if (t.getType() == OperationType.INCREASE.getType()) {
                    ItemRuleId itemRuleId = ItemRuleId.valueOf(t.getWay());
                    t.setWayName(itemRuleId != null ? itemRuleId.getName() : "未知");
                } else if (t.getType() == OperationType.REDUCE.getType()) {
                    ItemReduce itemReduce = ItemReduce.valueOf(t.getWay());
                    t.setWayName(itemReduce != null ? itemReduce.getName() : "未知");
                }
            });
        }
        return pageList;
    }

    /**
     * 分页列表查询
     *
     * @param entity 数据实体
     * @param req    请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_player_item_log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayerItemLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (null == entity.getPlayerId()) {
            return Result.error("请输入玩家id！");
        }

        return queryPageList(entity.getPlayerId(), entity, pageNo, pageSize, req);
    }

}
