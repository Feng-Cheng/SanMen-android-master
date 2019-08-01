package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ShareBean;
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
 * Created by Administrator on 2018/6/19.
 *
 * 资源共享
 */

public class HomeShareMainActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.rl_share_main)
    RecyclerView rlShareMain;


    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;


    private ShareMianAdapter mAdapter;

    private List<ShareBean> list;

    private String page ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_home_share_main);
        ButterKnife.bind(this);

        initView();
    }



    private void initView() {
        tvTitle.setText("资源共享");
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        Bundle bundle = getIntent().getExtras();
        page = bundle.getString("page");

        if (page.equals("mine")){
            tvAll.setText(R.string.put);
            tvAll.setTextColor(0xfffaa8ae);
            tvAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                }
            });
        }

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.getInstance().popActivity((Activity) mContext);
            }
        });

        Subscription subscription = userRepository.getHomeShareList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    list = new ArrayList<>();
                    if (response!=null){
                        list = response;
                        mAdapter = new ShareMianAdapter(R.layout.item_home_share,list);
                        rlShareMain.setLayoutManager(new LinearLayoutManager(mContext));
                        rlShareMain.setAdapter(mAdapter);

                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(mContext,HomeShareHuifuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("organizer",list.get(position).getOrganizer());
                                bundle.putString("id",list.get(position).getId());
                                bundle.putString("detail",list.get(position).getDetail());
                                bundle.putString("title",list.get(position).getTitle());
                                bundle.putString("time",list.get(position).getTime());

                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常");
                });

        subscriptions.add(subscription);
    }

    class ShareMianAdapter extends BaseQuickAdapter<ShareBean,BaseViewHolder>{

        public ShareMianAdapter(int layoutResId, @Nullable List<ShareBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShareBean item) {
            helper.setText(R.id.tv_home_share_theme,item.getTitle());
            helper.setText(R.id.tv_home_share_content, utile.delHTMLTag(item.getDetail()));
            helper.setText(R.id.tv_home_share_time,item.getTime());
            helper.addOnClickListener(R.id.ll_all_point);
        }
    }

}
