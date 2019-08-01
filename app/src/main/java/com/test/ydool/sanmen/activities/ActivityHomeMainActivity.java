package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.ActivityAnnoceAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.UrlBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 活动预告
 */

public class ActivityHomeMainActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.btn_home_activity_annonce)
    Button btnActivity;
    @BindView(R.id.btn_home_ziyuan_manger)
    Button btnManger;
    @BindView(R.id.rl_activtity)
    RecyclerView rlActivity;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.query_et)
    TextView tvQueryEt;
    @BindView(R.id.btn_activity_home_search)
    Button btnSearch;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private List<ActivityPreviewBean> listShow = new ArrayList<>();

    private List<ActivityPreviewBean> list2 = new ArrayList<>();

    private ActivityAnnoceAdapter mAdapter;

    private ArrayList<String> list_path;

    private String[] words2;

    private int village = 0 ;
    private List<String> list1;

    SPUtils spUtils;
    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mAdapter = new ActivityAnnoceAdapter(R.layout.item_cun_activity, listShow);
                rlActivity.setLayoutManager(new LinearLayoutManager(mContext));
                rlActivity.setAdapter(mAdapter);
                mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent intent = new Intent(mContext,ActivityHomeSHowOnewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("auname", listShow.get(position).getAuname());
                        bundle.putString("title", listShow.get(position).getTitle());
                        bundle.putString("time", listShow.get(position).getTime());
                        bundle.putString("organizer", listShow.get(position).getOrganizer());
                        bundle.putString("detail", listShow.get(position).getDetail());
                        bundle.putString("ps", listShow.get(position).getRemarks());
                        ArrayList<String> urls = new ArrayList<>();
                        if (listShow.get(position).getPic()!=null&&listShow.get(position).getPic().size()>0){
                            for (UrlBean a : listShow.get(position).getPic()){
                                urls.add(a.getUrl());
                            }
                            bundle.putStringArrayList("urls",urls);
                        }else {
                            bundle.putStringArrayList("urls",list_path);
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (msg.what == 1){
                list_path = new ArrayList<>();
                for ( int i  = 0; i< list1.size() ; i++){
                    list_path.add(TerminalInfo.BASE_URL + list1.get(i));
                }
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setImageLoader(new MyLoader());
                banner.setImages(list_path);
                banner.setBannerAnimation(Transformer.Default);
                banner.setDelayTime(3000);
                banner.isAutoPlay(true);
                banner.setIndicatorGravity(BannerConfig.CENTER)
                        .setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
//                                Log.i("tag", "你点了第"+position+"张轮播图");
                            }
                        })
                        .start();
            }
            if (msg.what == 2) {
                mAdapter.setNewData(listShow);
                rlActivity.setAdapter(mAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_home_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.activty_announce);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Subscription subscription = userRepository.getpreviewList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response!=null){
                        listShow = response;
                        list2 = listShow;
                        List<String> list3 = new ArrayList<>();
                        list3.add("全部");
                        for (int i = 0; i < listShow.size() ; i++){
                            if (i == 0){
                                list3.add(listShow.get(i).getPname());
                            }else {
                                for (int j = 1 ; j < list3.size() ; j++){
                                    if (list3.get(j).equals(listShow.get(i).getPname()))
                                        break;
                                    if (j == list3.size()-1){
                                        list3.add(listShow.get(i).getPname());
                                    }
                                }
                            }
                        }
                        words2 = new String[list3.size()];
                        int k =0;
                        for (String s : list3){
                            words2[k]=s;
                            k++;
                        }
                        Message message = new Message();
                        message.what = 0;
                        mHander.sendMessage(message);
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
        Subscription subscription1 = userRepository.getAnnocePicture()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list1 = new ArrayList<>();
                    if (response!=null){
                        for (int i=0;i<response.size();i++)
                            list1.add(response.get(i).getUrl());
                        Message message = new Message();
                        message.what = 1;
                        mHander.sendMessage(message);
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription1);
        btnManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HomeShareMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("page","home");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvQueryEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showSingleChoose((Activity) mContext, "区域", village, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        village=position;
                        tvQueryEt.setText(text);
                    }
                }).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch(village);
            }
        });

    }


    private void doSearch(int genr){
        if (genr == 0){
            if (listShow.size()!=list2.size()){
                listShow.clear();
                listShow = list2;
                Message message = new Message();
                message.what = 2;
                mHander.sendMessage(message);
            }
        }else {
//            listShow.clear();
            listShow = new ArrayList<>();
            for (int i = 0 ; i <list2.size() ; i++){
                if (list2.get(i).getPname().equals(words2[village])){
                    listShow.add(list2.get(i));
                }
            }
            Message message = new Message();
            message.what = 2;
            mHander.sendMessage(message);
        }
    }


    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .thumbnail(0.01f)
                    .into(imageView);
        }
    }
}
