package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.ScoreAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ScoreBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
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
 * Created by Administrator on 2019/1/9.
 *
 * 分数页面
 */

public class ScoreRankActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_score)
    RecyclerView rlScore;


    private List<ScoreBean> scoreList;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private ScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_score_rank);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.score_rank);
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

        scoreList = new ArrayList<>();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        Subscription subscription = userRepository.getScoreOrder(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response!=null){
                        scoreList = response;
                        for (int i=0;i<scoreList.size();i++){
                            scoreList.get(i).setNumber(i+1);
                        }
                        mAdapter = new ScoreAdapter(R.layout.item_score_rank,scoreList);
                        rlScore.setLayoutManager(new LinearLayoutManager(mContext));
                        rlScore.setAdapter(mAdapter);
                    }
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常");
                });
        subscriptions.add(subscription);

    }
}
