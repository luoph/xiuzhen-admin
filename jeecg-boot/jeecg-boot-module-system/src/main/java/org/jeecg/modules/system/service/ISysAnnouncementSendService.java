package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.model.AnnouncementSendModel;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 用户通告阅读标记表
 * @date 2019-02-21
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

    List<String> queryByUserId(String userId);

    /**
     * 获取我的消息
     *
     * @param announcementSendModel
     * @return
     */
    Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page, AnnouncementSendModel announcementSendModel);

}
