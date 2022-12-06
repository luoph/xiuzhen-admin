package cn.youai.xiuzhen.stat.task;

import cn.youai.server.springboot.utils.SpringContextUtils;
import cn.youai.server.task.AbstractWorker;
import cn.youai.server.task.ITask;
import cn.youai.server.task.TaskExecutor;
import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;

/**
 * 删档返还
 *
 * @author ocean
 * @since 202212131651
 */
public class GameStopServerRefundWork implements ITask {

    @Override
    public void setup() {
        int period = 60 * 10 * 1000;
        TaskExecutor.getInstance().createRepeatTask(period, period, new AbstractWorker() {
            @Override
            public void execute() {
                SpringContextUtils.getBean(IGameStopServerRefundRecordService.class).checkSendStopServerRefund();
            }
        });

    }
}
