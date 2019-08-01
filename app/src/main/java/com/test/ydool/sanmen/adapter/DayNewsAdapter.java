package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.DayNewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xsg on 2016/9/4.
 */
public class DayNewsAdapter extends BaseQuickAdapter<DayNewsBean,BaseViewHolder> {

    public DayNewsAdapter(int layoutResId, @Nullable List<DayNewsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DayNewsBean item) {
        helper.setText(R.id.title,item.getActivity_theme());
        helper.setText(R.id.tv_theme_title,item.getTime());
        for (DayNewsBean.imgUrl a : item.getDocument()){
            if (a.getType().equals("1")){
                Glide.with(mContext)
                        .load(TerminalInfo.BASE_URL+a.getUrl())
                        .crossFade()
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.error)
                        .error(R.drawable.error)
                        .into((ImageView) helper.getView(R.id.pic));
            }
        }
        helper.addOnClickListener(R.id.item);
    }
}
