package org.jeecg.modules.message.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.SysMessageTemplate;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 消息模板
 * @date 2019-04-09
 */
public interface ISysMessageTemplateService extends JeecgService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
