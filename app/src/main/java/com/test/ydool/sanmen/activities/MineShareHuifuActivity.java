package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.ShareHuifuAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ShareHuifuBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.utils.utile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/27.
 *
 * 资源回复
 */

public class MineShareHuifuActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_home_share_theme)
    TextView tvTheme;
    @BindView(R.id.tv_home_share_time)
    TextView tvTime;
    @BindView(R.id.tv_home_share_content)
    TextView tvContent;
    @BindView(R.id.tv_home_share_organizer)
    TextView tvOrganizer;
    @BindView(R.id.rl_share_main)
    RecyclerView rlShareMain;


    private CompositeSubscription subscriptions;

    private UserRepository userRepository;


    private ShareHuifuAdapter mAdapter;

    private List<ShareHuifuBean> list;


    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_mine_share_huifu);
        ButterKnife.bind(this);

        initView();
    }



    private void initView() {
        tvTitle.setText("资源共享");
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.getInstance().popActivity((Activity) mContext);
            }
        });

        Bundle bundle = getIntent().getExtras();
        tvTheme.setText(bundle.getString("title"));
        tvOrganizer.setText(bundle.getString("organizer"));
        tvContent.setText(utile.delHTMLTag(bundle.getString("detail")));
        tvTime.setText(bundle.getString("time"));
        id = bundle.getString("id");

        System.out.println("id=========>"+id);

        Subscription subscription = userRepository.getHomeReplyShareList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = new ArrayList<>();
                    if (response!=null){
                        list = response;
                        mAdapter = new ShareHuifuAdapter(R.layout.item_share_huifu,list);
                        rlShareMain.setLayoutManager(new LinearLayoutManager(mContext));
                        rlShareMain.setAdapter(mAdapter);
                    }
                },throwable ->{
                    ToastUtil.showLongMessage(mContext,"网络异常");
                });
        subscriptions.add(subscription);


    }
}
