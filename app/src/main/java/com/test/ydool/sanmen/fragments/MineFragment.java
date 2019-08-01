package com.test.ydool.sanmen.fragments;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.AcitvityShowMineActivity;
import com.test.ydool.sanmen.activities.ActivityCunMainActivity;
import com.test.ydool.sanmen.activities.AuditorPotoShenActivtity;
import com.test.ydool.sanmen.activities.AuditorReportActivity;
import com.test.ydool.sanmen.activities.ChangePeopleActivity;
import com.test.ydool.sanmen.activities.HelpWenDangActivity;
import com.test.ydool.sanmen.activities.InformReportMainActivity;
import com.test.ydool.sanmen.activities.LiuLiangShowActivity;
import com.test.ydool.sanmen.activities.LoginActivity;
import com.test.ydool.sanmen.activities.MainActivity;
import com.test.ydool.sanmen.activities.OnlineAdviceListCunActivity;
import com.test.ydool.sanmen.activities.PointTownXianShowActivity;
import com.test.ydool.sanmen.activities.PonitActivityMainActivity;
import com.test.ydool.sanmen.activities.ScoreRankActivity;
import com.test.ydool.sanmen.activities.VersionDownActivity;
import com.test.ydool.sanmen.activities.WendangManagerMainActivity;
import com.test.ydool.sanmen.activities.WorkMainActivity;
import com.test.ydool.sanmen.activities.WorkStaticsActivity;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.AdminBean;
import com.test.ydool.sanmen.bean.NotificationBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/5/28.
 */

public class MineFragment extends BaseFragment{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_adm_name)
    TextView tvAdmnName;
    @BindView(R.id.iv_adm_photo)
    ImageView ivAdmPhoto;
    @BindView(R.id.tv_adm_tele)
    TextView tvAdmTele;
    @BindView(R.id.ll_auditor_main)
    LinearLayout llAuditorMain;
    @BindView(R.id.ll_work_report)
    LinearLayout llWorkReport;
    @BindView(R.id.ll_inform_put)
    LinearLayout llInformPut;
    @BindView(R.id.ll_activty_announce)
    LinearLayout llActivityAnounce;
    @BindView(R.id.ll_activty_show)
    LinearLayout llActivityShow;
    @BindView(R.id.ll_list_manger)
    LinearLayout llListManger;
    @BindView(R.id.ll_inform_change)
    LinearLayout llInformChange;
/*    @BindView(R.id.ll_declare_manger)
    LinearLayout llDeclareMager;*/
    @BindView(R.id.ll_Online_advice)
    LinearLayout llOnlineAdvice;
    @BindView(R.id.ll_raffic_statistics)
    LinearLayout llRaffStatist;
    @BindView(R.id.ll_work_statitstics)
    LinearLayout llWorkStatist;
    @BindView(R.id.ll_wendang_manager)
    LinearLayout llWenDangManger;
    @BindView(R.id.ll_help_wendang_manager)
    LinearLayout llHelpWendangManager;
    @BindView(R.id.ll_score_rank)
    LinearLayout llScoreRank;
    @BindView(R.id.iv_exit)
    ImageView ivexit;
    @BindView(R.id.tv_authiro_num)
    TextView tvAudiroNum;
    @BindView(R.id.tv_inform_put_num)
    TextView tvInformPutNum;
    @BindView(R.id.tv_activty_announce_num)
    TextView tvActivityNum;
    @BindView(R.id.tv_online_advices_num)
    TextView tvOnlineNum;
    @BindView(R.id.ll_download)
    LinearLayout llDownLoad;

    private  SPUtils spUtils;

    private BuildBean exitDialog;
    private FragmentInteraction listterner;

    private UserRepository userRepository;
    private CompositeSubscription subscriptions;

    private NotificationBean list;

    private boolean isLoginCheck = false;

    public static MineFragment newInstance(){
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        spUtils = SPUtils.getInstance("sanMen");
        if (TerminalInfo.bean!=null){
            list = TerminalInfo.bean;
            Message message = new Message();
            message.what = 0;
            mHandler.sendMessage(message);
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                SPUtils spUtils = SPUtils.getInstance("sanMen");
                if (spUtils.getInt("quanxian") == 1){
                    if (list.getAuditoriumPreviews().size()>0){
                        tvAudiroNum.setText(list.getAuditoriumPreviews().size()+"");
                        tvAudiroNum.setVisibility(View.VISIBLE);
                    }else {
                        tvAudiroNum.setVisibility(View.INVISIBLE);
                    }
                    if (list.getStatePreviews().size()>0){
                        tvOnlineNum.setText(list.getStatePreviews().size()+"");
                        tvOnlineNum.setVisibility(View.VISIBLE);
                    }else {
                        tvOnlineNum.setVisibility(View.INVISIBLE);
                    }
//                    if (list.getAnnouncement().size()>0){
//                        tvInformPutNum.setText(list.getAnnouncement().size()+"");
//                        tvInformPutNum.setVisibility(View.VISIBLE);
//                    }else {
//                        tvInformPutNum.setVisibility(View.INVISIBLE);
//                    }
                    if (list.getActivityPreviews().size()>0){
                        tvActivityNum.setText(list.getActivityPreviews().size()+"");
                        tvActivityNum.setVisibility(View.VISIBLE);
                    }else {
                        tvActivityNum.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
//        tvAdmTele.setText(TerminalInfo.phone);
//        Glide.with(getActivity()).load(TerminalInfo.photoUrl).into(ivAdmPhoto);
    }
    // 1 定义了所有activity必须实现的接口方法
    public interface FragmentInteraction {
        void retrunHome(boolean is);
    }

    private void initView(){
        tvTitle.setText(R.string.mine);
        tvTitle.setTextColor(0xfffaa8ae);
        spUtils = SPUtils.getInstance("sanMen");

        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        tvAdmnName.setText(spUtils.getString("Adm_name"));
        tvAdmTele.setText(spUtils.getString("phone"));

        Glide.with(getActivity()).load(spUtils.getString("icon")).into(ivAdmPhoto);

        exitDialog = DialogUIUtils.showAlert((Activity) getActivity(),
                getResources().getString(R.string.alert_notice),
                "是否确认注销当前账号", null, null,
                getResources().getString(R.string.cancel),
                getResources().getString(R.string.confirm),
                false, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }

                    @Override
                    public void onNegative() {
                        SPUtils spUtilas = SPUtils.getInstance("sanMen");
                        spUtilas.clear();
                        final SPUtils spUtils = SPUtils.getInstance("sanMen");
                        spUtils.put("isLogin",false);
                        listterner.retrunHome(true);
//                        AppUtils.exitApp();
                    }
                });

        ivexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.show();
                getFragmentManager().popBackStack();
            }
        });

        llAuditorMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    if (spUtils.getInt("quanxian") == 0 || spUtils.getInt("quanxian") == 1){
                        Intent intent = new Intent(getActivity(), AuditorPotoShenActivtity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), AuditorReportActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llWorkReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");

                if (isLogin){
                    Intent intent = new Intent(getActivity(), WorkMainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llInformChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");

                if (isLogin){
                    Intent intent = new Intent(getActivity(), ChangePeopleActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llInformPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), InformReportMainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llActivityAnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), ActivityCunMainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llActivityShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), AcitvityShowMineActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llListManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spUtils.getInt("quanxian")==0||spUtils.getInt("quanxian")==1){
                    Intent intent = new Intent(getActivity(), PointTownXianShowActivity.class);
                    startActivity(intent);
                }else {
                    if (!isLoginCheck){
                        loginCheck();
                    }
                }
            }
        });

        llOnlineAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), OnlineAdviceListCunActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llRaffStatist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), LiuLiangShowActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llWorkStatist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin){
                    Intent intent = new Intent(getActivity(), WorkStaticsActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llWenDangManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WendangManagerMainActivity.class);
                    startActivity(intent);
            }
        });

        llScoreRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScoreRankActivity.class);
                startActivity(intent);
            }
        });

        llHelpWendangManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HelpWenDangActivity.class);
                    startActivity(intent);
            }
        });

        llDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VersionDownActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentInteraction){
            listterner = (FragmentInteraction) context; // 2.2 获取到宿主activity并赋值
        }else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    private void loginCheck(){
        isLoginCheck = true;
        final SPUtils spUtils = SPUtils.getInstance("sanMen");
        String name = spUtils.getString("name");
        String pwd = spUtils.getString("pwd");

        Subscription subscription = null;
        subscription = userRepository.loginCheck(name,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response.getCode() ==0){
                        AdminBean adminBean = response.getData();
                        if (response.getData() != null){
                            if (adminBean.getPermissions()!=null){
                                spUtils.put("permissions",adminBean.getPermissions());
                            }
                            isLoginCheck = false;
                            spUtils.put("token",adminBean.getToken());
                            if (spUtils.getString("permissions").equals("1")){
                                Intent intent = new Intent(getActivity(), PonitActivityMainActivity.class);
                                startActivity(intent);
                            }else {
                                ToastUtil.showMessage(getActivity(),"暂无点单权限！");
                            }
                        }
                    }else {
                        ToastUtil.showLongMessage(getActivity(),"连接失败！"+response.getMsg());
                        SPUtils spUtilas = SPUtils.getInstance("sanMen");
                        spUtilas.clear();
                        spUtilas.put("isLogin",false);
                        listterner.retrunHome(true);
                        getFragmentManager().popBackStack();
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(getActivity(),"连接超时！");
                    isLoginCheck = false;
                });
        subscriptions.add(subscription);

    }
}
