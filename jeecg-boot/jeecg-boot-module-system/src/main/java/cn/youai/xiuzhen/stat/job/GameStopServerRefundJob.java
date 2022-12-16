package cn.youai.xiuzhen.stat.job;

import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 渠道删档测试仙玉返还
 *
 * @author ocean
 * @since 202212021758
 */
@Slf4j
public class GameStopServerRefundJob implements Job {

    @Autowired
    private IGameStopServerRefundRecordService gameStopServerRefundRecordService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        gameStopServerRefundRecordService.checkSendStopServerRefund();
    }
}
