package cn.youai.xiuzhen.game.controller;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.CampaignStatus;
import cn.youai.xiuzhen.game.constant.SwitchStatus;
import cn.youai.xiuzhen.game.constant.TimeType;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignSupportService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日活动信息
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaign")
public class GameCampaignController extends JeecgController<GameCampaign, IGameCampaignService> {

    @Autowired
    private IGameCampaignTypeService campaignTypeService;

    @Autowired
    private IGameCampaignSupportService campaignSupportService;

    @AutoLog(value = "节日活动信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaign entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
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
        IPage<GameCampaignServer> pageList = service.serverList(page, campaignServer.getCampaignId(),
                campaignServer.getTypeId(), campaignServer.getServer());
        for (GameCampaignServer record : pageList.getRecords()) {
            if (record.getStatus() == SwitchStatus.OFF.getValue()) {
                record.setCampaignStatus(CampaignStatus.CLOSED.getValue());
            } else if (record.getStatus() == SwitchStatus.ON.getValue()) {
                Date startTime = null;
                Date endTime = null;
                if (campaignType.getTimeType() == TimeType.TIME_RANGE.getType()) {
                    startTime = campaignType.getStartTime();
                    endTime = campaignType.getEndTime();
                } else if (campaignType.getTimeType() == TimeType.OPEN_DAY.getType()) {
                    startTime = DateUtils.startTimeOfDate(cn.youai.server.utils.DateUtils.addDays(record.getOpenTime(), campaignType.getStartDay()));
                    endTime = DateUtils.endTimeOfDate(cn.youai.server.utils.DateUtils.addDays(record.getOpenTime(),
                            Math.max(campaignType.getStartDay() + campaignType.getDuration() - 1, 0)));
                }
                record.setCampaignStatus(now.before(startTime) ? CampaignStatus.NOT_STARTED.getValue()
                        : now.after(endTime) ? CampaignStatus.COMPLETED.getValue()
                        : CampaignStatus.IN_PROGRESS.getValue());
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
        service.batchSwitch(model);
        return Result.ok("切换成功！");
    }

    @AutoLog(value = "节日活动信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaign entity) {
        if (entity.invalidTime()) {
            return Result.error("时间错误!");
        }
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日活动信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaign entity) {
        if (entity.invalidTime()) {
            return Result.error("时间错误!");
        }
        service.updateCampaign(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 同步游戏配置
     *
     * @param id 实体 id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "节日活动信息-同步到区服")
    @GetMapping(value = "/sync")
    public Result<?> sync(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = service.getById(id);
        if (gameCampaign == null) {
            return Result.error("找不到对应节日活动信息!");
        }

        service.syncCampaign(gameCampaign);
        return Result.ok("同步成功!");
    }

    @AutoLog(value = "节日活动信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日活动信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日活动信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = service.getById(id);
        if (gameCampaign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaign);
    }

    @AutoLog(value = "节日活动信息-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaign gameCampaign) {
        return super.exportXls(request, gameCampaign, GameCampaign.class, "节日活动信息");
    }

    @AutoLog(value = "节日活动信息-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaign.class);
    }

    @AutoLog(value = "节日活动信息-复制")
    @GetMapping(value = "/duplicate")
    public Result<?> duplicate(@RequestParam(name = "id") String id) {
        GameCampaign gameCampaign = service.getById(id);
        if (gameCampaign == null) {
            return Result.error("活动不存在!");
        }

        GameCampaign copy = new GameCampaign(gameCampaign);
        if (!service.save(copy)) {
            return Result.error("复制失败!");
        }

        List<GameCampaignType> campaignTypeList = campaignTypeService.list(Wrappers.<GameCampaignType>lambdaQuery()
                .eq(GameCampaignType::getCampaignId, gameCampaign.getId()).orderByAsc(GameCampaignType::getSort));
        for (GameCampaignType gameCampaignType : campaignTypeList) {
            campaignTypeService.duplicate(gameCampaignType, copy.getId());
        }
        return Result.ok("复制成功!");
    }

    @AutoLog(value = "节日活动信息-移除已结束活动")
    @GetMapping(value = "/removeCompletedServer")
    public Result<?> removeCompletedServer(@RequestParam(name = "id", defaultValue = "0") String id) {
        GameCampaign gameCampaign = service.getById(id);
        if (null == gameCampaign) {
            return Result.error("活动不存在！");
        }
        return service.removeCompletedServer(gameCampaign);
    }

}
