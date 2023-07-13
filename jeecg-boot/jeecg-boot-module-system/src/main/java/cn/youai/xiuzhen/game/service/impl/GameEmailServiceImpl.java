package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.model.ResponseCode;
import cn.youai.basics.utils.StringUtils;
import cn.youai.entities.Mail;
import cn.youai.enums.MailReceiver;
import cn.youai.enums.MailType;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ItemVO;
import cn.youai.xiuzhen.game.entity.GameEmail;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.mapper.GameEmailMapper;
import cn.youai.xiuzhen.game.service.IGameEmailService;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.dreamlu.mica.core.utils.$;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
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
@DS("master")
public class GameEmailServiceImpl extends ServiceImpl<GameEmailMapper, GameEmail> implements IGameEmailService {

    @Value("${app.send-mail-url}")
    private String sendMailUrl;

    @Value("${app.sync-mail-url}")
    private String syncMailUrl;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public Response saveEmail(GameEmail entity, String username) {
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
        } else if (entity.getType() == MailType.NO_ATTACHMENT.getType()) {
            entity.setContent(StringUtils.EMPTY);
        }

        Set<String> receiverIdSet = StringUtils.split2Set(entity.getReceiverIds());
        if (CollUtil.isEmpty(receiverIdSet)) {
            response.setFailure("接受者未设置！");
            return response;
        }

        if (StringUtils.isNotEmpty(username)) {
            SysUser sysUser = sysUserService.getUserByName(username);
            if (sysUser != null && (StringUtils.isNotEmpty(sysUser.getChannel()) || StringUtils.isNotEmpty(sysUser.getSdkChannel()))) {
                if (entity.getReceiverType() == 1) {
                    List<GamePlayer> gamePlayers = gamePlayerService.selectPlayerListByUser(sysUser, entity.getReceiverIds());
                    if (CollUtil.size(gamePlayers) != CollUtil.size(receiverIdSet)) {
                        response.setFailure("没有权限读取玩家信息！");
                        return response;
                    }
                } else if (entity.getReceiverType() == 2) {
                    List<GameServer> gameServers = gameServerService.selectChannelServerListByUser(sysUser, entity.getReceiverIds());
                    if (CollUtil.size(gameServers) != CollUtil.size(receiverIdSet)) {
                        response.setFailure("没有权限读取区服信息！");
                        return response;
                    }
                }
            }
        }

        if (save(entity)) {
            response.setSuccess("创建成功！");
        } else {
            response.setFailure("创建失败！");
        }
        return response;
    }

    @Override
    public Response sendEmail(GameEmail entity, String username) {
        Response response = new Response();
        if (entity == null || entity.getState() == 1) {
            response.setErrorCode(ResponseCode.FAILURE);
            return response;
        }

        Mail mail = new Mail();
        $.copy(entity, mail);
        mail.setSourceId(entity.getId());

        Integer receiverType = entity.getReceiverType();
        if (receiverType == MailReceiver.PLAYER.getType()) {
            List<Long> playerIds = StringUtils.split2Long(entity.getReceiverIds());
            List<GamePlayer> playerList = gamePlayerService.queryPlayerList(playerIds);

            Set<Integer> serverIds = new HashSet<>();
            playerList.forEach(t -> {
                mail.setReceiverId(t.getPlayerId());
                gameServerService.postUrl(CollUtil.newArrayList(t.getServerId()), sendMailUrl, mail);
                serverIds.add(t.getServerId());
            });

            gameServerService.getUrl(serverIds, syncMailUrl);
//            gameServerService.cleanCache(serverIds, "PlayerMailCache");
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
                    gameServerService.postUrl(CollUtil.newArrayList(serverId), sendMailUrl, mail);
                }
                gameServerService.getUrl(affectedServerIds, syncMailUrl);
//                gameServerService.cleanCache(affectedServerIds, "PlayerMailCache");
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
