package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.NewsAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PonitListAfterBean;
import com.test.ydool.sanmen.bean.TodayHotBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.DividerItemDecoration;
import com.test.ydool.sanmen.utils.ScreenManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/7/16.
 *
 * 主题活动页面
 */

public class ThemeActivityMainActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlActivity;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;


    private UserRepository userRepository;
    private CompositeSubscription subscriptions;

    private List<PonitListAfterBean> list = new ArrayList<>();

    private ThemeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.them_activiy);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        Bundle bundle = getIntent().getExtras();

        PonitListAfterBean bean = new PonitListAfterBean();
        bean.setActivity_theme(bundle.getString("theme"));
        bean.setUrl(bundle.getString("url"));
        bean.setTime(bundle.getString("time"));
        list.add(bean);
        mAdapter = new ThemeAdapter(R.layout.item_news,list);
        rlActivity.setLayoutManager(new LinearLayoutManager(mContext));
        rlActivity.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        rlActivity.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    class ThemeAdapter extends BaseQuickAdapter<PonitListAfterBean,BaseViewHolder> {

        public ThemeAdapter(int layoutResId, @Nullable List<PonitListAfterBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PonitListAfterBean item) {
            helper.setText(R.id.title, item.getActivity_theme());
            helper.setText(R.id.tv_put_time, item.getTime());

            Glide.with(mContext)
                    .load(TerminalInfo.BASE_URL + item.getUrl())
                    .crossFade()
                    .placeholder(R.drawable.error)
                    .error(R.drawable.error)
                    .into((ImageView) helper.getView(R.id.pic));
            helper.addOnClickListener(R.id.item);
        }
    }
}
