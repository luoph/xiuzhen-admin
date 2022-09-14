package org.jeecg.modules.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ServerLoginNumVO
 *
 * @author ocean
 * @since 202203031138
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MergeServerVO implements Serializable {

    private Integer serverId;
    private Integer num;

    private BigDecimal payAmount;

}
