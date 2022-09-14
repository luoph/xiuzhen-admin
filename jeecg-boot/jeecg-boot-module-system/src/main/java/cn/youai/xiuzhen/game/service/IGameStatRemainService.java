/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.game.entity.GameStatRemain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 留存统计
 * </p>
 *
 * @author luopeihuan
 * @since 2020-08-26
 */
public interface IGameStatRemainService extends IService<GameStatRemain> {

    void calcRemainStat(RoleType roleType, Collection<Integer> serverIds,
                        Date registerDate, int days, boolean updateAll);

}
