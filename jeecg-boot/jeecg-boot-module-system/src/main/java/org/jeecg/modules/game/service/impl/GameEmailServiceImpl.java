package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.model.ResponseCode;
import cn.youai.basics.utils.StringUtils;
import cn.youai.entities.GamePlayer;
import cn.youai.entities.Mail;
import cn.youai.enums.MailReceiver;
import cn.youai.enums.MailType;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ItemVO;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.dreamlu.mica.core.utils.$;
import org.jeecg.modules.game.entity.GameEmail;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameEmailMapper;
import org.jeecg.modules.game.service.IGameEmailService;
import org.jeecg.modules.game.service.IGamePlayerService;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Service
public class GameEmailServiceImpl extends ServiceImpl<GameEmailMapper, GameEmail> implements IGameEmailService {

    @Value("${app.send-mail-url}")
    private String sendMailUrl;

    @Value("${app.sync-mail-url}")
    private String syncMailUrl;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public Response saveEmail(GameEmail entity) {
        Response response = new Response();
        if (entity.getType() == MailType.ATTACHMENT.getType()) {
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

        Set<String> receiverIdSet = StringUtils.split2Set(entity.getReceiverIds());
        if (CollUtil.isEmpty(receiverIdSet)) {
            response.setFailure("接受者未设置！");
            return response;
        }

        if (save(entity)) {
            response.setSuccess("创建成功！");
        } else {
            response.setFailure("创建失败！");
        }
        return response;
    }

    @Override
    public Response sendEmail(GameEmail entity) {
        Response response = new Response();
        if (entity == null) {
            response.setErrorCode(ResponseCode.FAILURE);
            return response;
        }

        Mail mail = new Mail();
        $.copy(entity, mail);
        mail.setSourceId(entity.getId());

        Integer receiverType = entity.getReceiverType();
        if (receiverType == MailReceiver.PLAYER.getType()) {
            List<Long> playerIds = StringUtils.split2Long(entity.getReceiverIds());
            List<GamePlayer> playerList = gamePlayerService.getPlayerList(playerIds);

            Set<Integer> serverIds = new HashSet<>();
            playerList.forEach(t -> {
                mail.setReceiverId(t.getPlayerId());
                gameServerService.gameServerPost(CollUtil.newArrayList(t.getServerId()), sendMailUrl, mail);
                serverIds.add(t.getServerId());
            });

            gameServerService.gameServerGet(serverIds, syncMailUrl);
        } else if (receiverType == MailReceiver.SERVER.getType()) {
            List<Integer> serverIds = StringUtils.split2Int(entity.getReceiverIds());
            // 此处需要处理已合服的情况
            Set<Integer> affectedServerIds = new HashSet<>();
            for (Integer server : serverIds) {
                GameServer gameServer = gameServerService.getById(server);
                if (gameServer != null) {
                    if (gameServer.getPid() != null && gameServer.getPid() > 0) {
                        GameServer parent = gameServerService.getById(gameServer.getPid());
                        if (parent != null) {
                            affectedServerIds.add(parent.getId());
                        }
                    } else {
                        affectedServerIds.add(gameServer.getId());
                    }
                }
            }

            if (CollUtil.isNotEmpty(affectedServerIds)) {
                for (Integer serverId : affectedServerIds) {
                    mail.setReceiverId((long) serverId);
                    gameServerService.gameServerPost(CollUtil.newArrayList(serverId), sendMailUrl, mail);
                }
                gameServerService.gameServerGet(affectedServerIds, syncMailUrl);
            }
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
