package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.monitor.ServerMonitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
public interface ServerMonitorMapper extends BaseMapper<ServerMonitor> {

    List<ServerMonitor> queryList();

}
