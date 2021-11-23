package org.jeecg.common.constant;

import cn.youai.basics.model.ResponseCode;

/**
 * 接口报错或者业务异常响应给客户端的接口
 * 参考 https://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/basic-info/return-codes
 *
 * @author buliangliang
 */
public class ErrorCode extends ResponseCode {

    public ErrorCode(Integer code, String desc) {
        super(code, desc);
    }
}
