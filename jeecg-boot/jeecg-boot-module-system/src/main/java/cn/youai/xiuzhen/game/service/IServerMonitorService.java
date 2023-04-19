package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.monitor.ServerMonitor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 服务器监控信息
 * @date 2023-04-08
 */
public interface IServerMonitorService extends IService<ServerMonitor> {

    List<ServerMonitor> queryList();

}
