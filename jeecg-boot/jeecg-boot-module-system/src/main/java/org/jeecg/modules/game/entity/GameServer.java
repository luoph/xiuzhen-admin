/*
* create by mybatis-plus-generator  https://github.com/xiweile
*/
package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 游戏服务器表
 * </p>
 *
 * @author luopeihuan
 * @since 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器名字
     */
    private String name;

    /**
     * 服务器路径
     */
    private String host;

    /**
     * 服务器端口
     */
    private Integer port;

    /**
     * 登陆地址和端口
     */
    private String loginUrl;

    /**
     * 服务器状态
     */
    private Integer status;

    /**
     * 推荐标识
     */
    private Integer recommend;

    /**
     * 出错提示信息
     */
    private String warning;

    /**
     * 显示版本号
     */
    private Integer showVersion;

    /**
     * 进入游戏客户端版本
     */
    private Integer clientVersionCode;

    /**
     * 数据库路径
     */
    private String dbHost;

    /**
     * 数据库端口
     */
    private Integer dbPort;

    /**
     * 数据库用户名
     */
    private String dbUser;

    /**
     * 数据库密码
     */
    private String dbPassword;

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 后台HTTP端口
     */
    private Integer httpPort;

    /**
     * 排序字段
     */
    private Integer position;

    /**
     * 服务器类型:0为混服,1为专服
     */
    private Boolean type;

    /**
     * 合服时母服id
     */
    private Integer pid;

    /**
     * 合服时间
     */
    private Date mergeTime;

    /**
     * 扩展字段
     */
    private String extra;

    /**
     * 服务器开服时间
     */
    private Date openTime;

    /**
     * 服务器创建时间
     */
    private Date createTime;


}
