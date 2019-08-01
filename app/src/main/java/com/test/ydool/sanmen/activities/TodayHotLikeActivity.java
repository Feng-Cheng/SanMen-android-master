package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.NewsAdapter;
import com.test.ydool.sanmen.adapter.NewsManagerAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.TodayHotBean;
import com.test.ydool.sanmen.fragments.ToadyHotNewsFragment;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/25.
 *
 * 热点新闻页面
 */

public class TodayHotLikeActivity extends BaseActivity{

    @BindView(R.id.view_pager)
    ViewPager viewPager;


    private CompositeSubscription subscriptions;
    private UserRepository userRepository;

    private int showNum = 1;
    private int showSize = 0;

    private  NewsManagerAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_today_hot_like);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TerminalInfo.scrollNum = 7;
        TerminalInfo.PhotoNum = 0;
    }

    private void initView(){
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");

        if (TerminalInfo.HomePhotoBean==null){
            Subscription subscription = userRepository.getDayHotPhotoBean()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reponse ->{
                        if (reponse!=null){
                            TerminalInfo.HomePhotoBean = reponse;
                        }else {
                            ToastUtil.showLongMessage(mContext,"暂无数据！");
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.showLongMessage(mContext,"网络异常！");
                    });
            subscriptions.add(subscription);
        }
        if (TerminalInfo.newsList==null){
            Subscription subscription = userRepository.getNewsList(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reponse ->{
                        if (reponse!=null){
                            TerminalInfo.newsList = reponse;
                            TerminalInfo.size = reponse.size()/5;
                            initViewPage(TerminalInfo.size);
                        }else {
                            ToastUtil.showLongMessage(mContext,"暂无数据！");
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.showLongMessage(mContext,"网络异常！");
                    });
            subscriptions.add(subscription);
        }else {
            initViewPage(TerminalInfo.size);
        }
    }

    private void initViewPage(int size){
        viewPager.setOffscreenPageLimit(size);
        setupViewPager(viewPager,size);
    }


    private void setupViewPager(ViewPager mViewPager,int size) {
        newsAdapter = new NewsManagerAdapter(getSupportFragmentManager());
        if (size<7){
            for (int i = 0 ; i <size ; i++){
                newsAdapter.addFragment(ToadyHotNewsFragment.newInstance(i));
            }
        }else {
            for (int i = 0 ; i < TerminalInfo.scrollNum ; i++){
                newsAdapter.addFragment(ToadyHotNewsFragment.newInstance(i));
            }
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position+1 == TerminalInfo.scrollNum){
                    if (position+1 ==TerminalInfo.size){
                        ToastUtil.showMessage(mContext,"已是最后一页");
                    }else {
                        if (TerminalInfo.size - TerminalInfo.scrollNum > 7){
                            for (int i = 0 ; i < 7 ; i++){
                                newsAdapter.addFragment(ToadyHotNewsFragment.newInstance(position+i+1));
                                TerminalInfo.scrollNum++;
                            }
                            newsAdapter.notifyDataSetChanged();
                        }else {
                            for (int i = 0 ; i < TerminalInfo.size - TerminalInfo.scrollNum ; i++){
                                newsAdapter.addFragment(ToadyHotNewsFragment.newInstance(position+i+1));
                                TerminalInfo.scrollNum++;
                            }
                            newsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(newsAdapter);
    }
}
