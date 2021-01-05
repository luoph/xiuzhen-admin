package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.MilitaryStrengthVO;
import org.jeecg.modules.game.mapper.MilitaryStrengthMapper;
import org.jeecg.modules.game.service.IMilitaryStrengthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 胡立
 * @Description: MilitaryStrengthServiceImpl
 * @date 2021/1/4 11:54
 */

@Service
public class MilitaryStrengthServiceImpl implements IMilitaryStrengthService {
    Log log = LogFactory.getLog(this.getClass());
    @Resource
    MilitaryStrengthMapper militaryStrengthMapper;
    @Value("${app.log.db.table_log_player}")
    private String logPlayerTable;

    @Override
    public List<MilitaryStrengthVO> getMilitaryStrengVoDujieList(int serverId, String createDateBegin, String createDateEnd, String channel) {
        List<Map> practiceDujieList = null;
        //查询时间范围内 所有注册的用户
        List<Map> allRegisterUserList = militaryStrengthMapper.selectAllRegisterUser(channel, serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
        Map<String, List<Map>> allRegisterUserListMap_playerId= allRegisterUserList.stream().collect(Collectors.groupingBy(map ->map.get("player_id").toString()));
        try {
           // 通过serverId切换数据源
           DataSourceHelper.useServerDatabase(serverId);
           //查询时间范围内 所有 渡劫 战力变更信息
            practiceDujieList = militaryStrengthMapper.selectPracticeDujieByTimeRange(createDateBegin, createDateEnd);
        } catch (Exception e) {
           log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
           DataSourceHelper.useDefaultDatabase();
        }
        List<MilitaryStrengthVO> militaryStrengthVOList = new ArrayList<>();
        for (Map map : practiceDujieList) {
            MilitaryStrengthVO militaryStrengthVO = new MilitaryStrengthVO();
            if(null == allRegisterUserListMap_playerId.get(map.get("player_id").toString())){
                militaryStrengthVO.setUserName("");
            }else{
                militaryStrengthVO.setUserName(allRegisterUserListMap_playerId.get(map.get("player_id").toString()).get(0).get("account").toString());
            }
            militaryStrengthVO.setMilitaryStrengthChange(map.get("reduce_practice_value").toString());
            militaryStrengthVO.setNewMilitary(map.get("after_practice_value").toString());
            militaryStrengthVO.setOriginalMilitary(map.get("before_practice_value").toString());
            militaryStrengthVO.setUserId(map.get("player_id").toString());
            militaryStrengthVO.setTime(map.get("create_time").toString());
            militaryStrengthVO.setOperation("渡劫");
            militaryStrengthVOList.add(militaryStrengthVO);
        }
        return militaryStrengthVOList;
    }

    /**
     * 获取所有战力变化列表
     *
     * @param serverId
     * @param createDateBegin
     * @param createDateEnd
     * @param channel
     */
    @Override
    public List<MilitaryStrengthVO> getMilitaryStrengVoAllList(String userName, int serverId, String createDateBegin, String createDateEnd, String channel) {
        List<Map>  militaryStrengVoAllList;
        if(StringUtils.isEmpty(userName)){
            militaryStrengVoAllList = militaryStrengthMapper.selectMilitaryStrengVoAll(createDateBegin, createDateEnd,logPlayerTable);
        }else{
            List<Map> registerUserMap = militaryStrengthMapper.selectRegisterUserByAccount(userName,channel, serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
            militaryStrengVoAllList = militaryStrengthMapper.selectMilitaryStrengVoAllByPlayerId(registerUserMap.get(0).get("player_id").toString(), createDateBegin, createDateEnd,logPlayerTable);
        }

        //查询时间范围内 所有注册的用户
        List<Map> allRegisterUserList = militaryStrengthMapper.selectAllRegisterUser(channel, serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
        Map<String, List<Map>> allRegisterUserListMap_playerId= allRegisterUserList.stream().collect(Collectors.groupingBy(map ->map.get("player_id").toString()));
        List<MilitaryStrengthVO> militaryStrengthVOList = new ArrayList<>();
        for (Map map : militaryStrengVoAllList) {
            MilitaryStrengthVO militaryStrengthVO = new MilitaryStrengthVO();
            if(null == allRegisterUserListMap_playerId.get(map.get("player_id").toString())){
                militaryStrengthVO.setUserName("");
            }else{
                militaryStrengthVO.setUserName(allRegisterUserListMap_playerId.get(map.get("player_id").toString()).get(0).get("account").toString());
            }
//            militaryStrengthVO.setMilitaryStrengthChange(map.get("reduce_practice_value").toString());
            militaryStrengthVO.setNewMilitary(map.get("value").toString());
//            militaryStrengthVO.setOriginalMilitary(map.get("before_practice_value").toString());
            militaryStrengthVO.setUserId(map.get("player_id").toString());
            militaryStrengthVO.setTime(map.get("create_time").toString());
            militaryStrengthVO.setOperation("");
            militaryStrengthVOList.add(militaryStrengthVO);
        }
        return militaryStrengthVOList;
    }
}