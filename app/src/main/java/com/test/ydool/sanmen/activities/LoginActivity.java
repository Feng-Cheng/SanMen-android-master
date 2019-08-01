package com.test.ydool.sanmen.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
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
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AdminBean;
import com.test.ydool.sanmen.cmmmmm.Communal;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;


import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Administrator on 2018/4/18.
 *
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity ";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.bt_login)
    Button btnLogin;
    @BindView(R.id.tv_main_protocol)
    TextView mProtocol;
    @BindView(R.id.btn_selector_ishow)
    ImageView btnIsShow;
    @BindView(R.id.tv_lost_pwd)
    TextView tvLostPwd;
    @BindView(R.id.rl_changeShow)
    RelativeLayout rlChangShow;

    private Context mContext;
    private String name, pwd;
    private UserRepository userRepository;

    private CompositeSubscription subscriptions;
    private boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;


        SPUtils spUtilsSanMen = SPUtils.getInstance("sanMen");
        String userId = spUtilsSanMen.getString("userName");

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etName.setText("");
        etPwd.setText("");
        name = "";
        pwd = "";
    }

    private void initView(){
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        tvTitle.setText(R.string.login);
        tvTitle.setTextColor(0xfffaa8ae);
        tvAll.setText(R.string.registe);
        tvAll.setTextColor(0xfffaa8ae);

        ivMenu.setImageResource(R.drawable.ic_back);
        btnIsShow.setImageResource(R.drawable.ic_hint);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    ToastUtil.showMessage(mContext, getResources().getString(R.string.name_hint));
                    btnLogin.setClickable(true);
                } else {
                    pwd = etPwd.getText().toString().trim();
                    if (pwd.isEmpty()) {
                        ToastUtil.showMessage(mContext, getResources().getString(R.string.pas_hint));
                        btnLogin.setClickable(true);
                    } else {
                        doLogin();
                    }
                }
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisteActivity.class);
                startActivity(intent);
            }
        });
        toProtocol();
        mProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ShowWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",TerminalInfo.BASE_URL+"/agreement/agreement");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnIsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (etPwd.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    btnIsShow.setImageResource(R.drawable.ic_show);
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

                } else {
                    btnIsShow.setImageResource(R.drawable.ic_hint);
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etPwd.setSelection(etPwd.getText().toString().length());*/
            }
        });
        rlChangShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHideFirst){
                    showOrHide(isHideFirst);
                    isHideFirst = false;
                }else {
                    showOrHide(isHideFirst);
                    isHideFirst = true;
                }
            }
        });

        tvLostPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LostPassWordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toProtocol() {
        SpannableStringBuilder builder = new SpannableStringBuilder(mProtocol.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
        UnderlineSpan lineSpan = new UnderlineSpan();
        builder.setSpan(lineSpan,11,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //下划线
//        builder.setSpan(blueSpan,11,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //字体颜色

        mProtocol.setText(builder);
    }
    private BuildBean loading;
    private void doLogin() {
        Log.w(TAG, "doLogin: >>>>>>>>>111");
        loading = DialogUIUtils.showLoading(mContext,"正在登陆中...",true,false,false,true);
        loading.show();
        final SPUtils spUtils = SPUtils.getInstance("sanMen");
        if (Communal.testmode){
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("name",name);
            mContext.startActivity(intent);
            finish();
            btnLogin.setClickable(true);
            DialogUIUtils.dismiss(loading);
            return;
        }
        Subscription subscription = null;
        subscription = userRepository.loginCheck(name,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response.getCode() ==0){
                        AdminBean adminBean = response.getData();
                        if (response.getData() != null){
                            spUtils.put("name",name);
                            spUtils.put("pwd",pwd);
                            spUtils.put("token",adminBean.getToken());
                            spUtils.put("userId",adminBean.getId());
                            spUtils.put("Adm_name",adminBean.getAdminName());
                            if (adminBean.getAdminId()!=null){
                                spUtils.put("Adm_id",adminBean.getAdminId());
                            }
                            spUtils.put("icon", TerminalInfo.BASE_URL+adminBean.getIcon());
                            spUtils.put("position_id",adminBean.getPosition_id());
                            if (adminBean.getPhone() != null){
                                spUtils.put("phone",adminBean.getPhone());
                            }
                            if (adminBean.getPname() != null){
                                spUtils.put("pname",adminBean.getPname());
                            }
                            if (adminBean.getAuditor()!=null){
                                spUtils.put("auditorid",adminBean.getAuditor().getId());
                            }
                            if (adminBean.getPermissions()!=null){
                                spUtils.put("permissions",adminBean.getPermissions());
                            }

                            Set<String> list = adminBean.getResList();
                            spUtils.put("resList",list);
                            if (list.contains("/auditoriumInfo/auditorium/village")){
                                spUtils.put("quanxian", 2);
                            }else if (list.contains("/auditoriumlnfo/auditorium/town")){
                                spUtils.put("quanxian", 1);
                            }else if (list.contains("/auditoriumInfo/auditorium/county")){
                                spUtils.put("quanxian", 0);
                            }else{
                                spUtils.put("quanxian", -1);
                            }
                            spUtils.put("isLogin",true);
                        }
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("name",name);
                        mContext.startActivity(intent);
                        btnLogin.setClickable(true);
                        finish();
                    }else {
                        ToastUtil.showLongMessage(mContext,"登录失败"+response.getMsg());
                    }
                    DialogUIUtils.dismiss(loading);
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常");
                    DialogUIUtils.dismiss(loading);
                });

        subscriptions.add(subscription);
    }


    /**
     * 显示或隐藏
     * @param isShow
     */
    private void showOrHide(boolean isShow){
        //记住光标开始的位置
        int pos = etPwd.getSelectionStart();
        if(isShow){
            btnIsShow.setImageResource(R.drawable.ic_show);
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            btnIsShow.setImageResource(R.drawable.ic_hint);
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPwd.setSelection(pos);
    }
}
