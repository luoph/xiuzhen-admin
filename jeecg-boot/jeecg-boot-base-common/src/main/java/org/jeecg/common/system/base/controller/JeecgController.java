package org.jeecg.common.system.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.youai.basics.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.desensitization.annotation.SensitiveField;
import org.jeecg.common.desensitization.enums.SensitiveEnum;
import org.jeecg.common.desensitization.util.SensitiveInfoUtil;
import org.jeecg.common.system.annotation.HiddenField;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: Controller基类
 * @Author: dangzhenghui@163.com
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 */
@Slf4j
public class JeecgController<T, S extends IService<T>> {
    /**
     * issues/2933 JeecgController注入service时改用protected修饰，能避免重复引用service
     */
    @Autowired
    protected S service;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    protected QueryWrapper<T> prepareQuery(T entity, HttpServletRequest request) {
        return QueryGenerator.initQueryWrapper(entity, request.getParameterMap());
    }

    public IPage<T> pageList(T entity, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        QueryWrapper<T> queryWrapper = prepareQuery(entity, request);
        Page<T> page = new Page<>(pageNo, pageSize);
        return pageList(page, queryWrapper);
    }

    protected IPage<T> pageList(Page<T> page, QueryWrapper<T> queryWrapper) {
        Page<T> pageList = service.page(page, queryWrapper);
        return hideField(pageList);
    }

    private IPage<T> hideField(Page<T> pageList) {
        List<T> records = pageList.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(this::hideField);
        }
        return pageList;
    }

    private void hideField(T obj) {
        // 判断是不是一个对象
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            HiddenField annotation = field.getAnnotation(HiddenField.class);
            if (annotation != null) {
                Method setMethod = ReflectUtil.getMethodByNameIgnoreCase(obj.getClass(), "set" + field.getName());
                try {
                    if (setMethod != null) {
                        setMethod.invoke(obj, (Object) null);
                    } else {
                        field.set(obj, null);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("hideField error, obj:" + obj, e);
                }
            }
        }
    }

    public Result<?> queryPageList(T entity, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        return Result.ok(pageList(entity, pageNo, pageSize, request));
    }

    /**
     * 获取对象ID
     *
     * @return
     */
    private String getId(T entity) {
        try {
            return PropertyUtils.getProperty(entity, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected T getById(Serializable id) {
        T obj = service.getById(id);
        if (obj != null) {
            hideField(obj);
        }
        return obj;
    }

    public Result<?> queryById(@RequestParam(name = "id") Serializable id) {
        T entity = getById(id);
        if (entity == null) {
            return Result.error("未找到数据库记录");
        }
        return Result.ok(entity);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> add(@RequestBody T entity) {
        service.save(entity);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> edit(@RequestBody T entity) {
        service.updateById(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    public Result<?> delete(@RequestParam(name = "id") String id) {
        service.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        service.removeByIds(StringUtils.split2Set(ids));
        return Result.ok("批量删除成功！");
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
        if (CollUtil.isNotEmpty(pageList)) {
            pageList.forEach(this::hideField);
        }
        return ExcelUtils.exportXls(sysUser.getRealname(), pageList, request.getParameter("selections"), clazz, title);
    }

    /**
     * 根据每页sheet数量导出多sheet
     *
     * @param request      请求
     * @param object       实体类
     * @param clazz        实体类class
     * @param title        标题
     * @param exportFields 导出字段自定义
     * @param pageNum      每个sheet的数据条数
     */
    protected ModelAndView exportXlsSheet(HttpServletRequest request, T object, Class<T> clazz, String title, String exportFields, Integer pageNum) {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.2 计算分页sheet数据
        double total = service.count();
        int count = (int) Math.ceil(total / pageNum);
        //update-begin-author:liusq---date:20220629--for: 多sheet导出根据选择导出写法调整 ---
        // Step.3  过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //update-end-author:liusq---date:20220629--for: 多sheet导出根据选择导出写法调整 ---
        // Step.4 多sheet处理
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= count; i++) {
            Page<T> page = new Page<T>(i, pageNum);
            IPage<T> pageList = service.page(page, queryWrapper);
            List<T> exportList = pageList.getRecords();
            if (CollUtil.isNotEmpty(exportList)) {
                exportList.forEach(this::hideField);
            }
            Map<String, Object> map = new HashMap<>(5);
            ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title + i, upLoadPath);
            exportParams.setType(ExcelType.XSSF);
            //map.put("title",exportParams);
            //表格Title
            map.put(NormalExcelConstants.PARAMS, exportParams);
            //表格对应实体
            map.put(NormalExcelConstants.CLASS, clazz);
            //数据集合
            map.put(NormalExcelConstants.DATA_LIST, exportList);
            listMap.add(map);
        }
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.MAP_LIST, listMap);
        return mv;
    }


    /**
     * 根据权限导出excel，传入导出字段参数
     */
    protected ModelAndView exportXls(HttpServletRequest request, T object, Class<T> clazz, String title, String exportFields) {
        ModelAndView mv = this.exportXls(request, object, clazz, title);
        mv.addObject(NormalExcelConstants.EXPORT_FIELDS, exportFields);
        return mv;
    }

    /**
     * 通过excel导入数据
     */
    protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
        return ExcelUtils.importExcel(service, request, clazz);
    }
}
