package org.jeecg.modules.game.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 玩家注册信息表的实体类,player_register_info
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/9 14:42
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerRegisterInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 没有从player_register_info表中列出全部的属性,只写了两个,用于封装关联查询
     * 后续如有其他需求可以补上其他的属性
     */
    private Integer id;

    private String name;
}
