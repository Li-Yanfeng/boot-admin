package org.utility.util;

import cn.hutool.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 翻译工具类
 *
 * @author Li Yanfeng
 */
public class TranslatorUtils {

    /**
     * 中文翻译英文
     */
    public static String english2Chinese(String word) {
        try {
            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=en" +
                    "&tl=zh-CN" +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

            return translate(url);
        } catch (Exception e) {
            return word;
        }
    }

    /**
     * 中文翻译英文
     */
    public static String chinese2English(String word) {
        try {
            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=zh-CN" +
                    "&tl=en" +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

            return translate(url);
        } catch (Exception e) {
            return word;
        }
    }

    /**
     * 翻译
     */
    public static String translate(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return parseResult(response.toString());
    }

    /**
     * 解析结果
     */
    private static String parseResult(String inputJson) {
        JSONArray jsonArray2 = (JSONArray) new JSONArray(inputJson).get(0);
        StringBuilder result = new StringBuilder();
        for (Object o : jsonArray2) {
            result.append(((JSONArray) o).get(0).toString());
        }
        return result.toString();
    }
}
