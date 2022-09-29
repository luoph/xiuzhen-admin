package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameForbidden;
import cn.youai.xiuzhen.game.entity.GameForbiddenRecord;
import cn.youai.xiuzhen.game.service.IGameForbiddenRecordService;
import cn.youai.xiuzhen.game.service.IGameForbiddenService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("game/gameForbidden")
public class GameForbiddenController extends JeecgController<GameForbidden, IGameForbiddenService> {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameForbiddenRecordService forbiddenRecordService;

    @Value("${app.forbidden-update-url:/forbidden/update}")
    private String forbiddenUpdateUrl;

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
                || entity.getBanKey() == null || entity.getBanValue() == null) {
            return Result.error("添加失败！");
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

    @AutoLog(value = "账号封禁-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        GameForbidden entity = service.getById(id);
        service.removeById(id);
        addForbiddenRecord("delete", entity);
        notifyForbiddenUpdate(entity);
        return Result.ok("删除成功!");
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

    @AutoLog(value = "账号封禁-快捷封禁")
    @PutMapping(value = "/onBanned")
    public Result<?> onBanned(HttpServletRequest request, @RequestBody GameForbidden gameForbidden) {
        if (gameForbidden.getServerId() == null || gameForbidden.getType() == null
                || gameForbidden.getBanKey() == null || gameForbidden.getBanValue() == null) {
            return Result.error("封禁失败！");
        }

        GameForbidden existOne = selectExistOne(gameForbidden);
        if (existOne != null) {
            return Result.error("已封禁！");
        }
        return add(gameForbidden);
    }

    private void notifyForbiddenUpdate(GameForbidden gameForbidden) {
        notifyForbiddenUpdate(CollUtil.newArrayList(gameForbidden));
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
        Wrapper<GameForbidden> query = Wrappers.<GameForbidden>lambdaQuery()
                .eq(GameForbidden::getServerId, entity.getServerId())
                .eq(GameForbidden::getType, entity.getType())
                .eq(GameForbidden::getBanKey, entity.getBanKey())
                .eq(GameForbidden::getBanValue, entity.getBanValue());
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


}
