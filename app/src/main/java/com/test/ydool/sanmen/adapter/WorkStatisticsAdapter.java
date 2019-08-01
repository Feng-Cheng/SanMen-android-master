package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.WorkStatisticsBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class WorkStatisticsAdapter extends BaseQuickAdapter<WorkStatisticsBean,BaseViewHolder>{
    public WorkStatisticsAdapter(int layoutResId, @Nullable List<WorkStatisticsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkStatisticsBean item) {
        helper.setText(R.id.tv_work_statics_name,item.getDname());
        helper.setText(R.id.tv_work_statics_time,item.getTime());
        if (0==item.getJobstatus()){
            helper.setText(R.id.tv_work_statics_work_state,"已填");
        }else if (1==item.getJobstatus()){
            helper.setText(R.id.tv_work_statics_work_state,"未填");
        }else {
            helper.setText(R.id.tv_work_statics_work_state,item.getJobname());
        }
        if (0==item.getStatus()){
            helper.setText(R.id.tv_work_statics_sign_state,R.string.no_sign);
        }else if (1==item.getStatus()){
            helper.setText(R.id.tv_work_statics_sign_state,R.string.sign);
        }else {
            helper.setText(R.id.tv_work_statics_sign_state,item.getSignName());
        }
    }
}
