package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 系统通告表
 * @date 2019-01-02
 */
@Service
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;

    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Transactional
    @Override
    public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
            sysAnnouncementMapper.insert(sysAnnouncement);
        } else {
            // 1.插入通告表记录
            sysAnnouncementMapper.insert(sysAnnouncement);
            // 2.插入用户通告阅读标记表记录
            String userId = sysAnnouncement.getUserIds();
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            String anntId = sysAnnouncement.getId();
            Date refDate = new Date();
            IntStream.range(0, userIds.length).forEach(i -> {
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(userIds[i]);
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                announcementSend.setReadTime(refDate);
                sysAnnouncementSendMapper.insert(announcementSend);
            });
        }
    }

    /**
     * 编辑消息信息
     */
    @Transactional
    @Override
    public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement) {
        // 1.更新系统信息表数据
        sysAnnouncementMapper.updateById(sysAnnouncement);
        String userId = sysAnnouncement.getUserIds();
        if (oConvertUtils.isNotEmpty(userId) && sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_USER)) {
            // 2.补充新的通知用户数据
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            String anntId = sysAnnouncement.getId();
            Date refDate = new Date();
            for (String id : userIds) {
                LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
                queryWrapper.eq(SysAnnouncementSend::getUserId, id);
                List<SysAnnouncementSend> announcementSends = sysAnnouncementSendMapper.selectList(queryWrapper);
                if (announcementSends.size() <= 0) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(anntId);
                    announcementSend.setUserId(id);
                    announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                    announcementSend.setReadTime(refDate);
                    sysAnnouncementSendMapper.insert(announcementSend);
                }
            }
            // 3. 删除多余通知用户数据
            Collection<String> delUserIds = Arrays.asList(userIds);
            LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
            queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
            sysAnnouncementSendMapper.delete(queryWrapper);
        }
        return true;
    }

    // 流程执行完成保存消息通知
    @Override
    public void saveSysAnnouncement(String title, String msgContent) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitle(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender("JEECG BOOT");
        announcement.setPriority(CommonConstant.PRIORITY_L);
        announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        sysAnnouncementMapper.insert(announcement);
    }

    @Override
    public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory) {
        return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(page, userId, msgCategory));
    }

}
