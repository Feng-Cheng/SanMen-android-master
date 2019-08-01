package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.VedioAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.VedioBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ShareUtil;
import com.test.ydool.sanmen.utils.SystemShareUtils;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/21.
 */

public class OnlineVedioActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlVedio;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private VedioAdapter adapter;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数

    private int checkPositon;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private List<VedioBean> listVedio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_main);
        ButterKnife.bind(this);

        initDatas();
//        initListener();
    }

    private void initDatas() {
        tvTitle.setText(R.string.online_vedio);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        Subscription subscription = null;
        subscription = userRepository.getViedos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    listVedio=new ArrayList<>();
                    if (response!=null){
                        listVedio = response;
                        adapter = new VedioAdapter(R.layout.item_vedio,response);
                        rlVedio.setLayoutManager(new LinearLayoutManager(mContext));
                        rlVedio.setAdapter(adapter);
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                checkPositon = position;
                                if(Build.VERSION.SDK_INT>=23){
                                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                                    ActivityCompat.requestPermissions((Activity) mContext,mPermissionList,123);
                                }
                            }
                        });
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Subscription subscription1 = userRepository.getViedos()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if (response!=null){
                                listVedio=new ArrayList<>();
                                listVedio = response;
                                adapter.setNewData(listVedio);
                                rlVedio.setAdapter(adapter);
                                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                    @Override
                                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                        checkPositon = position;
                                        if(Build.VERSION.SDK_INT>=23){
                                            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                                            ActivityCompat.requestPermissions((Activity) mContext,mPermissionList,123);
                                        }
                                    }
                                });
                                refreshLayout.setRefreshing(false);
                            }
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription1);
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode!= 123){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }else{
            ShareUtil.shareVedio(mContext,listVedio.get(checkPositon));
        }

    }
}
