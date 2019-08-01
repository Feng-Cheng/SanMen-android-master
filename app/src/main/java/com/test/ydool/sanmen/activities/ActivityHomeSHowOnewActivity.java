package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.UrlBean;
import com.test.ydool.sanmen.fragments.HomeFragment;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.utils.utile;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

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
 * Created by Administrator on 2018/6/19.
 *
 * 活动预告审核
 */

public class ActivityHomeSHowOnewActivity extends BaseActivity  implements OnBannerListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_audtiorname)
    TextView tvAudtiorname;
    @BindView(R.id.tv_activity_home_show_title)
    TextView tvactivityTitle;
    @BindView(R.id.tv_home_show_time)
    TextView tvTime;
    @BindView(R.id.tv_home_organmize)
    TextView tvOrganmize;
    @BindView(R.id.tv_home_annonce_content)
    TextView tvContent;
    @BindView(R.id.tv_home_annonce_remrks)
    TextView tvRemrks;
    @BindView(R.id.ll_show_button)
    LinearLayout llShowButton;
    @BindView(R.id.btn_activity_cun_accpter)
    Button btnAccpter;
    @BindView(R.id.btn_activity_cun_no_accpter)
    Button btnNoAccpter;
    @BindView(R.id.banner)
    Banner banner;



    private UserRepository userRepository;

    private CompositeSubscription subscriptions;


    SPUtils spUtils;
    ArrayList<String> list_path = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_home_show_onew);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.activty_announce);
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

        Bundle bundle = getIntent().getExtras();
        tvAudtiorname.setText(bundle.getString("auname"));
        tvactivityTitle.setText(bundle.getString("title"));
        tvTime.setText(bundle.getString("time"));
        tvOrganmize.setText(bundle.getString("organizer"));
        tvContent.setText(utile.delHTMLTag(bundle.getString("detail")));
        tvRemrks.setText(bundle.getString("ps"));
        ArrayList<String> urlBeans = bundle.getStringArrayList("urls");

        if (urlBeans!=null&&urlBeans.size()>0){
            list_path = new ArrayList<>();
            for ( int i  = 0; i< urlBeans.size() ; i++){
                list_path.add(TerminalInfo.BASE_URL + urlBeans.get(i));
            }
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImageLoader(new MyLoader());
            banner.setImages(list_path);
            banner.setBannerAnimation(Transformer.Default);
            banner.setDelayTime(3000);
            banner.isAutoPlay(true);
            banner.setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(this)
                    .start();
        }

        SPUtils spUtils=SPUtils.getInstance("sanMen");
        Set<String> resList=spUtils.getStringSet("resList");
        if (resList.contains("/activity/preview/approve") && bundle.getString("status")!=null && bundle.getString("status").equals("0")){
            llShowButton.setVisibility(View.VISIBLE);
            btnAccpter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = bundle.getString("id");
                    accepter(id,true);
                }
            });
            btnNoAccpter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = bundle.getString("id");
                    accepter(id,false);
                }
            });
        }
    }

    private void accepter(String id,boolean isAccepter){
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");
        if (isAccepter){
            Subscription subscription = userRepository.passActivityPreview(token,id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reponse ->{
                        if (reponse.getCode() == 0){
                            ToastUtil.showLongMessage(mContext,"审核成功！");
                            ScreenManager.getInstance().popActivity((Activity) mContext);
                        }else {
                            ToastUtil.showLongMessage(mContext,"审核失败！"+reponse.getMsg());
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.showLongMessage(mContext,"网络异常！");
                    });
            subscriptions.add(subscription);
        }else {
            Subscription subscription = userRepository.noPassActivityPreview(token,id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reponse ->{
                        if (reponse.getCode() == 0){
                            ToastUtil.showLongMessage(mContext,"审核成功！");
                            ScreenManager.getInstance().popActivity((Activity) mContext);
                        }else {
                            ToastUtil.showLongMessage(mContext,"审核失败！"+reponse.getMsg());
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.showLongMessage(mContext,"网络异常！");
                    });
            subscriptions.add(subscription);
        }

    }

    @Override
    public void OnBannerClick(int position) {

    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }
}