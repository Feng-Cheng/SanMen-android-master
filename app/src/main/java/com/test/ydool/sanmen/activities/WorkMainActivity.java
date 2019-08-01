package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.WorkBaseShowAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.WorkBaseBean;
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
 * Created by Administrator on 2018/5/29.
 *
 * 工作小结列表页面
 */

public class WorkMainActivity extends BaseActivity{


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_all)
    TextView tvTitleAll;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rv_work_main)
    RecyclerView rvWorkMain;


    private WorkBaseShowAdapter mAdapter;

    private List<WorkBaseBean> mList;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter = new WorkBaseShowAdapter(R.layout.item_base_show,mList);
            rvWorkMain.setLayoutManager(new LinearLayoutManager(getApplication()));
            rvWorkMain.setAdapter(mAdapter);
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(mContext,WorkOnewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("auditorName",mList.get(position).getAuditorium());
                    bundle.putString("content",mList.get(position).getSummary());
                    bundle.putString("name",mList.get(position).getName());
                    bundle.putString("time",mList.get(position).getTime());
                    bundle.putString("issue",mList.get(position).getIssue());
                    bundle.putString("solution",mList.get(position).getSolution());
                    bundle.putString("activity_describe",mList.get(position).getActivity_describe());
                    bundle.putString("remark",mList.get(position).getRemark());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_work_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.work_report);
        tvTitleAll.setText(R.string.put);
        tvTitle.setTextColor(0xfffaa8ae);
        tvTitleAll.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");

        mList = new ArrayList<>();
        Subscription subscription = userRepository.getWorkListData(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mList = response.getData();
                    Message message = new Message();
                    mHandler.sendMessage(message);
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);

        System.out.println(mList.toString());


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WorkReportActivity.class);
                startActivity(intent);
            }
        });

    }

}



