package com.test.ydool.sanmen.utils;

import android.app.Activity;
import android.content.Context;

import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.bean.VedioBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ShareUtil {
    private static final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
            SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.WEIXIN_CIRCLE
//            SHARE_MEDIA.QQ,
//            SHARE_MEDIA.QZONE
    };


    public static void shareVedio(Context context, VedioBean vedioBean) {
        UMVideo video = new UMVideo(TerminalInfo.BASE_URL+vedioBean.getUrl());
        video.setTitle(vedioBean.getName());
        video.setDescription("活动视频");//视频的描述
        new ShareAction((Activity) context)
                .setDisplayList(displayList)
                .withText("分享视频")
                .withMedia(video)
                .open();
    }



}
