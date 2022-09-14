package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.LogPlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface LogPlayerMapper extends BaseMapper<LogPlayer> {

    /**
     * 获取所有战力列表
     */
    @Select("select player_id, value, create_time, type, param_1, param_2, param_3 from `log_player` where type = 9 and server_id = #{serverId}  and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_time desc")
    List<Map> selectMilitaryStrengVoAll(@Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 获取某用户某服务器中战力列表（playerId多个，name一个的情况）
     */
    @Select("select player_id, value, create_time, type, param_1, param_2, param_3 from `log_player` where player_id in ${playerId} and type = 9 and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_time desc")
    List<Map> selectMilitaryStrengVoAllByPlayerId(@Param("serverId") int serverId, @Param("playerId") String playerId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);
}
