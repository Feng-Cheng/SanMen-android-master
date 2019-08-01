package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AuditoriaAdminBean;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.VillageButtonBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2018/5/31.
 *
 * 礼堂风采提交
 */

public class AuditorReportActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    protected static final int PERMISSION_CODE = 100;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.btn_report)
    Button btnReport;
//    @BindView(R.id.ed_adm_name)
//    TextView tvAdmName;
    @BindView(R.id.ed_auditor_adress)
    EditText tvAuditorAdress;
    @BindView(R.id.ed_auditor_intdro)
    EditText tvAuditorIntdro;
//    @BindView(R.id.ed_xiangzheng)
//    TextView tvXiangZheng;
    @BindView(R.id.ed_team_build)
    EditText etTeamBuild;
    @BindView(R.id.et_village_situation)
    EditText etVillSit;
    @BindView(R.id.et_lishihui)
    EditText etLishihui;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.iv_iv4)
    ImageView iv4;
    @BindView(R.id.iv_iv3)
    ImageView iv3;
    @BindView(R.id.iv_iv2)
    ImageView iv2;
    @BindView(R.id.iv_iv1)
    ImageView iv1;
    @BindView(R.id.tv_work_statics_village)
    TextView tvViView;
    @BindView(R.id.tv_work_statics_check_type)
    TextView tvCtView;
    @BindView(R.id.tv_work_admin)
    TextView tcAdmin;
    @BindView(R.id.tv_audtior_name)
    TextView tcAudtiorName;
    @BindView(R.id.iv_auding_auditor_photo_del1)
    ImageView ivDelet1;
    @BindView(R.id.iv_auding_auditor_photo_del2)
    ImageView ivDelet2;
    @BindView(R.id.iv_auding_auditor_photo_del3)
    ImageView ivDelet3;



    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private AuditoriumBean auditoriumBean;

    private Uri imgPath;

    private ArrayList<String> list_path;

    private List<AuditoriumBean.img> imgs;

    private BuildBean loading;

    private static int REQUEST_CODE_CHOOSE = 0;
    private List<Uri> mSelected;

    private List<PositionRootBean.row> jsonArray;

    private List<PositionRootBean.row> jsonArray2;

    private List<AuditoriaAdminBean> admins;

    private Integer town=-1;

    private Integer village=-1;

    private Integer adminId=-1;

    private String ids="";

    private int num = 3;

    private boolean isChange = false;
    private boolean isChangeAdmin = false;

    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_audtior_report);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("礼堂风采");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        auditoriumBean = new AuditoriumBean();
        list_path = new ArrayList<>();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");
        String poid = spUtils.getString("position_id");

        Subscription subscription = userRepository.getAuditorium(poid,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reponse ->{
                    if (reponse!=null){
                        auditoriumBean = reponse;
                        tcAudtiorName.setText(reponse.getName());
                        tcAdmin.setText(reponse.getAdminName());
                        tvAuditorAdress.setText(reponse.getAdress());
                        tvAuditorIntdro.setText(reponse.getSurvey());
                        String[] splite = reponse.getPosition().split("/");
                        tvViView.setText(splite[1]);
                        tvCtView.setText(splite[2]);
                        //tvXiangZheng.setText(splite[1]);
                        etVillSit.setText(reponse.getBuild_introduce());
                        etLishihui.setText(reponse.getLishihui());
                        etTeamBuild.setText(reponse.getWentiduiwu());
                        etRemarks.setText(reponse.getPs());
                        imgs = reponse.getImgList();
                        if (reponse.getImgList() != null && reponse.getImgList().size() != 0){
                            Glide.with(mContext).load(TerminalInfo.BASE_URL+imgs.get(0).getUrl()).into(iv1);
                            ivDelet1.setVisibility(View.VISIBLE);
                            num--;
                            ivDelet1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iv1.setImageResource(0);
                                    ivDelet1.setVisibility(GONE);
                                    ids+=(","+imgs.get(0).getId());
                                    num++;
                                }
                            });
                            if (reponse.getImgList().size()>1){
                                Glide.with(mContext).load(TerminalInfo.BASE_URL+imgs.get(1).getUrl()).into(iv2);
                                ivDelet2.setVisibility(View.VISIBLE);
                                num--;
                                ivDelet2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        iv2.setImageResource(0);
                                        ivDelet2.setVisibility(GONE);
                                        ids+=(","+imgs.get(1).getId());
                                        num++;
                                    }
                                });
                            }
                            if (reponse.getImgList().size()>2){
                                Glide.with(mContext).load(TerminalInfo.BASE_URL+imgs.get(2).getUrl()).into(iv3);
                                ivDelet3.setVisibility(View.VISIBLE);
                                num--;
                                ivDelet3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        iv3.setImageResource(0);
                                        ivDelet3.setVisibility(GONE);
                                        ids+=(","+imgs.get(2).getId());
                                        num++;
                                    }
                                });
                            }
                        }
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);

        tcAdmin.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           String villageid = "";
                           if (isChange){
                               if (jsonArray2==null||village<0){
                                   ToastUtil.showMessage(mContext,"请先选择所属乡镇");
                                   return;
                               }
                               villageid = jsonArray2.get(village).getId();
                           }else {
                               villageid = auditoriumBean.getPosition_id();
                           }

                           loading = DialogUIUtils.showLoading(mContext, "正在加载列表...", true, false, false, true);
                           loading.show();
                           Subscription subscription = null;
                           subscription = userRepository.getLocalAdmin(villageid,spUtils.getString("token"))
                                   .subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .subscribe(response -> {
                                       admins = response;
                                       String[] words = new String[admins.size() + 1];
                                       words[0] = "请选择对应管理员";
                                       for (int i = 0; i < admins.size(); i++) {
                                           words[i + 1] = admins.get(i).getName();
                                       }
                                       DialogUIUtils.showSingleChoose((Activity) mContext, "管理员", adminId+1, words, new DialogUIItemListener() {
                                           @Override
                                           public void onItemClick(CharSequence text, int position) {
                                               adminId=position-1;
                                               tcAdmin.setText(words[position]);
                                               isChangeAdmin = true;
                                           }
                                       }).show();
                                       DialogUIUtils.dismiss(loading);
                                   }, throwable -> {
                                       throwable.printStackTrace();
                                       ToastUtil.showLongMessage(mContext,"网络异常！");
                                       DialogUIUtils.dismiss(loading);
                                   });
                           subscriptions.add(subscription);
                       }
                   });
        //tvViView.setText("请选择乡镇");
        //设置乡镇列表
        tvViView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
                loading.show();
                Subscription subscription = null;
                subscription = userRepository.getPositionRoot()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            //List<VillageButtonBean> list = new ArrayList<>();
                            jsonArray = response.getRows();
                            String[] words = new String[jsonArray.size()+1];

                            words[0] = "请选择乡或街道";
                            for (int i = 0 ; i < jsonArray.size() ; i++){
                                words[i+1] = jsonArray.get(i).getPosition_name();
                            }
                            DialogUIUtils.showSingleChoose((Activity) mContext, "乡，街道", town+1, words, new DialogUIItemListener() {
                                @Override
                                public void onItemClick(CharSequence text, int position) {
                                    town =position-1;
                                    if(town==-1) {
                                        tvCtView.setOnClickListener(null);
                                        return;
                                    }
                                    isChange = true;
                                    village=-1;
                                    tvViView.setText(text);
                                    //设置村列表
                                    tvCtView.setText("选择村");
                                    tcAdmin.setText("请先选择礼堂区域");
                                    adminId=-1;
                                    tvCtView.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            Subscription subscription = null;
                                            subscription = userRepository.getPositionRoot(jsonArray.get(town).getId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(response ->{
                                                        List<VillageButtonBean> list = new ArrayList<>();
                                                        jsonArray2 = response.getRows();
                                                        String[] words2 = new String[jsonArray2.size()+1];

                                                        words2[0] = "请选择村";
                                                        for (int i = 0 ; i < jsonArray2.size() ; i++){
                                                            words2[i+1] = jsonArray2.get(i).getPosition_name();
                                                        }
                                                        DialogUIUtils.showSingleChoose((Activity) mContext, "村", village+1, words2, new DialogUIItemListener() {
                                                            @Override
                                                            public void onItemClick(CharSequence text, int position) {
                                                                village =position-1;
                                                                tvCtView.setText(text);
                                                                tcAdmin.setText("请先选择礼堂区域");
                                                                adminId=-1;
                                                            }
                                                        }).show();
                                                    },throwable -> {
                                                        throwable.printStackTrace();
                                                        ToastUtil.showLongMessage(mContext,"网络异常！");
                                                    });
                                            subscriptions.add(subscription);
                                        }
                                    });
                                }
                            }).show();
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });

                subscriptions.add(subscription);
            }
        });

        tvCtView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Subscription subscription = null;
                subscription = userRepository.getPositionRoot(auditoriumBean.getVillageid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            List<VillageButtonBean> list = new ArrayList<>();
                            jsonArray2 = response.getRows();
                            String[] words2 = new String[jsonArray2.size()+1];

                            words2[0] = "请选择村";
                            for (int i = 0 ; i < jsonArray2.size() ; i++){
                                words2[i+1] = jsonArray2.get(i).getPosition_name();
                            }
                            DialogUIUtils.showSingleChoose((Activity) mContext, "村", village+1, words2, new DialogUIItemListener() {
                                @Override
                                public void onItemClick(CharSequence text, int position) {
                                    village =position-1;
                                    tvCtView.setText(text);
                                    isChange = true;
                                    tcAdmin.setText("请先选择礼堂区域");
                                    adminId=-1;
                                }
                            }).show();
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num==0){
                    ToastUtil.showMessage(mContext,"请删除之前礼堂图片！");
                    return;
                }
                if (EasyPermissions.hasPermissions(mContext, permissions)) {
                    Matisse.from(AuditorReportActivity.this)
                            .choose(MimeType.of(MimeType.JPEG,MimeType.PNG)) // 选择 mime 的类型
                            .countable(true)
                            .maxSelectable(num) // 图片选择的最多数量
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

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String admin_id = "";
                String townidstr = "";
                String villageidstr = "";
//                if (isChange){
//                    townidstr = jsonArray == null? auditoriumBean.getVillageid():jsonArray.get(town).getId();
//                    villageidstr = jsonArray2==null?auditoriumBean.getPosition_id():jsonArray2.get(village).getId();
//                }
                if (adminId == -1 && village == -1 && town == -1 && !isChange && !isChangeAdmin){
                    admin_id = auditoriumBean.getAdmin_id();
                    townidstr = auditoriumBean.getVillageid();
                    villageidstr = auditoriumBean.getPosition_id();
                }else if (adminId != -1 && village != -1 && town != -1){
                    admin_id = admins.get(adminId).getId();
                    townidstr = jsonArray.get(town).getId();
                    villageidstr = jsonArray2.get(village).getId();
                }else if (town == -1 && adminId != -1 && village != -1){
                    admin_id = admins.get(adminId).getId();
                    townidstr = auditoriumBean.getVillageid();
                    villageidstr = jsonArray2.get(village).getId();
                }else if (town == -1 && adminId != -1 && village == -1){
                    admin_id = admins.get(adminId).getId();
                    townidstr = auditoriumBean.getVillageid();
                    villageidstr = auditoriumBean.getPosition_id();
                }else {
                    ToastUtil.showMessage(mContext,"请选择礼堂所属区域和管理员");
                    return;
                }
                String path = null;
                loading = DialogUIUtils.showLoading(mContext,"正在上传中...",true,false,false,true);
                loading.show();
                Subscription subscription2 = userRepository.adsaveAuditorium(auditoriumBean.getId(),tvCtView.getText().toString()
                        ,townidstr,villageidstr,tvAuditorAdress.getText().toString()
                        ,etLishihui.getText().toString(),etTeamBuild.getText().toString(),auditoriumBean.getProportion()==null?"":auditoriumBean.getProportion()
                        ,auditoriumBean.getBuild_year()==null?"":auditoriumBean.getBuild_year()
                        ,admin_id,etVillSit.getText().toString(),etRemarks.getText().toString()
                        ,tvAuditorIntdro.getText().toString(),"0",token,mSelected,mContext,ids)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if (response.getCode() == 0){
                                ToastUtil.showLongMessage(mContext,"上传成功！");
                                finish();
                            }else {
                                ToastUtil.showLongMessage(mContext,response.getMsg());
                            }
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                subscriptions.add(subscription2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            int k =0;
            if (ivDelet1.getVisibility()==GONE){
                iv1.setImageURI(mSelected.get(k));
                ivDelet1.setVisibility(View.VISIBLE);
                ivDelet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv1.setImageResource(0);
                        ivDelet1.setVisibility(GONE);
                        num++;
                    }
                });
                num--;
                if (k+1==mSelected.size()){
                    return;
                }
                k++;
            }
            if (ivDelet2.getVisibility()==GONE){
                iv2.setImageURI(mSelected.get(k));
                ivDelet2.setVisibility(View.VISIBLE);
                ivDelet2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv2.setImageResource(0);
                        ivDelet2.setVisibility(GONE);
                        num++;
                    }
                });
                num--;
                if (k+1==mSelected.size()){
                    return;
                }
                k++;
            }
            if (ivDelet3.getVisibility()==GONE){
                iv3.setImageURI(mSelected.get(k));
                ivDelet3.setVisibility(View.VISIBLE);
                ivDelet3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv3.setImageResource(0);
                        ivDelet3.setVisibility(GONE);
                        num++;
                    }
                });
                num--;
            }
            Log.d("Matisse", "mSelected: " + mSelected);
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
