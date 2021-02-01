package com.example.mynote.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

    public static final String regex = "[\\u4e00-\\u9fa5]";

    /**
     * 提取字符串中的中文和数字
     * @author  feiyang
     * @param content
     * @return  java.lang.String
     * @date    2020/1/3
     * @throws
     */
    public static String extractCnAndDigi(String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuilder stringBuilder = new StringBuilder();
        while(matcher.find()) {
            stringBuilder.append(matcher.group());
        }
        return stringBuilder.toString();
    }
}