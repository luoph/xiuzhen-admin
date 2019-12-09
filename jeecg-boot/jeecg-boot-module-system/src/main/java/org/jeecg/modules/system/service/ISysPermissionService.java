package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<TreeModel> queryListByParentId(String parentId);

    /**
     * 真实删除
     */
    void deletePermission(String id) throws JeecgBootException;

    /**
     * 逻辑删除
     */
    void deletePermissionLogical(String id) throws JeecgBootException;

    void addPermission(SysPermission sysPermission) throws JeecgBootException;

    void editPermission(SysPermission sysPermission) throws JeecgBootException;

    List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    List<String> queryPermissionUrlWithStar();
}
