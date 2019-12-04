package org.jeecg.modules.message.handle.impl;

import org.jeecg.modules.message.handle.ISendMsgHandle;

public class SmsSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String receiver, String title, String content) {
        System.out.println("发短信");
    }

}
