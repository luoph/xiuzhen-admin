package org.jeecg.modules.game.constant;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * 等级 和 阶级名称 对应关系
 *
 * @author 胡立
 */
public class PracticeExp {
    /**
     * item_id  name
     */
    private static final Map<Long, String> LEVEL_TO_NAME = new HashMap<>();

    public PracticeExp() throws UnsupportedEncodingException {
        if (LEVEL_TO_NAME.size() <= 0) {
            InputStream in = this.getClass().getResourceAsStream("/json/practice_exp.json");
            String file1 = new BufferedReader(new InputStreamReader(in, "utf-8"))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            String dest = "";
            if (file1 != null) {
                Pattern p = compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(file1);
                dest = m.replaceAll("");
            }
            file1 = dest;
            JSONArray jsonArray = JSON.parseArray(file1);
            //道具id和名称map
            for (int i = 0; i < jsonArray.size(); i++) {
                LEVEL_TO_NAME.put(jsonArray.getJSONObject(i).getLong("practice_level"), jsonArray.getJSONObject(i).getString("practice_name"));
            }
        }
    }

    public Map<Long, String> getLevelToNameMap() {
        return LEVEL_TO_NAME;
    }
}