package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeThrowingEggs;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeThrowingEggsMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeThrowingEggsService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日砸蛋
 * @date 2021-03-10
 */
@Service
@DS("master")
public class GameCampaignTypeThrowingEggsServiceImpl extends ServiceImpl<GameCampaignTypeThrowingEggsMapper, GameCampaignTypeThrowingEggs> implements IGameCampaignTypeThrowingEggsService {

}
