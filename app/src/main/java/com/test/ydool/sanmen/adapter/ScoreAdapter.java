package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.ScoreBean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/9.
 */

public class ScoreAdapter extends BaseQuickAdapter<ScoreBean,BaseViewHolder> {
    public ScoreAdapter(int layoutResId, @Nullable List<ScoreBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreBean item) {
        helper.setText(R.id.tv_number,item.getNumber()+"");
        if (!item.getName().equals("")){
            helper.setText(R.id.tv_name,item.getName());
        }
        if (!item.getScore().equals("")){
            helper.setText(R.id.tv_score,item.getScore());
        }
    }
}
