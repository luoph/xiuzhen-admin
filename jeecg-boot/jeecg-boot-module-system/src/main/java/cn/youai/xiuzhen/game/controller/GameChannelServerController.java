package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.utils.SqlHelper;
import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
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
@RequestMapping("/game/channelServer")
public class GameChannelServerController extends JeecgController<GameChannelServer, IGameChannelServerService> {

    @Autowired
    private IGameServerService gameServerService;

    @AutoLog(value = "游戏渠道服配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameChannelServer entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameChannelServer> pageList = super.pageList(entity, pageNo, pageSize, req);
        onload(pageList.getRecords());
        return Result.ok(pageList);
    }

    @Override
    protected void onload(List<GameChannelServer> pageList) {
        if (CollUtil.isEmpty(pageList)) {
            return;
        }
        Set<Integer> results = pageList.stream().map(GameChannelServer::getServerId).collect(Collectors.toSet());
        Collection<GameServer> servers = gameServerService.listByIds(results);
        Map<Integer, GameServer> serverMap = servers.stream().collect(Collectors.toMap(GameServer::getId, Function.identity()));
        for (GameChannelServer record : pageList) {
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

    private int getPrePosition(String channelId) {
        GameChannelServer entity = service.getOne(Wrappers.<GameChannelServer>lambdaQuery().
                eq(GameChannelServer::getChannelId, channelId)
                .orderByDesc(GameChannelServer::getPosition)
                .last(SqlHelper.limit()));
        return null != entity ? entity.getPosition() : 0;
    }

    @AutoLog(value = "游戏渠道服配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameChannelServer entity) {
        String serverIds = entity.getServerIds();
        if (StringUtils.isBlank(serverIds)) {
            return Result.error("请输入区服");
        }

        Set<Integer> serverIdSet = new HashSet<>();
        List<String> strList = StringUtils.split2List(serverIds);
        for (String str : strList) {
            if (str.contains(StringUtils.SEPARATOR_HYPHEN)) {
                List<Integer> serverIdDateRange = StringUtils.split2Int(str, StringUtils.SEPARATOR_HYPHEN);
                if (serverIdDateRange.size() != 2) {
                    continue;
                }
                int startServerId = serverIdDateRange.get(0);
                int endServerId = serverIdDateRange.get(1);
                serverIdSet.add(startServerId);
                for (int i = startServerId + 1; i <= endServerId; ++i) {
                    serverIdSet.add(i);
                }
            } else {
                try {
                    serverIdSet.add(Integer.parseInt(str));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<Integer> serverIdList = new ArrayList<>(serverIdSet).stream().filter(e -> e > 0).sorted(Comparator.comparing(e -> e)).collect(Collectors.toList());
        if (serverIdList.isEmpty()) {
            return Result.error("请输入区服");
        }

        List<GameChannelServer> entities = new ArrayList<>(serverIdList.size());
        int position = getPrePosition(entity.getChannelId());
        for (int serverId : serverIdList) {
            entities.add(new GameChannelServer().setServerId(serverId).setChannelId(entity.getChannelId()).setPosition(++position).setDelFlag(entity.getDelFlag()).setNoNeedCount(entity.getNoNeedCount()));
        }

        if (!service.saveBatch(entities)) {
            return Result.error("保存失败");
        }

        service.autoAddCampaignServerIds(serverIdList);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "游戏渠道服配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameChannelServer entity) {
        GameChannelServer dbEntity = getById(entity.getId());
        boolean changeServerId = null != dbEntity && !Objects.equals(dbEntity.getServerId(), entity.getServerId());
        Result<?> result = super.edit(entity);
        if (result.isSuccess() && changeServerId) {
            service.autoAddCampaignServerIds(CollUtil.newArrayList(entity.getServerId()));
        }
        return result;
    }

    @AutoLog(value = "游戏渠道服配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏渠道服配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏渠道服配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏渠道服配置-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameChannelServer entity) {
        return super.exportXls(request, entity, GameChannelServer.class, "游戏渠道服配置");
    }

    @AutoLog(value = "游戏渠道服配置-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameChannelServer.class);
    }

    /**
     * 通过渠道id 查询关联的服务器
     */
    @RequestMapping(value = "channelWithServer")
    public Result<?> channelWithServer(@RequestParam(name = "channelId") Integer channelId) {
        List<GameServerVO> gameServers = service.selectServerListByChannelId(channelId);
        return Result.ok(gameServers);
    }

    @RequestMapping(value = "serverList")
    public Result<?> serverList(@RequestParam(name = "channel", required = false) String channel) {
        List<GameServerVO> gameServers = service.selectServerList(channel);
        return Result.ok(gameServers);
    }

    @RequestMapping(value = "channelList")
    public Result<?> channelList() {
        return Result.ok(service.selectChannelList());
    }

}
