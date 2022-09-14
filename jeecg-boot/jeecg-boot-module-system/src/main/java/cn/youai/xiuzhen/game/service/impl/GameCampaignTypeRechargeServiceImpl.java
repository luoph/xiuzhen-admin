package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeRecharge;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeRechargeMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeRechargeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值活动
 * @date 2020-10-15
 */
@Service
@DS("master")
public class GameCampaignTypeRechargeServiceImpl extends ServiceImpl<GameCampaignTypeRechargeMapper, GameCampaignTypeRecharge> implements IGameCampaignTypeRechargeService {

}
