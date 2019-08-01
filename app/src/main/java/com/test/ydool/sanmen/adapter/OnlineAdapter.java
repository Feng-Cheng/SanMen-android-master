package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.OnlineAdviceBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class OnlineAdapter extends BaseQuickAdapter<OnlineAdviceBean,BaseViewHolder> {

    public OnlineAdapter(int layoutResId, @Nullable List<OnlineAdviceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineAdviceBean item) {
        helper.setText(R.id.tv_online_advices_content,item.getContent());
        helper.setText(R.id.tv_online_advices_time,item.getCreate_time());
        helper.setText(R.id.tv_online_advices_name,item.getUserName());
        if (item.getStatus().equals("0")||item.getStatus().equals("3")||item.getStatus().equals("2")){
            helper.setImageResource(R.id.iv_online_advices_type,R.drawable.ic_online_advice_flase);
        }else{
            helper.setImageResource(R.id.iv_online_advices_type,R.drawable.ic_online_advice_true);
        }
        helper.addOnClickListener(R.id.rl_online);
    }
}
