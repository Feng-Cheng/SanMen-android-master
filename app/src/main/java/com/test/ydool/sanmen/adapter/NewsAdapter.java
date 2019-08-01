package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.TodayHotBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class NewsAdapter extends BaseQuickAdapter<TodayHotBean,BaseViewHolder>{
    public NewsAdapter(int layoutResId, @Nullable List<TodayHotBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayHotBean item) {
        helper.setText(R.id.tv_tile1,item.getTitle());
        helper.setText(R.id.tv_hot_time1,item.getDate());
        helper.setText(R.id.tv_hot_from1,item.getFrom());
        helper.setText(R.id.tv_tile2,item.getTitle1());
        helper.setText(R.id.tv_hot_time2,item.getDate1());
        helper.setText(R.id.tv_hot_from2,item.getFrom1());
        helper.addOnClickListener(R.id.rl_today_hot1);
        helper.addOnClickListener(R.id.rl_today_hot2);
    }
}
