package cn.youai.xiuzhen.stat.controller;

import cn.youai.server.conf.ConfItem;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.xiuzhen.stat.constant.OperationType;
import cn.youai.xiuzhen.stat.entity.ItemLog;
import cn.youai.xiuzhen.stat.service.IItemLogService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.base.PlayerDataSourceController;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@RestController
@RequestMapping("player/itemLog")
public class PlayerItemLogController extends PlayerDataSourceController<ItemLog, IItemLogService> {

    @Override
    protected void onload(ItemLog entity) {
        ConfItem confItem = GameConfigUtils.getItemById(entity.getItemId());
        if (confItem != null) {
            entity.setItemName(confItem.getName());
        }

        // 设置产销点名字
        if (entity.getType() == OperationType.INCREASE.getType()) {
            ItemRuleId itemRuleId = ItemRuleId.valueOf(entity.getWay());
            if (itemRuleId != null) {
                entity.setWayName(itemRuleId.getName());
            }
        } else if (entity.getType() == OperationType.REDUCE.getType()) {
            ItemReduce itemReduce = ItemReduce.valueOf(entity.getWay());
            if (itemReduce != null) {
                entity.setWayName(itemReduce.getName());
            }
        }
    }

    /**
     * 分页列表查询
     */
    @AutoLog(value = "道具日志-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (null == entity.getPlayerId()) {
            return Result.error("请输入玩家id！");
        }

        return queryPageList(entity.getPlayerId(), entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "道具日志-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemLog entity) {
        return super.exportXls(entity.getPlayerId(), request, entity, ItemLog.class, "道具日志");
    }
}
