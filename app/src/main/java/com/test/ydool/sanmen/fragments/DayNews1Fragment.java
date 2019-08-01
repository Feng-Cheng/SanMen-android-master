package com.test.ydool.sanmen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.ShowWebViewActivity;
import com.test.ydool.sanmen.adapter.DayNewsAdapter;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.DayNewsBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.DividerItemDecoration;
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
 * Created by Administrator on 2018/7/16.
 */

public class DayNews1Fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener
                        ,BaseQuickAdapter.OnItemClickListener,FragmentLifecycle{
    private static final String TAG = "DayNews1Fragment";
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlVedio;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private List<DayNewsBean> listNews = new ArrayList<>();

    private DayNewsAdapter adapter;

    private int pageIndex = 0;

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;

    public static DayNews1Fragment newInstance() {
        return new DayNews1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vedios, container, false);
        ButterKnife.bind(this, view);

        init();
        initViews();
        isCreateView = true;

        return view;
    }
    private void init(){
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if (!isLoadData) {
            //加载数据操作
            loadData();
            isLoadData = true;
        }
    }

    private void initViews(){
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        rlVedio.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DayNewsAdapter(R.layout.item_news,listNews);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, rlVedio);
//        adapter.disableLoadMoreIfNotFullPage();
//        adapter.setEnableLoadMore(true);
        rlVedio.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        rlVedio.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this::onItemClick);
        lazyLoad();
    }

    private void loadData() {
        showLoading();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        adapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        getEverydayActivtes(pageIndex,TerminalInfo.PAGE_SIZE);
    }

    private void getEverydayActivtes(int start, int length) {
        Subscription subscription = userRepository.getEverydayActivtesTest(start,length)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dayNewsBeans ->{
                    hideLoading();
                    loadDayNews(dayNewsBeans);
                },throwable -> {
                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                    hideLoading();
                });
        subscriptions.add(subscription);
    }

    private void loadDayNews(List<DayNewsBean> data) {
        if (data != null && data.size() > 0){
            if (pageIndex != 0){//上拉加载
                for (DayNewsBean a : data)
                    listNews.add(a);
//                adapter.addData(data);
            }else {//下拉刷新
                listNews = data;
                adapter.setNewData(data);
            }
            pageIndex += data.size();
            if (data.size()<TerminalInfo.PAGE_SIZE){
                adapter.loadMoreEnd();
//                adapter.setEnableLoadMore(false);
            }else {
                adapter.loadMoreComplete();
            }
        }else {
            adapter.loadMoreEnd();
            adapter.setEnableLoadMore(false);
        }
    }

    private void showLoading(){
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
    }

    private void hideLoading(){
        refreshLayout.post(() -> refreshLayout.setRefreshing(false));
    }

    @Override
    public void onLoadMoreRequested() {
        adapter.setEnableLoadMore(true);
        getEverydayActivtes(pageIndex,TerminalInfo.PAGE_SIZE);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), ShowWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", TerminalInfo.BASE_URL+"/img/img?id="+listNews.get(position).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onFragmentPause() {

    }

    @Override
    public void onFragmentResume() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityPause() {

    }

    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityDestroy() {

    }
}
