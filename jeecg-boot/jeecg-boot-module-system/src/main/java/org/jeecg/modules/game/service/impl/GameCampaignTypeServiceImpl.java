package org.jeecg.modules. game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules. game.entity.GameCampaignType;
import org.jeecg.modules.game.mapper.GameCampaignTypeMapper;
import org.jeecg.modules. game.service.IGameCampaignTypeService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
@Service
public class GameCampaignTypeServiceImpl extends ServiceImpl<GameCampaignTypeMapper, GameCampaignType> implements IGameCampaignTypeService {

}
