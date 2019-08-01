package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.dou361.dialogui.listener.DialogUIListener;
import com.dou361.dialogui.listener.OnItemClickListener;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.InformPutBaseAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.User;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/4.
 *
 * 信息发布
 */

public class InformReportActivity extends BaseActivity implements OnItemClickListener{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ll_inform_report_type)
    LinearLayout llType;
    @BindView(R.id.tv_inform_report_putman)
    TextView tvName;
    @BindView(R.id.tv_other_report_file_name)
    TextView tvFileName;
    @BindView(R.id.et_inform_report_title)
    EditText etTitle;
    @BindView(R.id.et_inform_report_notice_content)
    EditText edContent;
    @BindView(R.id.et_inform_report_remarks)
    EditText edRemarks;
    @BindView(R.id.tv_inform_report_type)
    TextView tvType;
    @BindView(R.id.iv_report)
    ImageView ivReport;
    @BindView(R.id.btn_report)
    Button btnReport;
    @BindView(R.id.tv_inform_report_tonotice_pepole)
    TextView tvAnnocePepole;

    private String title = "";
    private String content = "";
    private String remark = "";
    private Uri filePath;
    private int type = 0;
    String token = "";
    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    protected static final int PERMISSION_CODE = 100;

    private static final int REQUEST_CODE_CHOOSE = 0;


    private List<Uri> mSelected;
    private BuildBean loading;
    String[] words2;
    boolean[] booleans;
    boolean[] booleans2;

    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

//    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_inform_report);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.inform_put);
        tvTitle.setTextColor(0xfffaa8ae);

        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");
        token = spUtils.getString("token");

//        Calendar calendar = Calendar.getInstance();
//        //获取系统的日期
//        //年
//        int year = calendar.get(Calendar.YEAR);
//        //月
//        int month = calendar.get(Calendar.MONTH)+1;
//        //日
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        //获取系统时间
//        //小时
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        //分钟
//        int minute = calendar.get(Calendar.MINUTE);
//
//        time = year+"-"+month+"-"+day+" "+hour+":"+minute;
//
//        tvTime.setText(time);
        tvName.setText(spUtils.getString("name"));

        if (spUtils.getInt("quanxian") == 0){
            words2 = new String[]{"全部人", "县特定人群","乡镇特定人群","村特定人群"};
            booleans=new boolean[]{true,false, false, false};
            booleans2=new boolean[]{true,false, false, false};
        }else{
            words2 = new String[]{"全部人", "乡镇特定人群","村特定人群"};
            booleans=new boolean[]{true,false, false};
            booleans2=new boolean[]{true,false, false};
        }
        for (int i = 0; i < words2.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("text", words2[i]);
            mData.add(item);
        }
        tvAnnocePepole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showMdMultiChoose((Activity) mContext,
                        "发送人群",
                        words2,
                        booleans,
                        true,
                        false,new DialogUIListener() {
                        @Override
                        public void onPositive() {
                            String text = "";
                            if (booleans[0]){
                                text+=words2[0];
                            }else {
                                for (int i = 0 ; i <words2.length ; i++){
                                    if (booleans[i]){
                                        text += words2[i]+",";
                                    }
                                }
                                if (!text.equals("")){
                                    text = text.substring(0,text.length()-1);
                                }else {
                                    ToastUtil.showMessage(mContext,"请选择发送对象！");
                                    if (spUtils.getInt("quanxian") == 0){
                                        booleans=new boolean[]{true,false, false, false};
                                    }else{
                                        booleans=new boolean[]{true,false, false};
                                    }
                                    text+=words2[0];
                                }
                            }
                            for (int i =0 ; i< booleans.length;i++)
                                booleans2[i] = booleans[i];
                            tvAnnocePepole.setText(text);
                        }

                        @Override
                        public void onNegative() {
                            for (int i =0 ; i< booleans2.length;i++)
                                booleans[i] = booleans2[i];
                        }
                }).show();
            }
        });


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] words2 = new String[]{"通知", "政策"};
                DialogUIUtils.showSingleChoose((Activity) mContext, "通知类型", type, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        type=position;
                        tvType.setText(words2[position]);
                    }
                }).show();
            }
        });

        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(mContext, permissions)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
                } else {
                    EasyPermissions.requestPermissions((Activity) mContext, "请给予权限",
                            PERMISSION_CODE, permissions);
                }
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();
                content = edContent.getText().toString();
                remark = edRemarks.getText().toString();
                loading = DialogUIUtils.showLoading(mContext,"正在发布中...",true,false,false,true);
                loading.show();
                String names = "";
                if (booleans[0]){
                    names = "0";
                }else {
                    for (int i = 1 ; i < booleans.length ; i++){
                        if (booleans[i]&&booleans.length==4){
                            names += (i+",");
                        }else if (booleans[i]&&booleans.length==3){
                            names += ((i+1)+",");
                        }
                    }
                    names = names.substring(0,names.length()-1);
                }
                Subscription subscription = userRepository.InformReport(token,title,content,type,remark,filePath,mContext,names)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.getCode() == 0){
                                ToastUtil.showLongMessage(mContext,"上传成功");
                                finish();
                            }else {
                                ToastUtil.showLongMessage(mContext,"上传失败！"+response.getMsg());
                            }
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"上传失败");
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
                tvFileName.setText(path[path.length-1]);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position==0){
            for (int i = 1; i <booleans.length;i++){
                booleans[i] = false;
            }
        }else {
            if (booleans[0]){
                booleans[0]=false;
            }
        }
    }
}

