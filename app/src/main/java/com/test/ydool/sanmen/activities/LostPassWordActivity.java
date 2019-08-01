package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by Administrator on 2018/7/2.
 *
 * 忘记密码页面
 */

public class LostPassWordActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.btn_selector_ishow_1)
    ImageView btnSelectirIshow1;
    @BindView(R.id.btn_selector_ishow_2)
    ImageView btnSelectirIshow2;
    @BindView(R.id.tv_send_message)
    TextView tvSendMessage;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pas_agian)
    EditText etPwdAgain;
    @BindView(R.id.et_name)
    EditText etPhone;
    @BindView(R.id.ed_yanzhengma)
    EditText edYanzhengMa;
    @BindView(R.id.bt_login)
    Button btnLost;
    @BindView(R.id.rl_changeShow)
    RelativeLayout rlChangShow;
    @BindView(R.id.rl_changeShow1)
    RelativeLayout rlChangShow1;
    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private int count = 60;
    private static int WHAT_COUNT = 0;
    private boolean isSend = false;
    private String phone = "";
    private String pwd="";
    private String pwdAgain = "";
    private String yanZhengma = "";

    private boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true
    private boolean isHideFirst1 = true;// 输入框密码是否是隐藏的，默认为true

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_COUNT) {
                if (count <= 0) {
                    tvSendMessage.setText("获取验证码");
                    count = 60;
                    isSend = false;
                } else {
                    isSend = true;
                    tvSendMessage.setText("" + count-- + "s后重试");
                    mHandler.sendEmptyMessageDelayed(WHAT_COUNT, 1000);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_lost_password);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("找回密码");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        btnSelectirIshow1.setImageResource(R.drawable.ic_hint);
        btnSelectirIshow2.setImageResource(R.drawable.ic_hint);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        rlChangShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHideFirst){
                    showOrHide(etPwd,btnSelectirIshow1,isHideFirst);
                    isHideFirst = false;
                }else {
                    showOrHide(etPwd,btnSelectirIshow1,isHideFirst);
                    isHideFirst = true;
                }
            }
        });
        rlChangShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHideFirst1){
                    showOrHide(etPwdAgain,btnSelectirIshow2,isHideFirst1);
                    isHideFirst1 = false;
                }else {
                    showOrHide(etPwdAgain,btnSelectirIshow2,isHideFirst1);
                    isHideFirst1 = true;
                }
            }
        });
        tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSend){
                    phone = etPhone.getText().toString();
                    if (!phone.equals("")){
                        if (isMobileNum(phone)){
                            Subscription subscription = userRepository.getPhoneCode(phone,"1")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response ->{
                                        if (response.getCode()==0){
                                            Message message = new Message();
                                            message.what = WHAT_COUNT;
                                            mHandler.sendMessage(message);
                                        }else {
                                            ToastUtil.showMessage(mContext,"发送失败"+response.getMsg());
                                        }
                                    },throwable -> {
                                        ToastUtil.showMessage(mContext,"网络异常!");
                                    });
                            subscriptions.add(subscription);
                        }else {
                            ToastUtil.showMessage(mContext,"请输入正确的手机号！");
                        }
                    }else {
                        ToastUtil.showMessage(mContext,"请输入手机号!");
                    }
                }
            }
        });


        btnLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etPhone.getText().toString();
                pwd = etPwd.getText().toString();
                pwdAgain = etPwdAgain.getText().toString();
                yanZhengma = edYanzhengMa.getText().toString();
                if (!isMobileNum(phone)){
                    ToastUtil.showMessage(mContext,"请输入正确的手机号！");
                    return;
                }
                if(pwd.length()<6){
                    ToastUtil.showLongMessage(mContext,"密码至少6位！请重新输入！");
                    return;
                }
                if(pwd==null||pwdAgain==null||"".equals(pwd)||"".equals(pwdAgain)||!pwd.equals(pwdAgain)){
                    ToastUtil.showLongMessage(mContext,"两次输入的密码不一致或者未输入密码！请重新输入！");
                    return;
                }
                if (yanZhengma.equals("")||yanZhengma.length()<6){
                    ToastUtil.showMessage(mContext,"请输入正确验证码！");
                    return;
                }
                Subscription subscription = userRepository.findPwd(phone,yanZhengma,pwd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if (response.getCode() == 0){
                                ToastUtil.showMessage(mContext,"修改成功！");
                                ScreenManager.getInstance().popActivity((Activity) mContext);
                            }else {
                                ToastUtil.showMessage(mContext,"修改失败！"+response.getMsg());
                            }
                        },throwable -> {
                            ToastUtil.showMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription);
            }
        });

    }


    private boolean isMobileNum(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、177、178、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中
        // 的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 显示或隐藏
     * @param isShow
     */
    private void showOrHide(EditText ed,ImageView iv,boolean isShow){
        //记住光标开始的位置
        int pos = ed.getSelectionStart();
        if(isShow){
            iv.setImageResource(R.drawable.ic_show);
            ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            iv.setImageResource(R.drawable.ic_hint);
            ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        ed.setSelection(pos);
    }
}
