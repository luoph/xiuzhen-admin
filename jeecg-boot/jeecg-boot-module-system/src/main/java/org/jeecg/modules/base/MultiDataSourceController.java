package org.jeecg.modules.base;

import cn.youai.xiuzhen.database.DataSourceHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author luopeihuan
 * @version 1.0
 * @description 多数据源查询基类
 * @date 2019-4-21 8:13
 */
@Slf4j
public class MultiDataSourceController<T, S extends IService<T>> {

    @Autowired
    protected S service;

    private void useServerDatabase(Integer serverId) {
        if (serverId != null && serverId > 0) {
            DataSourceHelper.useServerDatabase(serverId);
        }
    }

    private void useDefaultDatabase() {
        DataSourceHelper.useDefaultDatabase();
    }

    protected QueryWrapper<T> prepareQuery(T entity, HttpServletRequest req) {
        return QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
    }

    protected IPage<T> pageList(Page<T> page, QueryWrapper<T> queryWrapper) {
        return service.page(page, queryWrapper);
    }

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    public Result<?> queryPageList(T entity, Integer pageNo, Integer pageSize, Integer serverId, HttpServletRequest req) {
        QueryWrapper<T> queryWrapper = prepareQuery(entity, req);
        Page<T> page = new Page<>(pageNo, pageSize);
        try {
            useServerDatabase(serverId);
            return Result.ok(pageList(page, queryWrapper));
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> add(T entity, Integer serverId) {
        try {
            useServerDatabase(serverId);
            service.save(entity);
            return Result.ok("添加成功！");
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> edit(T entity, Integer serverId) {
        try {
            useServerDatabase(serverId);
            service.updateById(entity);
            return Result.ok("编辑成功!");
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    public Result<?> delete(String id, Integer serverId) {
        try {
            useServerDatabase(serverId);
            service.removeById(id);
            return Result.ok("删除成功!");
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    public Result<?> deleteBatch(String ids, Integer serverId) {
        try {
            useServerDatabase(serverId);
            service.removeByIds(Arrays.asList(ids.split(",")));
            return Result.ok("批量删除成功！");
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    public Result<?> queryById(String id, Integer serverId) {
        try {
            useServerDatabase(serverId);
            T entity = service.getById(id);
            if (entity == null) {
                return Result.error("未找到对应数据");
            }
            return Result.ok(entity);
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView exportXls(Integer serverId, HttpServletRequest request, T object, Class<T> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        try {
            useServerDatabase(serverId);
            // Step.2 获取导出数据
            List<T> pageList = service.list(queryWrapper);
            return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), clazz, title);
        } finally {
            useDefaultDatabase();
        }
    }

    /**
     * 通过excel导入数据
     *
     * @param serverId
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    protected Result<?> importExcel(Integer serverId, HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
        try {
            useServerDatabase(serverId);
            return ExcelUtils.importExcel(service, request, clazz);
        } finally {
            useDefaultDatabase();
        }
    }
}
