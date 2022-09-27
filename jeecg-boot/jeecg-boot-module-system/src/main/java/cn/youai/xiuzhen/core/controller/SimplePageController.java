package cn.youai.xiuzhen.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 多数据源查询基类
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2022/09/27
 */
@Slf4j
public abstract class SimplePageController<T> {

    public Result<?> queryPageList(T entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<T> page = new Page<>(pageNo, pageSize);
        IPage<T> pageList = pageList(page, entity, req);
        onload(pageList.getRecords());
        return Result.ok(pageList);
    }

    protected ModelAndView exportXls(HttpServletRequest req, T entity, Class<T> clazz, String title) {
        Page<T> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<T> pageList = pageList(page, entity, req);
        onload(pageList.getRecords());
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), clazz, title);
    }

    protected abstract IPage<T> pageList(Page<T> page, T entity, HttpServletRequest req);

    protected void onload(List<T> pageList) {
        if (pageList == null) {
            return;
        }
        pageList.forEach(this::onload);
    }

    protected void onload(T entity) {
    }
}
