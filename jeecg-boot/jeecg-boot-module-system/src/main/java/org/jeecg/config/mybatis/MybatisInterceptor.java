package org.jeecg.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 *
 * @author scott
 * @Date 2019-01-19
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------{}", sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------{}", sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            Field[] fields = oConvertUtils.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name------{}", field.getName());
                try {
                    //update-begin--Author:scott  Date:20190828 for：关于使用Quzrtz 开启线程任务， #465
                    // 获取登录用户信息
                    LoginUser sysUser = null;
                    try {
                        sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    } catch (Exception e) {
                        sysUser = null;
                    }
                    //update-end--Author:scott  Date:20190828 for：关于使用Quzrtz 开启线程任务， #465
                    if ("createBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object localCreateBy = field.get(parameter);
                        field.setAccessible(false);
                        if (localCreateBy == null || "".equals(localCreateBy)) {
                            String createBy = "jeecg";
                            if (sysUser != null) {
                                // 登录账号
                                createBy = sysUser.getUsername();
                            }
                            if (oConvertUtils.isNotEmpty(createBy)) {
                                field.setAccessible(true);
                                field.set(parameter, createBy);
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object localCreateDate = field.get(parameter);
                        field.setAccessible(false);
                        if (localCreateDate == null || "".equals(localCreateDate)) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                    // 注入部门编码
                    if ("sysOrgCode".equals(field.getName())) {
                        field.setAccessible(true);
                        Object localSysOrgCode = field.get(parameter);
                        field.setAccessible(false);
                        if (localSysOrgCode == null || "".equals(localSysOrgCode)) {
                            String sysOrgCode = "";
                            // 获取登录用户信息
                            if (sysUser != null) {
                                // 登录账号
                                sysOrgCode = sysUser.getOrgCode();
                            }
                            if (oConvertUtils.isNotEmpty(sysOrgCode)) {
                                field.setAccessible(true);
                                field.set(parameter, sysOrgCode);
                                field.setAccessible(false);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = null;
            if (parameter instanceof ParamMap) {
                ParamMap<?> p = (ParamMap<?>) parameter;
                //update-begin-author:scott date:20190729 for:批量更新报错issues/IZA3Q--
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                //update-end-author:scott date:20190729 for:批量更新报错issues/IZA3Q-

                //update-begin-author:scott date:20190729 for:更新指定字段时报错 issues/#516-
                if (parameter == null) {
                    return invocation.proceed();
                }
                //update-end-author:scott date:20190729 for:更新指定字段时报错 issues/#516-

                fields = oConvertUtils.getAllFields(parameter);
            } else {
                fields = oConvertUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------{}", field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        // 获取登录用户信息
                        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                        if (sysUser != null) {
                            // 登录账号
                            String updateBy = sysUser.getUsername();
                            field.setAccessible(true);
                            field.set(parameter, updateBy);
                            field.setAccessible(false);
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

}
