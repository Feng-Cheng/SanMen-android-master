package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.NotificationBean;
import com.test.ydool.sanmen.fragments.HomeFragment;
import com.test.ydool.sanmen.fragments.MineFragment;
import com.test.ydool.sanmen.fragments.NormalFragment;
import com.test.ydool.sanmen.fragments.SignTimeFragment;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity implements MineFragment.FragmentInteraction,NormalFragment.FragmentInteraction{
    private static final int BACK_PRESSED_INTERVAL = 2000;

    // 上次按下返回键的系统时间
    private long lastBackTime = 0;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private BaseFragment fragment;

    private HomeFragment homeFragment;

    private MineFragment mineFragment;

    private NormalFragment normalFragment;

    protected Context mContext;

    private CompositeSubscription subscriptions;
    private UserRepository userRepository;
    private int num=0;

    private View badge;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                NotificationBean bean = TerminalInfo.bean;
                num = bean.getAuditoriumPreviews().size() + bean.getStatePreviews().size() + bean.getActivityPreviews().size();
                updataBotton(num,true);
            }else if( msg.what == 1){
                try {
                    TextView textView = badge.findViewById(R.id.tv_msg_count);
                    textView.setVisibility(View.GONE);
                }catch (Exception e){
                    Log.i("error",e.toString());
                }
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (null == homeFragment) {
                        homeFragment = HomeFragment.newInstance();
                    }
                    switchContent(fragment, homeFragment);
                    fragment = homeFragment;
                    return true;
                case R.id.navigation_mine:
                    SPUtils spUtils = SPUtils.getInstance("sanMen");
                    if (spUtils.getBoolean("isLogin")){
                        if (spUtils.getInt("quanxian") == -1){
                            if (null == mineFragment) {
                                normalFragment = NormalFragment.newInstance();
                            }
                            switchContent(fragment, normalFragment);
                            fragment = normalFragment;
                        }else {
                            if (null == mineFragment) {
                                mineFragment = MineFragment.newInstance();
                            }
                            switchContent(fragment, mineFragment);
                            fragment = mineFragment;
                        }
                        return true;
                    }else {
                        Intent intent = new Intent(mContext,LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }
                default:
                    break;
            }
            return false;
        }
    };



    protected int getFragmentContentId() {
        return R.id.content_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDown();
    }

    private void initView(){
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        fragment = homeFragment;
        ft.replace(R.id.content_layout, homeFragment, homeFragment.getClass().getSimpleName());
        ft.addToBackStack(homeFragment.getClass().getSimpleName());
        ft.commit();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Subscription subscription = userRepository.getAppVersionCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(version->{
                    int versionCode = AppUtils.getAppVersionCode();
                    if (versionCode < Integer.parseInt(version.getVersion()) ){
                        DialogUIUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.alert_update),
                                "欢迎您下载使用新版本", null, null,
                                getResources().getString(R.string.othertime),
                                getResources().getString(R.string.togo),
                                false, true, true, new DialogUIListener() {
                                    @Override
                                    public void onPositive() {

                                    }

                                    @Override
                                    public void onNegative() {
                                        Uri uri = Uri.parse("http://zs.ydool.com:8082/download");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                },throwable -> {
                    throwable.printStackTrace();
                });
        subscriptions.add(subscription);
    }

    public void switchContent(Fragment from, Fragment to) {
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            transaction.hide(from)
                    .add(getFragmentContentId(), to, to.getClass().getSimpleName()).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }

    @Override
    public void onBackPressed() {
        long currentBackTime = System.currentTimeMillis();
        // 比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
        if (currentBackTime - lastBackTime > BACK_PRESSED_INTERVAL) {
            lastBackTime = currentBackTime;
            Toast.makeText(this, R.string.exit_sanmen, Toast.LENGTH_SHORT).show();
        } else { // 如果两次按下的时间差小于2秒，则退出程序
            finish();
        }
    }

    private void changeDown(){
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        if (spUtils.getBoolean("isLogin")){
            if ( spUtils.getInt("quanxian") == 1){
                Subscription subscription = userRepository.getNotifiMain(spUtils.getString("token"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response->{
                            if (response!=null){
                                TerminalInfo.bean = response;
                                Message message = new Message();
                                message.what = 0;
                                mHandler.sendMessage(message);
                            }
                        },throwable -> {
//                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
                subscriptions.add(subscription);
            }
        }
    }

    @Override
    public void retrunHome(boolean is) {
        if (is){
//            switchContent(fragment,homeFragment);
            navigation.setSelectedItemId(R.id.navigation_home);
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void close(boolean is) {
        if (is){
            navigation.setSelectedItemId(R.id.navigation_home);
            if (badge!=null){
//                Message message = new Message();
//                message.what = 1;
//                mHandler.sendMessage(message);
            }

        }
    }

    public void updataBotton(int num,boolean isShow){
        if (num<1){
            return;
        }
        //获取整个的NavigationView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        //这里就是获取所添加的每一个Tab(或者叫menu)，
        View tab = menuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        //加载我们的角标View，新创建的一个布局
        badge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        //添加到Tab上
        itemView.addView(badge);
        TextView textView = badge.findViewById(R.id.tv_msg_count);
        textView.setText(String.valueOf(num));
        if (isShow){
            //无消息时可以将它隐藏即可
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
