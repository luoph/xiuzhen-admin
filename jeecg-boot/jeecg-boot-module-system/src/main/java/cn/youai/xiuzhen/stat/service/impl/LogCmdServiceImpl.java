package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.log.LogCmd;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameStatCmd;
import cn.youai.xiuzhen.game.service.IGameStatCmdService;
import cn.youai.xiuzhen.stat.mapper.LogCmdMapper;
import cn.youai.xiuzhen.stat.service.ILogCmdService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 接口日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Slf4j
@Service
@DS("shardingSphere")
public class LogCmdServiceImpl extends ServiceImpl<LogCmdMapper, LogCmd> implements ILogCmdService {

    @Autowired
    private IGameStatCmdService gameStatCmdService;

    private List<GameStatCmd> selectCmdList(int serverId, Date date, Date startTime, Date endTime, int costTime) {
        return getBaseMapper().selectCmdList(serverId, date, startTime, endTime, costTime);
    }

    @Override
    public void genReport(int serverId, Date date, int costTime) {
        String dateString = DateUtil.formatDate(date);
        log.info("start genReport, serverId:{}, date:{}", serverId, dateString);
        long time = System.currentTimeMillis();
        // 删除历史记录
        gameStatCmdService.delete(serverId, date);
        List<GameStatCmd> statCmds = selectCmdList(serverId, date, DateUtils.startTimeOfDate(date), DateUtils.endTimeOfDate(date), costTime);
        if (CollUtil.isNotEmpty(statCmds)) {
            gameStatCmdService.saveBatch(statCmds);
        }
        log.info("finish genReport, serverId:{}, date:{}, size:{}, cost:{}ms", serverId, dateString, statCmds.size(), (System.currentTimeMillis() - time));
    }

}
