package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignSupport;
import cn.youai.xiuzhen.game.mapper.GameCampaignSupportMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignSupportService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动区服配置
 * @date 2020-10-15
 */
@Service
@DS("master")
public class GameCampaignSupportServiceImpl extends ServiceImpl<GameCampaignSupportMapper, GameCampaignSupport> implements IGameCampaignSupportService {

}
