package com.test.ydool.sanmen.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.ShowWebViewActivity;
import com.test.ydool.sanmen.adapter.DayNewsAdapter;
import com.test.ydool.sanmen.adapter.VedioAdapter;
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

public class DayNewsFragment extends BaseFragment {
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlVedio;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private List<DayNewsBean> listNews;

    private DayNewsAdapter adapter;

    private int start = 0;

    public static DayNewsFragment newInstance() {
        return new DayNewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vedios, container, false);
        ButterKnife.bind(this, view);

        initDatas();
        return view;
    }

    private void initDatas() {
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        Subscription subscription = null;
        subscription = userRepository.getEverydayActivtes(start,TerminalInfo.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    listNews=new ArrayList<>();
                    if (response!=null){
                        listNews = response;
                        adapter = new DayNewsAdapter(R.layout.item_news,listNews);
                        rlVedio.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rlVedio.setAdapter(adapter);
                        rlVedio.addItemDecoration(new DividerItemDecoration(getActivity(),
                                DividerItemDecoration.VERTICAL_LIST));
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getActivity(), ShowWebViewActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("url", TerminalInfo.BASE_URL+"/img/img?id="+listNews.get(position).getId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }else {
                        ToastUtil.showLongMessage(getActivity(),"");
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
