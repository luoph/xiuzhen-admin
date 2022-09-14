package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.ChatMessage;
import cn.youai.xiuzhen.stat.entity.ChatMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:22
 */
public interface ChatMessageMapper {

    List<ChatMessage> queryForList(@Param("rangeTimeBeginDate") Date rangeTimeBeginDate,
                                   @Param("rangeTimeEndDate") Date rangeTimeEndDate,
                                   @Param("channelId") Integer channelId,
                                   @Param("playerId") Long playerId,
                                   @Param("nickname") String nickname,
                                   @Param("message") String message);

    /**
     * 查询跨服的聊天记录
     *
     * @param rangeTimeBeginDate
     * @param rangeTimeEndDate
     * @param playerId
     * @param message
     * @return
     */
    List<ChatMessageVO> queryGameMessageList(@Param("rangeTimeBeginDate") String rangeTimeBeginDate,
                                             @Param("rangeTimeEndDate") String rangeTimeEndDate,
                                             @Param("serverId") Integer serverId,
                                             @Param("playerId") Long playerId,
                                             @Param("message") String message);
}
