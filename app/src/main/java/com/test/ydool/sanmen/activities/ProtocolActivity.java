package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ProtocolActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
    }
}
