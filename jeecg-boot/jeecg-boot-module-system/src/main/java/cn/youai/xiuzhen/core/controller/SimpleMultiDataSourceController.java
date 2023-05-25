package cn.youai.xiuzhen.core.controller;

import cn.youai.server.exception.AppException;
import cn.youai.xiuzhen.core.base.IServerData;
import cn.youai.xiuzhen.core.database.DataSourceHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 多数据源查询基类
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2022/09/27
 */
@Slf4j
public abstract class SimpleMultiDataSourceController<T extends IServerData> extends SimplePageController<T> {

    protected void useServerDatabase(Integer serverId) {
        if (serverId != null && serverId > 0) {
            DataSourceHelper.useServerDatabase(serverId);
        }
    }

    protected void useDefaultDatabase() {
        DataSourceHelper.useDefaultDatabase();
    }

    public Result<?> queryPageList(T entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }

        Page<T> page = new Page<>(pageNo, pageSize);
        try {
            useServerDatabase(entity.getServerId());
            IPage<T> pageList = pageList(page, entity, req);
            onload(pageList.getRecords());
            return Result.ok(pageList);
        } catch (AppException e) {
            return Result.error(e.getMessage());
        } finally {
            useDefaultDatabase();
        }
    }

    protected ModelAndView exportXls(HttpServletRequest req, T entity, Class<T> clazz, String title) {
        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return null;
        }

        return super.exportXls(req, entity, clazz, title);
    }
}
