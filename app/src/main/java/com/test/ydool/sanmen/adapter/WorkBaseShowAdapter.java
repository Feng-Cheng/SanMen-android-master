package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.WorkBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class WorkBaseShowAdapter extends BaseQuickAdapter<WorkBaseBean,BaseViewHolder>{
    public WorkBaseShowAdapter(int layoutResId, @Nullable List<WorkBaseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkBaseBean item) {
        helper.setText(R.id.tv_base_time,item.getTime());
        helper.setText(R.id.tv_base_name,item.getName());
        helper.setText(R.id.tv_auditorName,item.getAuditorium());
        helper.setText(R.id.tv_base_main,item.getSummary());
        helper.addOnClickListener(R.id.tv_checkmore);
    }
}
