package com.test.ydool.sanmen.adapter;

import android.support.annotation.Nullable;
import android.widget.Adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.HomeEntity;
import com.test.ydool.sanmen.bean.HomeOnlineBean;
import com.test.ydool.sanmen.bean.OnlineAdviceBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class HomeOnlineAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>{


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeOnlineAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(HomeEntity.TYPE_TITLE,R.layout.item_home_online_advices);
        addItemType(HomeEntity.TYPE_LIST,R.layout.item_online_advices);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        HomeEntity homeEntity = (HomeEntity) item;
        switch (homeEntity.getItemType()){
            case HomeEntity.TYPE_TITLE:
                HomeOnlineBean homeOnlineBean = homeEntity.getHomeOnlineBean();
                helper.setText(R.id.tv_home_online_postition
                        ,homeOnlineBean.getPostionStr());
                break;
            case HomeEntity.TYPE_LIST:
                OnlineAdviceBean onlineAdviceBean = homeEntity.getOnlineAdviceBean();
                helper.setText(R.id.tv_online_advices_content,onlineAdviceBean.getContent());
                helper.setText(R.id.tv_online_advices_time,onlineAdviceBean.getCreate_time());
                helper.setText(R.id.tv_online_advices_name,onlineAdviceBean.getUserName());
                helper.addOnClickListener(R.id.rl_online);
        }

    }
}
