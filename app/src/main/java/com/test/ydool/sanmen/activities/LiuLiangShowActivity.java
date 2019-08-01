package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.FlowStatisticsBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/7.
 *
 *
 * 流量统计页面
 */

public class LiuLiangShowActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_user_num_title)
    TextView tvUserTitle;
    @BindView(R.id.tv_login_title)
    TextView tvLoginTitle;
    @BindView(R.id.tv_user_num)
    TextView tvUserNum;
    @BindView(R.id.tv_login_statistics)
    TextView tvLogin;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private SPUtils spUtils;

    private List<FlowStatisticsBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_liuliang_show);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        tvTitle.setText(R.string.raffic_statistics);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);


        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");

        Subscription subscription = userRepository.getFlowStatisticsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    list = response.getData();
                    tvLoginTitle.setText(list.get(0).getRname());
                    tvLogin.setText(list.get(0).getLoginTime());
                    tvUserTitle.setText(list.get(1).getRname());
                    tvUserNum.setText(list.get(1).getLoginTime());
                },throwable -> {

                });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
