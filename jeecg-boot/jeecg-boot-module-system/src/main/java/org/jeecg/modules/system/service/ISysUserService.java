package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysUserSysDepartModel;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 重置密码
     *
     * @param username
     * @param oldpassword
     * @param newpassword
     * @param confirmpassword
     * @return
     */
    Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    Result<?> changePassword(SysUser sysUser);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    boolean deleteUser(String userId);

    /**
     * 批量删除用户
     *
     * @param userIds
     * @return
     */
    boolean deleteBatchUsers(String userIds);

    SysUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    void addUserWithRole(SysUser user, String roles);


    /**
     * 修改用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    void editUserWithRole(SysUser user, String roles);

    /**
     * 获取用户的授权角色
     *
     * @param username
     * @return
     */
    List<String> getRole(String username);

    /**
     * 查询用户信息包括 部门信息
     *
     * @param username
     * @return
     */
    SysUserCacheInfo getCacheUser(String username);

    /**
     * 根据部门Id查询
     *
     * @param
     * @return
     */
    IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page
     * @param departId
     * @param queryWrapper
     * @return
     */
    IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @param page       分页参数
     * @return
     */
    IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);

    /**
     * 根据角色Id查询
     *
     * @param
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    SysUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    SysUser getUserByEmail(String email);


    /**
     * 添加用户和用户部门关系
     *
     * @param user
     * @param selectedParts
     */
    void addUserWithDepart(SysUser user, String selectedParts);

    /**
     * 编辑用户和用户部门关系
     *
     * @param user
     * @param departs
     */
    void editUserWithDepart(SysUser user, String departs);

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);

}
