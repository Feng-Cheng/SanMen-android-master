package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.SignAdapater;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.DaySignBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.test.ydool.sanmen.utils.ToastUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 修改密码
 */

public class ChangePeopleActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_change_people_name)
    TextView tvUser;
    @BindView(R.id.et_change_people_new_password)
    EditText etNewPasswork;
    @BindView(R.id.et_change_people_pas_again)
    EditText etPasAgain;
//    @BindView(R.id.et_change_people_danwei)
//    EditText etDanwei;
    @BindView(R.id.bt_cahnge_people_submission)
    Button btnSubMission;
    @BindView(R.id.rv_change_people_live_xiangz)
    TextView tvPname;
    @BindView(R.id.rv_change_people_real_name)
    TextView rvRealName;


    private UserRepository userRepository;

    private CompositeSubscription subscriptions;


    SPUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_change_people);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.mine);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");
        String token=spUtils.getString("token");
        tvPname.setText(spUtils.getString("pname"));
        tvUser.setText(spUtils.getString("name"));
        rvRealName.setText(spUtils.getString("Adm_name"));

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPwd=etNewPasswork.getText().toString();
                String pasAgn=etPasAgain.getText().toString();
                if(newPwd.length()<6){
                    ToastUtil.showLongMessage(mContext,"密码至少6位！请重新输入！");
                    return;
                }
                if(newPwd==null||pasAgn==null||"".equals(newPwd)||"".equals(pasAgn)||!newPwd.equals(pasAgn)){
                    ToastUtil.showLongMessage(mContext,"两次输入的密码不一致或者未输入密码！请重新输入！");
                    return;
                }
                Subscription subscription = userRepository.savePwdUpdate(token,pasAgn)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0){
                                ToastUtil.showLongMessage(mContext,"提交成功！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext,response.getMsg());
                            }
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
            }
        });
    }

}
