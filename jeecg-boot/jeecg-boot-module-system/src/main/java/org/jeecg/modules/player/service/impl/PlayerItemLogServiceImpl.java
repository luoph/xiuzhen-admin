package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.mapper.PlayerItemLogMapper;
import org.jeecg.modules.player.service.IPlayerItemLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Service
public class PlayerItemLogServiceImpl extends ServiceImpl<PlayerItemLogMapper, PlayerItemLog> implements IPlayerItemLogService {

    @Override
    public PlayerItemLog writePlayerItemLog(long serverId, BackpackLog backpacklog) {
        return new PlayerItemLog().setServerId(serverId).setPlayerId(backpacklog.getPlayerId()).setType(backpacklog.getType())
                .setWay(backpacklog.getWay()).setSyncTime(backpacklog.getCreateDate()).setItemId(backpacklog.getItemId())
                .setBeforeNum(backpacklog.getBeforeNum()).setAfterNum(backpacklog.getAfterNum())
                .setNum(backpacklog.getNum()).setCreateTime(backpacklog.getCreateTime()).setUpdateTime(DateUtils.now());
    }

    @Override
    public void saveBatchLog(List<PlayerItemLog> logs) {
        DataSourceHelper.useDefaultDatabase();
        saveBatch(logs, 500);
    }
}
