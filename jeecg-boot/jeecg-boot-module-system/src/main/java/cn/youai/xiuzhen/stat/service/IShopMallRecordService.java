package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.ShopMallProduct;
import cn.youai.xiuzhen.stat.entity.ShopMallRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商城购买日志 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
public interface IShopMallRecordService extends IService<ShopMallRecord> {

    List<ShopMallProduct> queryShopMallProductGroupByDate(String channel, int serverId, int tabId, Date startDate, Date endDate);

    List<ShopMallProduct> queryShopMallProductGroupByItemId(String channel, int serverId, int tabId, Date startDate, Date endDate);

}
