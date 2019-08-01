package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.ShareHuifuBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class ShareHuifuAdapter extends BaseQuickAdapter<ShareHuifuBean,BaseViewHolder>{
    public ShareHuifuAdapter(int layoutResId, @Nullable List<ShareHuifuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareHuifuBean item) {
        if (item.getAuditorium()!=null&&item.getAuditorium()!=""){
            helper.setText(R.id.tv_share_audname,item.getAuditorium());
        }else {
            helper.setText(R.id.tv_share_audname,item.getArea());
        }
        helper.setText(R.id.tv_share_man,item.getUsername());
        helper.setText(R.id.tv_share_time,item.getTime());
        helper.setText(R.id.tv_share_content,item.getDetail());
    }
}
