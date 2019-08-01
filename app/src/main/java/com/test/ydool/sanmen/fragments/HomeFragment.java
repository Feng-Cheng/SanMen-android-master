package com.test.ydool.sanmen.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.ActivityHomeListActivity;
import com.test.ydool.sanmen.activities.ActivityHomeMainActivity;
import com.test.ydool.sanmen.activities.ActivityVideoToPeo;
import com.test.ydool.sanmen.activities.AuditorMapActivity;
import com.test.ydool.sanmen.activities.AuditorReportActivity;
import com.test.ydool.sanmen.activities.DayActivityMainActivity;
import com.test.ydool.sanmen.activities.HomeInformReportMainActivity;
import com.test.ydool.sanmen.activities.HomeOnlineAdvicesMainActivity;
import com.test.ydool.sanmen.activities.LoginActivity;
import com.test.ydool.sanmen.activities.MainActivity;
import com.test.ydool.sanmen.activities.OnlineAdviceListCunActivity;
import com.test.ydool.sanmen.activities.OnlineVedioActivity;
import com.test.ydool.sanmen.activities.ShowVideoActivity;
import com.test.ydool.sanmen.activities.ShowWebViewActivity;
import com.test.ydool.sanmen.activities.SignTimesActivity;
import com.test.ydool.sanmen.activities.TestActivity;
import com.test.ydool.sanmen.activities.TodayHotActivity;
import com.test.ydool.sanmen.activities.TodayHotLikeActivity;
import com.test.ydool.sanmen.activities.WorkMainActivity;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/5/28.
 */

public class HomeFragment extends BaseFragment implements OnBannerListener{
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_auditor)
    LinearLayout ll_auditor;
    @BindView(R.id.ll_day_hot)
    LinearLayout llDayHot;
    @BindView(R.id.ll_home_activty_menu)
    LinearLayout llActivityMenu;
    @BindView(R.id.ll_home_inform_put)
    LinearLayout llInformPut;
    @BindView(R.id.ll_home_online_advice)
    LinearLayout llOnlineAdvice;
    @BindView(R.id.ll_vedio_to_people)
    LinearLayout llVedioToPeo;
    @BindView(R.id.ll_vedio_play)
    LinearLayout llVedioPlay;
    @BindView(R.id.ll_home_activty_announce)
    LinearLayout llActivityAnnounce;
    @BindView(R.id.tv_sign)
    TextView tvSign;


    private ArrayList<String> list_path;
    private SPUtils spUtils;

    private int recLen = 0;

    private CompositeSubscription subscriptions;
    private UserRepository userRepository;


    public static HomeFragment newInstance(){
        return new HomeFragment();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tvSign.getVisibility()==View.VISIBLE && !spUtils.getBoolean("isLogin")){
            tvSign.setVisibility(View.GONE);
        }
    }

    private void initView(){
        //放图片地址的集合
        list_path = new ArrayList<>();

        spUtils = SPUtils.getInstance("sanMen");
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        new Thread(new MyThread()).start();         // start thread

        Subscription subscription = userRepository.getHomePhotoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response != null && response.size()>0){
                        int k = 0;
                        for (int i = 0 ; i < response.size() ;i++){
                            if (k>4){
                                break;
                            }
                            list_path.add(TerminalInfo.BASE_URL+response.get(i));
                            k++;
                        }
                        if (list_path != null){
                            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                            banner.setImageLoader(new MyLoader());
                            banner.setImages(list_path);
                            banner.setBannerAnimation(Transformer.Default);
                            banner.setDelayTime(3000);
                            banner.isAutoPlay(true);
                            banner.setIndicatorGravity(BannerConfig.CENTER)
                                    .setOnBannerListener(this)
                                    .start();
                        }
                    }
                },throwable -> {
//                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription);

        ll_auditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuditorMapActivity.class);
//                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });

        llDayHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TodayHotActivity.class);
                Intent intent = new Intent(getActivity(), TodayHotLikeActivity.class);
                startActivity(intent);
            }
        });

        llActivityAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityHomeMainActivity.class);
                startActivity(intent);
            }
        });

        llActivityMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityHomeListActivity.class);
                startActivity(intent);
            }
        });

        llInformPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeInformReportMainActivity.class);
                startActivity(intent);
            }
        });

        llOnlineAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spUtils.getBoolean("isLogin")){
                    Intent intent = new Intent(getActivity(), HomeOnlineAdvicesMainActivity.class);
                    startActivity(intent);
                }else {
                    ToastUtil.showMessage(getActivity(),"请先登录");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llVedioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DayActivityMainActivity.class);
                startActivity(intent);
            }
        });

        Subscription subscription1 = userRepository.getModuleOpen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response.equals("0")){
                        llVedioToPeo.setVisibility(View.VISIBLE);
                        llVedioToPeo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ToastUtil.showMessage(getActivity(),"暂未开放！！！！");
                                Intent intent = new Intent(getActivity(), ActivityVideoToPeo.class);
                                startActivity(intent);
                            }
                        });
                    }
                },throwable -> {
//                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription1);



        if (spUtils.getBoolean("isLogin")&&spUtils.getInt("quanxian")==2){
            tvSign.setVisibility(View.VISIBLE);
        }
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recLen < 120) {
                    ToastUtil.showMessage(getActivity(),"请登录两分钟后进行签到!");
                }else {
                    Intent intent = new Intent(getActivity(), SignTimesActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    final Handler handler = new Handler(){          // handle
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    recLen++;
            }
            super.handleMessage(msg);
        }
    };

    public class MyThread implements Runnable {      // thread
        @Override
        public void run() {
            while (true) {
                try {
                    if(recLen < 120){
                        Thread.sleep(1000);     // sleep 1000ms
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }


   //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }


}
