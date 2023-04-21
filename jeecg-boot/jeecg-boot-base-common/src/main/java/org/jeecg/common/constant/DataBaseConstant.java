package org.jeecg.common.constant;

/**
 * 数据库上下文常量
 *
 * @author: jeecg-boot
 */
public interface DataBaseConstant {
    //*********数据库类型****************************************

    /**
     * MYSQL数据库
     */
    String DB_TYPE_MYSQL = "MYSQL";

    /**
     * ORACLE
     */
    String DB_TYPE_ORACLE = "ORACLE";

    /**
     * 达梦数据库
     */
    String DB_TYPE_DM = "DM";

    /**
     * postgreSQL达梦数据库
     */
    String DB_TYPE_POSTGRESQL = "POSTGRESQL";

    /**
     * sqlserver数据库
     */
    String DB_TYPE_SQLSERVER = "SQLSERVER";

    /**
     * mariadb 数据库
     */
    String DB_TYPE_MARIADB = "MARIADB";

    /**
     * DB2 数据库
     */
    String DB_TYPE_DB2 = "DB2";

    /**
     * HSQL 数据库
     */
    String DB_TYPE_HSQL = "HSQL";

//	// 数据库类型，对应 database_type 字典
//	String DB_TYPE_MYSQL_NUM = "1";
//	String DB_TYPE_MYSQL7_NUM = "6";
//	String DB_TYPE_ORACLE_NUM = "2";
//	String DB_TYPE_SQLSERVER_NUM = "3";
//	String DB_TYPE_POSTGRESQL_NUM = "4";
//	String DB_TYPE_MARIADB_NUM = "5";

    //*********系统上下文变量****************************************
    /**
     * 数据-所属机构编码
     */
    String SYS_ORG_CODE = "sysOrgCode";
    String SYS_ORG_CODE_TABLE = "sys_org_code";

    /**
     * 数据-所属机构编码
     */
    String SYS_MULTI_ORG_CODE = "sysMultiOrgCode";
    String SYS_MULTI_ORG_CODE_TABLE = "sys_multi_org_code";
    /**
     * 数据-系统用户编码（对应登录用户账号）
     */
    String SYS_USER_CODE = "sysUserCode";
    String SYS_USER_CODE_TABLE = "sys_user_code";

    /**
     * 登录用户真实姓名
     */
    String SYS_USER_NAME = "sysUserName";
    String SYS_USER_NAME_TABLE = "sys_user_name";
    /**
     * 系统日期"yyyy-MM-dd"
     */
    String SYS_DATE = "sysDate";
    String SYS_DATE_TABLE = "sys_date";
    /**
     * 系统时间"yyyy-MM-dd HH:mm"
     */
    String SYS_TIME = "sysTime";
    String SYS_TIME_TABLE = "sys_time";

    /**
     * 数据-所属机构编码
     */
    String SYS_BASE_PATH = "sys_base_path";
    //*********系统上下文变量****************************************


    //*********系统建表标准字段****************************************
    /**
     * 创建者登录名称
     */
    String CREATE_BY_TABLE = "create_by";
    String CREATE_BY = "createBy";
    /**
     * 创建日期时间
     */
    String CREATE_TIME_TABLE = "create_time";
    String CREATE_TIME = "createTime";
    /**
     * 更新用户登录名称
     */
    String UPDATE_BY_TABLE = "update_by";
    String UPDATE_BY = "updateBy";
    /**
     * 更新日期时间
     */
    String UPDATE_TIME = "updateTime";
    String UPDATE_TIME_TABLE = "update_time";

    /**
     * 业务流程状态
     */
    String BPM_STATUS = "bpmStatus";
    String BPM_STATUS_TABLE = "bpm_status";
    //*********系统建表标准字段****************************************


    /**
     * 租户ID 实体字段名
     */
    String TENANT_ID = "tenantId";
    String TENANT_ID_TABLE = "tenant_id";

    /**
     * sql语句 where
     */
    String SQL_WHERE = "where";

    /**
     * sql语句 asc
     */
    String SQL_ASC = "asc";

    /**
     * sqlserver数据库,中间有空格
     */
    String DB_TYPE_SQL_SERVER_BLANK = "sql server";
}
