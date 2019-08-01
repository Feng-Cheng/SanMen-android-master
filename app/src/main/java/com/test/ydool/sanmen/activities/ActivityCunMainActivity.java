package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.ActivityAnnoceAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.UrlBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.view.PopupList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/6.
 *
 * 村的活动预告
 */

public class ActivityCunMainActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvTiltleAll;
    @BindView(R.id.rlCun)
    RecyclerView rlCun;
    @BindView(R.id.btn_activity_cun_accpter)
    Button btnAccepter;
    @BindView(R.id.btn_activity_cun_no_accpter)
    Button btnNoAccepter;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private ActivityAnnoceAdapter mAdapter;

    private List<ActivityPreviewBean> list;

    private List<ActivityPreviewBean> list1;

    private List<ActivityPreviewBean> list2;

    private List<String> popupMenuItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_main_cun);
        ButterKnife.bind(this);

        initView();
    }


    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mAdapter = new ActivityAnnoceAdapter(R.layout.item_cun_activity,list);
                rlCun.setLayoutManager(new LinearLayoutManager(mContext));
                rlCun.setAdapter(mAdapter);
                mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent intent = new Intent(mContext,ActivityHomeSHowOnewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("auname", list.get(position).getAuname());
                        bundle.putString("title", list.get(position).getTitle());
                        bundle.putString("time", list.get(position).getTime());
                        bundle.putString("organizer", list.get(position).getOrganizer());
                        bundle.putString("detail", list.get(position).getDetail());
                        bundle.putString("ps", list.get(position).getRemarks());
                        bundle.putString("id", list.get(position).getId());
                        bundle.putString("status", list.get(position).getStatus());
                        ArrayList<String> urls = new ArrayList<>();
                        if (list.get(position).getPic()!=null){
                            for (UrlBean a : list.get(position).getPic()){
                                urls.add(a.getUrl());
                            }
                        }
                        bundle.putStringArrayList("urls",urls);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                        return false;
                    }
                });
            }else {
                mAdapter.setNewData(list);
                rlCun.setAdapter(mAdapter);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (list2!=null&&list2 == list){
            upDate();
        }
    }

    private void initView() {
        tvTitle.setText(R.string.activty_announce);
        tvTiltleAll.setText(R.string.put);
        tvTitle.setTextColor(0xfffaa8ae);
        tvTiltleAll.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        popupMenuItemList.add("删除");

        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTiltleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),ActivityShowPutActivity.class);
                startActivity(intent);
            }
        });
        SPUtils spUtils = SPUtils.getInstance("sanMen");

        if (spUtils.getInt("quanxian") == 1){
            btnNoAccepter.setText("待审核");
        }
        btnAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != list1){
                    list = list1;
                    btnAccepter.setBackgroundResource(R.drawable.button_selector_choose);
                    btnNoAccepter.setBackgroundResource(R.drawable.button_selector);
                    Message message = new Message();
                    message.what = 1;
                    mHander.sendMessage(message);
                }
            }
        });

        btnNoAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoAccepter.setBackgroundResource(R.drawable.button_selector_choose);
                btnAccepter.setBackgroundResource(R.drawable.button_selector);
                upDate();
            }
        });

        btnAccepter.setBackgroundResource(R.drawable.button_selector_choose);
        btnNoAccepter.setBackgroundResource(R.drawable.button_selector);
        Subscription subscription = userRepository.getActivityPreviewAccepterList(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = new ArrayList<>();
                    list1 = new ArrayList<>();
                    list2 = new ArrayList<>();
                    if (response!=null){
                        list1 = response;
                        list = response;
                        Message message = new Message();
                        message.what = 0;
                        mHander.sendMessage(message);
                    }
                },throwable ->{
                    Log.i("Error-->",throwable.toString());
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);

    }

    private void upDate(){
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        Subscription subscription1 = userRepository.getActivityPreviewNoPassList(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list2 = new ArrayList<>();
                    if (response!=null){
                        list2 = response;
                        if (list != list2){
                            list = list2;
                            Message message = new Message();
                            message.what = 1;
                            mHander.sendMessage(message);
                        }
                    }
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription1);
    }

}
