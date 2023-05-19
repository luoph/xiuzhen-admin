package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.database.MongoDataSourceHelper;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.constant.OperationType;
import cn.youai.xiuzhen.stat.entity.MgBackpackLog;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
public class MgBackpackLogController {

    @Autowired
    private IGamePlayerService gamePlayerService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "道具日志-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MgBackpackLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (null == entity.getPlayerId()) {
            return Result.error("请输入玩家id！");
        }

        GamePlayer player = gamePlayerService.queryPlayer(entity.getPlayerId());
        if (player == null) {
            return Result.error("找不到玩家！");
        }

        DateRange dageRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime");
        PageQueryUtils.addTime(dageRange);
        MongoTemplate mongoTemplate = MongoDataSourceHelper.getInstance().getMongoTemplate(player.getServerId());
        if (mongoTemplate == null) {
            return Result.error("找不到数据源！");
        }

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

        // 查询总数
        Query countQuery = Query.query(criteria);
        long count = mongoTemplate.count(countQuery, MgBackpackLog.class);
        // PageRequest 是从0开始计数
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, Sort.by("createTime").descending());
        Query query = Query.query(criteria).with(pageRequest);
        List<MgBackpackLog> list = mongoTemplate.find(query, MgBackpackLog.class);
        list.forEach(this::onload);
        return Result.ok(PageQueryUtils.makePage(list, pageNo, pageSize, count));
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
