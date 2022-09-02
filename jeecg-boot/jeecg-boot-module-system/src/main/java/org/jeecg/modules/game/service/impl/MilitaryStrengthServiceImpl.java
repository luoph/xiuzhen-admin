package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.youai.server.constant.AttrType;
import cn.youai.server.utils.DateUtils;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.MilitaryStrengthVO;
import org.jeecg.modules.game.mapper.MilitaryStrengthMapper;
import org.jeecg.modules.game.service.IMilitaryStrengthService;
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

@Slf4j
@Service
public class MilitaryStrengthServiceImpl implements IMilitaryStrengthService {
    @Resource
    MilitaryStrengthMapper militaryStrengthMapper;

    @Override
    public List<MilitaryStrengthVO> getMilitaryStrengVoDujieList(int serverId, String createDateBegin, String createDateEnd, String channel) {
        List<Map> practiceDuJieList = null;
        //查询时间范围内 所有注册的用户
        List<Map> allRegisterUserList = militaryStrengthMapper.selectAllRegisterUser(channel, serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
        Map<String, List<Map>> allRegisterUserListMapPlayerId = allRegisterUserList.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            //查询时间范围内 所有 渡劫 战力变更信息
            practiceDuJieList = militaryStrengthMapper.selectPracticeDujieByTimeRange(createDateBegin, createDateEnd);
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        List<MilitaryStrengthVO> militaryStrengthVOList = new ArrayList<>();
        if (null == practiceDuJieList) {
            return militaryStrengthVOList;
        }
        for (Map map : practiceDuJieList) {
            MilitaryStrengthVO militaryStrengthVO = new MilitaryStrengthVO();
            if (null == allRegisterUserListMapPlayerId.get(map.get("player_id").toString())) {
                militaryStrengthVO.setUserName("");
            } else {
                militaryStrengthVO.setUserName(allRegisterUserListMapPlayerId.get(map.get("player_id").toString()).get(0).get("account").toString());
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
     */
    @Override
    public List<MilitaryStrengthVO> getMilitaryStrengVoAllList(String userName, int serverId, String createDateBegin, String createDateEnd, String channel) {
        List<Map> militaryStrengVoAllList;
        List<MilitaryStrengthVO> militaryStrengthVOList = new ArrayList<>();
        if (StringUtils.isEmpty(userName)) {
            militaryStrengVoAllList = militaryStrengthMapper.selectMilitaryStrengVoAll(serverId, createDateBegin, createDateEnd);
        } else {
            List<Map> registerUserMap = militaryStrengthMapper.selectRegisterUserByName(userName, channel, serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
            if (CollectionUtil.isEmpty(registerUserMap)) {
                return militaryStrengthVOList;
            }
            //这个名字下所有player_id集合
            List<String> playerIdCollect = registerUserMap.stream().map(map -> map.get("player_id").toString()).collect(Collectors.toList());
            String playerIdCollectString = "";
            for (int i = 0; i < playerIdCollect.size(); i++) {
                if (i == playerIdCollect.size() - 1) {
                    playerIdCollectString = playerIdCollectString + playerIdCollect.get(i);
                } else {
                    playerIdCollectString = playerIdCollectString + playerIdCollect.get(i) + ",";
                }
            }
            if (StringUtils.isEmpty(playerIdCollectString)) {
                return militaryStrengthVOList;
            }
            militaryStrengVoAllList = militaryStrengthMapper.selectMilitaryStrengVoAllByPlayerId(serverId, "(" + playerIdCollectString + ")", createDateBegin, createDateEnd);
        }

        //查询时间范围内 所有注册的用户
        List<Map> allRegisterUserList = militaryStrengthMapper.selectAllRegisterUserNoChannel(serverId, "2000-01-01", DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN));
        Map<String, List<Map>> allRegisterUserListMapPlayerId = allRegisterUserList.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
        for (Map map : militaryStrengVoAllList) {
            MilitaryStrengthVO militaryStrengthVO = new MilitaryStrengthVO();
            if (null == allRegisterUserListMapPlayerId.get(map.get("player_id").toString())) {
                militaryStrengthVO.setUserName("");
            } else {
                militaryStrengthVO.setUserName(allRegisterUserListMapPlayerId.get(map.get("player_id").toString()).get(0).get("name").toString());
            }
            militaryStrengthVO.setMilitaryStrengthChange(map.get("param_2").toString());
            militaryStrengthVO.setNewMilitary(map.get("param_3").toString());
            militaryStrengthVO.setOriginalMilitary(map.get("param_1").toString());
            militaryStrengthVO.setUserId(map.get("player_id").toString());
            militaryStrengthVO.setTime(map.get("create_time").toString());
            AttrType attrType = AttrType.valueOf(Integer.parseInt(map.get("value").toString()));
            militaryStrengthVO.setOperation(attrType != null ? attrType.getName() : "未知操作");
            militaryStrengthVOList.add(militaryStrengthVO);
        }
        return militaryStrengthVOList;
    }
}