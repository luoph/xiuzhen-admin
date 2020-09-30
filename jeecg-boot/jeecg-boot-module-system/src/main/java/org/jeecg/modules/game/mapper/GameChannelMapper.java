package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServer;

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
     * @return
     */
    @Select("SELECT s.`id`, s.`game_id`, s.`name`, s.`host`, s.`login_url`, s.`status`, s.`recommend`, s.`warning`, s.`min_version`, s.`max_version`" +
            " FROM `game_server` s LEFT JOIN `game_channel_server` c ON c.`server_id` = s.`id`" +
            " WHERE c.`channel_id` = #{channel_id}  AND c.`del_flag` = 0 ORDER BY s.`position`")
    List<GameServer> getServerListChannelId(@Param("channel_id") Long channelId);

    @Select("SELECT simple_name FROM game_channel WHERE id=#{channelId}")
    String queryChannelNameById(@Param("channelId") Integer channelId);
}
