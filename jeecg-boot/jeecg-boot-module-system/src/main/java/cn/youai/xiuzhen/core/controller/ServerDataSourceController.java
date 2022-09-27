package cn.youai.xiuzhen.core.controller;

import cn.youai.xiuzhen.core.base.IServerData;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 游戏服相关的多数据源 controller
 *
 * @author luopeihuan
 * @version 1.0
 * @description 多数据源查询基类
 * @date 2019-4-21 8:13
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ServerDataSourceController<T extends IServerData, S extends IService<T>> extends MultiDataSourceController<T, S> {

    /**
     * 分页列表查询
     */
    public Result<?> queryPageList(T entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }

        return super.queryPageList(entity, pageNo, pageSize, serverId, req);
    }

    /**
     * 添加
     */
    public Result<?> add(T model) {
        Integer serverId = model.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.add(model, serverId);
    }

    /**
     * 编辑
     */
    public Result<?> edit(T model) {
        Integer serverId = model.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.edit(model, serverId);
    }

    /**
     * 通过id删除
     */
    public Result<?> delete(Integer serverId, String id) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.delete(id, serverId);
    }

    /**
     * 批量删除
     */
    public Result<?> deleteBatch(Integer serverId, String ids) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return deleteBatch(ids, serverId);
    }

    /**
     * 通过id查询
     */
    public Result<?> queryById(Integer serverId, String id) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return queryById(id, serverId);
    }

    /**
     * 导出excel
     */
    protected ModelAndView exportXls(HttpServletRequest request, T entity, Class<T> clazz, String title) {
        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return null;
        }
        return super.exportXls(serverId, request, entity, clazz, title);
    }
}
