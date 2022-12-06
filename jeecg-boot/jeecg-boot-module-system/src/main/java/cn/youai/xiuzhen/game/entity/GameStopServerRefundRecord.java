/*
* create by mybatis-plus-generator  https://github.com/xiweile
*/
package cn.youai.xiuzhen.game.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 删档返还记录
 * </p>
 *
 * @author buliangliang
 * @since 2022-12-05
 */
@Data
@Accessors(chain = true)
public class GameStopServerRefundRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 停服的服务器id
     */
    private Integer sourceServerId;

    /**
     * 停服的玩家id
     */
    private Long sourcePlayerId;

    /**
     * 返还的服务器id
     */
    private Integer targetServerId;

    /**
     * 返还的玩家id
     */
    private Long targetPlayerId;

    /**
     * 充值总金额
     */
    private BigDecimal sourceAmount;

    /**
     * 返还总仙玉
     */
    private Long targetNum;

    /**
     * 创建时间
     */
    private Date createTime;


}
