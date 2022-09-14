package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.PayOrderGift;
import cn.youai.xiuzhen.stat.mapper.PayOrderGiftMapper;
import cn.youai.xiuzhen.stat.service.IPayOrderGiftService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Service
@DS("shardingSphere")
public class PayOrderGiftServiceImpl extends ServiceImpl<PayOrderGiftMapper, PayOrderGift> implements IPayOrderGiftService {

}
