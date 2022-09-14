package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypePartyProgress;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypePartyProgressMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypePartyProgressService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Service
@DS("master")
public class GameCampaignTypePartyProgressServiceImpl extends ServiceImpl<GameCampaignTypePartyProgressMapper, GameCampaignTypePartyProgress> implements IGameCampaignTypePartyProgressService {

}
