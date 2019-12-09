package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysAnnouncement;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 系统通告表
 * @date 2019-01-02
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

    void saveAnnouncement(SysAnnouncement sysAnnouncement);

    boolean upDateAnnouncement(SysAnnouncement sysAnnouncement);

    void saveSysAnnouncement(String title, String msgContent);

    Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory);


}
