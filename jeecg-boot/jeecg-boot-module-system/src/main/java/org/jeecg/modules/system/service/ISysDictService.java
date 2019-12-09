package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.TreeSelectModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictService extends IService<SysDict> {


    List<DictModel> queryDictItemsByCode(String code);

    /**
     * 查询 dictCode，生成选项数组
     *
     * @param table 表名
     * @param text  文本显示字段
     * @param code  值字段
     * @return {@linkplain DictModel}选项数组
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询 dictCode，生成选项数组
     *
     * @param table     表名
     * @param text      文本显示字段
     * @param code      值字段
     * @param filterSql 过滤条件
     * @return {@linkplain DictModel}选项数组
     */
    List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

    String queryDictTextByKey(String code, String key);

    String queryTableDictTextByKey(String table, String text, String code, String key);

    List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict
     * @return
     */
    boolean deleteByDictId(SysDict sysDict);

    /**
     * 添加一对多
     */
    void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有用户  作为字典信息 username -->value,realname -->text
     *
     * @return
     */
    List<DictModel> queryAllUserBackDictModel();

    /**
     * 通过关键字查询字典表
     *
     * @param table   表名
     * @param text    文本显示字段
     * @param code    值字段
     * @param keyword 关键字
     * @return
     */
    List<DictModel> queryTableDictItems(String table, String text, String code, String keyword);

    /**
     * 根据表名、显示字段名、存储字段名 查询树
     *
     * @param table
     * @param text
     * @param code
     * @param pidField
     * @param pid
     * @param hasChildField
     * @return
     */
    List<TreeSelectModel> queryTreeList(Map<String, String> query, String table, String text, String code, String pidField, String pid, String hasChildField);

    /**
     * 真实删除
     *
     * @param id
     */
    public void deleteOneDictPhysically(String id);

    /**
     * 修改delFlag
     *
     * @param delFlag
     * @param id
     */
    public void updateDictDelFlag(int delFlag, String id);

    /**
     * 查询被逻辑删除的数据
     *
     * @return
     */
    public List<SysDict> queryDeleteList();


}
