package com.test.ydool.sanmen.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/11/30.
 *
 * 版本信息页面
 */

public class VersionDownActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_copy_address)
    Button btnCopy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_version_download);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("地址下载");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String appversion = AppUtils.getAppVersionName();

        tvVersion.setText("当前版本号："+appversion);

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("下载地址", "http://zs.ydool.com:8082/download");
//                ToastUtil.showMessage(mContext,"复制成功");
                cm.setPrimaryClip(clipData);
                if (!cm.hasPrimaryClip()) {
                    ToastUtil.showMessage(mContext,"剪贴板没有内容");
                }else {
                    ToastUtil.showMessage(mContext,"复制成功");
                }
            }
        });

    }


}
