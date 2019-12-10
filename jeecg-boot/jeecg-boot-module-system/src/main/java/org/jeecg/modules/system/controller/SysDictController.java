package org.jeecg.modules.system.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.SysDictTree;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.vo.SysDictPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

    /**
     * 参数分隔符
     */
    private static final String DICT_PARAM_SPLIT_CHAR = ",";

    /**
     * 不带查询条件的参数：表名,文本字段,取值字段
     */
    private static final int DICT_PARAM_NO_FILTER_LEN = 3;
    /**
     * 带查询条件的参数：表名,文本字段,取值字段,查询条件
     */
    private static final int DICT_PARAM_WITH_FILTER_LEN = DICT_PARAM_NO_FILTER_LEN + 1;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private ISysDictItemService sysDictItemService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysDict>> queryPageList(SysDict sysDict, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDict>> result = new Result<>();
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        Page<SysDict> page = new Page<>(pageNo, pageSize);
        IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
        log.debug("查询当前页:{}, 查询当前页数量:{}, 查询结果数量:{}, 数据总数:{}",
                pageList.getCurrent(), pageList.getSize(), pageList.getRecords().size(), pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @param sysDict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：获取树形字典数据
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public Result<List<SysDictTree>> treeList(SysDict sysDict, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<List<SysDictTree>> result = new Result<>();
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        // 构造查询条件
        String dictName = sysDict.getDictName();
        if (oConvertUtils.isNotEmpty(dictName)) {
            query.like(true, SysDict::getDictName, dictName);
        }
        query.orderByDesc(true, SysDict::getCreateTime);
        List<SysDict> list = sysDictService.list(query);
        List<SysDictTree> treeList = new ArrayList<>();
        for (SysDict node : list) {
            treeList.add(new SysDictTree(node));
        }
        result.setSuccess(true);
        result.setResult(treeList);
        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : " + dictCode);
        Result<List<DictModel>> result = new Result<>();
        List<DictModel> ls = null;
        try {
            if (dictCode.contains(DICT_PARAM_SPLIT_CHAR)) {
                // 关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(DICT_PARAM_SPLIT_CHAR);

                if (params.length < DICT_PARAM_NO_FILTER_LEN) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }

                //S QL注入校验（只限制非法串改数据库）
                final String[] sqlInjCheck = {params[0], params[1], params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);

                if (params.length == DICT_PARAM_WITH_FILTER_LEN) {
                    // SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                    SqlInjectionUtil.specialFilterContent(params[DICT_PARAM_NO_FILTER_LEN]);
                    ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0], params[1], params[2], params[3]);
                } else if (params.length == DICT_PARAM_NO_FILTER_LEN) {
                    ls = sysDictService.queryTableDictItemsByCode(params[0], params[1], params[2]);
                } else {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
            } else {
                // 字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
    public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
        log.info(" dictCode : " + dictCode);
        Result<String> result = new Result<String>();
        String text = null;
        try {
            text = sysDictService.queryDictTextByKey(dictCode, key);
            result.setSuccess(true);
            result.setResult(text);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }
        return result;
    }

    /**
     * @param sysDict
     * @return
     * @功能：新增
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysDict> add(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<>();
        try {
            sysDict.setCreateTime(new Date());
            sysDict.setDelFlag(CommonConstant.DEL_FLAG_0);
            sysDictService.save(sysDict);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @param sysDict
     * @return
     * @功能：编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysDict> edit(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<>();
        SysDict sysdict = sysDictService.getById(sysDict.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体");
            return result;
        }

        sysDict.setUpdateTime(new Date());
        boolean ok = sysDictService.updateById(sysDict);
        //TODO 返回false说明什么？
        if (ok) {
            result.success("编辑成功!");
        }
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDict> delete(@RequestParam(name = "id") String id) {
        Result<SysDict> result = new Result<SysDict>();
        boolean ok = sysDictService.removeById(id);
        if (ok) {
            result.success("删除成功!");
        } else {
            result.error500("删除失败!");
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDict> deleteBatch(@RequestParam(name = "ids") String ids) {
        Result<SysDict> result = new Result<>();
        if (oConvertUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
            return result;
        }
        
        sysDictService.removeByIds(Arrays.asList(ids.split(DICT_PARAM_SPLIT_CHAR)));
        result.success("删除成功!");
        return result;
    }

    /**
     * 导出excel
     *
     * @param sysDict
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDict sysDict, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDictPage> pageList = new ArrayList<>();

        List<SysDict> sysDictList = sysDictService.list(queryWrapper);
        for (SysDict dictMain : sysDictList) {
            SysDictPage vo = new SysDictPage();
            BeanUtils.copyProperties(dictMain, vo);
            // 查询机票
            List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
            vo.setSysDictItemList(sysDictItemList);
            pageList.add(vo);
        }

        // 导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "数据字典");
        // 注解对象Class
        mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
        // 自定义表格参数
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", "导出人:" + user.getRealname(), "数据字典"));
        // 导出数据列表
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(2);
            params.setNeedSave(true);
            try {
                List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
                for (SysDictPage page : list) {
                    SysDict po = new SysDict();
                    BeanUtils.copyProperties(page, po);
                    if (page.getDelFlag() == null) {
                        po.setDelFlag(1);
                    }
                    sysDictService.saveMain(po, page.getSysDictItemList());
                }
                return Result.ok("文件导入成功！");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     *
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/loadDict/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> loadDict(@PathVariable String dictCode, @RequestParam(name = "keyword") String keyword) {
        log.info(" 加载字典表数据,加载关键字: " + keyword);
        Result<List<DictModel>> result = new Result<>();
        List<DictModel> ls = null;
        try {
            if (dictCode.contains(DICT_PARAM_SPLIT_CHAR)) {
                String[] params = dictCode.split(DICT_PARAM_SPLIT_CHAR);
                if (params.length != 3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                ls = sysDictService.queryTableDictItems(params[0], params[1], params[2], keyword);
                result.setSuccess(true);
                result.setResult(ls);
                log.info(result.toString());
            } else {
                result.error500("字典Code格式不正确！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 根据字典code加载字典text 返回
     */
    @RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
    public Result<List<String>> loadDictItem(@PathVariable String dictCode, @RequestParam(name = "key") String key) {
        Result<List<String>> result = new Result<>();
        try {
            if (dictCode.contains(DICT_PARAM_SPLIT_CHAR)) {
                String[] params = dictCode.split(DICT_PARAM_SPLIT_CHAR);
                if (params.length != DICT_PARAM_NO_FILTER_LEN) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                List<String> texts = sysDictService.queryTableDictByKeys(params[0], params[1], params[2], key.split(","));

                result.setSuccess(true);
                result.setResult(texts);
                log.info(result.toString());
            } else {
                result.error500("字典Code格式不正确！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 根据表名——显示字段-存储字段 pid 加载树形数据
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
    public Result<List<TreeSelectModel>> loadDict(@RequestParam(name = "pid") String pid, @RequestParam(name = "pidField") String pidField,
                                                  @RequestParam(name = "tableName") String tableName,
                                                  @RequestParam(name = "text") String text,
                                                  @RequestParam(name = "code") String code,
                                                  @RequestParam(name = "hasChildField") String hasChildField,
                                                  @RequestParam(name = "condition") String condition) {
        Result<List<TreeSelectModel>> result = new Result<>();
        Map<String, String> query = null;
        if (oConvertUtils.isNotEmpty(condition)) {
            query = JSON.parseObject(condition, Map.class);
        }
        List<TreeSelectModel> ls = sysDictService.queryTreeList(query, tableName, text, code, pidField, pid, hasChildField);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }


    /**
     * 查询被删除的列表
     *
     * @return
     */
    @RequestMapping(value = "/deleteList", method = RequestMethod.GET)
    public Result<List<SysDict>> deleteList() {
        Result<List<SysDict>> result = new Result<>();
        List<SysDict> list = this.sysDictService.queryDeleteList();
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    /**
     * 物理删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePhysic/{id}", method = RequestMethod.DELETE)
    public Result<?> deletePhysic(@PathVariable String id) {
        try {
            sysDictService.deleteOneDictPhysically(id);
            return Result.ok("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败!");
        }
    }

    /**
     * 取回
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/back/{id}", method = RequestMethod.PUT)
    public Result<?> back(@PathVariable String id) {
        try {
            sysDictService.updateDictDelFlag(0, id);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败!");
        }
    }

}
