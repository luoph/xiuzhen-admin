package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.model.ResponseCode;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ItemVO;
import cn.youai.server.springboot.component.OkHttpHelper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.jeecg.modules.utils.GameConfigUtils;
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
     * 有附件的邮件 1无附件 2有附件
     */
    private static final int EMAIL_CONTENT_TYPE = 2;

    /**
     * 目标类型 1玩家 2全服
     */
    private static final int TARGET_BODY_TYPE = 1;

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @Value("${app.send-email-path}")
    private String path;

    @Override
    public Response saveEmail(GameEmail gameEmail) {
        Response response = new Response();
        Integer emailType = gameEmail.getEmailType();
        if (emailType == EMAIL_CONTENT_TYPE) {
            String content = gameEmail.getContent();
            if (StringUtils.isBlank(content)) {
                response.setFailure("附件内容不能为空！");
                return response;
            }

            List<ItemVO> list = JSON.parseArray(content, ItemVO.class);
            if (CollUtil.isEmpty(list)) {
                response.setFailure("附件格式错误！");
                return response;
            }
        }

        String[] targetBodyStr = StringUtils.split(gameEmail.getTargetBodyIds(), ",");
        if (targetBodyStr == null || targetBodyStr.length <= 0) {
            response.setFailure("投放目标不存在！");
            return response;
        }
        if (super.save(gameEmail)) {
            return response;
        }
        response.setFailure("创建失败！");
        return response;
    }

    @Override
    public Response sendEmailToGameCenterServer(GameEmail gameEmail) {
        Response response = new Response();
        if (gameEmail == null) {
            response.setErrorCode(ResponseCode.FAILURE);
            return response;
        }
        OkHttpHelper.post(gameCenterUrl + path, gameEmail);
        response.setErrorCode(ResponseCode.SUCCESS);
        return response;
    }

    @Override
    public List<ConfItem> itemTree(Integer itemId, String itemName) {
        if (itemId != null) {
            ConfItem confItem = GameConfigUtils.getItemById(itemId);
            if (confItem != null) {
                return CollUtil.newArrayList(confItem);
            }
            return CollUtil.newArrayList();
        } else if (itemName != null) {
            return GameConfigUtils.getItemListByName(itemName);
        }
        return CollUtil.newArrayList();
    }
}
