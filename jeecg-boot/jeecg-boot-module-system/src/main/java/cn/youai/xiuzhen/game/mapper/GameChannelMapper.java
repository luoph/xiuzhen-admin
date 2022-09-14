package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
public interface GameChannelMapper extends BaseMapper<GameChannel> {

    /**
     * 查询渠道服
     *
     * @param channelId 渠道id
     * @return List Of {@linkplain GameServer}
     */
    List<GameServerVO> selectChannelServerList(@Param("channel_id") int channelId);

    /**
     * 超找渠道名
     *
     * @param channelId 渠道id
     * @return 渠道名
     */
    String queryChannelNameById(@Param("channelId") int channelId);
}
