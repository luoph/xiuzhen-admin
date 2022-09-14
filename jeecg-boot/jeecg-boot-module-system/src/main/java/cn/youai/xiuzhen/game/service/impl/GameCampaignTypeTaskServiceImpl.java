package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeTask;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeTaskMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeTaskService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Service
@DS("master")
public class GameCampaignTypeTaskServiceImpl extends ServiceImpl<GameCampaignTypeTaskMapper, GameCampaignTypeTask> implements IGameCampaignTypeTaskService {

}
