package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.bean.VideoToPeoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class VideoToPeoAdapter extends BaseQuickAdapter<VideoToPeoBean,BaseViewHolder>{
    public VideoToPeoAdapter(int layoutResId, @Nullable List<VideoToPeoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoToPeoBean item) {
        Glide.with(mContext).load(TerminalInfo.BASE_URL+item.getImg()).into((ImageView) helper.getView(R.id.iv_video_img));
        helper.setText(R.id.tv_video_title, item.getTitle());
        helper.addOnClickListener(R.id.iv_video_img);
    }
}
