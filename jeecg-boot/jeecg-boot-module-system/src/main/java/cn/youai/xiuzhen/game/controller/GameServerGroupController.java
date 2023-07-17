package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 跨服分组
 * @date 2023-04-08
 */
@Slf4j
@RestController
@Api(tags = "跨服分组")
@RequestMapping("/game/group")
public class GameServerGroupController extends JeecgController<GameServerGroup, IGameServerGroupService> {

    @Autowired
    private IGameServerService serverService;

    @AutoLog(value = "跨服分组-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameServerGroup entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameServerGroup> page = new Page<>(pageNo, pageSize);
        DateRange createTimeRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime");
        DateRange crossSettleTimeRange = PageQueryUtils.parseRange(req.getParameterMap(), "crossSettleTime");
        IPage<GameServerGroup> pageList = service.queryList(page, entity, createTimeRange, crossSettleTimeRange);
        this.onload(pageList.getRecords());
        return Result.ok(pageList);
    }

    @AutoLog(value = "跨服分组-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameServerGroup entity) {
        return super.add(entity);
    }

    @AutoLog(value = "跨服分组-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameServerGroup entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "跨服分组-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "跨服分组-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "跨服分组-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "跨服分组-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameServerGroup gameInfo) {
        return super.exportXls(request, gameInfo, GameServerGroup.class, "跨服分组");
    }

    @AutoLog(value = "跨服分组-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameServerGroup.class);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    protected void onload(List<GameServerGroup> pageList) {
        for (GameServerGroup entity : pageList) {
            List<Integer> serverIds = StringUtils.split2Int(entity.getServerIds());
            entity.setServerNum(CollUtil.size(serverIds));
            if (CollUtil.isEmpty(serverIds)) {
                entity.setOnlineNum(0);
            } else {
                List<GameServer> servers = serverService.selectGameServerList(serverIds);
                serverService.updateOnlineNum(servers);
                entity.setOnlineNum(servers.stream().filter(t -> t.getOnlineNum() != null).mapToInt(GameServer::getOnlineNum).sum());
            }
        }
    }
}
