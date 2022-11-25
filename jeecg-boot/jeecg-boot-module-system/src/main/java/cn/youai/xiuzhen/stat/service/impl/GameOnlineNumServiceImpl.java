package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.basics.model.DataResponse;
import cn.youai.enums.OutdatedType;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.entity.GameOnlineNum;
import cn.youai.xiuzhen.stat.mapper.GameOnlineNumMapper;
import cn.youai.xiuzhen.stat.service.IGameOnlineNumService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
@Service
public class GameOnlineNumServiceImpl extends ServiceImpl<GameOnlineNumMapper, GameOnlineNum> implements IGameOnlineNumService {

    private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
    }.getType();

    @Value("${app.online-num-url:/game/onlineNum}")
    private String onlineNumUrl;

    @Autowired
    private IGameChannelServerService gameChannelServerService;
    @Autowired
    private IGameChannelService gameChannelService;
    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public void doGameOnlineNumSave() {
        try {
            List<GameChannelServer> list = gameChannelServerService.list();
            list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
            for (GameChannelServer gameChannelServer : list) {
                GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
                if (gameServer == null) {
                    continue;
                }

                GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
                // 获取服务器id和服务器名字
                Integer serverId = gameServer.getId();
                String channel = gameChannel.getSimpleName();

                if (gameServer.getOnlineStat() == 1 && gameServer.getOutdated() == OutdatedType.NORMAL.getValue()) {
                    // http调用查询在线人数
                    DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
                    GameOnlineNum gameOnlineNum = new GameOnlineNum();
                    // 不为空就保存数据
                    long data = 0;
                    if (response != null) {
                        // 获取在线人数
                        data = response.getData();
                    }
                    //http调用如果 response为空,查不到就设置为0
                    gameOnlineNum.setChannel(channel);
                    gameOnlineNum.setServerId(serverId);
                    gameOnlineNum.setOnlineNum(data);
                    Date now = DateUtils.now();
                    gameOnlineNum.setGetTime(now);
                    gameOnlineNum.setUpdateTime(now);
                    gameOnlineNum.setCreateTime(now);
                    save(gameOnlineNum);
                }
            }
        } catch (Exception e) {
            log.error("定时器调用GameOnlineNumJob: 查询在线人数异常", e);
        }
    }

    @Override
    public List<GameOnlineNum> queryGameOnlineNumByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        if (days == 0) {
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            return getBaseMapper().queryGameOnlineNumByRangDate(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
        }
        //如果有选天数,就使用就近天数查询
        //获取过去第几天的日期
        Date nowDate = new Date();
        Date pastDate = DateUtils.addDays(nowDate, days * (-1));
        return getBaseMapper().queryGameOnlineNumByRangDate(pastDate, nowDate, serverId, channel);
    }

    @Override
    public List<GameOnlineNum> queryGameOnlineCollectByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        if (days == 0) {
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            return getDataTreating(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
        }
        //如果有选天数,就使用就近天数查询
        //获取过去第几天的日期
        Date nowDate = new Date();
        Date pastDate = DateUtils.addDays(nowDate, days * (-1));
        return getDataTreating(pastDate, nowDate, serverId, channel);
    }


    /**
     * 获取数据处理
     */
    private List<GameOnlineNum> getDataTreating(Date rangeDateBeginTime, Date rangeDateEndTime, Integer serverId, String channel) {
        List<GameOnlineNum> gameOnlineNumList = getBaseMapper().queryGameOnlineCollectByRangDate(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
        for (GameOnlineNum gameOnlineNum : gameOnlineNumList) {
            Date getTime = gameOnlineNum.getGetTime();
            gameOnlineNum.setDau(logAccountService.queryDau(getTime));
        }
        return gameOnlineNumList;
    }
}
