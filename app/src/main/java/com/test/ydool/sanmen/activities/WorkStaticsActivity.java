package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.GridViewAdapter;
import com.test.ydool.sanmen.adapter.WorkStatisticsAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.VillageButtonBean;
import com.test.ydool.sanmen.bean.WorkStatisticsBean;
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
 * Created by Administrator on 2018/6/8.
 *
 * 工作统计页面
 */
public class WorkStaticsActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_work_statistics)
    RecyclerView rlWorkStatiscs;
    @BindView(R.id.tv_work_statics_village)
    TextView tvCheckVillAge;
    @BindView(R.id.tv_work_statics_check_type)
    TextView tvCheckType;
    @BindView(R.id.btn_work_statics_search)
    Button btnSearch;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private List<WorkStatisticsBean> list;

    private List<PositionRootBean.row> rows;

    private SPUtils spUtils;

    private WorkStatisticsAdapter mAdapter;

    private int genre = 0;

    private int village = 0;

    private String area="";

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_work_statistics);
        ButterKnife.bind(this);

        initView();
    }

    private Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 ){
                mAdapter = new WorkStatisticsAdapter(R.layout.item_work_statistics,list);
                rlWorkStatiscs.setLayoutManager(new LinearLayoutManager(mContext));
                rlWorkStatiscs.setAdapter(mAdapter);
            }else {
                if (genre == 1){
                    for (int i = 0 ; i<list.size() ; i++)
                    {
                        list.get(i).setJobstatus(-1);
                        list.get(i).setJobname("/");
                    }
                }
                if (genre == 2){
                    for (int i = 0 ; i<list.size() ; i++){
                        list.get(i).setStatus(-1);
                        list.get(i).setSignName("/");
                    }
                }
                mAdapter.setNewData(list);
                rlWorkStatiscs.setAdapter(mAdapter);
            }

        }
    };

    private void initView(){
        tvTitle.setText(R.string.work_statitstics);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        spUtils = SPUtils.getInstance("sanMen");
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        String token = spUtils.getString("token");
        String poid = spUtils.getString("position_id");


        if (spUtils.getInt("quanxian")==2){
            tvCheckVillAge.setText(spUtils.getString("pname"));
        }else {
            tvCheckVillAge.setText(R.string.all);
            tvCheckVillAge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subscription subscription = null;
                    subscription = userRepository.getPositionRoot(poid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response ->{
//                            System.out.println(response);
//                            addLinearLayout(response.getRows().size());
                                List<VillageButtonBean> list = new ArrayList<>();
                                List<PositionRootBean.row> jsonArray = response.getRows();
                                String[] words2 = new String[jsonArray.size()+1];

                                words2[0] = "全部";
                                rows = jsonArray;
                                for (int i = 0 ; i < jsonArray.size() ; i++){
                                    words2[i+1] = jsonArray.get(i).getPosition_name();
                                }
                                DialogUIUtils.showSingleChoose((Activity) mContext, "类型", village, words2, new DialogUIItemListener() {
                                    @Override
                                    public void onItemClick(CharSequence text, int position) {
                                        village=position;
                                        if (position!=0){
                                            area = rows.get(village-1).getId();
                                        }else {
                                            area = "";
                                        }
                                        tvCheckVillAge.setText(text);
                                    }
                                }).show();
                            },throwable -> {
                                throwable.printStackTrace();
                                ToastUtil.showLongMessage(mContext,"网络异常！");
                            });

                    subscriptions.add(subscription);
                }
            });
        }
        tvCheckType.setText(R.string.all);

        list = new ArrayList<>();
        System.out.println("pid"+poid);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateData(token,genre,area);



        tvCheckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] words2 = new String[]{"全部","考勤记录", "日常工作"};
                DialogUIUtils.showSingleChoose((Activity) mContext, "类型", genre, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        genre=position;
                        tvCheckType.setText(text);
                    }
                }).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String village_name = rows.get(village-1).getPosition_name();
                SPUtils spUtils = SPUtils.getInstance("sanMen");
                updateData(spUtils.getString("token"),genre,area);
            }
        });

    }

    private void updateData(String token,int genre,String area){
        Subscription subscription = userRepository.getWorkStatisticsListData(token,genre,area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!isLoading){
                        list = response.getData();
                        isLoading = true;
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }else {
                        list = response.getData();
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });

        subscriptions.add(subscription);
    }
}
