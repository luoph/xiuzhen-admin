package org.jeecg.common.constant;

/**
 * @author: huangxutao
 * @date: 2019-06-14
 * @description: 缓存常量
 */
public interface CacheConstant {

    /**
     * 字典信息缓存
     */
    String SYS_DICT_CACHE = "sys:cache:dict";
    /**
     * 表字典信息缓存
     */
    String SYS_DICT_TABLE_CACHE = "sys:cache:dictTable";

    /**
     * 数据权限配置缓存
     */
    String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:datarules";

    /**
     * 缓存用户信息
     */
    String SYS_USERS_CACHE = "sys:cache:user";

    /**
     * 全部部门信息缓存
     */
    String SYS_DEPARTS_CACHE = "sys:cache:depart:alldata";


    /**
     * 全部部门ids缓存
     */
    String SYS_DEPART_IDS_CACHE = "sys:cache:depart:allids";


    /**
     * 测试缓存key
     */
    String TEST_DEMO_CACHE = "test:demo";

}
