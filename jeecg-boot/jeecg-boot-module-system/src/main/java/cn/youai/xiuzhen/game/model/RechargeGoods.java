package cn.youai.xiuzhen.game.model;

import cn.youai.server.model.ItemVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_recharge_goods
 * @date 2023-05-08
 */
@Data
public class RechargeGoods {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer goodsId;
    private Integer goodsType;
    private String name;
    private String sku;
    private String webSku;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal localPrice;
    private BigDecimal webLocalPrice;
    private BigDecimal displayPrice;
    private BigDecimal webDisplayPrice;
    private List<ItemVO> rewards;
    private String currency;
    private List<ItemVO> additions;
    private Integer exchange;
    private Integer recommend;
    private Integer buyType;
    private Long gmCoin;
}
