package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.OrderMealBean;
import com.test.ydool.sanmen.customview.CustomLoadMoreView;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.DividerItemDecoration;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/7/23.
 *
 * 点单显示
 */

public class PointTownXianShowActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout swLayout;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlOrderMeal;
    @BindView(R.id.tv_title_all)
    TextView tvAll;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private OrderMealAdapter mAdapter;

    private List<OrderMealBean> list;

    private int pageIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
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
        if (spUtils.getInt("quanxian")==1){
            tvAll.setText("管理");
            tvAll.setTextColor(0xfffaa8ae);
            tvAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,GiveQuanXianActivity.class);
                    startActivity(intent);
                }
            });
        }
        swLayout.setOnRefreshListener(this);
        swLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        Subscription subscription = userRepository
                .getOrderMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = new ArrayList<>();
                    if (response!=null){
                        list = response;
                        mAdapter = new OrderMealAdapter(R.layout.item_ordermeal_main,list);
//                        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//                            @Override
//                            public void onLoadMoreRequested() {
//                                mAdapter.setEnableLoadMore(true);
//                                getPonitList();
//                            }
//                        }, rlOrderMeal);
//                        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//                        mAdapter.setEnableLoadMore(true);
                        rlOrderMeal.setLayoutManager(new LinearLayoutManager(mContext));
                        rlOrderMeal.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
                        rlOrderMeal.setAdapter(mAdapter);
                    }
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
    }


    private void getPonitList(){
        Subscription subscription = userRepository
                .getOrderMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderMealBean -> {
                    hideLoading();
                    loadList(orderMealBean);
                },throwable -> {
                    throwable.printStackTrace();
                    hideLoading();
                });
        subscriptions.add(subscription);
    }

    private void loadList(List<OrderMealBean> data){
        if (data != null && data.size() > 0){
            if (pageIndex != 0){//上拉加载
                mAdapter.addData(data);
            }else {//下拉刷新
                mAdapter.setNewData(data);
            }
//            pageIndex += data.size();
        }else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onRefresh() {
//        SPUtils spUtils = SPUtils.getInstance("sanMen");
//        String token = spUtils.getString("token");
//        getPonitList(token,pageIndex, TerminalInfo.PAGE_SIZE);
        getPonitList();
    }

    class OrderMealAdapter extends BaseQuickAdapter<OrderMealBean,BaseViewHolder>{

        public OrderMealAdapter(int layoutResId, @Nullable List<OrderMealBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, OrderMealBean item) {
            helper.setText(R.id.tv_base_time,item.getTitle());
            helper.setText(R.id.tv_base_name,item.getActivity_theme());
            helper.setText(R.id.tv_conten_title,item.getTime());
            helper.setText(R.id.tv_base_main,item.getActivity_content());
            helper.setText(R.id.tv_ohter_content,"活动数量:"+item.getActivity_number());
            helper.setText(R.id.tv_ohter_name,"已点数量:"+item.getSurplus());
            String text = "已点: ";
            if (item.getAuditoriums()!=null&&item.getAuditoriums().size()>0){
                List<OrderMealBean.auditorium> list = item.getAuditoriums();
                for (int i = 0 ; i < list.size() ; i++){
                    OrderMealBean.auditorium a = list.get(i);
                    if (i==0){
                        text += (a.getStreet()+ "-" + a.getAuditorium()+"\n");
                    }else {
                        text += ("\u3000\u3000  "+a.getStreet()+ "-" + a.getAuditorium()+"\n");
                    }

                }
                if (text.length()>4){
                    text = text.substring(0,text.length()-1);
                }
            }else {
                text += "暂无";
            }
            helper.setText(R.id.tv_is_checked,text);
        }
    }

    private void showLoading() {
        swLayout.post(() -> swLayout.setRefreshing(true));
    }

    private void hideLoading() {
        swLayout.post(() -> swLayout.setRefreshing(false));
    }
}
