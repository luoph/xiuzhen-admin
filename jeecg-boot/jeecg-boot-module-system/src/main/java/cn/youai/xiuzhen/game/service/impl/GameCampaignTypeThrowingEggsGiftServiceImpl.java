package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeThrowingEggsGift;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeThrowingEggsGiftMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeThrowingEggsGiftService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋礼包
 * @date 2021-03-10
 */
@Service
@DS("master")
public class GameCampaignTypeThrowingEggsGiftServiceImpl extends ServiceImpl<GameCampaignTypeThrowingEggsGiftMapper, GameCampaignTypeThrowingEggsGift> implements IGameCampaignTypeThrowingEggsGiftService {

}
