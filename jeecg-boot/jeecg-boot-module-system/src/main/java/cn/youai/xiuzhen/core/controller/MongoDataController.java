package cn.youai.xiuzhen.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.core.base.IPlayerData;
import cn.youai.xiuzhen.core.database.MongoDataSourceHelper;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * mongodb 多数据源查询基类
 *
 * @author luopeihuan
 * @version 1.0
 * @since 2023-05-22
 */
@Readonly
public class MongoDataController<T extends IPlayerData> {

    @Autowired
    protected IGamePlayerService playerService;

    protected Class<T> entityClass = currentModelClass();

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 数据权限检查
     */
    protected boolean hasPermission(GamePlayer player, LoginUser sysUser) {
        return true;
    }

    /**
     * 分页列表查询
     */
    public Result<?> queryPageList(T entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (null == entity.getPlayerId()) {
            return Result.error("请输入玩家id！");
        }

        GamePlayer player = playerService.queryPlayer(entity.getPlayerId());
        if (player == null) {
            return Result.error("找不到玩家！");
        }

        // 获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean hasPermission = hasPermission(player, sysUser);
        if (!hasPermission) {
            return Result.error("无查询权限！");
        }

        MongoTemplate mongoTemplate = MongoDataSourceHelper.getInstance().getMongoTemplate(player.getServerId());
        if (mongoTemplate == null) {
            return Result.error("找不到数据源！");
        }

        // 查询总数
        Criteria criteria = queryCriteria(entity, req);
        Query countQuery = Query.query(criteria);
        long count = mongoTemplate.count(countQuery, getEntityClass());

        // PageRequest 是从0开始计数
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, getSort());
        List<T> pageList = doQuery(mongoTemplate, criteria, pageRequest);
        return Result.ok(PageQueryUtils.makePage(pageList, pageNo, pageSize, count));
    }

    /**
     * 通过id查询
     */
    public Result<?> queryById(long playerId, String id) {
        GamePlayer player = playerService.queryPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家！");
        }

        MongoTemplate mongoTemplate = MongoDataSourceHelper.getInstance().getMongoTemplate(player.getServerId());
        if (mongoTemplate == null) {
            return Result.error("找不到数据源！");
        }

        Criteria criteria = Criteria.where("id").is(id);
        List<T> list = mongoTemplate.find(Query.query(criteria), getEntityClass());
        T entity = CollUtil.isNotEmpty(list) ? list.get(0) : null;
        if (entity == null) {
            return Result.error("未找到数据");
        }
        return Result.ok(entity);
    }

    protected ModelAndView exportXls(HttpServletRequest request, T entity, Class<T> clazz, String title) {
        if (null == entity.getPlayerId()) {
            return null;
        }

        GamePlayer player = playerService.queryPlayer(entity.getPlayerId());
        if (player == null) {
            return null;
        }

        // 获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean hasPermission = hasPermission(player, sysUser);
        if (!hasPermission) {
            return null;
        }

        MongoTemplate mongoTemplate = MongoDataSourceHelper.getInstance().getMongoTemplate(player.getServerId());
        if (mongoTemplate == null) {
            return null;
        }

        PageRequest page = PageRequest.of(0, Integer.MAX_VALUE, getSort());
        List<T> pageList = doQuery(entity, mongoTemplate, page, request);
        return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), clazz, title);
    }

    protected void onload(List<T> pageList) {
        if (pageList == null) {
            return;
        }
        pageList.forEach(this::onload);
    }

    protected void onload(T entity) {
    }

    protected Criteria queryCriteria(T entity, HttpServletRequest req) {
        return new Criteria();
    }

    protected Sort getSort() {
        return Sort.by("createTime").descending();
    }

    private List<T> doQuery(T entity, MongoTemplate template, PageRequest page, HttpServletRequest req) {
        return doQuery(template, queryCriteria(entity, req), page);
    }

    private List<T> doQuery(MongoTemplate template, Criteria criteria, PageRequest page) {
        Query query = Query.query(criteria).with(page);
        List<T> pageList = template.find(query, getEntityClass());
        pageList.forEach(this::onload);
        onload(pageList);
        return pageList;
    }
}
