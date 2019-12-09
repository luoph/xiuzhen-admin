package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 订单机票
 * @date 2019-02-15
 */
public interface JeecgOrderTicketMapper extends BaseMapper<JeecgOrderTicket> {

    /**
     * 通过主表外键批量删除客户
     *
     * @param mainId
     * @return
     */
    @Delete("DELETE FROM JEECG_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    public boolean deleteTicketsByMainId(String mainId);


    @Select("SELECT * FROM JEECG_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    public List<JeecgOrderTicket> selectTicketsByMainId(String mainId);
}
