package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.commons.model.Response;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameForbidden;
import org.jeecg.modules.game.service.IGameForbiddenService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

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

    @Resource
    private IGameForbiddenService gameForbiddenService;

    @Value("${app.forbidden-update-url:/forbidden/update}")
    private String forbiddenUpdateUrl;

    /**
     * 分页列表查询
     *
     * @param gameForbidden 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameForbidden gameForbidden,
                                   @RequestParam(name = "serverId", defaultValue = "") Integer serverId,
                                   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "type", defaultValue = "") String type,
                                   @RequestParam(name = "banValue", defaultValue = "") String banValue,
                                   @RequestParam(name = "isForever", defaultValue = "") String isForever,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Date rangeDateBeginDate = null;
        Date rangeDateEndDate = null;
        if (!StringUtils.isEmpty(rangeDateBegin) || !StringUtils.isEmpty(rangeDateEnd)) {
            rangeDateBeginDate = DateUtils.startTimeOfDate(DateUtils.parseDate(rangeDateBegin));
            rangeDateEndDate = DateUtils.startTimeOfDate(DateUtils.parseDate(rangeDateEnd));
        }
        gameForbidden.setDelFlag(0);
        gameForbidden.setServerId(serverId);
        if (!StringUtils.isEmpty(type)) {
            gameForbidden.setType(Integer.parseInt(type));
        }
        gameForbidden.setBanValue(banValue);
        if (!StringUtils.isEmpty(isForever)) {
            gameForbidden.setIsForever(Integer.parseInt(isForever));
        }
        QueryWrapper<GameForbidden> queryWrapper = QueryGenerator.initQueryWrapper(gameForbidden, req.getParameterMap());
        Page<GameForbidden> page = new Page<>(pageNo, pageSize);
        if (null != rangeDateBeginDate) {
            queryWrapper.ge("create_time", rangeDateBeginDate);
        }
        if (null != rangeDateBeginDate) {
            queryWrapper.le("create_time", rangeDateEndDate);
        }
        IPage<GameForbidden> pageList = gameForbiddenService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameForbidden 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameForbidden gameForbidden) {
        gameForbiddenService.save(gameForbidden);
        notifyForbiddenUpdate(gameForbidden);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameForbidden 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_forbidden-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameForbidden gameForbidden) {
        gameForbiddenService.updateById(gameForbidden);
        notifyForbiddenUpdate(gameForbidden);
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
        GameForbidden entity = gameForbiddenService.getById(id);
        gameForbiddenService.removeById(id);
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
        this.gameForbiddenService.removeByIds(Arrays.asList(ids.split(",")));
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
        GameForbidden gameForbidden = gameForbiddenService.getById(id);
        if (gameForbidden == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameForbidden);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param gameForbidden 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameForbidden gameForbidden) {
        return super.exportXls(request, gameForbidden, GameForbidden.class, "game_forbidden");
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

    private void notifyForbiddenUpdate(GameForbidden gameForbidden) {
        if (gameForbidden != null && gameForbidden.getServerId() != null) {
            Map<Integer, Response> responseMap = gameServerService.gameServerGet(CollUtil.newArrayList(gameForbidden.getServerId()), forbiddenUpdateUrl);
            log.info("notifyForbiddenUpdate response:{}", responseMap);
        }
    }

}
