package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.FriendChatChannel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/21 16:09
 */
public interface FriendChatChannelMapper {

    List<FriendChatChannel> queryListBySender(@Param("rangeTimeBeginDate") Date rangeTimeBeginDate,
                                              @Param("rangeTimeEndDate") Date rangeTimeEndDate,
                                              @Param("channelId") Integer channelId,
                                              @Param("nickname") String nickname,
                                              @Param("playerId") Long playerId,
                                              @Param("message") String message);

}
