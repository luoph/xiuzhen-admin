package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameVps;
import cn.youai.xiuzhen.game.monitor.DiskUsageInfo;
import cn.youai.xiuzhen.game.monitor.ServerMonitor;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameVpsService;
import cn.youai.xiuzhen.game.service.IServerMonitorService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.$;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
@Slf4j
@RestController
@Api(tags = "虚拟主机")
@RequestMapping("/game/vps")
public class GameVpsController extends JeecgController<GameVps, IGameVpsService> {

    @Autowired
    private IServerMonitorService monitorService;

    @Autowired
    private IGameServerService serverService;

    @Value("${app.wgcloud.url}")
    private String wgcloudUrl;

    @Value("${app.wgcloud.token}")
    private String wgToken;

    @AutoLog(value = "虚拟主机-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameVps entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameVps> page = new Page<>(pageNo, pageSize);
        DateRange createTimeRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime");
        IPage<GameVps> pageList = service.queryList(page, entity, createTimeRange);
        this.onload(pageList.getRecords());
        return Result.ok(pageList);
    }

    @AutoLog(value = "虚拟主机-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameVps entity) {
        return super.add(entity);
    }

    @AutoLog(value = "虚拟主机-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameVps entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "虚拟主机-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "虚拟主机-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "虚拟主机-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "虚拟主机-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameVps gameInfo) {
        return super.exportXls(request, gameInfo, GameVps.class, "虚拟主机");
    }

    @AutoLog(value = "虚拟主机-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameVps.class);
    }

    @Override
    protected void onload(List<GameVps> pageList) {
        super.onload(pageList);
        if (CollUtil.isEmpty(pageList)) {
            return;
        }

        List<ServerMonitor> serverMonitors = monitorService.queryList();
        Map<String, ServerMonitor> map = serverMonitors.stream().collect(Collectors.toMap(ServerMonitor::getHostname, Function.identity(), (key1, key2) -> key2));
        for (GameVps entity : pageList) {
            ServerMonitor serverMonitor = map.get(entity.getHostname());
            if (serverMonitor != null) {
                $.copy(serverMonitor, entity);
                entity.setDiskList(DiskUsageInfo.parse(serverMonitor.getDiskUsage()));
            }

            List<Integer> serverIds = StringUtils.split2Int(entity.getGameServerIds());
            if (CollUtil.isEmpty(serverIds)) {
                entity.setOnlineNum(0);
            } else {
                List<GameServer> servers = serverService.selectGameServerList(serverIds);
                serverService.updateOnlineNum(servers);
                entity.setOnlineNum(servers.stream().mapToInt(GameServer::getOnlineNum).sum());
            }
        }
    }
}
