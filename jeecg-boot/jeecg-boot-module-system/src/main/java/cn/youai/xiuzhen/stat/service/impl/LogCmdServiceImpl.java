package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.log.LogCmd;
import cn.youai.xiuzhen.stat.mapper.LogCmdMapper;
import cn.youai.xiuzhen.stat.service.ILogCmdService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 接口日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Service
@DS("shardingSphere")
public class LogCmdServiceImpl extends ServiceImpl<LogCmdMapper, LogCmd> implements ILogCmdService {

    @Override
    public List<LogCmd> selectLogCmdList(int serverId, int costTime, Date date) {
        return getBaseMapper().selectLogCmdList(costTime, date);
    }

}
