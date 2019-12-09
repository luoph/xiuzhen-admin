package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 订单客户
 * @date 2019-02-15
 */
public interface IJeecgOrderCustomerService extends IService<JeecgOrderCustomer> {

    public List<JeecgOrderCustomer> selectCustomersByMainId(String mainId);
}
