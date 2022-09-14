/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * LTV统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface IGameStatLtvDetailService extends IService<GameStatLtvDetail> {

    void calcLtvDetailStat(Set<Integer> keySet, Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll);

}
