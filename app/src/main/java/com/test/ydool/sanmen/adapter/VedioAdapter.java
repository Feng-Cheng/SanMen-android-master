package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.bean.VedioBean;
import com.test.ydool.sanmen.widget.MediaController;

import java.util.List;

import static com.test.ydool.sanmen.utils.Utils.createAVOptions;


/**
 * Created by Administrator on 2018/6/21.
 */

public class VedioAdapter extends BaseQuickAdapter<VedioBean,VedioAdapter.ViewHolder> {

    public interface OnFullScreenListener {
        void onFullScreen(PLVideoTextureView videoView, MediaController mediaController);
    }

    private VedioAdapter.OnFullScreenListener mOnFullScreenListener;
    private VedioAdapter.ViewHolder mCurViewHolder;


    public VedioAdapter(int layoutResId, @Nullable List<VedioBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(VedioAdapter.ViewHolder helper, VedioBean item) {
//        helper.videoPath = "http://zs.ydool.com:8082/upload/Exhibition/%E7%88%B1%E5%9C%A8%E4%B8%89%E9%97%A8%E4%B9%90%E5%9C%A8%E7%A4%BC%E5%A0%82%20%E9%97%B9%E5%85%83%E5%AE%B5.mp4";
        try {
            helper.videoPath = item.getUrl();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (item.getPic() != null && item.getPic().length()>0) {
            Glide.with(mContext).load(TerminalInfo.BASE_URL+item.getPic()).into(helper.coverImage);
        }else {
            Glide.with(mContext).load(R.drawable.black_background).into(helper.coverImage);
        }

        if ( item.getTheme()!=null){
            helper.setText(R.id.tv_video_userName, item.getTheme());
        }

        helper.addOnClickListener(R.id.tv_share_to_man);
    }
    public void setOnFullScreenListener(VedioAdapter.OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }
    class ViewHolder extends BaseViewHolder{
        PLVideoTextureView videoView;
        String videoPath;
        ImageButton stopPlayImage;
        ImageView coverImage;
        View loadingView;
        ImageButton fullScreenImage;
        public ViewHolder(View itemView) {
            super(itemView);

            coverImage = itemView.findViewById(R.id.cover_image);
            stopPlayImage = itemView.findViewById(R.id.cover_stop_play);
            videoView = itemView.findViewById(R.id.video_texture_view);
            loadingView = itemView.findViewById(R.id.loading_view);
            fullScreenImage = itemView.findViewById(R.id.full_screen_image);
            final MediaController mediaController = itemView.findViewById(R.id.media_controller);

            videoView.setAVOptions(createAVOptions());
            videoView.setBufferingIndicator(loadingView);
            videoView.setMediaController(mediaController);
            videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            videoView.setLooping(true);
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
                    mCurViewHolder = ViewHolder.this;
                    startCurVideoView();
                }
            });

            fullScreenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnFullScreenListener != null) {
                        mOnFullScreenListener.onFullScreen(ViewHolder.this.videoView, mediaController);
                    }
                }
            });

        }


    }



    public void startCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.setVideoPath(mCurViewHolder.videoPath);
            mCurViewHolder.videoView.start();
            mCurViewHolder.loadingView.setVisibility(View.VISIBLE);
            mCurViewHolder.stopPlayImage.setVisibility(View.GONE);
        }
    }

    public void restartCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.start();
            mCurViewHolder.stopPlayImage.setVisibility(View.GONE);
        }
    }

    public boolean isCurVideoPlaying() {
        return mCurViewHolder != null && mCurViewHolder.videoView.isPlaying();
    }

    public boolean needBackstagePlay() {
//        return mCurViewHolder != null && BACKSTAGE_PLAY_TAG.equals(mCurViewHolder.videoView.getTag());
         return false;
    }

    public void pauseCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.pause();
            mCurViewHolder.loadingView.setVisibility(View.GONE);
        }
    }

    private void resetConfig() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.setRotation(0);
            mCurViewHolder.videoView.setMirror(false);
            mCurViewHolder.videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        }
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            resetConfig();
            mCurViewHolder.videoView.stopPlayback();
            mCurViewHolder.loadingView.setVisibility(View.GONE);
            mCurViewHolder.coverImage.setVisibility(View.VISIBLE);
            mCurViewHolder.stopPlayImage.setVisibility(View.VISIBLE);
        }
    }
}
