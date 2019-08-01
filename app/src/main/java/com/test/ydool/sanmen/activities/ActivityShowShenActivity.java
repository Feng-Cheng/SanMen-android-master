package com.test.ydool.sanmen.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/27.
 *
 * 活动预告审核
 */

public class ActivityShowShenActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ll_activty_announce_time)
    LinearLayout llTime;
    @BindView(R.id.tv_activity_put_time)
    TextView tvTime;
    @BindView(R.id.ed_adm_name)
    TextView tvBlowarea;
    @BindView(R.id.ed_activity_organizer)
    TextView edOrganizer;
    @BindView(R.id.ed_activity_theme)
    TextView edTheme;
    @BindView(R.id.ed_activity_announce_content)
    TextView edContent;
    @BindView(R.id.et_remarks)
    TextView edRemarks;
    @BindView(R.id.banner)
    Banner banner;

    private CompositeSubscription subscriptions;
    private Uri imgPath;
    private UserRepository userRepository;

    private List<Uri> mSelected;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 ){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_show_shen);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.activty_announce);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();


        SPUtils spUtils = SPUtils.getInstance("sanMen");

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
