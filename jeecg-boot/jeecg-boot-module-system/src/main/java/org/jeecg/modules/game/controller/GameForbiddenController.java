package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameForbidden;
import org.jeecg.modules.game.entity.GameForbiddenRecord;
import org.jeecg.modules.game.service.IGameForbiddenRecordService;
import org.jeecg.modules.game.service.IGameForbiddenService;
import org.jeecg.modules.game.service.IGameServerService;
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
    private IGameForbiddenService forbiddenService;

    @Autowired
    private IGameForbiddenRecordService forbiddenRecordService;

    @Value("${app.forbidden-update-url:/forbidden/update}")
    private String forbiddenUpdateUrl;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameForbidden entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameForbidden> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<GameForbidden> page = new Page<>(pageNo, pageSize);
        IPage<GameForbidden> pageList = forbiddenService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameForbidden entity) {
        if (entity.getServerId() == null || entity.getType() == null
                || entity.getBanKey() == null || entity.getBanValue() == null) {
            return Result.error("添加失败！");
        }

        GameForbidden existOne = selectExistOne(entity);
        if (existOne == null) {
            forbiddenService.save(entity);
            addForbiddenRecord("add", entity);
        } else {
            existOne.copy(entity);
            forbiddenService.updateById(existOne);
            addForbiddenRecord("update", existOne);
        }

        notifyForbiddenUpdate(entity);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameForbidden entity) {
        forbiddenService.updateById(entity);
        addForbiddenRecord("update", entity);
        notifyForbiddenUpdate(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        GameForbidden entity = forbiddenService.getById(id);
        forbiddenService.removeById(id);
        addForbiddenRecord("delete", entity);
        notifyForbiddenUpdate(entity);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        Wrapper<GameForbidden> query = Wrappers.<GameForbidden>lambdaQuery().in(GameForbidden::getId, idList);
        List<GameForbidden> list = forbiddenService.list(query);
        this.forbiddenService.removeByIds(idList);
        addForbiddenRecordList("delete", list);
        notifyForbiddenUpdate(list);
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameForbidden gameForbidden = forbiddenService.getById(id);
        if (gameForbidden == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameForbidden);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, @RequestParam("obj") GameForbidden entity) {
        return super.exportXls(request, entity, GameForbidden.class, "game_forbidden");
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
        return super.importExcel(request, response, GameForbidden.class);
    }

    /**
     * 快捷封禁
     */
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
        return forbiddenService.getOne(query);
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
