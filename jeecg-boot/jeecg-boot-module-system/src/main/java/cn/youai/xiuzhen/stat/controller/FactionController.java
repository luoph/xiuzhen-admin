package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.core.controller.ServerDataSourceController;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.entity.Faction;
import cn.youai.xiuzhen.stat.mapper.FactionMapper;
import cn.youai.xiuzhen.stat.service.IFactionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 仙宗
 *
 * @author ocean
 * @since 202306021116
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("player/faction")
public class FactionController extends ServerDataSourceController<Faction, IFactionService> {

    @Resource
    private FactionMapper factionMapper;

    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.faction-forceRename-url:/faction/forceRename}")
    private String factionForceRenameUrl;

    @Value("${app.faction-forceDissolve-url:/faction/forceDissolve}")
    private String factionForceDissolveUrl;

    @AutoLog(value = "仙宗-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Faction entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<Faction> pageList(Page<Faction> page, Faction entity, HttpServletRequest req) {
        return factionMapper.queryList(page, entity);
    }

    @AutoLog(value = "仙宗-修改仙宗名")
    @PutMapping(value = "/rename")
    public Result<?> rename(@RequestBody Faction entity) {
        if (null == entity || null == entity.getFactionId()) {
            return Result.error("请选择仙宗");
        }
        if (StringUtils.isBlank(entity.getName())) {
            return Result.error("仙宗名不能为空");
        }
        if (null == entity.getServerId()) {
            return Result.error("请选择仙宗区服id");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("factionId", entity.getFactionId());
        params.put("name", entity.getName());
        Integer serverId = entity.getServerId();
        Map<Integer, Response> responseMap = gameServerService.getUrl(CollUtil.newArrayList(serverId), factionForceRenameUrl, params);
        Response response = responseMap.get(serverId);
        if (null == response) {
            return Result.error("请求失败");
        }
        return response.isSuccess() ? Result.ok("修改成功") : Result.error(response.getDesc());
    }

    @AutoLog(value = "仙宗-删除仙宗")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(Faction entity) {
        if (null == entity || null == entity.getFactionId()) {
            return Result.error("请选择仙宗");
        }
        if (null == entity.getServerId()) {
            return Result.error("请选择仙宗区服id");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("factionId", entity.getFactionId());
        Integer serverId = entity.getServerId();
        Map<Integer, Response> responseMap = gameServerService.getUrl(CollUtil.newArrayList(serverId), factionForceDissolveUrl, params);
        Response response = responseMap.get(serverId);
        if (null == response) {
            return Result.error("请求失败");
        }
        return response.isSuccess() ? Result.ok("删除成功") : Result.error(response.getDesc());
    }

    @Deprecated
    @AutoLog(value = "仙宗-批量删除仙宗")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return Result.ok();
    }

    @AutoLog(value = "仙宗信息-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Faction entity) {
//        return super.exportXls(request, entity, Faction.class, "仙宗信息");

        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return null;
        }

        try {
            Page<Faction> page = new Page<>(1, Integer.MAX_VALUE);
            useServerDatabase(serverId);
            IPage<Faction> pageList = factionMapper.queryList(page, entity);
            onload(pageList.getRecords());
            return ExcelUtils.exportXls(pageList, request.getParameter("selections"), Faction.class, "仙宗信息");
        } catch (Exception e) {
            log.error("仙宗信息-导出", e);
            return null;
        } finally {
            useDefaultDatabase();
        }
    }
}
