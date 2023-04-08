package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.entity.GameVps;
import cn.youai.xiuzhen.game.entity.GameVps;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
public interface IGameVpsService extends IService<GameVps> {

    IPage<GameVps> queryList(Page<GameVps> page,
                             GameVps entity,
                             DateRange createTimeRange);

}
