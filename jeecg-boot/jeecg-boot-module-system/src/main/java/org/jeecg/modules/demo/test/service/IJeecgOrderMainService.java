package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 订单
 * @date 2019-02-15
 */
public interface IJeecgOrderMainService extends IService<JeecgOrderMain> {

    /**
     * 添加一对多
     *
     * @param jeecgOrderMain
     * @param jeecgOrderCustomerList
     * @param jeecgOrderTicketList
     */
    void saveMain(JeecgOrderMain jeecgOrderMain, List<JeecgOrderCustomer> jeecgOrderCustomerList, List<JeecgOrderTicket> jeecgOrderTicketList);

    /**
     * 修改一对多
     *
     * @param jeecgOrderMain
     * @param jeecgOrderCustomerList
     * @param jeecgOrderTicketList
     */
    void updateMain(JeecgOrderMain jeecgOrderMain, List<JeecgOrderCustomer> jeecgOrderCustomerList, List<JeecgOrderTicket> jeecgOrderTicketList);

    /**
     * 删除一对多
     *
     * @param id
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     *
     * @param idList
     */
    void delBatchMain(Collection<? extends Serializable> idList);
}
