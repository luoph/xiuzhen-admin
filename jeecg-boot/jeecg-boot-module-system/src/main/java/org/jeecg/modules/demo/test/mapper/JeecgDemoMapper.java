package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description jeecg 测试demo
 * @date 2018-12-29
 */
public interface JeecgDemoMapper extends BaseMapper<JeecgDemo> {

    List<JeecgDemo> getDemoByName(@Param("name") String name);

    /**
     * 查询列表数据 直接传数据权限的sql进行数据过滤
     *
     * @param page
     * @param permissionSql
     * @return
     */
    IPage<JeecgDemo> queryListWithPermission(Page<JeecgDemo> page, @Param("permissionSql") String permissionSql);

}
