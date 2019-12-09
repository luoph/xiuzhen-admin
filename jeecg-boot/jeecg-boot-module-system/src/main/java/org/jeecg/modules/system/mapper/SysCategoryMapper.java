package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.model.TreeSelectModel;

import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 分类字典
 * @date 2019-05-29
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

    /**
     * 根据父级ID查询树节点数据
     *
     * @param pid
     * @return
     */
    List<TreeSelectModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

    @Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
    String queryIdByCode(@Param("code") String code);


}
