package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.commons.model.Response;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.constant.CampaignStatus;
import org.jeecg.modules.game.constant.SwitchStatus;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.IGameCampaignService;
import org.jeecg.modules.game.service.IGameCampaignSupportService;
import org.jeecg.modules.game.service.IGameCampaignTypeService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @description 活动配置
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaign")
public class GameCampaignController extends JeecgController<GameCampaign, IGameCampaignService> {

    @Autowired
    private IGameCampaignService campaignService;

    @Autowired
    private IGameCampaignTypeService campaignTypeService;

    @Autowired
    private IGameCampaignSupportService campaignSupportService;

    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.campaign-update-url:/campaign/update}")
    private String campaignUpdateUrl;

    /**
     * 分页列表查询
     *
     * @param gameCampaign 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @param req          请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaign gameCampaign,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaign> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaign, req.getParameterMap());
        Page<GameCampaign> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaign> pageList = campaignService.page(page, queryWrapper);
        // 查询子页签列表
        for (GameCampaign record : pageList.getRecords()) {
            getGameCampaignTypeList(record);
        }
        return Result.ok(pageList);
    }

    @GetMapping(value = "/serverList")
    public Result<?> queryServerList(GameCampaignServer campaignServer,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        if (campaignServer.getTypeId() == null) {
            return Result.ok();
        }

        Page<GameServer> page = new Page<>(pageNo, pageSize);
        Date now = DateUtils.now();

        GameCampaignType campaignType = campaignTypeService.getById(campaignServer.getTypeId());
        IPage<GameCampaignServer> pageList = campaignService.serverList(page, campaignServer.getCampaignId(),
                campaignServer.getTypeId(), campaignServer.getServer());
        for (GameCampaignServer record : pageList.getRecords()) {
            if (record.getStatus() == SwitchStatus.OFF.getValue()) {
                record.setCampaignStatus(CampaignStatus.CLOSED.getValue());
            } else if (record.getStatus() == SwitchStatus.ON.getValue()) {
                if (now.before(campaignType.getStartTime())) {
                    record.setCampaignStatus(CampaignStatus.NOT_STARTED.getValue());
                } else if (now.after(campaignType.getEndTime())) {
                    record.setCampaignStatus(CampaignStatus.COMPLETED.getValue());
                } else {
                    record.setCampaignStatus(CampaignStatus.IN_PROGRESS.getValue());
                }
            } else {
                record.setCampaignStatus(CampaignStatus.NONE.getValue());
            }
        }
        return Result.ok(pageList);
    }

    @GetMapping(value = "/serverSwitch")
    public Result<?> switchServer(GameCampaignServer model, HttpServletRequest req) {
        log.debug("switchServer {}", model);

        Wrapper<GameCampaignSupport> query = Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, model.getCampaignId())
                .eq(GameCampaignSupport::getTypeId, model.getTypeId())
                .eq(GameCampaignSupport::getServerId, model.getServerId());

        GameCampaignSupport campaignSupport = campaignSupportService.getOne(query);
        if (campaignSupport == null) {
            campaignSupport = new GameCampaignSupport()
                    .setStatus(model.getStatus())
                    .setCampaignId(model.getCampaignId())
                    .setTypeId(model.getTypeId())
                    .setServerId(model.getServerId());
            campaignSupportService.save(campaignSupport);
        } else {
            if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                campaignSupport.setStatus(model.getStatus());
                campaignSupportService.updateById(campaignSupport);
            }
        }
        return Result.ok("修改成功！");
    }

    @GetMapping(value = "/switchBatch")
    public Result<?> batchSwitch(GameCampaignServer model, HttpServletRequest req) {
        batchSwitch(model);
        return Result.ok("切换成功！");
    }

    /**
     * 添加
     *
     * @param gameCampaign 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaign gameCampaign) {
        campaignService.save(gameCampaign);
        updateTypeList(true, gameCampaign);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaign 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaign gameCampaign) {
        GameCampaign dbEntity = campaignService.getById(gameCampaign.getId());
        Set<String> dbServerIds = new HashSet<>(StrUtil.splitTrim(dbEntity.getServerIds(), ","));
        Set<String> newServerIds = new HashSet<>(StrUtil.splitTrim(gameCampaign.getServerIds(), ","));

        // 去重处理
        gameCampaign.setServerIds(StrUtil.join(",", newServerIds));
        List<Integer> addList = new ArrayList<>();
        List<Integer> removeList = new ArrayList<>();
        for (String serverId : newServerIds) {
            if (!dbServerIds.contains(serverId)) {
                addList.add(Integer.valueOf(serverId));
            }
        }

        for (String serverId : dbServerIds) {
            if (!newServerIds.contains(serverId)) {
                removeList.add(Integer.valueOf(serverId));
            }
        }

        campaignService.updateById(gameCampaign);
        updateTypeList(false, gameCampaign);

        // 批量关闭
        batchSwitchOff(gameCampaign.getId(), removeList);

        // 处理新增区服id
        if (CollUtil.isNotEmpty(addList)) {
            String serverIds = StrUtil.join(",", addList);
            List<GameCampaignType> typeList = getGameCampaignTypeList(gameCampaign);
            for (GameCampaignType model : typeList) {
                GameCampaignServer campaignServer = new GameCampaignServer()
                        .setCampaignId(gameCampaign.getId())
                        .setServer(serverIds)
                        .setTypeId(model.getId())
                        .setStatus(SwitchStatus.ON.getValue());
                batchSwitch(campaignServer);
            }
        }

        return Result.ok("编辑成功!");
    }

    /**
     * 同步游戏配置
     *
     * @param id 实体 id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-同步到区服")
    @GetMapping(value = "/sync")
    public Result<?> sync(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = campaignService.getById(id);
        if (gameCampaign == null) {
            return Result.error("找不到对应活动配置!");
        }

        Wrapper<GameCampaignSupport> query = Wrappers.<GameCampaignSupport>lambdaQuery()
                .eq(GameCampaignSupport::getCampaignId, id)
                .groupBy(GameCampaignSupport::getServerId);

        List<Integer> serverIds = new ArrayList<>();
        List<GameCampaignSupport> supports = campaignSupportService.list(query);
        supports.forEach(t -> {
            serverIds.add(t.getServerId());
        });

        Map<Integer, Response> response = gameServerService.gameServerGet(serverIds, campaignUpdateUrl);
        log.info("sync id:{} response:{}", id, response);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        campaignService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.campaignService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = campaignService.getById(id);
        if (gameCampaign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaign);
    }

    /**
     * 导出excel
     *
     * @param request      请求
     * @param gameCampaign 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaign gameCampaign) {
        return super.exportXls(request, gameCampaign, GameCampaign.class, "活动配置");
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
        return super.importExcel(request, response, GameCampaign.class);
    }

    private void updateTypeList(boolean isAdd, GameCampaign gameCampaign) {
        // 过滤空的新增页签
        List<GameCampaignType> nowList = gameCampaign.getTypeList().stream().filter(t -> t.getType() != null).collect(Collectors.toList());
        for (GameCampaignType model : nowList) {
            model.setCampaignId(gameCampaign.getId());
            if (model.getStartTime() == null) {
                model.setStartTime(gameCampaign.getStartTime());
            }

            if (model.getEndTime() == null) {
                model.setEndTime(gameCampaign.getEndTime());
            }
        }

        List<GameCampaignType> addList = new ArrayList<>();
        List<GameCampaignType> updateList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        if (isAdd) {
            addList.addAll(nowList);
        } else {
            for (GameCampaignType model : nowList) {
                if (model.getId() == null) {
                    addList.add(model);
                }
            }

            nowList.removeAll(addList);
            updateList.addAll(nowList);

            if (!nowList.isEmpty()) {
                Map<Long, GameCampaignType> typeMap = nowList.stream().collect(Collectors.toMap(GameCampaignType::getId, Function.identity()));

                // 更新子页签
                List<GameCampaignType> typeList = getGameCampaignTypeList(gameCampaign);
                for (GameCampaignType model : typeList) {
                    if (typeMap.containsKey(model.getId())) {
                        updateList.remove(model);
                    } else {
                        removeList.add(model.getId());
                    }
                }
            }
        }

        log.debug("addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        updateGameCampaignTypeList(addList, updateList, removeList);

        // 新增页签，根据活动选定的区服生成页签与服务器的关联关系
        if (isAdd) {
            for (GameCampaignType model : addList) {
                GameCampaignServer campaignServer = new GameCampaignServer()
                        .setCampaignId(gameCampaign.getId())
                        .setServer(gameCampaign.getServerIds())
                        .setTypeId(model.getId())
                        .setStatus(SwitchStatus.ON.getValue());
                batchSwitch(campaignServer);
            }
        }
    }

    private void updateGameCampaignTypeList(List<GameCampaignType> addList, List<GameCampaignType> updateList, List<Long> removeList) {
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeService.removeByIds(removeList);
        }
    }

    private void batchSwitchOff(long campaignId, List<Integer> serverList) {
        if (CollUtil.isNotEmpty(serverList)) {
            Wrapper<GameCampaignSupport> updateWrapper = Wrappers.<GameCampaignSupport>lambdaUpdate()
                    .set(GameCampaignSupport::getStatus, SwitchStatus.OFF.getValue())
                    .eq(GameCampaignSupport::getCampaignId, campaignId)
                    .in(GameCampaignSupport::getServerId, serverList);
            campaignSupportService.update(updateWrapper);
        }
    }

    private void batchSwitch(GameCampaignServer model) {
        int[] ids = StrUtil.splitToInt(model.getServer(), ",");
        List<GameCampaignSupport> addList = new ArrayList<>();
        List<GameCampaignSupport> updateList = new ArrayList<>();
        for (int serverId : ids) {
            Wrapper<GameCampaignSupport> query = Wrappers.<GameCampaignSupport>lambdaQuery()
                    .eq(GameCampaignSupport::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignSupport::getTypeId, model.getTypeId())
                    .eq(GameCampaignSupport::getServerId, serverId);

            GameCampaignSupport campaignSupport = campaignSupportService.getOne(query);
            if (campaignSupport == null) {
                campaignSupport = new GameCampaignSupport()
                        .setStatus(model.getStatus())
                        .setCampaignId(model.getCampaignId())
                        .setTypeId(model.getTypeId())
                        .setServerId(serverId);
                addList.add(campaignSupport);
            } else {
                if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                    campaignSupport.setStatus(model.getStatus());
                    updateList.add(campaignSupport);
                }
            }
        }

        if (CollUtil.isNotEmpty(addList)) {
            campaignSupportService.saveBatch(addList);
        }

        if (CollUtil.isNotEmpty(updateList)) {
            campaignSupportService.updateBatchById(updateList);
        }
    }

    private List<GameCampaignType> getGameCampaignTypeList(GameCampaign gameCampaign) {
        long campaignId = gameCampaign.getId();
        LambdaQueryWrapper<GameCampaignType> query = Wrappers.<GameCampaignType>lambdaQuery()
                .eq(GameCampaignType::getCampaignId, campaignId)
                .orderByAsc(GameCampaignType::getSort);

        List<GameCampaignType> list = campaignTypeService.list(query);
        for (GameCampaignType model : list) {
            campaignTypeService.fillTabDetail(model, true);
        }
        gameCampaign.setTypeList(list);
        return list;
    }
}
