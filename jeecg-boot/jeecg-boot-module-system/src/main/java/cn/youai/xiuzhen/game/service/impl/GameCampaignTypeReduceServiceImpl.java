package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeReduce;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeReduceMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeReduceService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_campaign_type_reduce
 * @date 2021-02-02
 */
@Service
@DS("master")
public class GameCampaignTypeReduceServiceImpl extends ServiceImpl<GameCampaignTypeReduceMapper, GameCampaignTypeReduce> implements IGameCampaignTypeReduceService {

}
