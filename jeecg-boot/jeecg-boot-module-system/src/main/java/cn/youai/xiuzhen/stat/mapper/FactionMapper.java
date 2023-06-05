/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.Faction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 仙盟 Mapper 接口
 * </p>
 *
 * @author ocean
 * @since 2020-04-11
 */
public interface FactionMapper extends BaseMapper<Faction> {

    IPage<Faction> queryList(Page<Faction> page, @Param("entity") Faction entity);
}
