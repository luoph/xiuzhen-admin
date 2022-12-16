package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.BanKey;
import cn.youai.xiuzhen.game.constant.BanType;
import cn.youai.xiuzhen.game.entity.GameForbidden;
import cn.youai.xiuzhen.game.entity.GameForbiddenRecord;
import cn.youai.xiuzhen.game.service.IGameForbiddenRecordService;
import cn.youai.xiuzhen.game.service.IGameForbiddenService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_forbidden
 * @date 2020-12-17
 */
@Slf4j
@RestController
@RequestMapping("game/forbidden")
public class GameForbiddenController extends JeecgController<GameForbidden, IGameForbiddenService> {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameForbiddenRecordService forbiddenRecordService;

    @Value("${app.forbidden-update-url:/forbidden/update}")
    private String forbiddenUpdateUrl;

    @Value("${app.kick-off-url:/player/kickOff}")
    private String kickOffUrl;

    @AutoLog(value = "账号封禁-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameForbidden entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "账号封禁-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameForbidden entity) {
        if (entity.getServerId() == null || entity.getType() == null
                || StringUtils.isEmpty(entity.getBanKey())
                || StringUtils.isEmpty(entity.getBanValue())) {
            return Result.error("添加失败！");
        }

        // 封禁时长
        if (entity.getDuration() != null && entity.getDuration() > 0) {
            Date now = DateUtils.now();
            entity.setIsForever(0);
            entity.setStartTime(now);
            entity.setEndTime(DateUtils.addSeconds(now, entity.getDuration()));
        }

        GameForbidden existOne = selectExistOne(entity);
        if (existOne == null) {
            service.save(entity);
            addForbiddenRecord("add", entity);
        } else {
            existOne.copy(entity);
            service.updateById(existOne);
            addForbiddenRecord("update", existOne);
        }

        notifyForbiddenUpdate(entity);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "账号封禁-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameForbidden entity) {
        addForbiddenRecord("update", entity);
        notifyForbiddenUpdate(entity);
        return super.edit(entity);
    }

    @AutoLog(value = "账号封禁-撤销")
    @GetMapping(value = "/undo")
    public Result<?> undo(@RequestParam(name = "type") Integer type,
                          @RequestParam(name = "serverId") Integer serverId,
                          @RequestParam(name = "playerId") Long playerId) {
        GameForbidden entity = selectExistOne(type, serverId, BanKey.PLAYER_ID.getName(), String.valueOf(playerId));
        boolean success = delete(entity);
        if (success) {
            String message = "撤销成功!";
            if (type == BanType.CHAT.getValue()) {
                message = "撤销禁言成功!";
            } else if (type == BanType.LOGIN.getValue()) {
                message = "撤销封号成功!";
            }
            return Result.ok(message);
        }
        return Result.error("未找到封禁记录!");
    }

    @AutoLog(value = "账号封禁-踢下线")
    @GetMapping(value = "/kickOff")
    public Result<?> kickOff(@RequestParam(name = "playerId") Long playerId, @RequestParam(name = "serverId") Integer serverId) {
        Map<String, Response> response = gameServerService.gameServerGet(CollUtil.newArrayList(String.valueOf(serverId)), kickOffUrl, ImmutableMap.of("playerId", playerId));
        log.info("kickOff response:{}", response);
        return Result.ok("成功！");
    }

    @AutoLog(value = "账号封禁-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        GameForbidden entity = service.getById(id);
        boolean success = delete(entity);
        return success ? Result.ok("删除成功!") : Result.error("未找到记录!");
    }

    @AutoLog(value = "账号封禁-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        Wrapper<GameForbidden> query = Wrappers.<GameForbidden>lambdaQuery().in(GameForbidden::getId, idList);
        List<GameForbidden> list = service.list(query);
        this.service.removeByIds(idList);
        addForbiddenRecordList("delete", list);
        notifyForbiddenUpdate(list);
        return Result.ok("批量删除成功！");
    }

    @AutoLog(value = "账号封禁-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "账号封禁-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, @RequestParam("obj") GameForbidden entity) {
        return super.exportXls(request, entity, GameForbidden.class, "账号封禁");
    }

    @AutoLog(value = "账号封禁-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameForbidden.class);
    }

    private void notifyForbiddenUpdate(GameForbidden entity) {
        notifyForbiddenUpdate(CollUtil.newArrayList(entity));
    }

    private void notifyForbiddenUpdate(List<GameForbidden> list) {
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> serverIds = new HashSet<>();
            for (GameForbidden entity : list) {
                serverIds.add(entity.getServerId());
            }

            for (Integer serverId : serverIds) {
                Map<Integer, Response> responseMap = gameServerService.gameServerGet(CollUtil.newArrayList(serverId), forbiddenUpdateUrl);
                log.info("notifyForbiddenUpdate response:{}", responseMap);
            }
        }
    }

    private GameForbidden selectExistOne(GameForbidden entity) {
        return selectExistOne(entity.getType(), entity.getServerId(), entity.getBanKey(), entity.getBanValue());
    }

    private GameForbidden selectExistOne(Integer type, Integer serverId, String key, String value) {
        Wrapper<GameForbidden> query = Wrappers.<GameForbidden>lambdaQuery()
                .eq(GameForbidden::getType, type)
                .eq(GameForbidden::getServerId, serverId)
                .eq(GameForbidden::getBanKey, key)
                .eq(GameForbidden::getBanValue, value);
        return service.getOne(query);
    }

    private void addForbiddenRecord(String operation, GameForbidden entity) {
        GameForbiddenRecord record = new GameForbiddenRecord().setOperation(operation);
        BeanUtils.copyProperties(entity, record);
        if (entity.getId() != null) {
            record.setForbiddenId(entity.getId());
        }
        record.setId(null);
        forbiddenRecordService.save(record);
    }

    private void addForbiddenRecordList(String operation, List<GameForbidden> list) {
        List<GameForbiddenRecord> addList = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (GameForbidden entity : list) {
                GameForbiddenRecord record = new GameForbiddenRecord().setOperation(operation);
                BeanUtils.copyProperties(entity, record);
                if (entity.getId() != null) {
                    record.setForbiddenId(entity.getId());
                }
                record.setId(null);
                addList.add(record);
            }
        }
        forbiddenRecordService.saveBatch(addList);
    }

    private boolean delete(GameForbidden entity) {
        if (entity != null) {
            service.removeById(entity);
            addForbiddenRecord("delete", entity);
            notifyForbiddenUpdate(entity);
            return true;
        }
        return false;
    }

}
