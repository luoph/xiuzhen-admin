package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameServerVO;
import org.jeecg.modules.game.service.IGameChannelServerService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏渠道服配置")
@RequestMapping("/game/gameChannelServer")
public class GameChannelServerController extends JeecgController<GameChannelServer, IGameChannelServerService> {

    @Autowired
    private IGameServerService gameServerService;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-列表查询")
    @ApiOperation(value = "游戏渠道服配置-列表查询", notes = "游戏渠道服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannelServer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameChannelServer> pageList = super.pageList(entity, pageNo, pageSize, req);

        // 完善数据
        if (CollUtil.isNotEmpty(pageList.getRecords())) {
            Set<Integer> results = pageList.getRecords().stream().map(GameChannelServer::getServerId).collect(Collectors.toSet());
            Collection<GameServer> servers = gameServerService.listByIds(results);
            Map<Integer, GameServer> serverMap = servers.stream().collect(Collectors.toMap(GameServer::getId, Function.identity()));

            for (GameChannelServer record : pageList.getRecords()) {
                GameServer gameServer = serverMap.get(record.getServerId());
                if (gameServer != null) {
                    record.setServerName(gameServer.getName())
                            .setOpenTime(gameServer.getOpenTime())
                            .setOnlineTime(gameServer.getOnlineTime())
                            .setServerStatus(gameServer.getStatus())
                            .setIsMaintain(gameServer.getIsMaintain());
                }
            }
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-添加")
    @ApiOperation(value = "游戏渠道服配置-添加", notes = "游戏渠道服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannelServer entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-编辑")
    @ApiOperation(value = "游戏渠道服配置-编辑", notes = "游戏渠道服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannelServer entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-通过id删除")
    @ApiOperation(value = "游戏渠道服配置-通过id删除", notes = "游戏渠道服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-批量删除")
    @ApiOperation(value = "游戏渠道服配置-批量删除", notes = "游戏渠道服配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏渠道服配置-通过id查询")
    @ApiOperation(value = "游戏渠道服配置-通过id查询", notes = "游戏渠道服配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannelServer entity) {
        return super.exportXls(request, entity, GameChannelServer.class, "游戏渠道服配置");
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
        return super.importExcel(request, response, GameChannelServer.class);
    }

    /**
     * 通过渠道id 查询关联的服务器
     *
     * @param channelId 渠道id
     * @return
     */
    @RequestMapping(value = "channelWithServer")
    public Result<?> channelWithServer(@RequestParam(name = "channelId") Integer channelId) {
        List<GameServerVO> gameServers = service.selectServerListByChannelId(channelId);
        return Result.ok(gameServers);
    }
}
