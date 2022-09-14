package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameRedeemActivityGroup;
import cn.youai.xiuzhen.game.mapper.GameRedeemActivityGroupMapper;
import cn.youai.xiuzhen.game.service.IGameRedeemActivityGroupService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码活动分组
 * @date 2020-01-07
 */
@Service
@DS("master")
public class GameRedeemActivityGroupServiceImpl extends ServiceImpl<GameRedeemActivityGroupMapper, GameRedeemActivityGroup> implements IGameRedeemActivityGroupService {

}
