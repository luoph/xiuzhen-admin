package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.mapper.JeecgOrderTicketMapper;
import org.jeecg.modules.demo.test.service.IJeecgOrderTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 订单机票
 * @date 2019-02-15
 */
@Service
public class JeecgOrderTicketServiceImpl extends ServiceImpl<JeecgOrderTicketMapper, JeecgOrderTicket> implements IJeecgOrderTicketService {
    @Autowired
    private JeecgOrderTicketMapper jeecgOrderTicketMapper;

    @Override
    public List<JeecgOrderTicket> selectTicketsByMainId(String mainId) {
        return jeecgOrderTicketMapper.selectTicketsByMainId(mainId);
    }

}
