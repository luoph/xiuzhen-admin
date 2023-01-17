/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.game.entity.GameUserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 玩家表 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface GameUserAccountMapper extends BaseMapper<GameUserAccount> {
    int userAccountNum(@Param("channel") String channel, @Param("sdkChannel") String sdkChannel, @Param("date") Date date);

}
