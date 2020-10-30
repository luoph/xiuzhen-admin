package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.constant.CampaignFestivalType;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignType")
public class GameCampaignTypeController extends JeecgController<GameCampaignType, IGameCampaignTypeService> {

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Autowired
    private IGameCampaignTypeLoginService campaignTypeLoginService;

    @Autowired
    private IGameCampaignTypeTaskService campaignTypeTaskService;

    @Autowired
    private IGameCampaignTypeRechargeService campaignTypeRechargeService;

    @Autowired
    private IGameCampaignTypeBuffService campaignTypeBuffService;

    @Autowired
    private IGameCampaignTypeExchangeService campaignTypeExchangeService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignType 数据实体
     * @param pageNo           页码
     * @param pageSize         分页大小
     * @param req              请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignType gameCampaignType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaignType> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignType, req.getParameterMap());
        Page<GameCampaignType> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaignType> pageList = gameCampaignTypeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignType 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignType gameCampaignType) {
        gameCampaignTypeService.save(gameCampaignType);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignType 数据实体
     * @return {@linkplain Result}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @AutoLog(value = "活动类型配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignType gameCampaignType) {
        gameCampaignTypeService.updateById(gameCampaignType);

        // 更新具体的内容配置
        if (StringUtils.isNotBlank(gameCampaignType.getDetailsData())) {
            CampaignFestivalType festivalType = CampaignFestivalType.valueOf(gameCampaignType.getType());
            if (festivalType != null) {
                List detailList = JSON.parseArray(gameCampaignType.getDetailsData(), festivalType.getTableClass());
                log.info("detailList:{}", detailList);
                switch (festivalType) {
                    case LOGIN:
                        handleLoginTab(gameCampaignType, detailList);
                        break;

                    case TASK:
                        handleTaskTab(gameCampaignType, detailList);
                        break;

                    case EXCHANGE:
                        handleExchangeTab(gameCampaignType, detailList);
                        break;

                    case RECHARGE:
                        handleRechargeTab(gameCampaignType, detailList);
                        break;

                    case BUFF_ANIMA:
                    case BUFF_PRACTICE:
                        handleBuffTab(gameCampaignType, detailList);
                        break;

                    default:
                        break;
                }
                log.info("detailList:{}", detailList);
            }
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameCampaignTypeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameCampaignTypeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaignType gameCampaignType = gameCampaignTypeService.getById(id);
        if (gameCampaignType == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaignType);
    }

    /**
     * 导出excel
     *
     * @param request          请求
     * @param gameCampaignType 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignType gameCampaignType) {
        return super.exportXls(request, gameCampaignType, GameCampaignType.class, "活动类型配置");
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
        return super.importExcel(request, response, GameCampaignType.class);
    }

    private void handleLoginTab(GameCampaignType model, List<GameCampaignTypeLogin> list) {
        List<GameCampaignTypeLogin> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeLogin item : list) {
            item.setCampaignId(model.getCampaignId());
            item.setTypeId(model.getId());

            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeLogin> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeLogin> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeLogin::getId, Function.identity()));
            Wrapper<GameCampaignTypeLogin> detailQuery = Wrappers.<GameCampaignTypeLogin>lambdaQuery()
                    .eq(GameCampaignTypeLogin::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignTypeLogin::getTypeId, model.getId());
            List<GameCampaignTypeLogin> dbList = campaignTypeLoginService.list(detailQuery);
            for (GameCampaignTypeLogin item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(model.getId());
                }
            }
        }

        log.debug("handleLoginTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeLoginService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeLoginService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeLoginService.removeByIds(removeList);
        }
    }

    private void handleRechargeTab(GameCampaignType model, List<GameCampaignTypeRecharge> list) {
        List<GameCampaignTypeRecharge> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeRecharge item : list) {
            item.setCampaignId(model.getCampaignId());
            item.setTypeId(model.getId());

            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeRecharge> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeRecharge> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeRecharge::getId, Function.identity()));
            Wrapper<GameCampaignTypeRecharge> detailQuery = Wrappers.<GameCampaignTypeRecharge>lambdaQuery()
                    .eq(GameCampaignTypeRecharge::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignTypeRecharge::getTypeId, model.getId());
            List<GameCampaignTypeRecharge> dbList = campaignTypeRechargeService.list(detailQuery);
            for (GameCampaignTypeRecharge item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(model.getId());
                }
            }
        }

        log.debug("handleRechargeTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeRechargeService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeRechargeService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeRechargeService.removeByIds(removeList);
        }
    }

    private void handleExchangeTab(GameCampaignType model, List<GameCampaignTypeExchange> list) {
        List<GameCampaignTypeExchange> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeExchange item : list) {
            item.setCampaignId(model.getCampaignId());
            item.setTypeId(model.getId());

            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeExchange> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeExchange> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeExchange::getId, Function.identity()));
            Wrapper<GameCampaignTypeExchange> detailQuery = Wrappers.<GameCampaignTypeExchange>lambdaQuery()
                    .eq(GameCampaignTypeExchange::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignTypeExchange::getTypeId, model.getId());
            List<GameCampaignTypeExchange> dbList = campaignTypeExchangeService.list(detailQuery);
            for (GameCampaignTypeExchange item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(model.getId());
                }
            }
        }

        log.debug("handleExchangeTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeExchangeService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeExchangeService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeExchangeService.removeByIds(removeList);
        }
    }

    private void handleTaskTab(GameCampaignType model, List<GameCampaignTypeTask> list) {
        List<GameCampaignTypeTask> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeTask item : list) {
            item.setCampaignId(model.getCampaignId());
            item.setTypeId(model.getId());

            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeTask> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeTask> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeTask::getId, Function.identity()));
            Wrapper<GameCampaignTypeTask> detailQuery = Wrappers.<GameCampaignTypeTask>lambdaQuery()
                    .eq(GameCampaignTypeTask::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignTypeTask::getTypeId, model.getId());
            List<GameCampaignTypeTask> dbList = campaignTypeTaskService.list(detailQuery);
            for (GameCampaignTypeTask item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(model.getId());
                }
            }
        }

        log.debug("handleTaskTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeTaskService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeTaskService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeTaskService.removeByIds(removeList);
        }
    }

    private void handleBuffTab(GameCampaignType model, List<GameCampaignTypeBuff> list) {
        List<GameCampaignTypeBuff> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeBuff item : list) {
            item.setTypeId(model.getId());
            item.setCampaignId(model.getCampaignId());
            item.setDescription(model.getBuffDesc());
            item.setAddition(model.getAddition());

            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeBuff> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeBuff> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeBuff::getId, Function.identity()));
            Wrapper<GameCampaignTypeBuff> detailQuery = Wrappers.<GameCampaignTypeBuff>lambdaQuery()
                    .eq(GameCampaignTypeBuff::getCampaignId, model.getCampaignId())
                    .eq(GameCampaignTypeBuff::getTypeId, model.getId());
            List<GameCampaignTypeBuff> dbList = campaignTypeBuffService.list(detailQuery);
            for (GameCampaignTypeBuff item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(model.getId());
                }
            }
        }

        log.debug("handleBuffTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        if (CollUtil.isNotEmpty(addList)) {
            campaignTypeBuffService.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            campaignTypeBuffService.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            campaignTypeBuffService.removeByIds(removeList);
        }
    }
}
