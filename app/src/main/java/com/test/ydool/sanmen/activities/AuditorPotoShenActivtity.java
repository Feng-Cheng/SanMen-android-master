package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.AuditingPhotoAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AuditingPhotoBean;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.bean.HttpResponseBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/11.
 *
 *
 * 礼堂风采照片审核
 */

public class AuditorPotoShenActivtity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_photo_main)
    RecyclerView rlPhotoMain;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;

    private AuditingPhotoAdapter mAdapter;

    private BuildBean loading;

    private List<AuditingPhotoBean> list1;

    SPUtils spUtils = SPUtils.getInstance("sanMen");

    String token = spUtils.getString("token");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_photo_shen);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void initView() {
        tvTitle.setText("礼堂信息");
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

    }


    private void update(){
        loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
        loading.show();
        Subscription subscription = null;
        subscription = userRepository.getAuditoriumShenInfoList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    list1 = new ArrayList<>();
                    List<AuditoriumBean> list = response.getData();
                    for (int i = 0 ;i < list.size() ; i++){
                        AuditingPhotoBean bean = new AuditingPhotoBean();
                        bean.setId(list.get(i).getId());
                        bean.setName(list.get(i).getName());
                        bean.setAdminName(list.get(i).getAdminName());
                        bean.setState(list.get(i).getStatus());
                        bean.setImgList(list.get(i).getImgList());
                        list1.add(bean);
                    }
                    mAdapter = new AuditingPhotoAdapter(R.layout.item_auditing_photo,list1);
                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(mContext,AuditorPhotoShenShowActivity.class);
                            intent.putExtra("imgList",(Serializable)list1.get(position).getImgList());
                            intent.putExtra("id",list1.get(position).getId());
                            startActivity(intent);
                        }
                    });
                    rlPhotoMain.setLayoutManager(new LinearLayoutManager(mContext));
                    rlPhotoMain.setAdapter(mAdapter);
                    DialogUIUtils.dismiss(loading);
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                    DialogUIUtils.dismiss(loading);
                });

        subscriptions.add(subscription);
    }
}
