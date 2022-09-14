package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeExchange;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeExchangeMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeExchangeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 兑换活动
 * @date 2020-10-15
 */
@Service
@DS("master")
public class GameCampaignTypeExchangeServiceImpl extends ServiceImpl<GameCampaignTypeExchangeMapper, GameCampaignTypeExchange> implements IGameCampaignTypeExchangeService {

}
