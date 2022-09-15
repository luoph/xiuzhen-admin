package cn.youai.xiuzhen.core.controller;

import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luopeihuan
 * @version 1.0
 * @description 多数据源查询基类
 * @date 2019-4-21 8:13
 */
@Slf4j
@SuppressWarnings("unchecked")
public class PlayerDataSourceController<T, S extends IService<T>> extends MultiDataSourceController<T, S> {

    @Autowired
    private IGamePlayerService playerService;

    /**
     * 分页列表查询
     *
     * @param entity 数据实体
     * @param req    请求
     * @return {@linkplain Result}
     */
    public Result<?> queryPageList(long playerId, T entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (playerId <= 0) {
            return Result.error("请输入玩家id！");
        }

        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return super.queryPageList(entity, pageNo, pageSize, player.getServerId(), req);
    }

    /**
     * 添加
     *
     * @param model 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> add(long playerId, T model) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }

        return add(model, player.getServerId());
    }

    /**
     * 编辑
     *
     * @param model 数据实体
     * @return {@linkplain Result}
     */
    public Result<?> edit(long playerId, T model) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return edit(model, player.getServerId());
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    public Result<?> delete(long playerId, String id) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return delete(id, player.getServerId());
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    public Result<?> deleteBatch(long playerId, String ids) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return deleteBatch(ids, player.getServerId());
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    public Result<?> queryById(long playerId, String id) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return queryById(id, player.getServerId());
    }

    /**
     * 导出excel
     */
    protected ModelAndView exportXls(long playerId, HttpServletRequest request, T object, Class<T> clazz, String title) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return null;
        }

        return exportXls(player.getServerId(), request, object, clazz, title);
    }

    /**
     * 通过excel导入数据
     */
    protected Result<?> importExcel(long playerId, HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
        GamePlayer player = playerService.getPlayer(playerId);
        if (player == null) {
            return Result.error("找不到玩家信息！");
        }
        return importExcel(player.getServerId(), request, response, clazz);
    }
}
