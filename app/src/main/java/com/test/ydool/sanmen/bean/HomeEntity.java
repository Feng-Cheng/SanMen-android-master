package com.test.ydool.sanmen.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/6/21.
 */

public class HomeEntity implements MultiItemEntity{

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_LIST = 2;


    private int itemType;

    public HomeEntity(int itemType){
        this.itemType=itemType;
    }

    public HomeOnlineBean homeOnlineBean;
    public OnlineAdviceBean onlineAdviceBean;


    public HomeOnlineBean getHomeOnlineBean() {
        return homeOnlineBean;
    }

    public void setHomeOnlineBean(HomeOnlineBean homeOnlineBean) {
        this.homeOnlineBean = homeOnlineBean;
    }

    public OnlineAdviceBean getOnlineAdviceBean() {
        return onlineAdviceBean;
    }

    public void setOnlineAdviceBean(OnlineAdviceBean onlineAdviceBean) {
        this.onlineAdviceBean = onlineAdviceBean;
    }

    @Override
    public int getItemType() {
        return this.itemType;
    }

}
