package com.test.ydool.sanmen.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.utils.utile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/6/14.
 */

public class ActivityAnnoceAdapter extends BaseQuickAdapter<ActivityPreviewBean,BaseViewHolder>{

    public ActivityAnnoceAdapter(int layoutResId, @Nullable List<ActivityPreviewBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityPreviewBean item) {
        if (item.getTitle() != null){
            helper.setText(R.id.tv_activity_title,item.getTitle());
        }
        if (!item.getOrganizer().equals("")){
            helper.setText(R.id.tv_activity_put_name,item.getOrganizer());
        }
        if (!item.getTime().equals("")){
             helper.setText(R.id.tv_activity_time,item.getTime());
        }
        helper.setText(R.id.tv_activity_content, "\u3000\u3000"+utile.delHTMLTag(item.getDetail()));

        if (item.getStatus()!=null){
            if (item.getStatus().equals("0")){
                helper.setText(R.id.tv_isShow,"待审核");
                helper.setTextColor(R.id.tv_isShow, Color.GREEN);
                helper.setVisible(R.id.tv_isShow,true);
            }else if (item.getStatus().equals("2")){
                helper.setText(R.id.tv_isShow,"不通过");
                helper.setTextColor(R.id.tv_isShow, Color.RED);
                helper.setVisible(R.id.tv_isShow,true);
            }
        }
        helper.addOnClickListener(R.id.tv_checkmore);
    }
}
