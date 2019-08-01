package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.ActivityBriefBean;
import com.test.ydool.sanmen.bean.ActivityExhibitionBean;
import com.test.ydool.sanmen.utils.utile;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class ActivityMainAdapter extends BaseQuickAdapter<ActivityExhibitionBean,BaseViewHolder>{
    public ActivityMainAdapter(int layoutResId, @Nullable List<ActivityExhibitionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityExhibitionBean item) {
        helper.setText(R.id.activity_title,item.getActivity_theme());
//        helper.setText(R.id.tv_authort,item.getCreate_user_id());
        helper.setText(R.id.tv_authort,"");
        helper.setText(R.id.tv_read_num,"");
        helper.setText(R.id.tv_time,item.getTime());
        if (item.getContent() == null || item.getContent().equals("")){
            helper.setText(R.id.tv_main,item.getContent());
        }else {
            String content = "\t\t\t\t" +utile.delHTMLTag(item.getContent());
            helper.setText(R.id.tv_main, content);

        }

        helper.setVisible(R.id.tv_read_all,false);

        helper.addOnClickListener(R.id.tv_read_all);
    }
}
