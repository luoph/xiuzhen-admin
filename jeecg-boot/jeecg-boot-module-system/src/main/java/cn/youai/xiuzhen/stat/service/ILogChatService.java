package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.LogChat;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 聊天日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
public interface ILogChatService extends IService<LogChat> {

    IPage<LogChat> selectList(Page<?> page, LogChat logChat, Date start, Date end);

}
