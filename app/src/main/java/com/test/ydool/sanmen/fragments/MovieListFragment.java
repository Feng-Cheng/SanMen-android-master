package com.test.ydool.sanmen.fragments;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.activities.DayActivityMainActivity;
import com.test.ydool.sanmen.adapter.VedioAdapter;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.VedioBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ShareUtil;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.widget.GestureControllerListener;
import com.test.ydool.sanmen.widget.MediaController;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MovieListFragment extends BaseFragment implements FragmentLifecycle, VedioAdapter.OnFullScreenListener, View.OnTouchListener {

    private RecyclerView mVideoList;
    private PLVideoTextureView mCurVideoView;
    private FrameLayout mFullScreenGroup;
    private List<VedioBean> mItemList;
    private VedioAdapter mMovieListAdapter;
    private ViewGroup mCurViewHolder;
//    private PlayConfigView mPlayConfigView;
    private MediaController mLandscapeMC;
    private MediaController mPortraitMC;
    private GestureDetector mGestureDetector;
    private boolean mNeedRestart;
    private UserRepository userRepository;
    private int checkPositon;

    private ImageButton igBtn;

    private CompositeSubscription subscriptions;
    public static MovieListFragment newInstance(){
        return new MovieListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
//        initMovieData(rootView);
        initView(rootView);
        return rootView;
    }

    private void initView(View root) {
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
        mVideoList = root.findViewById(R.id.video_list);
        mFullScreenGroup = root.findViewById(R.id.full_screen_group);
        mFullScreenGroup.setVisibility(View.GONE);
        mLandscapeMC = root.findViewById(R.id.media_controller);
//        mPlayConfigView = root.findViewById(R.id.play_config_view);

        mLandscapeMC.setOnTouchListener(this);
//        mPlayConfigView.setOnTouchListener(this);
        igBtn = root.findViewById(R.id.back_image_btn);

        igBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        Subscription subscription = userRepository.getViedos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    mItemList=new ArrayList<>();
                    if (response!=null){
                        mItemList = response;
                        mMovieListAdapter = new VedioAdapter(R.layout.item_vedio,response);
                        mMovieListAdapter.setOnFullScreenListener(this);
                        mVideoList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mVideoList.setAdapter(mMovieListAdapter);
                        mMovieListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                                    ShareUtil.shareVedio(getActivity(),mItemList.get(checkPositon));
                                }else {
                                    ShareUtil.shareVedio(getActivity(),mItemList.get(checkPositon));
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

        mGestureDetector = new GestureDetector(new GestureControllerListener(getActivity()));
    }


    private void onPortraitChanged() {
        if (mCurVideoView == null) {
            return;
        }
        mFullScreenGroup.setVisibility(View.GONE);
        mFullScreenGroup.removeAllViews();

        mVideoList.setVisibility(View.VISIBLE);

        mCurVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        mCurViewHolder.addView(mCurVideoView, -1);
        mCurVideoView.setMediaController(mPortraitMC);
        mPortraitMC.setAnchorView(mCurVideoView);

        getDayActivityMainActivity().setTabViewVisible(true);
    }

    public DayActivityMainActivity getDayActivityMainActivity() {
        return (DayActivityMainActivity) getActivity();
    }


    private void onLandscapeChanged() {
        if (mCurVideoView == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) mCurVideoView.getParent();
        viewGroup.removeAllViews();
        mVideoList.setVisibility(View.GONE);

        mCurViewHolder = viewGroup;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        mFullScreenGroup.addView(mCurVideoView, layoutParams);
        mFullScreenGroup.setVisibility(View.VISIBLE);
        mCurVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);

        getDayActivityMainActivity().setTabViewVisible(false);

        mCurVideoView.setMediaController(mLandscapeMC);
        mLandscapeMC.setOnShownListener(new MediaController.OnShownListener() {
            @Override
            public void onShown() {
//                if (mPlayConfigView.getVisibility() == View.VISIBLE) {
//                    mPlayConfigView.setVisibility(View.GONE);
//                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onPortraitChanged();
        } else {
            onLandscapeChanged();
        }
    }


    @Override
    public void onFragmentPause() {
        if (mMovieListAdapter != null) {
            mMovieListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onFragmentResume() {
    }

    @Override
    public void onActivityPause() {
        if (mMovieListAdapter != null && !mMovieListAdapter.needBackstagePlay()) {
            mNeedRestart = mMovieListAdapter.isCurVideoPlaying();
            if (mNeedRestart) {
                mMovieListAdapter.pauseCurVideoView();
            } else {
                mMovieListAdapter.stopCurVideoView();
            }
        }
    }

    @Override
    public void onActivityResume() {
        if (mMovieListAdapter != null) {
            if (mNeedRestart) {
                mMovieListAdapter.restartCurVideoView();
                mNeedRestart = false;
            }
        }
    }

    @Override
    public void onActivityDestroy() {
        if (mMovieListAdapter != null) {
            mMovieListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFullScreenGroup.getVisibility() == View.VISIBLE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().finish();
        }
    }


    @Override
    public void onFullScreen(PLVideoTextureView videoView, MediaController mediaController) {
        if (videoView == null) {
            return;
        }
        mCurVideoView = videoView;
        mPortraitMC = mediaController;
//        mPlayConfigView.setVideoView(mCurVideoView);

        if (mFullScreenGroup.getVisibility() != View.VISIBLE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
//            case R.id.play_config_view:
//                return true;
            case R.id.media_controller:
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
        }
        return false;
    }

}
