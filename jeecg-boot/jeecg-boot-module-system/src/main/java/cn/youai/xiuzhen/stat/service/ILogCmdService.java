package cn.youai.xiuzhen.stat.service;

import cn.youai.log.LogCmd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: 接口日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
public interface ILogCmdService extends IService<LogCmd> {

    List<LogCmd> selectLogCmdList(int serverId, int costTime, Date date);

}
