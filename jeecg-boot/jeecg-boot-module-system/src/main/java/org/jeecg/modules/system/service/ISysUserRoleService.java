package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserRole;

import java.util.Map;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 查询所有的用户角色信息
     *
     * @return
     */
    Map<String, String> queryUserRole();
}
