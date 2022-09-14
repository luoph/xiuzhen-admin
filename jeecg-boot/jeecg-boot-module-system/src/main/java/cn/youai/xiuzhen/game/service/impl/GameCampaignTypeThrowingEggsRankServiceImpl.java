package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeThrowingEggsRank;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeThrowingEggsRankMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeThrowingEggsRankService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋榜
 * @date 2021-03-10
 */
@Service
@DS("master")
public class GameCampaignTypeThrowingEggsRankServiceImpl extends ServiceImpl<GameCampaignTypeThrowingEggsRankMapper, GameCampaignTypeThrowingEggsRank> implements IGameCampaignTypeThrowingEggsRankService {

}
