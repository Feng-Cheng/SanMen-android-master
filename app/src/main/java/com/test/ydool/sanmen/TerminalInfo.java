package com.test.ydool.sanmen;

import com.test.ydool.sanmen.bean.HomePhotoBean;
import com.test.ydool.sanmen.bean.NewsBean;
import com.test.ydool.sanmen.bean.NotificationBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class TerminalInfo {


    public static TerminalInfo adu;

    static {
        adu = new TerminalInfo();
    }


    public static int test = 0;//0测试1正式
    public static String UserId;
    public static String notice_position;
    public static String phone="";
    public static String photoUrl = "";
    public static final String CHANNEL_ID = "channelId";
    public static final int PAGE_SIZE = 10;
    public static int size = 0;
    public static int PhotoNum = 0;

    public static NotificationBean bean ;

    public static List<NewsBean> newsList ;
    public static List<HomePhotoBean> HomePhotoBean;
    public static List<NewsBean> loadingNewsList = new ArrayList<>();
    public static int scrollNum = 7;

//    public final String ydDataExportPath = SDCardUtils.getSDCardPath() + "文件导出" + File.separator;
    public static String BASE_URL = "http://zs.ydool.com:8082";
//    public static String BASE_URL = "http://192.168.0.108:8082";
//    public static String BASE_URL = "http://192.168.0.113:8082";
//    public static String BASE_URL = "http://192.168.0.105:8088";
//    public static String BASE_URL = "http://192.168.1.39:8082";

}