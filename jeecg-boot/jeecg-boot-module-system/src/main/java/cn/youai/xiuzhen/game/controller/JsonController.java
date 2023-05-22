package cn.youai.xiuzhen.game.controller;

import cn.youai.basics.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取json文件
 *
 * @author jeecg-boot
 * @version V1.0
 * @since 2021-06-10
 */
@Slf4j
@RestController
@RequestMapping("json")
public class JsonController {
    private static final String CONFIG_FOLDER = "json/";

    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "file") String file,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "value") String value,
                                   HttpServletRequest req) {

        ClassPathResource classPathResource = new ClassPathResource(CONFIG_FOLDER + file + ".json");
        if (!classPathResource.exists()) {
            return Result.error("文件不存在!");
        }

        List<JSONObject> list = new ArrayList<>();
        try {
            byte[] fileData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String fileContent = new String(fileData, StandardCharsets.UTF_8);
            JSONArray jsonArray = JSON.parseArray(fileContent);
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                if (json.containsKey(name) && json.containsKey(value)) {
                    String itemName = json.getString(name);
                    String itemValue = json.getString(value);
                    if (StringUtils.isNotEmpty(itemName)) {
                        JSONObject item = new JSONObject();
                        item.put("name", itemName);
                        item.put("value", itemValue);
                        list.add(item);
                    }
                }
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.ok(list);
    }

}
