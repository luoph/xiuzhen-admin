/*
* create by mybatis-plus-generator  https://github.com/xiweile
*/
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameStatCmd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 接口调用超时统计 服务类
 * </p>
 *
 * @author luopeihuan
 * @since 2023-07-11
 */
public interface IGameStatCmdService extends IService<GameStatCmd> {

    void delete(int serverId, Date date);

}
