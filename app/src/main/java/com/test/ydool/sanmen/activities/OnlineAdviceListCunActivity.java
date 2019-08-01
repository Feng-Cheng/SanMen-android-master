package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.ActivityAnnoceAdapter;
import com.test.ydool.sanmen.adapter.OnlineAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.OnlineAdviceBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.test.ydool.sanmen.utils.ToastUtil;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/6.
 *
 * 在线建言村列表
 */

public class OnlineAdviceListCunActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.rlOnlineAcvice)
    RecyclerView rvList;
    @BindView(R.id.btn_activity_online_accpter)
    Button btnAccepter;
    @BindView(R.id.btn_activity_online_no_accpter)
    Button btnNoAccepter;

    private OnlineAdapter oaAdapter;

    private UserRepository userRepository;

    private List<OnlineAdviceBean> list;

    private List<OnlineAdviceBean> list1;

    private BuildBean loading;

    private List<OnlineAdviceBean> list2;

    private CompositeSubscription subscriptions;

    private SPUtils spUtils = SPUtils.getInstance("sanMen");

    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                oaAdapter = new OnlineAdapter(R.layout.item_online_advices,list);
                rvList.setLayoutManager(new LinearLayoutManager(mContext));
                rvList.setAdapter(oaAdapter);

                oaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        Bundle bundle=new Bundle();
                        bundle.putString("pname",list.get(position).getPname());
                        bundle.putString("content",list.get(position).getContent());
                        bundle.putString("topname",list.get(position).getStatePosition());
                        bundle.putString("status",list.get(position).getStatus());
                        bundle.putBoolean("flag",list.get(position).isFlag());
                        bundle.putString("id",list.get(position).getId().toString());
                        Intent intent;
                        intent = new Intent(mContext, OnlineAdviceShenActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                DialogUIUtils.dismiss(loading);
            }else {
                oaAdapter.setNewData(list);
                rvList.setAdapter(oaAdapter);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        update();
        btnAccepter.setBackgroundResource(R.drawable.button_selector);
        btnNoAccepter.setBackgroundResource(R.drawable.button_selector);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_online_advicelist_cun);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.Online_advice);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        tvAll.setText("建言");
        tvAll.setTextColor(0xfffaa8ae);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OnlineAdviceActivity.class);
                startActivity(intent);
            }
        });

        //SPUtils spUtils = SPUtils.getInstance("sanMen");

//        update();
        btnAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != list1){
                    list = list1;
                    Message message = new Message();
                    message.what = 1;
                    mHander.sendMessage(message);
                    btnAccepter.setBackgroundResource(R.drawable.button_selector_choose);
                    btnNoAccepter.setBackgroundResource(R.drawable.button_selector);
                }
            }
        });

        btnNoAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != list2){
                    list = list2;
                    Message message = new Message();
                    message.what = 1;
                    mHander.sendMessage(message);
                    btnNoAccepter.setBackgroundResource(R.drawable.button_selector_choose);
                    btnAccepter.setBackgroundResource(R.drawable.button_selector);
                }
            }
        });
    }

    private void update(){
        loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
        loading.show();
        Subscription subscription = userRepository.getOnlineAdviceList(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response != null){
                        list = new ArrayList<>();
                        list1 = new ArrayList<>();
                        list2 = new ArrayList<>();
                        list=response.getData();
                        for (OnlineAdviceBean oab:list){
                            if("1".equals(oab.getStatus())){
                                list1.add(oab);
                            }else{
                                list2.add(oab);
                            }
                        }
                        Message message = new Message();
                        message.what = 0;
                        mHander.sendMessage(message);
                    }else {
                        DialogUIUtils.dismiss(loading);
                        ToastUtil.showLongMessage(mContext,"加载失败！");
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                    DialogUIUtils.dismiss(loading);
                });

        subscriptions.add(subscription);
    }
}
