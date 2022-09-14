package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameStatOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_stat_order
 * @date 2021-04-20
 */
public interface IGameStatOrderService extends IService<GameStatOrder> {

    /**
     * 查询充值统计列表
     *
     * @param gameStatOrder 查询条件
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @return 结果
     */
    IPage<GameStatOrder> queryGameStatOrderList(GameStatOrder gameStatOrder, Integer pageNo, Integer pageSize);
}
