/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 每日数据统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
public interface IGameStatDailyService extends IService<GameStatDaily> {

    int updateOrInsert(List<GameStatDaily> list);

    GameStatDaily getGameStatDaily(String channel, Integer serverId, Date date);
}
