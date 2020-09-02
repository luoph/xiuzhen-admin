package org.jeecg.common.okhttp;

import cn.hutool.http.HttpStatus;
import cn.youai.commons.utils.ObjectReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-11-21.
 */
@Slf4j
@Component
public class OkHttpHelper {

    private static final int HTTP_OK = HttpStatus.HTTP_OK;
    private static final MediaType MIME_JSON = MediaType.parse("application/json; charset=utf-8");


    private static final ObjectReference<OkHttpHelper> REFERENCE = new ObjectReference<>();

    @Resource
    private OkHttpClient okHttpClient;

    /**
     * 获得实例
     *
     * @return {@link OkHttpHelper}
     */
    private static OkHttpHelper getInstance() {
        return REFERENCE.get();
    }

    @PostConstruct
    void init() {
        REFERENCE.set(this);
    }

    /**
     * get 请求
     *
     * @param url 请求url
     * @return 请求结果
     */
    public static String get(String url) {
        return doGet(url);
    }

    /**
     * get
     *
     * @param url     请求url
     * @param queries 请求参数，在浏览器？后面的数据，没有可以传null
     * @return 请求结果
     */
    public static String get(String url, Map<String, Object> queries) {
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, Object> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }

        return doGet(sb.toString());
    }

    /**
     * get 请求
     *
     * @param url  请求url
     * @param json 请求参数
     * @return 请求结果
     */
    public static String get(String url, JSONObject json) {
        StringBuilder sb = new StringBuilder(url);
        if (json != null && json.size() > 0) {
            boolean firstFlag = true;
            for (String key : json.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(json.getString(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(json.getString(key));
                }
            }
        }

        return doGet(sb.toString());
    }

    private static String doGet(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = getInstance().okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == HTTP_OK && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("doGet error, url:" + url, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * post 请求
     *
     * @param url  地址
     * @param json json 参数
     * @return
     */
    public static String post(String url, JSONObject json) {
        RequestBody requestBody = RequestBody.create(MIME_JSON, json.toString());
        return doPost(url, requestBody);
    }

    public static String post(String url, Object object) {
        String body = JSON.toJSONString(object);
        RequestBody requestBody = RequestBody.create(MIME_JSON, body);
        return doPost(url, requestBody);
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        return doPost(url, builder.build());
    }

    private static String doPost(String url, RequestBody requestBody) {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        try {
            response = getInstance().okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == HTTP_OK && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("doPost error, url:" + url, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * post 上传文件
     *
     * @param url
     * @param params
     * @param fileType
     * @return
     */
    public static String postFile(String url, Map<String, Object> params, String fileType) {
        String responseBody = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
                    continue;
                }
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = null;
        try {
            response = getInstance().okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == HTTP_OK && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("postFile error, url:" + url, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
}