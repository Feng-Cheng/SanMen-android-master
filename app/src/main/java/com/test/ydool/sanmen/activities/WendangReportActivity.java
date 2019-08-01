package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 文档管理页面
 */

public class WendangReportActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_wendang_adm_name)
    TextView tvAdmName;
    @BindView(R.id.tv_file_report)
    TextView tvFileReport;
    @BindView(R.id.tv_wendang_live_xiangzheng)
    TextView tvLiveXiangzheng;
    @BindView(R.id.tv_inform_report_notice_time)
    TextView tvTime;
    @BindView(R.id.et_wendang_content)
    EditText etContent;
    @BindView(R.id.iv_report)
    ImageView ivReport;
    @BindView(R.id.btn_report)
    Button btnReport;

    private Uri filePath;


    private CompositeSubscription subscriptions;

    private UserRepository userRepository;
    private String token="";
    private String user_name="";
    private String name="";
    private String position_id="";
    private String time="";
    private String content="";

    private BuildBean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_wendang_report);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.wendang_manager);
        tvTitle.setTextColor(0xfffaa8ae);

        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");

        tvAdmName.setText(spUtils.getString("Adm_name"));
        tvLiveXiangzheng.setText(spUtils.getString("pname"));

        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvTime.setText(year+"-"+month+"-"+day);


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loading = DialogUIUtils.showLoading(mContext,"正在上传中...",true,false,false,true);
        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPermission();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                SPUtils spUtils = SPUtils.getInstance("sanMen");
                String token = spUtils.getString("token");
                user_name = tvAdmName.getText().toString();
                position_id = spUtils.getString("position_id");
                time = tvTime.getText().toString();
                content = etContent.getText().toString();
                Subscription subscription = userRepository.saveaReportve(token,user_name,position_id,time,content,filePath,mContext)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if (response.getCode() == 0){
                                ToastUtil.showLongMessage(mContext,"上传成功!");
                                DialogUIUtils.dismiss(loading);
                                finish();
                            }else {
                                ToastUtil.showLongMessage(mContext,"上传失败!"+response.getMsg());
                                DialogUIUtils.dismiss(loading);
                            }
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                subscriptions.add(subscription);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = uri;
                String[] path = uri.getPath().toString().split("/");
                tvFileReport.setText(path[path.length-1]);
            }
        }
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
