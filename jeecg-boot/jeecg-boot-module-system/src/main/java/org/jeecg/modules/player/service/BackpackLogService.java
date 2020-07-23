package org.jeecg.modules.player.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.BackpackLog;

import java.util.Map;

/**
 * @ClassName BackpackLogService
 * @Author buliangliang
 * @Description 背包日志统计
 * @Date 2019/11/27 20:10
 * @Version 1.0
 */
public interface BackpackLogService extends IService<BackpackLog> {
    /**
     * 同步背包日志
     *
     * @param model
     * @param serverId
     * @param paramMap
     * @param syncTimeBegin
     * @param syncTimeEnd
     */
    void syncBackpackLog(BackpackLog model, int serverId, Map<String, String[]> paramMap, String syncTimeBegin, String syncTimeEnd);
}
