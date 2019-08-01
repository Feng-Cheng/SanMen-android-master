package com.test.ydool.sanmen.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.VedioAdapter;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.VedioBean;
import com.test.ydool.sanmen.customview.CustomLoadMoreView;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ShareUtil;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.widget.MediaController;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by Administrator on 2018/6/21.
 */

public class OnlineVedioFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,VedioAdapter.OnFullScreenListener{
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlVedio;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private VedioAdapter adapter;
    private PLVideoTextureView mCurVideoView;
//    private PlayConfigView mPlayConfigView;
    private MediaController mLandscapeMC;
    private MediaController mPortraitMC;

    private int checkPositon;

//    private StandardGSYVideoPlayer currPlayer;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数
    private RecyclerView.OnScrollListener onScrollListener;

    private List<VedioBean> listVedio;
    public static OnlineVedioFragment newInstance(){
        return new OnlineVedioFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this,view);

        initDatas();
        initListener();
        return view;
    }


    private void initDatas() {
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
                        adapter.setOnFullScreenListener(this);
                        rlVedio.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rlVedio.setAdapter(adapter);
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                checkPositon = position;
                                if(Build.VERSION.SDK_INT>=23){
                                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                            ,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE
                                            ,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE
                                            , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP
                                            ,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                                    ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
                                    ShareUtil.shareVedio(getActivity(),listVedio.get(checkPositon));
                                }else {
                                    ShareUtil.shareVedio(getActivity(),listVedio.get(checkPositon));
                                }
                            }
                        });
                    }else {
                        ToastUtil.showLongMessage(getActivity(),"暂无视频！");
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        JZVideoPlayer.releaseAllVideos();
//        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
//            GSYVideoManager.releaseAllVideos();
        }
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(RecyclerView view) {
//        for (int i = 0; i < visibleCount; i++) {
//            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.videoplayer) != null) {
//                currPlayer =  view.getChildAt(i).findViewById(R.id.videoplayer);
//                Rect rect = new Rect();
//                //获取当前view 的 位置
//                currPlayer.getLocalVisibleRect(rect);
//                int videoheight = currPlayer.getHeight();
//                if (rect.top == 0 && rect.bottom == videoheight) {
//                    if (currPlayer.currentState == JZVideoPlayer.CURRENT_STATE_NORMAL
//                            || currPlayer.currentState == JZVideoPlayer.CURRENT_STATE_ERROR) {
//                        currPlayer.startButton.performClick();
//                    }
//                    return;
//                }
//            }
//        }
        //释放其他视频资源
//        JZVideoPlayer.releaseAllVideos();
//        GSYVideoManager.onPause();
    }


    /**
     * 滑动监听
     */
    private void initListener() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case SCROLL_STATE_IDLE:
//                        autoPlayVideo(recyclerView);
                        break;
                    case SCROLL_STATE_SETTLING:
//                        autoPlayVideo(recyclerView);
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy)>20){
//                    autoPlayVideo(recyclerView);
                }
            }
        };
        rlVedio.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode!= 123){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
            ShareUtil.shareVedio(getActivity(),listVedio.get(checkPositon));
        }else{
            ShareUtil.shareVedio(getActivity(),listVedio.get(checkPositon));
        }
    }

    @Override
    public void onLoadMoreRequested() {
        adapter.setEnableLoadMore(true);
//        refreshFromServer();
    }


    @Override
    public void onFullScreen(PLVideoTextureView videoView, MediaController mediaController) {
        if (videoView == null) {
            return;
        }
        mCurVideoView = videoView;
//        mPortraitMC = mediaController;
//        mPlayConfigView.setVideoView(mCurVideoView);

//        if (mFullScreenGroup.getVisibility() != View.VISIBLE) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

}
