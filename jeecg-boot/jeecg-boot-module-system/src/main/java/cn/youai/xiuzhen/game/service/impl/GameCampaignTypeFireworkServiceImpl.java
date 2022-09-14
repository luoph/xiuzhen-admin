package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeFirework;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeFireworkMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeFireworkService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日烟花
 * @date 2021-01-15
 */
@Service
@DS("master")
public class GameCampaignTypeFireworkServiceImpl extends ServiceImpl<GameCampaignTypeFireworkMapper, GameCampaignTypeFirework> implements IGameCampaignTypeFireworkService {

}
