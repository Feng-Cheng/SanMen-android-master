package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.VedioAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityExhibitionBean;
import com.test.ydool.sanmen.bean.UrlBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.widget.MediaController;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.test.ydool.sanmen.utils.Utils.createAVOptions;

/**
 * Created by Administrator on 2018/7/26.
 *
 * 活动展示审核
 */

public class ActivityMineVillageShenActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ll_approve)
    LinearLayout llApprove;
//    @BindView(R.id.videoplayer)
//    JZVideoPlayerStandard vedio;
//    @BindView(R.id.detail_player)
//    SampleCoverVideo player;
    @BindView(R.id.tv_inform_report_type)
    TextView tvAudName;
    @BindView(R.id.tv_inform_report_putman)
    TextView tvActivTime;
    @BindView(R.id.tv_inform_report_tonotice_pepole)
    TextView tvOrgainze;
    @BindView(R.id.tv_inform_report_notice_time)
    TextView tvActivTheme;
    @BindView(R.id.et_inform_report_notice_content)
    TextView tvContent;
    @BindView(R.id.et_inform_report_remarks)
    TextView tvPs;
    @BindView(R.id.btn_activity_cun_accpter)
    Button btnAccpter;
    @BindView(R.id.btn_activity_cun_no_accpter)
    Button btnNoAccpter;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.fgLayout)
    FrameLayout fgVideo;
    @BindView(R.id.video_texture_view)
    PLVideoTextureView videoView;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.media_controller)
    MediaController mediaController;
    @BindView(R.id.cover_stop_play)
    ImageButton stopPlayImage;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.full_screen_image)
    ImageButton fullScreenImage;



    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;

    private ArrayList<String> list_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_mine_village_shen);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("活动展示");
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        spUtils = SPUtils.getInstance("sanMen");
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActivityExhibitionBean bean = (ActivityExhibitionBean) getIntent().getSerializableExtra("bean");

        tvAudName.setText(bean.getAddr());
        tvActivTime.setText(bean.getTime());
        tvOrgainze.setText("");
        tvActivTheme.setText(bean.getActivity_theme());
        tvContent.setText(bean.getContent());
        tvPs.setText(bean.getRemark());
        if (bean.getVideo()!=null&&bean.getVideo().length()>0){
            initVideo(bean.getVideo(),"");
//            vedio.setVisibility(View.VISIBLE);
//            vedio.setUp(TerminalInfo.BASE_URL+bean.getVideo(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//            player.setVisibility(View.VISIBLE);
//            player.setUpLazy(TerminalInfo.BASE_URL+bean.getVideo(), true, null, null, "");
//            player.getBackButton().setVisibility(View.GONE);
//            player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    player.startWindowFullscreen(mContext, false, true);
//                }
//            });
//
//            player.setAutoFullWithSize(true);
        }
        list_path = new ArrayList<>();
        if (bean.getPic()!=null&&bean.getPic().size()>0){
            for (UrlBean u : bean.getPic()){
                list_path.add(TerminalInfo.BASE_URL + u.getUrl());
            }
            banner.setVisibility(View.VISIBLE);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImageLoader(new MyLoader());
            banner.setImages(list_path);
            banner.setBannerAnimation(Transformer.Default);
            banner.setDelayTime(3000);
            banner.isAutoPlay(true);
            banner.setIndicatorGravity(BannerConfig.CENTER)
                    .start();
        }
        Set<String> resList=spUtils.getStringSet("resList");
        if(resList.contains("/auditoriumlnfo/auditorium/town")&&bean.getStatus().equals("1")) {
            llApprove.setVisibility(View.VISIBLE);
            String token = spUtils.getString("token");
            btnAccpter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subscription subscription = userRepository.postActivityExhibitionPass(token,bean.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(reponse ->{
                                if (reponse.getCode() == 0){
                                    ToastUtil.showLongMessage(mContext,"审核成功！");
                                    ScreenManager.getInstance().popActivity((Activity) mContext);
                                }else {
                                    ToastUtil.showLongMessage(mContext,"审核失败！"+reponse.getMsg());
                                }
                            },throwable -> {
                                throwable.printStackTrace();
                                ToastUtil.showLongMessage(mContext,"网络异常！");
                            });
                    subscriptions.add(subscription);
                }
            });

            btnNoAccpter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subscription subscription = userRepository.postActivityExhibitionNoPass(token,bean.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(reponse ->{
                                if (reponse.getCode() == 0){
                                    ToastUtil.showLongMessage(mContext,"审核成功！");
                                    ScreenManager.getInstance().popActivity((Activity) mContext);
                                }else {
                                    ToastUtil.showLongMessage(mContext,"审核失败！"+reponse.getMsg());
                                }
                            },throwable -> {
                                throwable.printStackTrace();
                                ToastUtil.showLongMessage(mContext,"网络异常！");
                            });
                    subscriptions.add(subscription);
                }
            });
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopCurVideoView();
    }
    private void initVideo(String videoPath,String pic){
        fgVideo.setVisibility(View.VISIBLE);


        videoView.setAVOptions(createAVOptions());
        videoView.setBufferingIndicator(loadingView);
        videoView.setMediaController(mediaController);
        videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        videoView.setLooping(true);
        if (pic != null && pic.length()>0) {
            Glide.with(mContext).load(TerminalInfo.BASE_URL+pic).into(coverImage);
        }else {
            Glide.with(mContext).load(R.drawable.black_background).into(coverImage);
        }
        videoView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    coverImage.setVisibility(View.GONE);
                    stopPlayImage.setVisibility(View.GONE);
                    mediaController.hide();
                }
            }
        });
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCurVideoView();
//                mCurViewHolder = VedioAdapter.ViewHolder.this;
                startCurVideoView(videoPath);
            }
        });

        fullScreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mOnFullScreenListener != null) {
//                    mOnFullScreenListener.onFullScreen(VedioAdapter.ViewHolder.this.videoView, mediaController);
//                }
            }
        });

    }

    public void startCurVideoView(String videoPath) {
//        if (mCurViewHolder != null) {
            videoView.setVideoPath(videoPath);
            videoView.start();
            loadingView.setVisibility(View.VISIBLE);
            stopPlayImage.setVisibility(View.GONE);
//        }
    }

    public void restartCurVideoView() {
//        if (mCurViewHolder != null) {
            videoView.start();
            stopPlayImage.setVisibility(View.GONE);
//        }
    }

    public boolean needBackstagePlay() {
//        return mCurViewHolder != null && BACKSTAGE_PLAY_TAG.equals(mCurViewHolder.videoView.getTag());
        return false;
    }

    public void pauseCurVideoView() {
//        if (mCurViewHolder != null) {
            videoView.pause();
            loadingView.setVisibility(View.GONE);
//        }
    }

    private void resetConfig() {
//        if (mCurViewHolder != null) {
            videoView.setRotation(0);
            videoView.setMirror(false);
            videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
//        }
    }

    public void stopCurVideoView() {
//        if (mCurViewHolder != null) {
            resetConfig();
            videoView.stopPlayback();
            loadingView.setVisibility(View.GONE);
            coverImage.setVisibility(View.VISIBLE);
            stopPlayImage.setVisibility(View.VISIBLE);
//        }
    }

}
