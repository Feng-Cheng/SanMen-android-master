package com.test.ydool.sanmen.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.ActivityMainAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityExhibitionBean;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/5/30.
 *
 * 礼堂风采页面
 */

public class AuditorInformActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_activtity)
    RecyclerView rlActivity;
    @BindView(R.id.tv_audtiorname)
    TextView tvAuditorName;
    @BindView(R.id.tv_live_xiangzheng)
    TextView tvXiangzheng;
    @BindView(R.id.tv_adm)
    TextView tvAdmn;
    @BindView(R.id.tv_build_area)
    TextView tvBuildErea;
    @BindView(R.id.tv_build_year)
    TextView tvBuildYear;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_read_more)
    TextView tvReadMore;



    private UserRepository userRepository;

    private CompositeSubscription subscriptions;



    private ActivityMainAdapter mAdapter;

    private ArrayList<String> list_path;

    private AuditoriumBean auditoriumBean;

    private List<ActivityExhibitionBean> listActivity;
    private String name = "";


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mAdapter = new ActivityMainAdapter(R.layout.item_activity_main,listActivity);
                rlActivity.setLayoutManager(new LinearLayoutManager(mContext));
                rlActivity.setAdapter(mAdapter);

                mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent intent = new Intent(mContext, ShowWebViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", TerminalInfo.BASE_URL+"/img/img?id="+listActivity.get(position).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_inform);
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
        Bundle bundle = getIntent().getExtras();
        String poid = bundle.getString("id");
        name = bundle.getString("name");


        System.out.println("poid=="+poid);

        Subscription subscription = userRepository.getAuditorium(poid,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reponse ->{
                    if (reponse!=null){
                        auditoriumBean = reponse;
                        tvAuditorName.setText(reponse.getName());
                        tvAdmn.setText(reponse.getAdminName());
                        String[] splite = reponse.getPosition().split("/");
                        tvXiangzheng.setText(splite[1]);
                        if (reponse.getProportion()==null||reponse.getProportion().equals("")){
                            tvBuildErea.setText("/");
                        }else {
                            tvBuildErea.setText(reponse.getProportion()+"平方米");
                        }
                        if (reponse.getBuild_year()==null||reponse.getBuild_year().equals("")){
                            tvBuildYear.setText("/");
                        }else {
                            tvBuildYear.setText(reponse.getBuild_year());
                        }

                        //放图片地址的集合
                        list_path = new ArrayList<>();
                        int size = 0;
                        if (reponse.getImgList().size()>5){
                            size=5;
                        }else {
                            size = reponse.getImgList().size();
                        }
                        for (int i = 0; i < size; i++){
                            list_path.add(TerminalInfo.BASE_URL+reponse.getImgList().get(i).getUrl());
                        }
                        if (list_path!=null){
                            //设置内置样式，共有六种可以点入方法内逐一体验使用。
                            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                            //设置图片加载器，图片加载器在下方
                            banner.setImageLoader(new MyLoader());
                            //设置图片网址或地址的集合
                            banner.setImages(list_path);
                            //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                            banner.setBannerAnimation(Transformer.Default);
                            //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
                            //设置轮播间隔时间
                            banner.setDelayTime(3000);
                            //设置是否为自动轮播，默认是“是”。
                            banner.isAutoPlay(true);
                            //设置指示器的位置，小点点，左中右。
                            banner.setIndicatorGravity(BannerConfig.CENTER)
                                    //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                                    .setOnBannerListener(this)
                                    //必须最后调用的方法，启动轮播图。
                                    .start();
                        }else {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        }
                    }
                },throwable -> {
                    tvAuditorName.setText(name);
                    tvAdmn.setText("\\");
                    tvXiangzheng.setText("\\");
                    tvBuildErea.setText("\\");
                    tvBuildYear.setText("\\");
                    ToastUtil.showMessage(mContext,"正在完善信息！");
                    System.out.println("error"+throwable.toString());
                });

        subscriptions.add(subscription);

        Subscription subscription1 = userRepository.getHomeExhibitionList(poid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reponse ->{
                    listActivity = new ArrayList<>();
                    if (reponse!=null){
                        listActivity = reponse;
                        Message message = new Message();
                        message.what =0;
                        mHandler.sendMessage(message);
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });

        subscriptions.add(subscription1);


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),AuditorShowActivity.class);
                Bundle bundle1 = new Bundle();
                String[] splite;
                if (auditoriumBean.getPosition()!=null){
                    splite = auditoriumBean.getPosition().split("/");
                    bundle1.putString("vill",splite[1]);
                }else {
                    bundle1.putString("vill","/");
                }
                if(auditoriumBean.getName()!=null){
                    bundle1.putString("name",auditoriumBean.getName());
                }else {
                    bundle1.putString("name",name);
                }
                if (auditoriumBean.getAdminName()!=null){
                    bundle1.putString("adm",auditoriumBean.getAdminName());
                }else {
                    bundle1.putString("adm","");
                }
                if (auditoriumBean.getProportion()!=null){
                    bundle1.putString("area",auditoriumBean.getProportion()+"平方米");
                }else {
                    bundle1.putString("area","/");
                }
                if (auditoriumBean.getBuild_year()!=null){
                    bundle1.putString("year",auditoriumBean.getBuild_year());
                }else {
                    bundle1.putString("year","/");
                }
                if (auditoriumBean.getSurvey()!=null){
                    bundle1.putString("survey",auditoriumBean.getSurvey());
                }else {
                    bundle1.putString("survey","");
                }
                if (auditoriumBean.getBuild_introduce()!=null){
                    bundle1.putString("audtior_intro",auditoriumBean.getBuild_introduce());
                }else {
                    bundle1.putString("audtior_intro","");
                }
                if (auditoriumBean.getWentiduiwu()!=null){
                    bundle1.putString("duiwu",auditoriumBean.getWentiduiwu());
                }else {
                    bundle1.putString("duiwu","");
                }
                if (auditoriumBean.getPs()!=null){
                    bundle1.putString("ps",auditoriumBean.getPs());
                }else {
                    bundle1.putString("ps","");
                }
                if (list_path!=null){
                    bundle1.putStringArrayList("imgPath",list_path);
                }else {
                    bundle1.putStringArrayList("imgPath",list_path);
                }
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .into(imageView);
        }
    }
}
