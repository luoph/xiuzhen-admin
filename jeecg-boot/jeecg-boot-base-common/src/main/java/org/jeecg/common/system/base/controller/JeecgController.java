package org.jeecg.common.system.base.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @author dangzhenghui@163.com
 * @version 1.0
 * @description Controller基类
 * @date 2019-4-21 8:13
 */
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class JeecgController<T, S extends IService<T>> {

    @Autowired
    protected S service;

    protected QueryWrapper<T> prepareQuery(T entity, HttpServletRequest request) {
        return QueryGenerator.initQueryWrapper(entity, request.getParameterMap());
    }

    protected IPage<T> pageList(Page<T> page, QueryWrapper<T> queryWrapper) {
        return service.page(page, queryWrapper);
    }

    public Result<?> queryPageList(T entity, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        QueryWrapper<T> queryWrapper = prepareQuery(entity, request);
        Page<T> page = new Page<>(pageNo, pageSize);
        return Result.ok(pageList(page, queryWrapper));
    }

    protected T getById(Serializable id) {
        return service.getById(id);
    }

    public Result<?> queryById(@RequestParam(name = "id") Serializable id) {
        T entity = getById(id);
        if (entity == null) {
            return Result.error("未找到数据库记录");
        }
        return Result.ok(entity);
    }

    /**
     * 导出excel
     */
    protected ModelAndView exportXls(HttpServletRequest request, T entity, Class<T> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = prepareQuery(entity, request);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<T> pageList = service.list(queryWrapper);
        return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), clazz, title);
    }

    /**
     * 通过excel导入数据
     */
    protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
        return ExcelUtils.importExcel(service, request, clazz);
    }
}
