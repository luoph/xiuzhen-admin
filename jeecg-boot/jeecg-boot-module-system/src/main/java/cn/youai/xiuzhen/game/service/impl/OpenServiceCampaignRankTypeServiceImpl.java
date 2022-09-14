package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankType;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignRankTypeMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankTypeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-类型库
 * @date 2020-12-21
 */
@Service
@DS("master")
public class OpenServiceCampaignRankTypeServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankTypeMapper, OpenServiceCampaignRankType> implements IOpenServiceCampaignRankTypeService {

    @Override
    public void duplicate(OpenServiceCampaignRankType other, long campaignId) {
    }
}
