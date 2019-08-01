package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/6.
 *
 * 活动预告发布
 */

public class ActivityShowPutActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ll_activty_announce_time)
    LinearLayout llTime;
    @BindView(R.id.tv_activity_put_time)
    TextView tvTime;
    @BindView(R.id.ed_adm_name)
    TextView tvBlowarea;
    @BindView(R.id.ed_activity_organizer)
    EditText edOrganizer;
    @BindView(R.id.ed_activity_theme)
    EditText edTheme;
    @BindView(R.id.ed_activity_announce_content)
    EditText edContent;
    @BindView(R.id.et_remarks)
    EditText edRemarks;
    @BindView(R.id.btn_report)
    Button btnReport;
    @BindView(R.id.iv_iv1)
    ImageView iv1;
    @BindView(R.id.iv_iv2)
    ImageView iv2;
    @BindView(R.id.iv_iv3)
    ImageView iv3;
    @BindView(R.id.iv_iv4)
    ImageView iv4;

    int mYear;
    int mMonth;
    int mDay;
    public String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected static final int PERMISSION_CODE = 100;

    private CompositeSubscription subscriptions;
    private Uri imgPath;
    private UserRepository userRepository;

    private List<Uri> mSelected;
    private static int REQUEST_CODE_CHOOSE = 0;

    private BuildBean loading;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 ){
                if (mSelected!=null){
                    Glide.with(mContext).load(mSelected.get(0)).into(iv1);
                    if (mSelected.size()>1)
                        Glide.with(mContext).load(mSelected.get(1)).into(iv2);
                    if (mSelected.size()>2)
                        Glide.with(mContext).load(mSelected.get(2)).into(iv3);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_show_put);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.activty_announce);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        tvTime.setText(mYear + "-" + (mMonth+1) + "-" + mDay);

        SPUtils spUtils = SPUtils.getInstance("sanMen");
        tvBlowarea.setText(spUtils.getString("pname"));

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(mContext, permissions)) {
                    Matisse.from(ActivityShowPutActivity.this)
                            .choose(MimeType.of(MimeType.JPEG,MimeType.PNG)) // 选择 mime 的类型
                            .countable(true)
                            .maxSelectable(3) // 图片选择的最多数量
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.photo_big))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f) // 缩略图的比例
                            .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                            .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                } else {
                    EasyPermissions.requestPermissions((Activity) mContext, "请给予权限",
                            PERMISSION_CODE, permissions);
                }
            }
        });

        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, onDateSetListener, mYear, mMonth, mDay).show();
            }
        });


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils spUtils = SPUtils.getInstance("sanMen");
                String aud = "";
                String town = "";
                if (spUtils.getInt("quanxian") == 1 || spUtils.getInt("quanxian") == 2){
                    town = spUtils.getString("position_id");
                }
                if (spUtils.getInt("quanxian") == 2){
                    aud = spUtils.getString("auditorid");
                }
                if (edTheme.getText().toString().equals("")){
                    ToastUtil.showMessage(mContext,"请填写活动主题！");
                    return;
                }
                if (edContent.getText().toString().equals("")){
                    ToastUtil.showMessage(mContext,"请填写活动内容！");
                    return;
                }
                loading = DialogUIUtils.showLoading(mContext,"正在上传中...",true,false,false,true);
                loading.show();
                Subscription subscription = userRepository
                        .ActivityPreViewadSave(spUtils.getString("token"),aud,town,edTheme.getText().toString()
                                ,edContent.getText().toString(),"",tvTime.getText().toString()+" 00:00",edOrganizer.getText().toString()
                                ,edRemarks.getText().toString(),mSelected,mContext)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response .getCode() == 0){
                                ToastUtil.showMessage(mContext,"发布成功");
                                finish();
                            }else {
                                ToastUtil.showMessage(mContext,"发布失败"+response.getMsg());
                            }
                            DialogUIUtils.dismiss(loading);
                        },throwable ->{
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                subscriptions.add(subscription);
            }
        });

    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).append("-").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).append("-").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).append("-").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).append("-").toString();
                }

            }
            tvTime.setText(days);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            imgPath = mSelected.get(0);
            Log.d("Matisse", "mSelected: " + mSelected);
            Message message = new Message();
            message.what = 0;
            mHandler.sendMessage(message);
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
                if (!EasyPermissions.hasPermissions(this, permissions)) {
                    //这里响应的是AppSettingsDialog点击取消按钮的效果
                    finish();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这里需要重新设置Rationale和title，否则默认是英文格式
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        } else if (!EasyPermissions.hasPermissions(this, permissions)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

}
