package com.test.ydool.sanmen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.ShowWebViewActivity;
import com.test.ydool.sanmen.adapter.SignAdapater;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.DaySignBean;
import com.test.ydool.sanmen.bean.HomePhotoBean;
import com.test.ydool.sanmen.net.repository.UserRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/25.
 */

public class ToadyHotNewsFragment extends BaseFragment{
    @BindView(R.id.tv_tile1)
    TextView tvTitle1;
    @BindView(R.id.tv_hot_from1)
    TextView tvHotFrom1;
    @BindView(R.id.tv_hot_time1)
    TextView tvHotTime1;
     @BindView(R.id.tv_tile2)
    TextView tvTitle2;
    @BindView(R.id.tv_hot_from2)
    TextView tvHotFrom2;
    @BindView(R.id.tv_hot_time2)
    TextView tvHotTime2;
     @BindView(R.id.tv_tile3)
    TextView tvTitle3;
    @BindView(R.id.tv_hot_from3)
    TextView tvHotFrom3;
    @BindView(R.id.tv_hot_time3)
    TextView tvHotTime3;
     @BindView(R.id.tv_tile4)
    TextView tvTitle4;
    @BindView(R.id.tv_hot_from4)

    TextView tvHotFrom4;
    @BindView(R.id.tv_hot_time4)
    TextView tvHotTime4;
     @BindView(R.id.tv_tile5)
    TextView tvTitle5;
    @BindView(R.id.tv_hot_from5)
    TextView tvHotFrom5;
    @BindView(R.id.tv_hot_time5)
    TextView tvHotTime5;
    @BindView(R.id.rl_new1)
    RelativeLayout rlNew1;
    @BindView(R.id.rl_new2)
    RelativeLayout rlNew2;
    @BindView(R.id.rl_new3)
    RelativeLayout rlNew3;
    @BindView(R.id.rl_new4)
    RelativeLayout rlNew4;
    @BindView(R.id.rl_new5)
    RelativeLayout rlNew5;
    @BindView(R.id.tv_where)
    TextView tvWhere;
    @BindView(R.id.banner)
    ImageView ivShow;
    @BindView(R.id.tv_theme)
    TextView tvTheme;


    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private SPUtils spUtils;
    // 频道id
    private int channelId;
    protected boolean isCreate = false;

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;


    public static ToadyHotNewsFragment newInstance(int position) {
        ToadyHotNewsFragment fragment = new ToadyHotNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TerminalInfo.CHANNEL_ID, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_today_hot, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");
        channelId = getArguments().getInt(TerminalInfo.CHANNEL_ID);

        tvWhere.setText((channelId+1)+"/"+TerminalInfo.scrollNum);
        initFragment();
        isCreate = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser &&isCreate){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }).start();
            tvWhere.setText((channelId+1)+"/"+TerminalInfo.scrollNum);
        }
//        if (isVisibleToUser && isCreateView) {
//            lazyLoad();
//            tvWhere.setText((channelId+1)+"/"+TerminalInfo.scrollNum);
//        }
    }

    private void initFragment(){
        tvTitle1.setText(TerminalInfo.newsList.get(channelId*5).getTitle());
        tvHotTime1.setText(TerminalInfo.newsList.get(channelId*5).getTime());
        tvHotFrom1.setText(TerminalInfo.newsList.get(channelId*5).getFrom());
        rlNew1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.newsList.get(channelId*5).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvTitle2.setText(TerminalInfo.newsList.get(channelId*5+1).getTitle());
        tvHotTime2.setText(TerminalInfo.newsList.get(channelId*5+1).getTime());
        tvHotFrom2.setText(TerminalInfo.newsList.get(channelId*5+1).getFrom());
        rlNew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.newsList.get(channelId*5+1).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvTitle3.setText(TerminalInfo.newsList.get(channelId*5+2).getTitle());
        tvHotTime3.setText(TerminalInfo.newsList.get(channelId*5+2).getTime());
        tvHotFrom3.setText(TerminalInfo.newsList.get(channelId*5+2).getFrom());
        rlNew3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.newsList.get(channelId*5+2).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvTitle4.setText(TerminalInfo.newsList.get(channelId*5+3).getTitle());
        tvHotTime4.setText(TerminalInfo.newsList.get(channelId*5+3).getTime());
        tvHotFrom4.setText(TerminalInfo.newsList.get(channelId*5+3).getFrom());
        rlNew4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.newsList.get(channelId*5+3).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvTitle5.setText(TerminalInfo.newsList.get(channelId*5+4).getTitle());
        tvHotTime5.setText(TerminalInfo.newsList.get(channelId*5+4).getTime());
        tvHotFrom5.setText(TerminalInfo.newsList.get(channelId*5+4).getFrom());
        rlNew5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.newsList.get(channelId*5+4).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if (TerminalInfo.PhotoNum<TerminalInfo.HomePhotoBean.size()){
            initPhoto();
            TerminalInfo.PhotoNum++;
        }else {
            TerminalInfo.PhotoNum=0;
            initPhoto();
        }
    }

    private void lazyLoad(){
        //如果没有加载过就加载，否则就不再加载了
        if (!isLoadData) {
            //加载数据操作
//            loadData();
            initFragment();
            isLoadData = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
//        if (getUserVisibleHint()) {
//            lazyLoad();
//        }
    }


    private void initPhoto(){
        if (TerminalInfo.HomePhotoBean!=null && TerminalInfo.HomePhotoBean.size()>0){
            HomePhotoBean homePhotoBean = TerminalInfo.HomePhotoBean.get(TerminalInfo.PhotoNum);
            Glide.with(getActivity())
                    .load(TerminalInfo.BASE_URL+homePhotoBean.getUrl())
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.error)
                    .into(ivShow);
            tvTheme.setText(homePhotoBean.getThem());
            ivShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowWebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", TerminalInfo.BASE_URL+"/img/img?id="+homePhotoBean.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}

