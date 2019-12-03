package org.jeecg.common.system.api;

import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20
 * @Version:V1.0
 */
public interface ISysBaseAPI {

    /**
     * 日志添加
     *
     * @param logContent  内容
     * @param logType     日志类型(0:操作日志;1:登录日志;2:定时任务)
     * @param operateType 操作类型(1:添加;2:修改;3:删除;)
     */
    void addLog(String logContent, Integer logType, Integer operateType);

    /**
     * 根据用户账号查询用户信息
     *
     * @param username
     * @return
     */
    LoginUser getUserByName(String username);

    /**
     * 根据用户id查询用户信息
     *
     * @param id
     * @return
     */
    LoginUser getUserById(String id);

    /**
     * 通过用户账号查询角色集合
     *
     * @param username
     * @return
     */
    List<String> getRolesByUsername(String username);

    /**
     * 通过用户账号查询部门集合
     *
     * @param username
     * @return 部门 id
     */
    List<String> getDepartIdsByUsername(String username);

    /**
     * 通过用户账号查询部门 name
     *
     * @param username
     * @return 部门 name
     */
    List<String> getDepartNamesByUsername(String username);

    /**
     * 获取当前数据库类型
     *
     * @return
     * @throws Exception
     */
    String getDatabaseType() throws SQLException;

    /**
     * 获取数据字典
     *
     * @param code
     * @return
     */
    List<DictModel> queryDictItemsByCode(String code);

    /**
     * 查询所有的父级字典，按照create_time排序
     */
    List<DictModel> queryAllDict();

    /**
     * 获取表数据字典
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 发送系统消息
     *
     * @param fromUser   发送人(用户登录账户)
     * @param toUser     发送给(用户登录账户)
     * @param title      消息主题
     * @param msgContent 消息内容
     */
    void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent);

    /**
     * 查询表字典 支持过滤数据
     *
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 获取所有有效用户
     *
     * @return
     */
    List<ComboModel> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     *
     * @return
     */
    List<ComboModel> queryAllUser(String[] userIds);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<ComboModel> queryAllRole();

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     *
     * @return
     */
    List<ComboModel> queryAllRole(String[] roleIds);

    /**
     * 通过用户账号查询角色Id集合
     *
     * @param username
     * @return
     */
    List<String> getRoleIdsByUsername(String username);

    /**
     * 通过部门编号查询部门id
     *
     * @param orgCode
     * @return
     */
    String getDepartIdsByOrgCode(String orgCode);

    /**
     * 查询上一级部门
     *
     * @param departId
     * @return
     */
    DictModel getParentDepartId(String departId);

    /**
     * 查询所有部门
     *
     * @return
     */
    List<SysDepartModel> getAllSysDepart();

}
