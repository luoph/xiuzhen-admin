package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GamePlayerBanInfo;
import cn.youai.xiuzhen.game.mapper.GamePlayerBanInfoMapper;
import cn.youai.xiuzhen.game.service.IPlayerBanInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 封禁管理
 * @date 2020-10-22
 */
@Service
public class PlayerBanInfoServiceImpl extends ServiceImpl<GamePlayerBanInfoMapper, GamePlayerBanInfo> implements IPlayerBanInfoService {

}
