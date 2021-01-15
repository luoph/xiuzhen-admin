package org.jeecg.modules.game.constant;

import com.alibaba.fastjson.JSONArray;

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
 * itemId 和 名称 对应的map集合类
 * @author 胡立
 */
public class ItemId {
    /**
     * item_id  name
     */
    private static final Map<String, String> ITEM_ID_TO_NAME_MAP = new HashMap<>();
    /**
     * name item_id
     */
    private static final Map<String, String> ITEM_NAME_TO_ID_MAP = new HashMap<>();
    public ItemId() throws UnsupportedEncodingException {
        if (ITEM_ID_TO_NAME_MAP.size() <= 0 ) {
            InputStream in = this.getClass().getResourceAsStream("/json/item.json");
            String file1 = new BufferedReader(new InputStreamReader(in, "utf-8"))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            String dest = "";
            if (file1!=null) {
                Pattern p = compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(file1);
                dest = m.replaceAll("");
            }
            file1 = dest;
            JSONArray jsonArray = JSONArray.parseArray(file1);
            //道具id和名称map
            for (int i = 0; i < jsonArray.size(); i++) {
                ITEM_ID_TO_NAME_MAP.put(jsonArray.getJSONObject(i).getString("item_id"),jsonArray.getJSONObject(i).getString("name"));
                ITEM_NAME_TO_ID_MAP.put(jsonArray.getJSONObject(i).getString("name"),jsonArray.getJSONObject(i).getString("item_id"));
            }
        }
    }

    public Map<String, String> getItemIdToNameMap() {
        return ITEM_ID_TO_NAME_MAP;
    }
    public Map<String, String> getItemNameToIdMap() {
        return ITEM_NAME_TO_ID_MAP;
    }
}