package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 订单机票
 * @date 2019-02-15
 */
public interface IJeecgOrderTicketService extends IService<JeecgOrderTicket> {

    public List<JeecgOrderTicket> selectTicketsByMainId(String mainId);
}
