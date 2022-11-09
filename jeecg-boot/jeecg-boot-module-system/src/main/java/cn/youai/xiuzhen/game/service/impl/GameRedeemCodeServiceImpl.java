package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameRedeemCode;
import cn.youai.xiuzhen.game.mapper.GameRedeemCodeMapper;
import cn.youai.xiuzhen.game.service.IGameRedeemCodeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
@Service
@DS("master")
public class GameRedeemCodeServiceImpl extends ServiceImpl<GameRedeemCodeMapper, GameRedeemCode> implements IGameRedeemCodeService {

    @Override
    public List<String> selectExistsRedeemCode() {
        List<GameRedeemCode> list = list(Wrappers.<GameRedeemCode>lambdaQuery().select(GameRedeemCode::getCode));
        List<String> result = new ArrayList<>(list.size());
        list.forEach(t -> {
            if (StringUtils.isNotEmpty(t.getCode())) {
                result.add(t.getCode());
            }
        });
        return result;
    }

}
