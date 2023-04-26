package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameSdkChannel;
import cn.youai.xiuzhen.game.service.IGameSdkChannelService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏Sdk渠道
 * @date 2023-04-09
 */
@Slf4j
@RestController
@Api(tags = "游戏Sdk渠道")
@RequestMapping("/game/sdkChannel")
public class GameSdkChannelController extends JeecgController<GameSdkChannel, IGameSdkChannelService> {

    @AutoLog(value = "游戏Sdk渠道-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameSdkChannel entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏Sdk渠道-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameSdkChannel entity) {
        return super.add(entity);
    }

    @AutoLog(value = "游戏Sdk渠道-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameSdkChannel entity) {
        return super.edit(entity);
    }

    @Async
    @Scheduled(cron = "0 0/30 * * * ?")
    protected void doSync() {
        List<GameSdkChannel> gameSdkChannels = service.selectSdkChannelList();
        List<GameSdkChannel> existList = service.list();
        Map<String, ArrayList<GameSdkChannel>> channelGroup = new HashMap<>();
        if (CollUtil.isNotEmpty(existList)) {
            channelGroup = existList.stream()
                    .collect(Collectors.groupingBy(GameSdkChannel::getChannel,
                            HashMap::new, Collectors.toCollection(ArrayList::new)));
        }

        List<GameSdkChannel> addList = new ArrayList<>();
        for (GameSdkChannel entity : gameSdkChannels) {
            List<GameSdkChannel> sdkChannels = channelGroup.get(entity.getChannel());
            if (CollUtil.isEmpty(sdkChannels) || !exists(entity.getSdkChannel(), sdkChannels)) {
                addList.add(entity);
            }
        }

        if (CollUtil.isNotEmpty(addList)) {
            Date now = DateUtils.now();
            for (GameSdkChannel e : addList) {
                e.setCreateTime(now);
                e.setCreateBy("admin");
            }
            service.saveBatch(addList);
        }
    }

    @AutoLog(value = "游戏Sdk渠道-同步Sdk渠道")
    @GetMapping(value = "/sync")
    public Result<?> sync() {
        doSync();
        return Result.OK("同步成功");
    }

    @AutoLog(value = "游戏Sdk渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏Sdk渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏Sdk渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏Sdk渠道-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameSdkChannel entity) {
        return super.exportXls(request, entity, GameSdkChannel.class, "游戏Sdk渠道");
    }

    @AutoLog(value = "游戏Sdk渠道-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameSdkChannel.class);
    }

    private boolean exists(String sdkChannel, List<GameSdkChannel> list) {
        return list.stream().anyMatch(t -> StrUtil.equals(t.getSdkChannel(), sdkChannel));
    }

}
