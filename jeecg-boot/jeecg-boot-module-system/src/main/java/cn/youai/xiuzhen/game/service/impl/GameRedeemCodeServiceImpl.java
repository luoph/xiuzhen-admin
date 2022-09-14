package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameRedeemCode;
import cn.youai.xiuzhen.game.mapper.GameRedeemCodeMapper;
import cn.youai.xiuzhen.game.service.IGameRedeemCodeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
@Service
@DS("master")
public class GameRedeemCodeServiceImpl extends ServiceImpl<GameRedeemCodeMapper, GameRedeemCode> implements IGameRedeemCodeService {

}
