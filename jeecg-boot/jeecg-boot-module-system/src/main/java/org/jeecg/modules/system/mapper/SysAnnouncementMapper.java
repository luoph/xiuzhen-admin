package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysAnnouncement;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 系统通告表
 * @date 2019-01-02
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {


    List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId") String userId, @Param("msgCategory") String msgCategory);

}
