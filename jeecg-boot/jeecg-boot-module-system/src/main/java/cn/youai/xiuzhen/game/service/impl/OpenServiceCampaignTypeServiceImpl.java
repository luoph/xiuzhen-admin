package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.constant.OpenServiceType;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignTypeMapper;
import cn.youai.xiuzhen.game.service.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-类型(2级)
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignTypeServiceImpl extends ServiceImpl<GameOpenServiceCampaignTypeMapper, OpenServiceCampaignType> implements IOpenServiceCampaignTypeService {

    @Autowired
    private IOpenServiceCampaignRankDetailService rankDetailService;
    @Autowired
    private IOpenServiceCampaignGiftDetailService giftDetailService;
    @Autowired
    private IOpenServiceCampaignSingleGiftDetailService singleGiftDetailService;
    @Autowired
    private IOpenServiceCampaignLotteryDetailService lotteryDetailService;
    @Autowired
    private IOpenServiceCampaignConsumeDetailService consumeDetailService;

    @Override
    @SuppressWarnings("unchecked")
    public void duplicate(OpenServiceCampaignType other, long campaignId) {
        OpenServiceCampaignType copy = new OpenServiceCampaignType(other);
        copy.setCampaignId(campaignId);
        save(copy);

        if (CollUtil.isNotEmpty(other.getDetails())) {
            OpenServiceType openServiceType = OpenServiceType.valueOf(other.getType());
            if (openServiceType != null) {
                switch (openServiceType) {
                    case RANK: {
                        List<OpenServiceCampaignRankDetail> list = (List<OpenServiceCampaignRankDetail>) other.getDetails();
                        if (CollUtil.isNotEmpty(list)) {
                            for (OpenServiceCampaignRankDetail entity : list) {
                                rankDetailService.duplicate(entity, copy.getId(), campaignId);
                            }
                        }
                    }
                    break;

                    case GIFT: {
                        List<OpenServiceCampaignGiftDetail> list = (List<OpenServiceCampaignGiftDetail>) other.getDetails();
                        if (CollUtil.isNotEmpty(list)) {
                            for (OpenServiceCampaignGiftDetail entity : list) {
                                giftDetailService.duplicate(entity, copy.getId(), campaignId);
                            }
                        }
                    }
                    break;

                    case RECHARGE: {
                        List<OpenServiceCampaignSingleGiftDetail> list = (List<OpenServiceCampaignSingleGiftDetail>) other.getDetails();
                        if (CollUtil.isNotEmpty(list)) {
                            for (OpenServiceCampaignSingleGiftDetail entity : list) {
                                singleGiftDetailService.duplicate(entity, copy.getId(), campaignId);
                            }
                        }
                    }
                    break;

                    case LOTTERY: {
                        List<OpenServiceCampaignLotteryDetail> list = (List<OpenServiceCampaignLotteryDetail>) other.getDetails();
                        if (CollUtil.isNotEmpty(list)) {
                            for (OpenServiceCampaignLotteryDetail entity : list) {
                                lotteryDetailService.duplicate(entity, copy.getId(), campaignId);
                            }
                        }
                    }
                    break;

                    case CONSUME: {
                        List<OpenServiceCampaignConsumeDetail> list = (List<OpenServiceCampaignConsumeDetail>) other.getDetails();
                        if (CollUtil.isNotEmpty(list)) {
                            for (OpenServiceCampaignConsumeDetail entity : list) {
                                consumeDetailService.duplicate(entity, copy.getId(), campaignId);
                            }
                        }
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void fillTabDetail(OpenServiceCampaignType model) {
        long campaignId = model.getCampaignId();
        OpenServiceType openServiceType = OpenServiceType.valueOf(model.getType());

        if (openServiceType != null) {
            switch (openServiceType) {
                case RANK: {
                    Wrapper<OpenServiceCampaignRankDetail> query = Wrappers.<OpenServiceCampaignRankDetail>lambdaQuery()
                            .eq(OpenServiceCampaignRankDetail::getCampaignId, campaignId)
                            .eq(OpenServiceCampaignRankDetail::getCampaignTypeId, model.getId())
                            .orderByAsc(OpenServiceCampaignRankDetail::getSort);
                    List<OpenServiceCampaignRankDetail> list = rankDetailService.list(query);
                    for (OpenServiceCampaignRankDetail detail : list) {
                        rankDetailService.fillDetail(detail);
                    }
                    model.setDetails(list);
                }
                break;

                case GIFT: {
                    Wrapper<OpenServiceCampaignGiftDetail> query = Wrappers.<OpenServiceCampaignGiftDetail>lambdaQuery()
                            .eq(OpenServiceCampaignGiftDetail::getCampaignId, campaignId)
                            .eq(OpenServiceCampaignGiftDetail::getCampaignTypeId, model.getId())
                            .orderByAsc(OpenServiceCampaignGiftDetail::getSort);
                    List<OpenServiceCampaignGiftDetail> list = giftDetailService.list(query);
                    for (OpenServiceCampaignGiftDetail detail : list) {
                        giftDetailService.fillDetail(detail);
                    }
                    model.setDetails(list);
                }
                break;

                case RECHARGE: {
                    Wrapper<OpenServiceCampaignSingleGiftDetail> query = Wrappers.<OpenServiceCampaignSingleGiftDetail>lambdaQuery()
                            .eq(OpenServiceCampaignSingleGiftDetail::getCampaignId, campaignId)
                            .eq(OpenServiceCampaignSingleGiftDetail::getCampaignTypeId, model.getId())
                            .orderByAsc(OpenServiceCampaignSingleGiftDetail::getSort);
                    List<OpenServiceCampaignSingleGiftDetail> list = singleGiftDetailService.list(query);
                    for (OpenServiceCampaignSingleGiftDetail detail : list) {
                        singleGiftDetailService.fillDetail(detail);
                    }
                    model.setDetails(list);
                }
                break;

                case LOTTERY: {
                    Wrapper<OpenServiceCampaignLotteryDetail> query = Wrappers.<OpenServiceCampaignLotteryDetail>lambdaQuery()
                            .eq(OpenServiceCampaignLotteryDetail::getCampaignId, campaignId)
                            .eq(OpenServiceCampaignLotteryDetail::getCampaignTypeId, model.getId());
                    List<OpenServiceCampaignLotteryDetail> list = lotteryDetailService.list(query);
                    for (OpenServiceCampaignLotteryDetail detail : list) {
                        lotteryDetailService.fillDetail(detail);
                    }
                    model.setDetails(list);
                }
                break;

                case CONSUME: {
                    Wrapper<OpenServiceCampaignConsumeDetail> query = Wrappers.<OpenServiceCampaignConsumeDetail>lambdaQuery()
                            .eq(OpenServiceCampaignConsumeDetail::getCampaignId, campaignId)
                            .eq(OpenServiceCampaignConsumeDetail::getCampaignTypeId, model.getId());
                    List<OpenServiceCampaignConsumeDetail> list = consumeDetailService.list(query);
                    for (OpenServiceCampaignConsumeDetail detail : list) {
                        consumeDetailService.fillDetail(detail);
                    }
                    model.setDetails(list);
                }
                break;

                default:
                    break;
            }
        }
    }

    @Override
    public List<OpenServiceCampaignType> selectTypeListByCampaignId(long campaignId) {
        Wrapper<OpenServiceCampaignType> query = Wrappers.<OpenServiceCampaignType>lambdaQuery().eq(OpenServiceCampaignType::getCampaignId, campaignId)
                .orderByAsc(OpenServiceCampaignType::getSort);
        return list(query);
    }
}
