package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.model.ResponseCode;
import cn.youai.basics.utils.StringUtils;
import cn.youai.entities.GamePlayer;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ItemVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.constant.EmailReceiver;
import org.jeecg.modules.game.constant.EmailType;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.jeecg.modules.game.service.IGamePlayerService;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Service
public class GameEmailServiceImpl extends ServiceImpl<GameEmailMapper, GameEmail> implements IGameEmailService {

    @Value("${app.send-email-url}")
    private String sendMailUrl;

    @Value("${app.sync-email-url}")
    private String syncMailUrl;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public Response saveEmail(GameEmail entity) {
        Response response = new Response();
        if (entity.getEmailType() == EmailType.ATTACHMENT.getType()) {
            String content = entity.getContent();
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

        Set<String> targetBodyIdSet = StringUtils.split2Set(entity.getTargetBodyIds());
        if (CollUtil.isEmpty(targetBodyIdSet)) {
            response.setFailure("投放目标不存在！");
            return response;
        }

        if (super.save(entity)) {
            return response;
        }
        response.setFailure("创建失败！");
        return response;
    }

    @Override
    public Response dispatchEmail(GameEmail entity) {
        Response response = new Response();
        if (entity == null) {
            response.setErrorCode(ResponseCode.FAILURE);
            return response;
        }

        GameEmail copy = new GameEmail();
        BeanUtils.copyProperties(entity, copy);

        Integer targetBodyType = entity.getTargetBodyType();
        if (targetBodyType == EmailReceiver.PLAYER.getType()) {
            List<Long> playerIds = StringUtils.split2Long(entity.getTargetBodyIds());
            List<GamePlayer> playerList = gamePlayerService.getPlayerList(playerIds);

            Set<Integer> serverIds = new HashSet<>();
            playerList.forEach(t -> {
                copy.setTargetBodyId(t.getPlayerId());

                gameServerService.gameServerPost(CollUtil.newArrayList(t.getServerId()), sendMailUrl, copy);
                serverIds.add(t.getServerId());
            });

            gameServerService.gameServerGet(serverIds, syncMailUrl);
        } else if (targetBodyType == EmailReceiver.SERVER.getType()) {
            int[] serverIds = StrUtil.splitToInt(entity.getTargetBodyIds(), ",");
            for (int server : serverIds) {
                copy.setTargetBodyId((long) server);
                gameServerService.gameServerPost(CollUtil.newArrayList(server), sendMailUrl, copy);
            }

            gameServerService.gameServerGet(serverIds, syncMailUrl);
        }

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
