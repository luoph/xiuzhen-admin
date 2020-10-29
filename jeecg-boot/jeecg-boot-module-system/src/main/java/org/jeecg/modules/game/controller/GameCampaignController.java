package org.jeecg.modules.game.controller;

import cn.hutool.core.util.StrUtil;
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
 * @description 活动配置
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaign")
public class GameCampaignController extends JeecgController<GameCampaign, IGameCampaignService> {

    @Autowired
    private IGameCampaignService gameCampaignService;

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Autowired
    private IGameCampaignSupportService gameCampaignSupportService;

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
        IPage<GameCampaign> pageList = gameCampaignService.page(page, queryWrapper);
        // 查询子页签列表
        for (GameCampaign record : pageList.getRecords()) {
            record.setTypeList(getGameCampaignTypeList(record.getId()));
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

        GameCampaignType campaignType = gameCampaignTypeService.getById(campaignServer.getTypeId());
        IPage<GameCampaignServer> pageList = gameCampaignService.serverList(page, campaignServer.getCampaignId(),
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

        GameCampaignSupport campaignSupport = gameCampaignSupportService.getOne(query);
        if (campaignSupport == null) {
            campaignSupport = new GameCampaignSupport()
                    .setStatus(model.getStatus())
                    .setCampaignId(model.getCampaignId())
                    .setTypeId(model.getTypeId())
                    .setServerId(model.getServerId());
            gameCampaignSupportService.save(campaignSupport);
        } else {
            if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                campaignSupport.setStatus(model.getStatus());
                gameCampaignSupportService.updateById(campaignSupport);
            }
        }
        return Result.ok("修改成功！");
    }

    @GetMapping(value = "/switchBatch")
    public Result<?> batchSwitch(GameCampaignServer model, HttpServletRequest req) {
        int[] ids = StrUtil.splitToInt(model.getServer(), ",");
        List<GameCampaignSupport> addList = new ArrayList<>();
        List<GameCampaignSupport> updateList = new ArrayList<>();
        for (int serverId : ids) {

            Wrapper<GameCampaignSupport> query = Wrappers.<GameCampaignSupport>lambdaQuery()
                    .eq(GameCampaignSupport::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignSupport::getTypeId, model.getTypeId())
                    .eq(GameCampaignSupport::getServerId, serverId);

            GameCampaignSupport campaignSupport = gameCampaignSupportService.getOne(query);
            if (campaignSupport == null) {
                campaignSupport = new GameCampaignSupport()
                        .setStatus(model.getStatus())
                        .setCampaignId(model.getCampaignId())
                        .setTypeId(model.getTypeId())
                        .setServerId(serverId);
                addList.add(campaignSupport);
                gameCampaignSupportService.save(campaignSupport);
            } else {
                if (!Objects.equals(campaignSupport.getStatus(), model.getStatus())) {
                    campaignSupport.setStatus(model.getStatus());
                    updateList.add(campaignSupport);
                }
            }
        }

        gameCampaignSupportService.saveBatch(addList);
        gameCampaignSupportService.updateBatchById(updateList);
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
        gameCampaignService.save(gameCampaign);
        for (GameCampaignType model : gameCampaign.getTypeList()) {
            model.setCampaignId(gameCampaign.getId());
        }

        updateTypeList(gameCampaign);
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
        gameCampaignService.updateById(gameCampaign);
        updateTypeList(gameCampaign);
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
        gameCampaignService.removeById(id);
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
        this.gameCampaignService.removeByIds(Arrays.asList(ids.split(",")));
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
        GameCampaign gameCampaign = gameCampaignService.getById(id);
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

    private List<GameCampaignType> getGameCampaignTypeList(long campaignId) {
        LambdaQueryWrapper<GameCampaignType> query = Wrappers.<GameCampaignType>lambdaQuery()
                .eq(GameCampaignType::getCampaignId, campaignId)
                .orderByAsc(GameCampaignType::getSort);
        return gameCampaignTypeService.list(query);
    }

    private void updateTypeList(GameCampaign gameCampaign) {
        // 过滤空的新增页签
        List<GameCampaignType> nowList = gameCampaign.getTypeList().stream().filter(t -> t.getType() != null).collect(Collectors.toList());

        List<GameCampaignType> addList = new ArrayList<>();
        List<GameCampaignType> updateList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();
        for (GameCampaignType model : nowList) {
            if (model.getId() == null) {
                addList.add(model);
            }
        }

        nowList.removeAll(addList);
        Map<Long, GameCampaignType> typeMap = nowList.stream().collect(Collectors.toMap(GameCampaignType::getId, Function.identity()));

        // 更新子页签
        List<GameCampaignType> typeList = getGameCampaignTypeList(gameCampaign.getId());
        for (GameCampaignType model : typeList) {
            if (typeMap.containsKey(model.getId())) {
                updateList.add(model);
            } else {
                removeList.add(model.getId());
            }
        }

        log.debug("addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        updateGameCampaignTypeList(addList, updateList, removeList);
    }

    private void updateGameCampaignTypeList(List<GameCampaignType> addList, List<GameCampaignType> updateList, List<Long> removeList) {
        if (addList.size() > 0) {
            gameCampaignTypeService.saveBatch(addList);
        }
        if (updateList.size() > 0) {
            gameCampaignTypeService.updateBatchById(updateList);
        }
        if (removeList.size() > 0) {
            gameCampaignTypeService.removeByIds(removeList);
        }
    }
}
