package cn.youai.xiuzhen.entity.common;

import com.alibaba.fastjson.JSONObject;

/**
 * 配置数据接口
 *
 * @author luopeihuan
 */
public interface ConfigData {
   /* *//**
     * 加载后
     *
     * @return
     *//*
    void onload();*/

    /**
     * 加载后回调
     *
     * @param data 原始 json 数据
     */
    void onload(JSONObject data);
}
