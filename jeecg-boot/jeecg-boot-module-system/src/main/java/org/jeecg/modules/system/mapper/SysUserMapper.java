package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysUserSysDepartModel;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户账号查询用户信息
     *
     * @param username
     * @return
     */
    SysUser getUserByName(@Param("username") String username);

    /**
     * 根据部门Id查询用户信息
     *
     * @param page
     * @param departId
     * @return
     */
    IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

    /**
     * 根据角色Id查询用户信息
     *
     * @param page
     * @param
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param departId
     */
    void updateUserDepart(@Param("username") String username, @Param("orgCode") String orgCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    SysUser getUserByPhone(@Param("phone") String phone);


    /**
     * 根据邮箱查询用户信息
     *
     * @param email
     * @return
     */
    SysUser getUserByEmail(@Param("email") String email);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param page       分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
    List<SysUserSysDepartModel> getUserByOrgCode(IPage page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);


    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

}
