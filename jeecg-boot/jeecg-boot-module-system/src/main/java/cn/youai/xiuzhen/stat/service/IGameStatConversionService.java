/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatConversion;
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
public interface IGameStatConversionService extends IService<GameStatConversion> {

    int updateOrInsert(List<GameStatConversion> list);

    GameStatConversion getGameStatConversion(String channel, String sdkChannel, Integer serverId, Date date);
}
