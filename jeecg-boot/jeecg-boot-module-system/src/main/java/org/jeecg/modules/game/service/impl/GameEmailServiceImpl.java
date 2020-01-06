package org.jeecg.modules.game.service.impl;

import cn.hutool.json.JSONTokener;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;
import org.jeecg.modules.player.mapper.PlayerRegisterInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Service
public class GameEmailServiceImpl extends ServiceImpl<GameEmailMapper, GameEmail> implements IGameEmailService {

    /**
     * 有附件的邮件 1有附件 2无附件
     */
    private static final int EMAIL_CONTENT_TYPE = 1;

    /**
     * 目标类型 1玩家 2 全服
     */
    private static final int TARGET_BODY_TYPE = 1;

    @Resource
    private PlayerRegisterInfoMapper registerInfoMapper;

    @Override
    public boolean save(GameEmail gameEmail) {
        Integer emailType = gameEmail.getEmailType();
        if (emailType == EMAIL_CONTENT_TYPE) {
            String content = gameEmail.getContent();
            if (content == null) {
                return false;
            }
            Object typeObject = new JSONTokener(content).nextValue();
            if (!(typeObject instanceof cn.hutool.json.JSONArray)) {
                return false;
            }

        }
        return super.save(gameEmail);
    }

    private void sendEmail(GameEmail gameEmail) {
        Date date = new Date();
        Date sendTime = gameEmail.getSendTime();

        if (date.compareTo(sendTime) <= 0) {
            Integer targetBodyType = gameEmail.getTargetBodyType();
            if (targetBodyType == TARGET_BODY_TYPE) {
                Integer targetBodyId = gameEmail.getTargetBodyId();
                PlayerRegisterInfo registerInfo = getPlayerRegisterInfo(targetBodyId);
            }


        }
    }

    public void toPlayer(PlayerRegisterInfo registerInfo, GameEmail gameEmail) {
        JSONArray jsonStr = JSONArray.parseArray(gameEmail.getContent());
        OkHttpHelper.post("", jsonStr);
    }

    public void toGameServer(GameEmail gameEmail) {
        JSONArray jsonStr = JSONArray.parseArray(gameEmail.getContent());
        OkHttpHelper.post("", jsonStr);
    }


    private PlayerRegisterInfo getPlayerRegisterInfo(long playerId) {
        QueryWrapper<PlayerRegisterInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PlayerRegisterInfo::getPlayerId, playerId);
        return registerInfoMapper.selectOne(queryWrapper);
    }
}
