package cn.youai.xiuzhen.utils;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @ClassName StrHtmlUtil
 * @Description html字符串工具
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/3 5:10 下午
 */
public class StrHtmlUtils {

    public static String formatNoticeHtml(String input) {
        input = input.replace("\n<p>", "<br />&nbsp;<br />");
        input = input.replace("<br/>", "<br />");
        input = input.replace("\r\n", "<br />");
        input = input.replace("\n", "<br />");
        input = input.replace("<p>", "");
        input = input.replace("</p>", "");
        input = input.replace("&lsquo;", "");
        input = input.replace("&rsquo;", "");
        input = input.replace("&nbsp;", "");
        input = input.replace("......", "...");
        // strong 标签无效
        input = input.replace("<strong>", "");
        input = input.replace("</strong>", "");
        // 越南语特殊字符
        input = StringEscapeUtils.unescapeHtml(input);
        input = input.replace("&", "&amp;");
        return input;
    }
}
