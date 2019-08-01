package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.DaySignBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class SignAdapater extends BaseQuickAdapter<DaySignBean,BaseViewHolder> {
    public SignAdapater(int layoutResId, @Nullable List<DaySignBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DaySignBean item) {
        helper.setText(R.id.tv_sign_name,item.getName());
        helper.setText(R.id.tv_sign_time,item.getTime());
    }
}
