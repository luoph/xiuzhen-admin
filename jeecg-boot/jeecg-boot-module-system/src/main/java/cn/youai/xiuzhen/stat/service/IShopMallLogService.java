package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.ShopMallLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 山城购买日志 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
public interface IShopMallLogService extends IService<ShopMallLog> {

    /**
     * 查询商店购买记录统计
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param type
     * @return
     */
    List<ShopMallLog> queryShopMallList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int type);

    /**
     * 查询商店购买记录统计
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param type
     * @return
     */
    List<ShopMallLog> queryShopMallListNew(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int type);
}
