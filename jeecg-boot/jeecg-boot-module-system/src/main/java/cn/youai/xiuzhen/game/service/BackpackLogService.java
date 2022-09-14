package cn.youai.xiuzhen.game.service;


import cn.youai.basics.model.ResponseCode;
import cn.youai.xiuzhen.game.entity.BackpackLog;
import com.baomidou.mybatisplus.extension.service.IService;

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
    ResponseCode syncBackpackLog(BackpackLog model, long playerId, int serverId, Map<String, String[]> paramMap, String syncTimeBegin, String syncTimeEnd);
}
