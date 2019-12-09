package org.jeecg.common.system.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dangzhenghui@163.com
 * @version 1.1
 * @description Entity基类
 * @date 2019-4-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class JeecgEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "ID")
    private String id;
}
