package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.ActivityAnnoceAdapter;
import com.test.ydool.sanmen.adapter.SignAdapater;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityExhibitionBean;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.DaySignBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.utils.utile;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/19.
 * <p>
 * 活动展示页面
 */

public class AcitvityShowMineActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.rl_activtity)
    RecyclerView rlActivity;
    @BindView(R.id.btn_activity_cun_accpter)
    Button btnAccpter;
    @BindView(R.id.btn_activity_cun_no_accpter)
    Button btnNoAccpter;


    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private ActivityShowAdapter mAdapter;

    private List<ActivityExhibitionBean> list;

    private List<ActivityExhibitionBean> list1;

    private List<ActivityExhibitionBean> list2;


    private static SPUtils spUtils;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mAdapter = new ActivityShowAdapter(R.layout.item_cun_activity, list);
                rlActivity.setLayoutManager(new LinearLayoutManager(mContext));
                rlActivity.setAdapter(mAdapter);
                mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent intent = new Intent(mContext, ActivityMineVillageShenActivity.class);
                        ActivityExhibitionBean bean = list.get(position);
                        intent.putExtra("bean", bean);
                        startActivity(intent);
                    }
                });
            }
            if (msg.what == 1) {
                mAdapter.setNewData(list);
//                rlActivity.setAdapter(mAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_show_mine);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void initView() {
        tvTitle.setText(R.string.activty_show);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvAll.setText(R.string.put);
        tvAll.setTextColor(0xfffaa8ae);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        Set<String> resList = spUtils.getStringSet("resList");
        if (resList.contains("/auditoriumlnfo/auditorium/town")) {
            btnNoAccpter.setText("待审核");
        }


        btnAccpter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (list != list1){
                    list = list1;
                    Message message = new Message();
                    message.what = 1;
                    mHander.sendMessage(message);
                }*/
                update();
            }
        });

        btnNoAccpter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils spUtils = SPUtils.getInstance("sanMen");
/*                if (list2.size()>0){
                    if (list != list2){
                        list = list2;
                        Message message = new Message();
                        message.what = 1;
                        mHander.sendMessage(message);
                    }
                }else {*/
                Subscription subscription = userRepository.getExhibitionNoAccepterList(spUtils.getString("token"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            list2 = new ArrayList<>();
                            if (response != null) {
                                list2 = response;
                                list = list2;
                                btnAccpter.setBackgroundResource(R.drawable.button_selector);
                                btnNoAccpter.setBackgroundResource(R.drawable.button_selector_choose);
                                Message message = new Message();
                                message.what = 0;
                                mHander.sendMessage(message);
                            }
                        }, throwable -> {
                            Log.i("Error-->", throwable.toString());
                            ToastUtil.showLongMessage(mContext, "网络异常！");
                        });
                subscriptions.add(subscription);
//                }
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityMineNormalReportActivity.class);
                startActivity(intent);
            }
        });

    }

    class ActivityShowAdapter extends BaseQuickAdapter<ActivityExhibitionBean, BaseViewHolder> {

        public ActivityShowAdapter(int layoutResId, @Nullable List<ActivityExhibitionBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ActivityExhibitionBean item) {
            helper.setText(R.id.tv_activity_title, item.getActivity_theme());
            helper.setText(R.id.tv_activity_put_name, item.getTime());
            helper.setText(R.id.tv_activity_time, item.getAddr());
            helper.setText(R.id.tv_activity_content, "\u3000\u3000" + item.getContent());
            if (item.getStatus() != null) {
                if (item.getStatus().equals("1")) {
                    helper.setText(R.id.tv_shen_state, "待审核");
                    helper.setTextColor(R.id.tv_shen_state, Color.GREEN);
                    helper.setVisible(R.id.tv_shen_state, true);
                } else if (item.getStatus().equals("2")) {
                    helper.setText(R.id.tv_shen_state, "不通过");
                    helper.setTextColor(R.id.tv_shen_state, Color.RED);
                    helper.setVisible(R.id.tv_shen_state, true);
                }
            }
            helper.addOnClickListener(R.id.tv_checkmore);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void update() {
        Subscription subscription = userRepository.getActivityListDataNotAudit(spUtils.getString("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = new ArrayList<>();
                    list1 = new ArrayList<>();
                    list2 = new ArrayList<>();
                    if (response != null) {
                        list = response;
                        for (ActivityExhibitionBean a : list) {
                            if (a.getStatus().equals("0")) {
                                list1.add(a);
                            }
                            Log.e("Data=====", a.getContent());
                        }

                        list1 = list;
                        btnAccpter.setBackgroundResource(R.drawable.button_selector_choose);
                        btnNoAccpter.setBackgroundResource(R.drawable.button_selector);
                        Message message = new Message();
                        message.what = 0;
                        mHander.sendMessage(message);
                    }
                }, throwable -> {
                    Log.i("Error-->", throwable.toString());
                });
        subscriptions.add(subscription);
    }

}
