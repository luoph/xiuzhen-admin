package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONTokener;
import cn.youai.commons.model.Response;
import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfItem;
import cn.youai.xiuzhen.entity.pojo.ItemVO;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ConfigDataService configDataService;

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
            try {
                Object typeObject = new JSONTokener(content).nextValue();
                if (!(typeObject instanceof cn.hutool.json.JSONArray)) {
                    response.setFailure("附件格式错误！");
                    return response;
                }
            } catch (Exception e) {
                log.error("gameEmail:{}" + gameEmail.toString(), e);
            }

            List<ItemVO> list = JSONArray.parseArray(content, ItemVO.class);
            if (CollUtil.isEmpty(list)) {
                response.setFailure("附件格式错误！");
                return response;
            }
        }

        List<Long> list = JSONArray.parseArray(gameEmail.getTargetBodyIds(), Long.class);
        if (CollUtil.isEmpty(list)) {
            response.setFailure("投放目标不存在！");
            return response;
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

    @Override
    public List<ConfItem> itemTree(Integer itemId, String itemName) {
        QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfItem.ITEM_ID)));
        if (itemId != null && itemName != null) {
            Equal<ConfItem, Integer> query1 = QueryFactory.equal(ConfItem.ITEM_ID, itemId);
            Equal<ConfItem, String> query2 = QueryFactory.equal(ConfItem.NAME, itemName);
            And<ConfItem> and = QueryFactory.and(query1, query2);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, and, queryOptions);
        } else if (itemId != null) {
            Equal<ConfItem, Integer> query1 = QueryFactory.equal(ConfItem.ITEM_ID, itemId);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, query1, queryOptions);
        } else if (itemName != null) {
            Equal<ConfItem, String> query2 = QueryFactory.equal(ConfItem.NAME, itemName);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, query2, queryOptions);
        } else {
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, queryOptions);
        }

    }
}
