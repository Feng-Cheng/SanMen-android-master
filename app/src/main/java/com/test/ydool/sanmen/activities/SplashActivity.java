package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/3.
 *
 * 闪屏页
 */

public class SplashActivity extends AppCompatActivity{


    private int count = 3;
    private static int WHAT_COUNT = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_COUNT) {
                if (count <= 0) {
                    goToMain();
                } else {
                    count--;
                    mHandler.sendEmptyMessageDelayed(WHAT_COUNT, 1000);
                }
            }
        }
    };

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        ScreenManager.getInstance().popActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        Message message = new Message();
        message.what = WHAT_COUNT ;
        mHandler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
