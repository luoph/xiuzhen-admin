package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.core.base.IPlayerData;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 背包道具操作日志记录
 *
 * @author ocean
 * @since 202109131547
 */
@Data
@Accessors(chain = true)
@Document(collection = "backpack_log")
public class MgBackpackLog implements IPlayerData, Serializable {

    private static final long serialVersionUID = 7925160499013709552L;

    /**
     * 自增id
     */
    @Id
    private String _id;

    private Long playerId;

    private Integer itemId;

    private String itemName;

    private Long num;

    /**
     * 1存入 2消耗
     */
    private Integer type;

    /**
     * 获取途径
     */
    private Integer way;

    private String wayName;

    private Long beforeNum;

    private Long afterNum;

    private Date createDate;

    private Date createTime;
}
