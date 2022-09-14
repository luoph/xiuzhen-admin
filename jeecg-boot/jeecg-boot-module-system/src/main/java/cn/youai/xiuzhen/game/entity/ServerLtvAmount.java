package cn.youai.xiuzhen.game.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2021/12/10.
 */
@Data
@Accessors(chain = true)
public class ServerLtvAmount {

    private Integer serverId;
    private Date registerDate;
    private Integer days;
    private BigDecimal totalAmount;

}
