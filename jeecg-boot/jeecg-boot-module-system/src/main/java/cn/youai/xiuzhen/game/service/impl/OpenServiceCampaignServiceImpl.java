package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
@Service
@DS("master")
public class OpenServiceCampaignServiceImpl extends ServiceImpl<GameOpenServiceCampaignMapper, OpenServiceCampaign> implements IOpenServiceCampaignService {

    @Override
    public void duplicate(OpenServiceCampaign other) {

    }

}
