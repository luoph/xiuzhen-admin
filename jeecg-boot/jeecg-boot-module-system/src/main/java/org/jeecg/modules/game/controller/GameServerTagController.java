package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameServerTag;
import org.jeecg.modules.game.service.IGameServerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服标签
 * @date 2022-03-25
 */
@Slf4j
@RestController
@RequestMapping("game/gameServerTag")
public class GameServerTagController extends JeecgController<GameServerTag, IGameServerTagService> {

    @Autowired
    private IGameServerTagService gameServerTagService;

    /**
     * 分页列表查询
     *
     * @param gameServerTag 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameServerTag gameServerTag,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameServerTag> queryWrapper = QueryGenerator.initQueryWrapper(gameServerTag, req.getParameterMap());
        Page<GameServerTag> page = new Page<>(pageNo, pageSize);
        IPage<GameServerTag> pageList = gameServerTagService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameServerTag 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameServerTag gameServerTag) {
        gameServerTagService.save(gameServerTag);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameServerTag 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameServerTag gameServerTag) {
        gameServerTagService.updateById(gameServerTag);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameServerTagService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameServerTagService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏服标签-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameServerTag gameServerTag = gameServerTagService.getById(id);
        if (gameServerTag == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameServerTag);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param gameServerTag 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameServerTag gameServerTag) {
        return super.exportXls(request, gameServerTag, GameServerTag.class, "游戏服标签");
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
        return super.importExcel(request, response, GameServerTag.class);
    }

}
