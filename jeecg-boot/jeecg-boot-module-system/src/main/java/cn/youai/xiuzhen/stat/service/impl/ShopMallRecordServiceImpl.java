package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.ShopMallProduct;
import cn.youai.xiuzhen.stat.entity.ShopMallRecord;
import cn.youai.xiuzhen.stat.mapper.ShopMallRecordMapper;
import cn.youai.xiuzhen.stat.service.IShopMallRecordService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商城购买日志 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
@Service
@DS("shardingSphere")
public class ShopMallRecordServiceImpl extends ServiceImpl<ShopMallRecordMapper, ShopMallRecord> implements IShopMallRecordService {
    @Override
    public List<ShopMallProduct> queryShopMallProductGroupByDate(String channel, int serverId, int tabId, Date startDate, Date endDate) {
        return getBaseMapper().queryShopMallProductGroupByDate(channel, serverId, tabId, startDate, endDate);
    }

    @Override
    public List<ShopMallProduct> queryShopMallProductGroupByItemId(String channel, int serverId, int tabId, Date startDate, Date endDate) {
        return getBaseMapper().queryShopMallProductGroupByItemId(channel, serverId, tabId, startDate, endDate);
    }
}
