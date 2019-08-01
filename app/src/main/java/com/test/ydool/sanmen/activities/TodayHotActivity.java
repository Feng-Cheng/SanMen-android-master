package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.test.ydool.sanmen.adapter.NewsAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.bean.TodayHotBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/6.
 */

public class TodayHotActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_photo_main)
    RecyclerView rlPhotoMain;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private NewsAdapter mAdapter;
    private List<TodayHotBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_photo_shen);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.today_hot);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Subscription subscription = userRepository.getNewsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reponse ->{
                    if (reponse!=null){
                        int j = 1;
                        TodayHotBean todayHotBean = new TodayHotBean();
                        for (int i = 0 ; i < reponse.size() ; i++){
                            if (j!=2){
                                todayHotBean.setDate(reponse.get(i).getTime());
                                todayHotBean.setFrom(reponse.get(i).getFrom());
                                todayHotBean.setTitle(reponse.get(i).getTitle());
                                todayHotBean.setUrl(reponse.get(i).getUrl());
                                j=2;
                            }else {
                                todayHotBean.setDate1(reponse.get(i).getTime());
                                todayHotBean.setFrom1(reponse.get(i).getFrom());
                                todayHotBean.setTitle1(reponse.get(i).getTitle());
                                todayHotBean.setUrl1(reponse.get(i).getUrl());
                                j=1;
                                list.add(todayHotBean);
                                todayHotBean = new TodayHotBean();
                            }
                        }
                        mAdapter = new NewsAdapter(R.layout.item_today_hot_day,list);
                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                switch (view.getId()) {
                                    case R.id.rl_today_hot1:
                                        Intent intent = new Intent(mContext,ShowWebViewActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("url",list.get(position).getUrl());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        break;
                                    case R.id.rl_today_hot2:
                                        Intent intent1 = new Intent(mContext,ShowWebViewActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("url",list.get(position).getUrl1());
                                        intent1.putExtras(bundle1);
                                        startActivity(intent1);
                                        break;
                                }
                            }
                        });
                        rlPhotoMain.setLayoutManager(new LinearLayoutManager(mContext));
                        rlPhotoMain.setAdapter(mAdapter);
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
    }
}
