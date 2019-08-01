package com.test.ydool.sanmen.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast util class.
 * 狂按都不会出问题的Toast
 *
 * @author zhuwp arui319
 * @version 2017/6/28
 */

public class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;
    private static long shortTime = 2000;
    private static long longTime = 3500;

    private static Object synObj = new Object();
    private static Runnable r = new Runnable() {
        public void run() {
            try {
                if (toast != null) {
                    System.out.println("toast != null");
                    toast.cancel();
                } else {
                    System.out.println("toast = null");
                }
                toast = null;//toast隐藏后，将其置为null
            } catch (Exception e) {
//                LogTool.ToSD("ToastUtil隐藏toast时出现异常：" + e.toString());
                e.printStackTrace();
            }
        }
    };

    public static void showMessage(final Context act, final String msg) {

        showMessage(act, msg, Toast.LENGTH_SHORT, shortTime);
    }

    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT, shortTime);
    }

    public static void showMessage(final Context act, final String msg, long time) {
        showMessage(act, msg, Toast.LENGTH_SHORT, time);
    }

    public static void showMessage(final Context act, final int msg, long time) {
        showMessage(act, msg, Toast.LENGTH_SHORT, time);
    }

    public static void showLongMessage(final Context act, final String msg) {

        showMessage(act, msg, Toast.LENGTH_LONG, longTime);
    }

    public static void showLongMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_LONG, longTime);
    }

    public static void showLongMessage(final Context act, final String msg, long time) {
        showMessage(act, msg, Toast.LENGTH_LONG, time);
    }

    public static void showLongMessage(final Context act, final int msg, long time) {
        showMessage(act, msg, Toast.LENGTH_LONG, time);
    }


    public static void showMessage(final Context act, final String msg,
                                   final int len, long time) {
        if (time < 2000) {
            handler.removeCallbacks(r);
            handler.postDelayed(r, time);//隐藏toast
        }
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
                                // toast.cancel();
                                toast.setText(msg);
                                toast.setDuration(len);
                            } else {
                                toast = Toast.makeText(act, msg, len);
                            }
                            toast.show();
                            // LogTool.ToSD("toast显示：" + msg);
                        }
                    }
                });
            }
        }).start();
    }

    public static void showMessage(final Context act, final int msg,
                                   final int len, long time) {
        if (time < 2000) {
            handler.removeCallbacks(r);
            handler.postDelayed(r, time);//延迟隐藏toast
        }
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
                                toast.cancel();
                                toast.setText(msg);
                                toast.setDuration(len);
                            } else {
                                toast = Toast.makeText(act, msg, len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }
}
