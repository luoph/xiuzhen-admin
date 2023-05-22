package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.xiuzhen.core.controller.MongoDataController;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.stat.constant.OperationType;
import cn.youai.xiuzhen.stat.entity.MgBackpackLog;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("player/backpackLog")
public class MgBackpackLogController extends MongoDataController<MgBackpackLog> {

    /**
     * 分页列表查询
     */
    @AutoLog(value = "道具日志-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MgBackpackLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected boolean hasPermission(GamePlayer player, LoginUser sysUser) {
        if (StringUtils.isNotEmpty(sysUser.getChannel())) {
            Set<String> channels = StringUtils.split2Set(sysUser.getChannel());
            if (!channels.contains(player.getChannel())) {
                return false;
            }
        }

        if (StringUtils.isNotEmpty(sysUser.getSdkChannel())) {
            Set<String> sdkChannels = StringUtils.split2Set(sysUser.getSdkChannel());
            if (!sdkChannels.contains(player.getSdkChannel())) {
                return false;
            }
        }
        return super.hasPermission(player, sysUser);
    }

    @Override
    protected Criteria queryCriteria(MgBackpackLog entity, HttpServletRequest req) {
        DateRange dageRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime");
        PageQueryUtils.addTime(dageRange);

        Criteria criteria = Criteria.where("playerId").is(entity.getPlayerId());
        if (entity.getItemId() != null && entity.getItemId() > 0) {
            criteria.and("itemId").is(entity.getItemId());
        }
        if (entity.getType() != null && entity.getType() > 0) {
            criteria.and("type").is(entity.getType());
        }
        if (entity.getWay() != null && entity.getWay() > 0) {
            criteria.and("way").is(entity.getWay());
        }

        if (dageRange.getStart() != null && dageRange.getEnd() != null) {
            criteria.and("createTime").gte(dageRange.getStart()).lte(dageRange.getEnd());
        } else if (dageRange.getStart() != null) {
            criteria.and("createTime").gte(dageRange.getStart());
        } else if (dageRange.getEnd() != null) {
            criteria.and("createTime").lte(dageRange.getEnd());
        }
        return criteria;
    }

    @Override
    @AutoLog(value = "道具日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(long playerId, String id) {
        return super.queryById(playerId, id);
    }

    @AutoLog(value = "道具日志-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MgBackpackLog entity) {
        return super.exportXls(request, entity, MgBackpackLog.class, "道具日志");
    }

    @SuppressWarnings("DuplicatedCode")
    protected void onload(MgBackpackLog entity) {
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
}
