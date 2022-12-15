package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.LogChat;
import cn.youai.xiuzhen.stat.mapper.LogChatMapper;
import cn.youai.xiuzhen.stat.service.ILogChatService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 聊天日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Service
@DS("shardingSphere")
public class LogChatServiceImpl extends ServiceImpl<LogChatMapper, LogChat> implements ILogChatService {

}
