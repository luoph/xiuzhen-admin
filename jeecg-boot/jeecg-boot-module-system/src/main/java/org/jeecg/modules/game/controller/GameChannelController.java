package org.jeecg.modules.game.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏渠道")
@RequestMapping("/game/gameChannel")
public class GameChannelController extends JeecgController<GameChannel, IGameChannelService> {

    @Value("${app.server.folder}")
    private String serverFolder;

    @Autowired
    private IGameChannelService gameChannelService;


    /**
     * 分页列表查询
     *
     * @param gameChannel 数据实体
     * @param pageNo      页码
     * @param pageSize    分页大小
     * @param req         请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-列表查询")
    @ApiOperation(value = "游戏渠道-列表查询", notes = "游戏渠道-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannel gameChannel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameChannel> queryWrapper = QueryGenerator.initQueryWrapper(gameChannel, req.getParameterMap());
        Page<GameChannel> page = new Page<>(pageNo, pageSize);
        IPage<GameChannel> pageList = gameChannelService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameChannel 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-添加")
    @ApiOperation(value = "游戏渠道-添加", notes = "游戏渠道-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannel gameChannel) {
        gameChannelService.save(gameChannel);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameChannel 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-编辑")
    @ApiOperation(value = "游戏渠道-编辑", notes = "游戏渠道-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannel gameChannel) {
        gameChannelService.updateById(gameChannel);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-通过id删除")
    @ApiOperation(value = "游戏渠道-通过id删除", notes = "游戏渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameChannelService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-批量删除")
    @ApiOperation(value = "游戏渠道-批量删除", notes = "游戏渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameChannelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道-通过id查询")
    @ApiOperation(value = "游戏渠道-通过id查询", notes = "游戏渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameChannel gameChannel = gameChannelService.getById(id);
        return Result.ok(gameChannel);
    }

    /**
     * 导出excel
     *
     * @param request     请求
     * @param gameChannel 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannel gameChannel) {
        return super.exportXls(request, gameChannel, GameChannel.class, "游戏渠道");
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
        return super.importExcel(request, response, GameChannel.class);
    }


    @GetMapping(value = "/writeServerFile")
    public Result<?> writeServerFile(HttpServletRequest req) {
        try {
            List<GameChannel> channelList = gameChannelService.list();
            for (GameChannel channel : channelList) {
                List<GameServer> servers = gameChannelService.getServerListChannelId(channel.getId());
                String content = JSON.toJSONString(servers);
                String fileName = channel.getSimpleName();

                String filePath = serverFolder + fileName + ".json";
                if (FileUtil.exist(filePath)) {
                    FileUtil.del(filePath);
                }

                FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("writeServerFile error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
