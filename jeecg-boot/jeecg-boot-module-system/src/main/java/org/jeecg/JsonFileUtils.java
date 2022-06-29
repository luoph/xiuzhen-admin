package org.jeecg;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.jeecg.common.constant.TimeConstant;

import java.nio.charset.StandardCharsets;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
public final class JsonFileUtils {

    private JsonFileUtils() {
    }

    /**
     * 写json文件
     *
     * @param object   数据对象
     * @param folder   文件目录
     * @param fileName 不包含 .json 的文件名
     */
    public static void writeJsonFile(Object object, String folder, String fileName) {
        String content = JSON.toJSONString(object, TimeConstant.DEFAULT_TIME_FORMAT, JSONWriter.Feature.WriteNonStringValueAsString);
        String filePath = folder + "/" + fileName + ".json";
        if (FileUtil.exist(filePath)) {
            FileUtil.del(filePath);
        }
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }

    public static void deleteJsonFile(String folder, String fileName) {
        String filePath = folder + "/" + fileName + ".json";
        if (FileUtil.exist(filePath)) {
            FileUtil.del(filePath);
        }
    }

}
