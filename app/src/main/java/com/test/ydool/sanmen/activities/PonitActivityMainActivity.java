package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PonitActivityTypeBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

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
 * 点单列表
 */

public class PonitActivityMainActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlPonit;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private PointActivityTypeAdapter mAdapter;

    private List<PonitActivityTypeBean>  list;

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
        tvTitle.setText(R.string.activty_ponit);
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


        SPUtils spUtils = SPUtils.getInstance("sanMen");
        Subscription subscription = userRepository.getPonitActivityTypeList(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = response;
                    mAdapter = new PointActivityTypeAdapter(R.layout.item_ponit_activity_main,response);
                    rlPonit.setLayoutManager(new LinearLayoutManager(mContext));
                    rlPonit.setAdapter(mAdapter);

                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(mContext,PonitActivityChooseActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("titleId",list.get(position).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常");
                });

        subscriptions.add(subscription);

    }

    class PointActivityTypeAdapter extends BaseQuickAdapter<PonitActivityTypeBean,BaseViewHolder>{

        public PointActivityTypeAdapter(int layoutResId, @Nullable List<PonitActivityTypeBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PonitActivityTypeBean item) {
            helper.setText(R.id.tv_sign_name,"活动主题");
            helper.setText(R.id.tv_sign_time,item.getTitle());

            helper.addOnClickListener(R.id.ll_all_point);
        }
    }

}
