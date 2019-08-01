package com.test.ydool.sanmen.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/28.
 */

public class SystemShareUtils {
    // 微信朋友圈发送界面
    public static final String NAME_ACTIVITY_WECHAT_CIRCLE_PUBLISH = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    // 微信好友发送界面
    public static final String NAME_ACTIVITY_WECHAT_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI";

    /**
     * 分享
     * @param context
     * @param activityName 分享类别。
     *            NAME_ACTIVITY_WECHAT_CIRCLE_PUBLISH表示朋友圈发布界面；NAME_ACTIVITY_WECHAT_FRIEND表示发送给好友。
     * @param text      文字内容，只对朋友圈有效
     * @param files     本地图片全路径列表
     */
    public static void shareTextAndPicToWechat(Context context, String activityName, String text, List<String> files) {
        String packageName = "com.tencent.mm"; // 微信包名

        if (isWeixinAvilible(context)) {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName(packageName, activityName);
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");

            ArrayList<Uri> imageUris = new ArrayList<>();
            if (null != files) {
                for (String f : files) {
                    // 7.0及以上需要使用MediaStore.Images.Media.insertImage()转化一下uri？
                    // 测试7.0  7.1 未发现这样有bug
                    imageUris.add(Uri.fromFile(new File(f)));
                }
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            intent.putExtra("Kdescription", text); // 作用于发朋友圈，对好友不会有影响
            context.startActivity(intent);
        } else {
            ToastUtil.showMessage(context,"请先安装微信APP");
        }
    }

    // 判断手机内是否安装了微信APP
    private static boolean isWeixinAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}
