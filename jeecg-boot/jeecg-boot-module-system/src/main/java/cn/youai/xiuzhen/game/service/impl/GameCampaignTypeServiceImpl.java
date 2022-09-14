package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.constant.CampaignFestivalType;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeMapper;
import cn.youai.xiuzhen.game.service.*;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Service
public class GameCampaignTypeServiceImpl extends ServiceImpl<GameCampaignTypeMapper, GameCampaignType> implements IGameCampaignTypeService {


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

    @Autowired
    private IGameCampaignTypeFallService campaignTypeFallService;
    @Autowired
    private IGameCampaignTypeFallRewardService campaignTypeFallRewardService;

    @Autowired
    private IGameCampaignTypeFireworkService campaignTypeFireworkService;

    @Autowired
    private IGameCampaignTypeReduceService campaignTypeReduceService;

    @Autowired
    private IGameCampaignTypeSwordService campaignTypeSwordService;

    @Autowired
    private IGameCampaignTypeThrowingEggsService campaignTypeThrowingEggsService;
    @Autowired
    private IGameCampaignTypeThrowingEggsRankService campaignTypeThrowingEggsRankService;
    @Autowired
    private IGameCampaignTypeThrowingEggsGiftService campaignTypeThrowingEggsGiftService;

    @Autowired
    private IGameCampaignTypePartyTaskService campaignTypePartyTaskService;
    @Autowired
    private IGameCampaignTypePartyProgressService campaignTypePartyProgressService;

    @Autowired
    private IGameCampaignDirectPurchaseService campaignDirectPurchaseService;

    @Autowired
    private IGameCampaignTypeRebateRechargeService campaignTypeRebateRechargeService;

    @Autowired
    private IGameCampaignTypeMarryRankService gameCampaignTypeMarryRankService;
    @Autowired
    private IGameCampaignTypeMarryRankRewardService gameCampaignTypeMarryRankRewardService;

    @Autowired
    private IGameCampaignTypeSelectDiscountItemService gameCampaignTypeSelectDiscountItemService;
    @Autowired
    private IGameCampaignTypeSelectDiscountMessageService gameCampaignTypeSelectDiscountMessageService;

    @Override
    public void fillTabDetail(GameCampaignType model, boolean merge) {
        long campaignId = model.getCampaignId();
        CampaignFestivalType festivalType = CampaignFestivalType.valueOf(model.getType());
        if (festivalType != null) {
            switch (festivalType) {
                case LOGIN: {
                    Wrapper<GameCampaignTypeLogin> detailQuery = Wrappers.<GameCampaignTypeLogin>lambdaQuery()
                            .eq(GameCampaignTypeLogin::getCampaignId, campaignId)
                            .eq(GameCampaignTypeLogin::getTypeId, model.getId());
                    model.setDetails(campaignTypeLoginService.list(detailQuery));
                }
                break;

                case TASK: {
                    Wrapper<GameCampaignTypeTask> detailQuery = Wrappers.<GameCampaignTypeTask>lambdaQuery()
                            .eq(GameCampaignTypeTask::getCampaignId, campaignId)
                            .eq(GameCampaignTypeTask::getTypeId, model.getId());
                    model.setDetails(campaignTypeTaskService.list(detailQuery));
                }
                break;

                case EXCHANGE: {
                    Wrapper<GameCampaignTypeExchange> detailQuery = Wrappers.<GameCampaignTypeExchange>lambdaQuery()
                            .eq(GameCampaignTypeExchange::getCampaignId, campaignId)
                            .eq(GameCampaignTypeExchange::getTypeId, model.getId());
                    model.setDetails(campaignTypeExchangeService.list(detailQuery));
                }
                break;

                case RECHARGE: {
                    Wrapper<GameCampaignTypeRecharge> detailQuery = Wrappers.<GameCampaignTypeRecharge>lambdaQuery()
                            .eq(GameCampaignTypeRecharge::getCampaignId, campaignId)
                            .eq(GameCampaignTypeRecharge::getTypeId, model.getId());
                    model.setDetails(campaignTypeRechargeService.list(detailQuery));
                }
                break;

                case BUFF_ANIMA:
                case BUFF_PRACTICE: {
                    Wrapper<GameCampaignTypeBuff> detailQuery = Wrappers.<GameCampaignTypeBuff>lambdaQuery()
                            .eq(GameCampaignTypeBuff::getCampaignId, campaignId)
                            .eq(GameCampaignTypeBuff::getTypeId, model.getId());
                    model.setDetails(campaignTypeBuffService.list(detailQuery));
                }
                break;

                case FALL: {
                    Wrapper<GameCampaignTypeFall> detailQuery = Wrappers.<GameCampaignTypeFall>lambdaQuery()
                            .eq(GameCampaignTypeFall::getCampaignId, campaignId)
                            .eq(GameCampaignTypeFall::getTypeId, model.getId());
                    model.setDetails(campaignTypeFallService.list(detailQuery));

                    Wrapper<GameCampaignTypeFallReward> rewardQuery = Wrappers.<GameCampaignTypeFallReward>lambdaQuery()
                            .eq(GameCampaignTypeFallReward::getCampaignId, campaignId)
                            .eq(GameCampaignTypeFallReward::getTypeId, model.getId());
                    model.setRewardList(campaignTypeFallRewardService.list(rewardQuery));
                }
                break;

                case FIREWORK: {
                    Wrapper<GameCampaignTypeFirework> detailQuery = Wrappers.<GameCampaignTypeFirework>lambdaQuery()
                            .eq(GameCampaignTypeFirework::getCampaignId, campaignId)
                            .eq(GameCampaignTypeFirework::getTypeId, model.getId());
                    model.setDetails(campaignTypeFireworkService.list(detailQuery));
                }
                break;

                case REDUCE_RANK: {
                    Wrapper<GameCampaignTypeReduce> detailQuery = Wrappers.<GameCampaignTypeReduce>lambdaQuery()
                            .eq(GameCampaignTypeReduce::getCampaignId, campaignId)
                            .eq(GameCampaignTypeReduce::getTypeId, model.getId());
                    model.setDetails(campaignTypeReduceService.list(detailQuery));
                }
                break;

                case LIMIT_TIME_SWORD: {
                    Wrapper<GameCampaignTypeSword> detailQuery = Wrappers.<GameCampaignTypeSword>lambdaQuery()
                            .eq(GameCampaignTypeSword::getCampaignId, campaignId)
                            .eq(GameCampaignTypeSword::getTypeId, model.getId());
                    model.setDetails(campaignTypeSwordService.list(detailQuery));
                }
                break;

                case THROWING_EGGS: {
                    Wrapper<GameCampaignTypeThrowingEggs> detailQuery = Wrappers.<GameCampaignTypeThrowingEggs>lambdaQuery()
                            .eq(GameCampaignTypeThrowingEggs::getCampaignId, campaignId)
                            .eq(GameCampaignTypeThrowingEggs::getTypeId, model.getId());
                    model.setDetails(campaignTypeThrowingEggsService.list(detailQuery));
                }
                break;

                case THROWING_EGGS_RANK: {
                    Wrapper<GameCampaignTypeThrowingEggsRank> detailQuery = Wrappers.<GameCampaignTypeThrowingEggsRank>lambdaQuery()
                            .eq(GameCampaignTypeThrowingEggsRank::getCampaignId, campaignId)
                            .eq(GameCampaignTypeThrowingEggsRank::getTypeId, model.getId());
                    model.setDetails(campaignTypeThrowingEggsRankService.list(detailQuery));
                }
                break;

                case THROWING_EGGS_GIFT: {
                    Wrapper<GameCampaignTypeThrowingEggsGift> detailQuery = Wrappers.<GameCampaignTypeThrowingEggsGift>lambdaQuery()
                            .eq(GameCampaignTypeThrowingEggsGift::getCampaignId, campaignId)
                            .eq(GameCampaignTypeThrowingEggsGift::getTypeId, model.getId());
                    model.setDetails(campaignTypeThrowingEggsGiftService.list(detailQuery));
                }
                break;

                case PARTY: {
                    Wrapper<GameCampaignTypePartyTask> detailQuery = Wrappers.<GameCampaignTypePartyTask>lambdaQuery()
                            .eq(GameCampaignTypePartyTask::getCampaignId, campaignId)
                            .eq(GameCampaignTypePartyTask::getTypeId, model.getId());
                    model.setDetails(campaignTypePartyTaskService.list(detailQuery));

                    Wrapper<GameCampaignTypePartyProgress> rewardsQuery = Wrappers.<GameCampaignTypePartyProgress>lambdaQuery()
                            .eq(GameCampaignTypePartyProgress::getCampaignId, campaignId)
                            .eq(GameCampaignTypePartyProgress::getTypeId, model.getId());
                    model.setRewardList(campaignTypePartyProgressService.list(rewardsQuery));
                }
                break;

                case DIRECT_PURCHASE: {
                    Wrapper<GameCampaignDirectPurchase> rewardsQuery = Wrappers.<GameCampaignDirectPurchase>lambdaQuery()
                            .eq(GameCampaignDirectPurchase::getCampaignId, campaignId)
                            .eq(GameCampaignDirectPurchase::getTypeId, model.getId());
                    model.setDetails(campaignDirectPurchaseService.list(rewardsQuery));
                }
                break;

                case REBATE_RECHARGE: {
                    Wrapper<GameCampaignTypeRebateRecharge> rewardsQuery = Wrappers.<GameCampaignTypeRebateRecharge>lambdaQuery()
                            .eq(GameCampaignTypeRebateRecharge::getCampaignId, campaignId)
                            .eq(GameCampaignTypeRebateRecharge::getTypeId, model.getId());
                    model.setDetails(campaignTypeRebateRechargeService.list(rewardsQuery));
                }
                break;

                case MARRY_RANK_WINE:
                case MARRY_RANK_CHARM: {
                    Wrapper<GameCampaignTypeMarryRank> detailQuery = Wrappers.<GameCampaignTypeMarryRank>lambdaQuery()
                            .eq(GameCampaignTypeMarryRank::getCampaignId, campaignId)
                            .eq(GameCampaignTypeMarryRank::getTypeId, model.getId());
                    model.setDetails(gameCampaignTypeMarryRankService.list(detailQuery));

                    Wrapper<GameCampaignTypeMarryRankReward> rewardQuery = Wrappers.<GameCampaignTypeMarryRankReward>lambdaQuery()
                            .eq(GameCampaignTypeMarryRankReward::getCampaignId, campaignId)
                            .eq(GameCampaignTypeMarryRankReward::getTypeId, model.getId());
                    model.setRewardList(gameCampaignTypeMarryRankRewardService.list(rewardQuery));
                }
                break;

                case SELECT_DISCOUNT_ITEM:
                    Wrapper<GameCampaignTypeSelectDiscountItem> detailQuery = Wrappers.<GameCampaignTypeSelectDiscountItem>lambdaQuery()
                            .eq(GameCampaignTypeSelectDiscountItem::getCampaignId, campaignId)
                            .eq(GameCampaignTypeSelectDiscountItem::getTypeId, model.getId());
                    model.setDetails(gameCampaignTypeSelectDiscountItemService.list(detailQuery));

                    Wrapper<GameCampaignTypeSelectDiscountMessage> rewardQuery = Wrappers.<GameCampaignTypeSelectDiscountMessage>lambdaQuery()
                            .eq(GameCampaignTypeSelectDiscountMessage::getCampaignId, campaignId)
                            .eq(GameCampaignTypeSelectDiscountMessage::getTypeId, model.getId());
                    model.setRewardList(gameCampaignTypeSelectDiscountMessageService.list(rewardQuery));
                    break;

                default:
                    break;
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void updateTabDetail(GameCampaignType model) {
        CampaignFestivalType festivalType = CampaignFestivalType.valueOf(model.getType());
        fillTabDetail(model, false);

        if (festivalType != null) {
            List detailList = JSON.parseArray(model.getDetailsData(), festivalType.getTableClass());
            log.info("detailList:{}", detailList);
            switch (festivalType) {
                case LOGIN:
                    handleLoginTab(model, detailList);
                    break;

                case TASK:
                    handleTaskTab(model, detailList);
                    break;

                case EXCHANGE:
                    handleExchangeTab(model, detailList);
                    break;

                case RECHARGE:
                    handleRechargeTab(model, detailList);
                    break;

                case BUFF_ANIMA:
                case BUFF_PRACTICE:
                    handleBuffTab(model, detailList);
                    break;
                default:
                    break;
            }
            log.info("detailList:{}", detailList);
        }
    }


    @SuppressWarnings("unchecked")
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
            List<GameCampaignTypeLogin> dbList = (List<GameCampaignTypeLogin>) model.getDetails();
            for (GameCampaignTypeLogin item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
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

    @SuppressWarnings("unchecked")
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
            List<GameCampaignTypeRecharge> dbList = (List<GameCampaignTypeRecharge>) model.getDetails();
            for (GameCampaignTypeRecharge item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
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

    @SuppressWarnings("unchecked")
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
            List<GameCampaignTypeExchange> dbList = (List<GameCampaignTypeExchange>) model.getDetails();
            for (GameCampaignTypeExchange item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
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

    @SuppressWarnings("unchecked")
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
            List<GameCampaignTypeTask> dbList = (List<GameCampaignTypeTask>) model.getDetails();
            for (GameCampaignTypeTask item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
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

    @SuppressWarnings("unchecked")
    private void handleBuffTab(GameCampaignType model, List<GameCampaignTypeBuff> list) {
        List<GameCampaignTypeBuff> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeBuff item : list) {
            item.setTypeId(model.getId());
            item.setCampaignId(model.getCampaignId());

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
            List<GameCampaignTypeBuff> dbList = (List<GameCampaignTypeBuff>) model.getDetails();
            for (GameCampaignTypeBuff item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
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


    @Override
    public void duplicate(GameCampaignType model, long copyCampaignId) {
        GameCampaignType copyCampaignType = new GameCampaignType(model);
        copyCampaignType.setCampaignId(copyCampaignId);
        CampaignFestivalType festivalType = CampaignFestivalType.valueOf(copyCampaignType.getType());
        if (save(copyCampaignType) && festivalType != null) {
            fillTabDetail(model, false);
            if (CollUtil.isEmpty(model.getDetails()) && CollUtil.isEmpty(model.getRewardList())) {
                return;
            }
            switch (festivalType) {
                case LOGIN: {
                    List<GameCampaignTypeLogin> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeLogin> details = (List<GameCampaignTypeLogin>) model.getDetails();
                    for (GameCampaignTypeLogin detail : details) {
                        GameCampaignTypeLogin copy = new GameCampaignTypeLogin(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeLoginService.saveBatch(list);
                }
                break;

                case RECHARGE: {
                    List<GameCampaignTypeRecharge> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeRecharge> details = (List<GameCampaignTypeRecharge>) model.getDetails();
                    for (GameCampaignTypeRecharge detail : details) {
                        GameCampaignTypeRecharge copy = new GameCampaignTypeRecharge(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeRechargeService.saveBatch(list);
                }
                break;

                case EXCHANGE: {
                    List<GameCampaignTypeExchange> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeExchange> details = (List<GameCampaignTypeExchange>) model.getDetails();
                    for (GameCampaignTypeExchange detail : details) {
                        GameCampaignTypeExchange copy = new GameCampaignTypeExchange(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeExchangeService.saveBatch(list);
                }
                break;

                case TASK: {
                    List<GameCampaignTypeTask> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeTask> details = (List<GameCampaignTypeTask>) model.getDetails();
                    for (GameCampaignTypeTask detail : details) {
                        GameCampaignTypeTask copy = new GameCampaignTypeTask(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeTaskService.saveBatch(list);
                }
                break;

                case BUFF_ANIMA:
                case BUFF_PRACTICE: {
                    List<GameCampaignTypeBuff> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeBuff> details = (List<GameCampaignTypeBuff>) model.getDetails();
                    for (GameCampaignTypeBuff detail : details) {
                        GameCampaignTypeBuff copy = new GameCampaignTypeBuff(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeBuffService.saveBatch(list);
                }
                break;

                case FALL: {
                    List<GameCampaignTypeFall> copyDetails = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeFall> details = (List<GameCampaignTypeFall>) model.getDetails();
                    for (GameCampaignTypeFall detail : details) {
                        GameCampaignTypeFall copy = new GameCampaignTypeFall(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        copyDetails.add(copy);
                    }
                    campaignTypeFallService.saveBatch(copyDetails);

                    if (CollUtil.isNotEmpty(model.getRewardList())) {
                        List<GameCampaignTypeFallReward> copyRewards = new ArrayList<>(model.getRewardList().size());
                        List<GameCampaignTypeFallReward> rewardList = (List<GameCampaignTypeFallReward>) model.getRewardList();
                        for (GameCampaignTypeFallReward reward : rewardList) {
                            GameCampaignTypeFallReward copy = new GameCampaignTypeFallReward(reward);
                            copy.setCampaignId(copyCampaignId);
                            copy.setTypeId(copyCampaignType.getId());
                            copyRewards.add(copy);
                        }
                        campaignTypeFallRewardService.saveBatch(copyRewards);
                    }
                }
                break;

                case FIREWORK: {
                    List<GameCampaignTypeFirework> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeFirework> details = (List<GameCampaignTypeFirework>) model.getDetails();
                    for (GameCampaignTypeFirework detail : details) {
                        GameCampaignTypeFirework copy = new GameCampaignTypeFirework(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeFireworkService.saveBatch(list);
                }
                break;

                case REDUCE_RANK: {
                    List<GameCampaignTypeReduce> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeReduce> details = (List<GameCampaignTypeReduce>) model.getDetails();
                    for (GameCampaignTypeReduce detail : details) {
                        GameCampaignTypeReduce copy = new GameCampaignTypeReduce(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeReduceService.saveBatch(list);
                }
                break;

                case LIMIT_TIME_SWORD: {
                    List<GameCampaignTypeSword> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeSword> details = (List<GameCampaignTypeSword>) model.getDetails();
                    for (GameCampaignTypeSword detail : details) {
                        GameCampaignTypeSword copy = new GameCampaignTypeSword(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeSwordService.saveBatch(list);
                }
                break;

                case THROWING_EGGS: {
                    List<GameCampaignTypeThrowingEggs> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeThrowingEggs> details = (List<GameCampaignTypeThrowingEggs>) model.getDetails();
                    for (GameCampaignTypeThrowingEggs detail : details) {
                        GameCampaignTypeThrowingEggs copy = new GameCampaignTypeThrowingEggs(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeThrowingEggsService.saveBatch(list);
                }
                break;

                case THROWING_EGGS_RANK: {
                    List<GameCampaignTypeThrowingEggsRank> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeThrowingEggsRank> details = (List<GameCampaignTypeThrowingEggsRank>) model.getDetails();
                    for (GameCampaignTypeThrowingEggsRank detail : details) {
                        GameCampaignTypeThrowingEggsRank copy = new GameCampaignTypeThrowingEggsRank(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeThrowingEggsRankService.saveBatch(list);
                }
                break;

                case THROWING_EGGS_GIFT: {
                    List<GameCampaignTypeThrowingEggsGift> list = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeThrowingEggsGift> details = (List<GameCampaignTypeThrowingEggsGift>) model.getDetails();
                    for (GameCampaignTypeThrowingEggsGift detail : details) {
                        GameCampaignTypeThrowingEggsGift copy = new GameCampaignTypeThrowingEggsGift(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        list.add(copy);
                    }
                    campaignTypeThrowingEggsGiftService.saveBatch(list);
                }
                break;

                case PARTY: {
                    List<GameCampaignTypePartyTask> copyDetails = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypePartyTask> details = (List<GameCampaignTypePartyTask>) model.getDetails();
                    for (GameCampaignTypePartyTask detail : details) {
                        GameCampaignTypePartyTask copy = new GameCampaignTypePartyTask(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        copyDetails.add(copy);
                    }
                    campaignTypePartyTaskService.saveBatch(copyDetails);

                    if (CollUtil.isNotEmpty(model.getRewardList())) {
                        List<GameCampaignTypePartyProgress> copyRewards = new ArrayList<>(model.getRewardList().size());
                        List<GameCampaignTypePartyProgress> rewardList = (List<GameCampaignTypePartyProgress>) model.getRewardList();
                        for (GameCampaignTypePartyProgress reward : rewardList) {
                            GameCampaignTypePartyProgress copy = new GameCampaignTypePartyProgress(reward);
                            copy.setCampaignId(copyCampaignId);
                            copy.setTypeId(copyCampaignType.getId());
                            copyRewards.add(copy);
                        }
                        campaignTypePartyProgressService.saveBatch(copyRewards);
                    }
                }
                break;

                case DIRECT_PURCHASE: {
                    List<GameCampaignDirectPurchase> copyDetails = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignDirectPurchase> details = (List<GameCampaignDirectPurchase>) model.getDetails();
                    for (GameCampaignDirectPurchase directPurchase : details) {
                        GameCampaignDirectPurchase copy = new GameCampaignDirectPurchase(directPurchase);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        copyDetails.add(copy);
                    }
                    campaignDirectPurchaseService.saveBatch(copyDetails);
                }
                break;

                case REBATE_RECHARGE: {
                    List<GameCampaignTypeRebateRecharge> copyDetails = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeRebateRecharge> details = (List<GameCampaignTypeRebateRecharge>) model.getDetails();
                    for (GameCampaignTypeRebateRecharge rebateRecharge : details) {
                        GameCampaignTypeRebateRecharge copy = new GameCampaignTypeRebateRecharge(rebateRecharge);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        copyDetails.add(copy);
                    }
                    campaignTypeRebateRechargeService.saveBatch(copyDetails);
                }
                break;

                case MARRY_RANK_WINE:
                case MARRY_RANK_CHARM: {
                    List<GameCampaignTypeMarryRank> copyDetails = new ArrayList<>(model.getDetails().size());
                    List<GameCampaignTypeMarryRank> details = (List<GameCampaignTypeMarryRank>) model.getDetails();
                    for (GameCampaignTypeMarryRank detail : details) {
                        GameCampaignTypeMarryRank copy = new GameCampaignTypeMarryRank(detail);
                        copy.setCampaignId(copyCampaignId);
                        copy.setTypeId(copyCampaignType.getId());
                        copyDetails.add(copy);
                    }
                    gameCampaignTypeMarryRankService.saveBatch(copyDetails);

                    if (CollUtil.isNotEmpty(model.getRewardList())) {
                        List<GameCampaignTypeMarryRankReward> copyRewards = new ArrayList<>(model.getRewardList().size());
                        List<GameCampaignTypeMarryRankReward> rewardList = (List<GameCampaignTypeMarryRankReward>) model.getRewardList();
                        for (GameCampaignTypeMarryRankReward reward : rewardList) {
                            GameCampaignTypeMarryRankReward copy = new GameCampaignTypeMarryRankReward(reward);
                            copy.setCampaignId(copyCampaignId);
                            copy.setTypeId(copyCampaignType.getId());
                            copyRewards.add(copy);
                        }
                        gameCampaignTypeMarryRankRewardService.saveBatch(copyRewards);
                    }
                }
                break;

                case SELECT_DISCOUNT_ITEM:
                    if (CollUtil.isNotEmpty(model.getDetails())) {
                        List<GameCampaignTypeSelectDiscountItem> copyDetails = new ArrayList<>(model.getDetails().size());
                        List<GameCampaignTypeSelectDiscountItem> details = (List<GameCampaignTypeSelectDiscountItem>) model.getDetails();
                        for (GameCampaignTypeSelectDiscountItem detail : details) {
                            GameCampaignTypeSelectDiscountItem copy = new GameCampaignTypeSelectDiscountItem(detail);
                            copy.setCampaignId(copyCampaignId);
                            copy.setTypeId(copyCampaignType.getId());
                            copyDetails.add(copy);
                        }
                        gameCampaignTypeSelectDiscountItemService.saveBatch(copyDetails);
                    }

                    if (CollUtil.isNotEmpty(model.getRewardList())) {
                        List<GameCampaignTypeSelectDiscountMessage> copyRewards = new ArrayList<>(model.getRewardList().size());
                        List<GameCampaignTypeSelectDiscountMessage> rewardList = (List<GameCampaignTypeSelectDiscountMessage>) model.getRewardList();
                        for (GameCampaignTypeSelectDiscountMessage reward : rewardList) {
                            GameCampaignTypeSelectDiscountMessage copy = new GameCampaignTypeSelectDiscountMessage(reward);
                            copy.setCampaignId(copyCampaignId);
                            copy.setTypeId(copyCampaignType.getId());
                            copyRewards.add(copy);
                        }
                        gameCampaignTypeSelectDiscountMessageService.saveBatch(copyRewards);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
