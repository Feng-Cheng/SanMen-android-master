package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.test.ydool.sanmen.utils.ToastUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.Set;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 在线建言审核
 */

public class OnlineAdviceShenActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ed_adm_name)
    TextView pView;
    @BindView(R.id.tv_check_advices_people)
    TextView tcView;
    @BindView(R.id.tv_advice_content)
    TextView taView;
    @BindView(R.id.btn_activity_online_accpter)
    Button btnAccepter;
    @BindView(R.id.btn_activity_online_no_accpter)
    Button btnNoAccepter;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_onlin_advice_shen);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.Online_advice);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        Bundle bundle = getIntent().getExtras();
        SPUtils spUtils=SPUtils.getInstance("sanMen");
        Set<String> resList=spUtils.getStringSet("resList");
        if(!(resList.contains("/state/state/approve")&&bundle.getString("status").equals("0"))) {
            btnAccepter.setVisibility(View.INVISIBLE);
            btnNoAccepter.setVisibility(View.INVISIBLE);
        }else if (bundle.getString("status").equals("0")&&!bundle.getBoolean("flag")){
            btnAccepter.setVisibility(View.INVISIBLE);
            btnNoAccepter.setVisibility(View.INVISIBLE);
        }
        pView.setText(bundle.getString("pname"));
        tcView.setText(bundle.getString("topname"));
        taView.setText(bundle.getString("content"));

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAccepter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Subscription subscription = userRepository.approveOnlineAdvice(spUtils.getString("token"),bundle.getString("id"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0) {
                                ToastUtil.showLongMessage(mContext, "提交成功！通过审核！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext, "提交失败！"+response.getMsg());
                            }
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription);
            }
        });
        btnNoAccepter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Subscription subscription = userRepository.rejectOnlineAdvice(spUtils.getString("token"),bundle.getString("id"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0) {
                                ToastUtil.showLongMessage(mContext, "提交成功！驳回审核！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext, "提交失败！"+response.getMsg());
                            }
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription);
            }
        });
    }
}
