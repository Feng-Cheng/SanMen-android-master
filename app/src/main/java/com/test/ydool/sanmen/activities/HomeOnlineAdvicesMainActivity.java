package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.HomeOnlineAdapter;
import com.test.ydool.sanmen.adapter.OnlineAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.*;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.test.ydool.sanmen.utils.ToastUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 在线建言
 */

public class HomeOnlineAdvicesMainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.rl_home_online)
    RecyclerView rlOnline;
    @BindView(R.id.tv_work_statics_village)
    TextView tvViView;
    @BindView(R.id.tv_work_statics_check_type)
    TextView tvCtView;
    @BindView(R.id.btn_work_statics_search)
    Button btnSearch;

    HomeOnlineAdapter homeOnlineAdapter;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;

    private BuildBean loading;

    private List list;

    private List<PositionRootBean.row> jsonArray;
    private List<PositionRootBean.row> jsonArray2;

    private Integer town=-1;

    private Integer village=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_home_online_advices_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.Online_advice);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        tvAll.setText("建言");
        tvAll.setTextColor(0xfffaa8ae);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        update();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.getInstance().popActivity((Activity) mContext);
            }
        });
        //tvViView.setText("请选择乡镇");
        //设置乡镇列表
        tvViView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
                loading.show();
                Subscription subscription = null;
                subscription = userRepository.getPositionRoot()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            //List<VillageButtonBean> list = new ArrayList<>();
                            jsonArray = response.getRows();
                            String[] words = new String[jsonArray.size()+1];

                            words[0] = "县级";
                            for (int i = 0 ; i < jsonArray.size() ; i++){
                                words[i+1] = jsonArray.get(i).getPosition_name();
                            }
                            DialogUIUtils.showSingleChoose((Activity) mContext, "乡，街道", 0, words, new DialogUIItemListener() {
                                @Override
                                public void onItemClick(CharSequence text, int position) {
                                    town =position-1;
                                    if(town==-1) {
                                        tvCtView.setOnClickListener(null);
                                        return;
                                    }
                                    village=-1;
                                    tvViView.setText(text);

                                    //设置村列表
                                    tvCtView.setText("");
                                    tvCtView.setOnClickListener(new View.OnClickListener(){

                                        @Override
                                        public void onClick(View v) {
                                            Subscription subscription = null;
                                            subscription = userRepository.getPositionRoot(jsonArray.get(town).getId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(response ->{
                                                        //List<VillageButtonBean> list = new ArrayList<>();
                                                        jsonArray2 = response.getRows();
                                                        String[] words2 = new String[jsonArray2.size()+1];

                                                        words2[0] = "全部";
                                                        for (int i = 0 ; i < jsonArray2.size() ; i++){
                                                            words2[i+1] = jsonArray2.get(i).getPosition_name();
                                                        }
                                                        DialogUIUtils.showSingleChoose((Activity) mContext, "村", 0, words2, new DialogUIItemListener() {
                                                            @Override
                                                            public void onItemClick(CharSequence text, int position) {
                                                                village =position-1;
                                                                tvCtView.setText(text);
                                                            }
                                                        }).show();
                                                    },throwable -> {
                                                        throwable.printStackTrace();
                                                        ToastUtil.showLongMessage(mContext,throwable.toString());
                                                    });

                                            subscriptions.add(subscription);
                                        }
                                    });
                                }
                            }).show();
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });

                subscriptions.add(subscription);
            }
        });
        //搜索按钮事件
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                update();
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OnlineAdviceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void update(){
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String townId=null;
        String villageId=null;
        if(jsonArray!=null&&town!=-1){
            townId=jsonArray.get(town).getId();
        }
        if(jsonArray2!=null&&village!=-1){
            villageId=jsonArray2.get(village).getId();
        }
        loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
        loading.show();
        Subscription subscription = userRepository.getOnlineAdviceList(spUtils.getString("token")
                ,townId,villageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response != null){
                        List<OnlineAdviceBean> list2 = response.getData();
                        list = new ArrayList<HomeEntity>();
                        int index=0;
                        while(index<list2.size()){
                            if (list2.get(index).getStatus().equals("1")){
                                HomeEntity he=new HomeEntity(HomeEntity.TYPE_TITLE);
                                HomeOnlineBean homeOnlineBean=new HomeOnlineBean(list2.get(index).getStatePosition());
                                he.setHomeOnlineBean(homeOnlineBean);
                                list.add(he);
                                int index2=0;
                                List<Integer> ids=new ArrayList<>();
                                while(index2<list2.size()){
                                    if(list2.get(index2).getStatePosition().equals(list2.get(index).getStatePosition()) && list2.get(index2).getStatus().equals("1")) {
                                        HomeEntity he2 = new HomeEntity(HomeEntity.TYPE_LIST);
                                        he2.setOnlineAdviceBean(list2.get(index2));
                                        list.add(he2);
                                        ids.add(index2);
                                    }
                                    index2++;
                                }
                                for(int i=ids.size()-1;i>0;i--){
                                    list2.remove(ids.get(i).intValue());
                                    if(i==index){
                                        index--;
                                    }
                                }
                            }
                            index++;
                        }
                        homeOnlineAdapter = new HomeOnlineAdapter(list);
                        homeOnlineAdapter.setNewData(list);
                        rlOnline.setLayoutManager(new LinearLayoutManager(mContext));
                        rlOnline.setAdapter(homeOnlineAdapter);
                    }
                    DialogUIUtils.dismiss(loading);
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                    DialogUIUtils.dismiss(loading);
                });

        subscriptions.add(subscription);
    }

}
