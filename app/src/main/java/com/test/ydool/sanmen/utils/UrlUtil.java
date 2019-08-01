package com.test.ydool.sanmen.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/8/22.
 */

public class UrlUtil {
    private final static String ENCODE = "GBK";
    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @return void
     * @author lifq
     * @date 2015-3-17 下午04:09:16
     */
    public static void main(String[] args) {
        String str = "http://zs.ydool.com:8082/upload/Announcement/三宣〔2018〕35号(关于开展“最美文化礼堂人”评选工作的通知.doc)11.doc";
        System.out.println(getURLEncoderString(str));
        System.out.println(getURLDecoderString(str));

    }
}
