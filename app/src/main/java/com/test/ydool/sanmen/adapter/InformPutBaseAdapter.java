package com.test.ydool.sanmen.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.InformBaseBean;
import com.test.ydool.sanmen.utils.utile;
import com.test.ydool.sanmen.view.MyTextview;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/6/11.
 */

public class InformPutBaseAdapter extends BaseQuickAdapter<InformBaseBean,BaseViewHolder>{


    public InformPutBaseAdapter(int layoutResId, @Nullable List<InformBaseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InformBaseBean item) {
        helper.setText(R.id.tv_base_time,item.getCreatedate());
        helper.setText(R.id.tv_base_name,item.getPname()+"/");
        helper.setText(R.id.tv_conten_title,item.getTitle());
        helper.setText(R.id.tv_base_main, utile.delHTMLTag(item.getDetail()));
//        MyTextview textview = helper.getView(R.id.tv_base_main);
//        textview.setText(utile.delHTMLTag(item.getDetail()));
        if (item.getType() == 0){
            helper.setTextColor(R.id.tv_conten_title, Color.parseColor("#ff9501"));
            helper.setImageResource(R.id.iv_base_kind,R.drawable.ic_tongzhi);
        }else {
            helper.setTextColor(R.id.tv_conten_title, Color.parseColor("#fd687e"));
            helper.setImageResource(R.id.iv_base_kind,R.drawable.ic_zhengche);
        }
        if (item.getDname()==null){
            helper.setGone(R.id.tv_ohter_name,false);
            helper.setGone(R.id.tv_ohter_content,false);
        }else {
            helper.setText(R.id.tv_ohter_name,item.getDname());
        }
        helper.addOnClickListener(R.id.tv_ohter_name);

    }
}
