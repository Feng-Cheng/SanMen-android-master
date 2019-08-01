package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AdminBean;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.VillageButtonBean;
import com.test.ydool.sanmen.cmmmmm.Communal;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/22.
 *
 *
 * 注册页面
 */

public class RegisteActivity extends BaseActivity{
    private static final String TAG = "RegisteActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.et_name)
    EditText etPhone;
    @BindView(R.id.et_real_name)
    EditText etRealName;
    @BindView(R.id.tv_xiangzheng_check)
    TextView tvXiangZheng;
    @BindView(R.id.tv_cun_check)
    TextView tvCun;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pas_agian)
    EditText etPwdAgain;
    @BindView(R.id.bt_registe)
    Button btRegiste;
    @BindView(R.id.tv_send_message)
    TextView tvSendMessage;
    @BindView(R.id.ed_yanzhengma)
    EditText edYanzhengMa;
    @BindView(R.id.btn_selector_ishow_1)
    ImageView btnSelectirIshow1;
    @BindView(R.id.btn_selector_ishow_2)
    ImageView btnSelectirIshow2;
    @BindView(R.id.rl_changeShow)
    RelativeLayout rlChangShow;
    @BindView(R.id.rl_changeShow1)
    RelativeLayout rlChangShow1;

    private CompositeSubscription subscriptions;
    private UserRepository userRepository;
    private BuildBean loading;

    private String phone="";
    private String realName="";
    private String xiangZheng="";
    private String cun="";
    private String pwd="";
    private String pwdAgain="";
    private String yanZhengma="";

    private List<PositionRootBean.row> jsonArray;
    private List<PositionRootBean.row> jsonArray2;

    private Integer town=-1;

    private Integer village=-1;
    private boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true
    private boolean isHideFirst1 = true;// 输入框密码是否是隐藏的，默认为true

    private int count = 60;
    private static int WHAT_COUNT = 0;
    private boolean isSend = false;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_registe_user);
        ButterKnife.bind(this);
        mContext = this;

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getInstance().popActivity(this);
        subscriptions.clear();
    }

    private void initView(){
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        tvTitle.setText(R.string.registe);
        tvTitle.setTextColor(0xfffaa8ae);
        btnSelectirIshow1.setImageResource(R.drawable.ic_hint);
        btnSelectirIshow2.setImageResource(R.drawable.ic_hint);

        //设置乡镇列表
        tvXiangZheng.setOnClickListener(new View.OnClickListener(){
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
                            words[0] = "三门县县级";
                            for (int i = 0 ; i < jsonArray.size() ; i++){
                                words[i+1] = jsonArray.get(i).getPosition_name();
                            }
                            DialogUIUtils.showSingleChoose((Activity) mContext, "乡，街道", town+1, words, new DialogUIItemListener() {
                                @Override
                                public void onItemClick(CharSequence text, int position) {
                                    town = position-1;
                                    if(town==-1) {
                                        tvCun.setOnClickListener(null);
                                        tvXiangZheng.setText("");
                                        return;
                                    }
                                    village=-1;
                                    tvXiangZheng.setText(text);
                                    //设置村列表
                                    tvCun.setText("");
                                    tvCun.setOnClickListener(new View.OnClickListener(){
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
                                                        words2[0] = "无";
                                                        for (int i = 0 ; i < jsonArray2.size() ; i++){
                                                            words2[i+1] = jsonArray2.get(i).getPosition_name();
                                                        }
                                                        DialogUIUtils.showSingleChoose((Activity) mContext, "村", village+1, words2, new DialogUIItemListener() {
                                                            @Override
                                                            public void onItemClick(CharSequence text, int position) {
                                                                village =position-1;
                                                                if (village==-1){
                                                                    tvCun.setText("");
                                                                    return;
                                                                }
                                                                tvCun.setText(text);
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

        ivMenu.setImageResource(R.drawable.ic_back);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etPhone.getText().toString();
                realName = etRealName.getText().toString();
                xiangZheng = town==-1?"":jsonArray.get(town).getId();
                cun = village==-1?"":jsonArray2.get(village).getId();
                pwd = etPwd.getText().toString();
                pwdAgain = etPwdAgain.getText().toString();
                yanZhengma = edYanzhengMa.getText().toString();
                if (!isMobileNum(phone)){
                    ToastUtil.showMessage(mContext,"请输入正确的手机号！");
                    return;
                }
                if (realName.equals("")){
                    ToastUtil.showMessage(mContext,"请输入姓名！");
                    return;
                }
                if (xiangZheng.equals("")){
                    ToastUtil.showMessage(mContext,"请选择所属乡镇！");
                    return;
                }
                if (cun.equals("")){
                    ToastUtil.showMessage(mContext,"请选择所属村！");
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
                if (yanZhengma.equals("")){
                    ToastUtil.showMessage(mContext,"请输入验证码！");
                    return;
                }
                loading = DialogUIUtils.showLoading(mContext,"正在加载列表...",true,false,false,true);
                loading.show();
                Subscription subscription = null;
                subscription = userRepository.saveUser(phone,realName,pwd,xiangZheng,cun,yanZhengma)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0) {
                                ToastUtil.showLongMessage(mContext, "注册成功！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext, "注册失败！"+response.getMsg());
                            }
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                subscriptions.add(subscription);
            }
        });


        tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSend){
                    phone = etPhone.getText().toString();
                    if (!phone.equals("")){
                        if (isMobileNum(phone)){
                            Subscription subscription = userRepository.getPhoneCode(phone,"0")
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
                                        Log.e(TAG, "error " +throwable.toString() );
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
