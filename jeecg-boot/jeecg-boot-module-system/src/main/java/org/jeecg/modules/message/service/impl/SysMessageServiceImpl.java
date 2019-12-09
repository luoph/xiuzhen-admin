package org.jeecg.modules.message.service.impl;

import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 消息
 * @date 2019-04-09
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
