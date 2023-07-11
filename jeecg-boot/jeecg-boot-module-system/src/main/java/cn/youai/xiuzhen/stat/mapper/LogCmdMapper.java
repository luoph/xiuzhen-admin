package cn.youai.xiuzhen.stat.mapper;

import cn.youai.log.LogCmd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * @Description: 接口日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
public interface LogCmdMapper extends BaseMapper<LogCmd> {

    List<LogCmd> selectLogCmdList(int costTime, Date date);

}
