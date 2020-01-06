package org.jeecg.modules.game.service.impl;

import cn.hutool.json.JSONTokener;
import cn.youai.commons.model.Response;
import cn.youai.xiuzhen.entity.pojo.ItemVo;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private static final int EMAIL_CONTENT_TYPE = 2;

    @Value("${app.url.gamecenter}")
    private String gameCenterUrl;

    @Value("${app.send-email-path}")
    private String path;

    @Override
    public Response saveEmail(GameEmail gameEmail) {
        Response response = new Response();
        Integer emailType = gameEmail.getEmailType();
        if (emailType == EMAIL_CONTENT_TYPE) {
            String content = gameEmail.getContent();
            if (content == null) {
                response.setFailure("附件内容不能为空！");
                return response;
            }
            Object typeObject = new JSONTokener(content).nextValue();
            if (!(typeObject instanceof cn.hutool.json.JSONArray)) {
                response.setFailure("附件格式错误！");
                return response;
            }
            List<ItemVo> list = JSONArray.parseArray(content, ItemVo.class);
            if (list == null || list.size() == 0) {
                response.setFailure("附件格式错误！");
                return response;
            }
        }
        boolean state = super.save(gameEmail);
        if (state) {
            sendEmailToGameCenterServer(gameEmail);
            return response;
        }
        response.setFailure("发送失败！");
        return response;
    }

    private void sendEmailToGameCenterServer(GameEmail gameEmail) {
        OkHttpHelper.post(gameCenterUrl + path, gameEmail);
    }
}
