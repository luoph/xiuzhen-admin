package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.entity.pojo.ItemReduce;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.ShopMallLog;
import org.jeecg.modules.game.mapper.ShopMallLogMapper;
import org.jeecg.modules.game.service.IShopMallLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 山城购买日志 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
@Service
@Slf4j
public class ShopMallLogServiceImpl extends ServiceImpl<ShopMallLogMapper, ShopMallLog> implements IShopMallLogService {
    @Resource
    private ShopMallLogMapper shopMallLogMapper;


    @Override
    public List<ShopMallLog> queryShopMallList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int type) {
        List<ShopMallLog> list = null;
        try {
            Date rangeDateBeginTime = null;
            Date rangeDateEndTime = null;
            if (days == 0) {
                rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
                rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            } else {
                rangeDateEndTime = DateUtils.dateOnly(new Date());
                rangeDateBeginTime = DateUtils.dateOnly(DateUtils.addDays(rangeDateEndTime, days * (-1)));
            }
            DataSourceHelper.useServerDatabase(serverId);

            // 全途径下的道具次数总和
            BigDecimal itemNumSum = shopMallLogMapper.queryItemSum(rangeDateBeginTime, rangeDateEndTime, type);

            list = shopMallLogMapper.queryShopMallList(rangeDateBeginTime, rangeDateEndTime, type);

            for (ShopMallLog shopMallLog : list) {

                Integer itemId = shopMallLog.getItemId();
                // 次数
                BigDecimal itemCount = shopMallLogMapper.queryItemCount(rangeDateBeginTime, rangeDateEndTime, shopMallLog.getType(), itemId);

                BigDecimal itemNum = shopMallLog.getItemNum();
                shopMallLog.setItemCount(itemCount);
                shopMallLog.setItemNumRate(BigDecimalUtil.divideFour(itemNum.doubleValue(), itemNumSum.doubleValue(), true));
                // 设置道具名字
                ItemReduce itemReduce = ItemReduce.valueOf(shopMallLog.getItemId());
                shopMallLog.setWayName(itemReduce.getName());
            }

        } catch (Exception e) {
            log.error("数据源切换异常,serverId:" + serverId, e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        return list;
    }
}
