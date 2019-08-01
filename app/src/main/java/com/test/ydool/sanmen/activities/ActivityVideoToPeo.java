package com.test.ydool.sanmen.activities;

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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.VideoToPeoAdapter;
import com.test.ydool.sanmen.adapter.WorkBaseShowAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.VideoCaremerBean;
import com.test.ydool.sanmen.bean.VideoToPeoBean;
import com.test.ydool.sanmen.bean.WorkBaseBean;
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
 * Created by Administrator on 2018/11/28.
 *
 * 萤石云视频互动
 */

public class ActivityVideoToPeo extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rv_work_main)
    RecyclerView videoList;


    private VideoToPeoAdapter mAdapter;

    private List<VideoToPeoBean> mList;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private VideoCaremerBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_work_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("视频互动");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        bean = new VideoCaremerBean();

        Subscription subscription = userRepository.findMonitors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null){
                        bean = response;
                        mList = response.getMonitoringList();
                        mAdapter = new VideoToPeoAdapter(R.layout.item_video_people,mList);
                        videoList.setLayoutManager(new LinearLayoutManager(mContext));
                        videoList.setAdapter(mAdapter);
                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(mContext,ShowVideoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("accessToken", bean.getCameraToken());
                                bundle.putString("playUrl",mList.get(position).getUrl());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                },err ->{

                });



    }

}
