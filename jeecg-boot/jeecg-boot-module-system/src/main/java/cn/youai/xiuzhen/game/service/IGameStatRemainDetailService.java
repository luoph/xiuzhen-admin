/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.game.entity.GameStatRemainDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 留存统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface IGameStatRemainDetailService extends IService<GameStatRemainDetail> {

    void calcRemainDetailStat(RoleType roleType, Collection<Integer> serverIds,
                              Date registerDate, int days, boolean updateAll);
}
