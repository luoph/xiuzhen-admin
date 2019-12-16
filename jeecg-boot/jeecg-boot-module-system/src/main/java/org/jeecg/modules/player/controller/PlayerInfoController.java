package org.jeecg.modules.player.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.PlayerInfo;
import org.jeecg.modules.player.service.IPlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
@Slf4j
@RestController
@RequestMapping("/player/playerInfo")
public class PlayerInfoController extends JeecgController<PlayerInfo, IPlayerInfoService> {

    @Autowired
    private IPlayerInfoService playerInfoService;

    /**
     * 分页列表查询
     *
     * @param playerInfo 数据实体
     * @param pageNo     页码
     * @param pageSize   分页大小
     * @param req        请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PlayerInfo playerInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   HttpServletRequest req) {

        QueryWrapper<PlayerInfo> queryWrapper = QueryGenerator.initQueryWrapper(playerInfo, req.getParameterMap());
        Page<PlayerInfo> page = new Page<>(pageNo, pageSize);
        DataSourceHelper.useServerDatabase(serverId);
        IPage<PlayerInfo> pageList = playerInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param playerInfo 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PlayerInfo playerInfo) {
        playerInfoService.save(playerInfo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param playerInfo 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PlayerInfo playerInfo) {
        playerInfoService.updateById(playerInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        playerInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.playerInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        PlayerInfo playerInfo = playerInfoService.getById(id);
        if (playerInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(playerInfo);
    }

    /**
     * 导出excel
     *
     * @param request    请求
     * @param playerInfo 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PlayerInfo playerInfo) {
        return super.exportXls(request, playerInfo, PlayerInfo.class, "玩家信息");
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
        return super.importExcel(request, response, PlayerInfo.class);
    }

}
