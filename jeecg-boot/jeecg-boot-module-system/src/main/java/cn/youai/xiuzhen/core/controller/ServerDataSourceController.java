package cn.youai.xiuzhen.core.controller;

import cn.youai.xiuzhen.core.base.IServerData;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 游戏服相关的多数据源 controller
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2019-4-21 8:13
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ServerDataSourceController<T extends IServerData, S extends IService<T>> extends MultiDataSourceController<T, S> {

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

    public Result<?> add(T model) {
        Integer serverId = model.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.add(model, serverId);
    }

    public Result<?> edit(T model) {
        Integer serverId = model.getServerId();
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.edit(model, serverId);
    }

    public Result<?> delete(Integer serverId, String id) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return super.delete(id, serverId);
    }

    public Result<?> deleteBatch(Integer serverId, String ids) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return deleteBatch(ids, serverId);
    }

    public Result<?> queryById(Integer serverId, String id) {
        if (serverId == null || serverId <= 0) {
            return Result.error("请选择游戏服id！");
        }
        return queryById(id, serverId);
    }

    protected ModelAndView exportXls(HttpServletRequest request, T entity, Class<T> clazz, String title) {
        Integer serverId = entity.getServerId();
        if (serverId == null || serverId <= 0) {
            return null;
        }
        return super.exportXls(serverId, request, entity, clazz, title);
    }
}
