package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.GameStatLtv;
import org.jeecg.modules.game.service.IGameStatLtvService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description ltv统计
 * @date 2019-12-14
 */
@Slf4j
@RestController
@RequestMapping("/gameStat/ltv")
public class GameStatLtvController extends JeecgController<GameStatLtv, IGameStatLtvService> {

    @Override
    protected QueryWrapper<GameStatLtv> prepareQuery(GameStatLtv entity, HttpServletRequest request) {
        QueryWrapper<GameStatLtv> queryWrapper = super.prepareQuery(entity, request);
        queryWrapper.orderByDesc("count_date");
        return queryWrapper;
    }

    @Override
    @AutoLog(value = "ltv查询")
    @ApiOperation(value = "ltv查询", notes = "ltv查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatLtv entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏下发邮件-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameStatLtv entity = service.getById(Long.parseLong(id));
        if (entity == null) {
            return Result.error("未找到ltv");
        }
        return Result.ok(entity);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatLtv entity) {
        return super.exportXls(request, entity, GameStatLtv.class, "ltv");
    }

}
