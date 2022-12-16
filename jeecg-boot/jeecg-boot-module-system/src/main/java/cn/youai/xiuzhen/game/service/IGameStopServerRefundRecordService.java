/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameStopServerRefundRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 删档返还记录 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2022-12-05
 */
public interface IGameStopServerRefundRecordService extends IService<GameStopServerRefundRecord> {

    void checkSendStopServerRefund();

}
