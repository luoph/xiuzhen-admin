package org.jeecg.modules.message.handle;

public interface ISendMsgHandle {

    /**
     * 推送消息
     *
     * @param receiver
     * @param title
     * @param content
     */
    void sendMsg(String receiver, String title, String content);
}
