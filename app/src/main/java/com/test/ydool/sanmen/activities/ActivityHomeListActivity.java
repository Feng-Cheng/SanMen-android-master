package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.OnlineAdviceBean;
import com.test.ydool.sanmen.bean.PonitActivityTypeBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 主题活动页面
 */

public class ActivityHomeListActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlList;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;


    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private ListAdapter mAdapter;

    private List<PonitActivityTypeBean> list;

    SPUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_main);
        ButterKnife.bind(this);

        refreshLayout.setEnabled(false);
        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.them_activiy);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Subscription subscription1 = userRepository.getPonitActivityTypeList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = new ArrayList<>();
                    if (response!=null){
                        list = response;
                        mAdapter = new ListAdapter(R.layout.item_activity_list_main,list);
                        rlList.setLayoutManager(new LinearLayoutManager(mContext));
                        rlList.setAdapter(mAdapter);

                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(mContext,ActivityListHomeMainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id",list.get(position).getId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                },throwable -> {
                    Log.e("throwable===>",throwable.toString());
                });
        subscriptions.add(subscription1);

    }

    class ListAdapter extends BaseQuickAdapter<PonitActivityTypeBean,BaseViewHolder> {

        public ListAdapter(int layoutResId, @Nullable List<PonitActivityTypeBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PonitActivityTypeBean item) {
            System.out.println(item.getUrl());
            Glide.with(mContext)
                    .load(TerminalInfo.BASE_URL+item.getUrl())
                    .error(R.drawable.error)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into((ImageView) helper.getView(R.id.iv_activity_list_main));
            helper.addOnClickListener(R.id.iv_activity_list_main);
        }
    }

}
