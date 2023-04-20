package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.mapper.ServerMonitorMapper;
import cn.youai.xiuzhen.game.monitor.ServerMonitor;
import cn.youai.xiuzhen.game.service.IServerMonitorService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
@Service
@DS("wgcloud")
public class ServerMonitorServiceImpl extends ServiceImpl<ServerMonitorMapper, ServerMonitor> implements IServerMonitorService {

    @Override
    public List<ServerMonitor> queryList() {
        return getBaseMapper().queryList();
    }

}
