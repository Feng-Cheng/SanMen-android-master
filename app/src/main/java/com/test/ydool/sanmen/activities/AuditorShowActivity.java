package com.test.ydool.sanmen.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/30.
 *
 * 礼堂风采前端显示
 */

public class AuditorShowActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_audtiorname)
    TextView tvAudtiorName;
    @BindView(R.id.tv_xiangzheng)
    TextView tvXiangzheng;
    @BindView(R.id.tv_adm)
    TextView tvAdm;
    @BindView(R.id.tv_build_area)
    TextView tvBuildArea;
    @BindView(R.id.tv_build_year)
    TextView tvBuildYear;
    @BindView(R.id.tv_village_situation)
    TextView tvVillageSit;
    @BindView(R.id.tv_auditor_stat)
    TextView tvAudtiorStat;
    @BindView(R.id.tv_wenti)
    TextView tvWenti;
    @BindView(R.id.tv_ps)
    TextView tvPs;

    private ArrayList<String> list_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_show);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("礼堂风采");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        list_path = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        tvAudtiorName.setText(bundle.getString("name"));
        tvXiangzheng.setText(bundle.getString("vill"));
        tvAdm.setText(bundle.getString("adm"));
        tvAudtiorStat.setText(bundle.getString("survey"));
        tvVillageSit.setText(bundle.getString("audtior_intro"));
        tvBuildArea.setText(bundle.getString("area"));
        tvBuildYear.setText(bundle.getString("year"));
        tvWenti.setText(bundle.getString("duiwu"));
        tvPs.setText(bundle.getString("ps"));
        list_path = bundle.getStringArrayList("imgPath");
        if (list_path!=null){
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImageLoader(new MyLoader());
            banner.setImages(list_path);
            banner.setBannerAnimation(Transformer.Default);
            banner.setDelayTime(3000);
            banner.isAutoPlay(true);
            banner.setIndicatorGravity(BannerConfig.CENTER)
                    .start();
        }

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
