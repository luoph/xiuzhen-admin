package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.ShopMallProduct;
import cn.youai.xiuzhen.stat.entity.ShopMallRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商城购买日志 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
public interface ShopMallRecordMapper extends BaseMapper<ShopMallRecord> {

    List<ShopMallProduct> queryShopMallProductGroupByDate(@Param("channel") String channel,
                                                          @Param("serverId") int serverId,
                                                          @Param("tabId") int tabId,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);

    List<ShopMallProduct> queryShopMallProductGroupByItemId(@Param("channel") String channel,
                                                            @Param("serverId") int serverId,
                                                            @Param("tabId") int tabId,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate);
}
