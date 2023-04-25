package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.LogChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 聊天日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
public interface LogChatMapper extends BaseMapper<LogChat> {

    IPage<LogChat> queryList(Page<?> page, @Param("entity") LogChat entity, @Param("dateRange") DateRange dateRange, @Param("configAuth") String configAuth);

}
