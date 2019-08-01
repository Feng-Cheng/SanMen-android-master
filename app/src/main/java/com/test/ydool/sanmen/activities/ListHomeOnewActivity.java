package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/20.
 *
 * 主题活动
 */

public class ListHomeOnewActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_list_auditorium_name)
    TextView tvAudName;
    @BindView(R.id.tv_list_activty_content)
    TextView tvContent;
    @BindView(R.id.tv_list_activitgy_type)
    TextView tvType;
    @BindView(R.id.tv_list_activty_time)
    TextView tvTime;
    @BindView(R.id.tv_list_toke_man)
    TextView tvTokeMan;
//    @BindView(R.id.tv_list_phonenumber)
//    TextView tvPhone;

    private SPUtils spUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_list_home_onew);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.them_activiy);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();


        tvAudName.setText(bundle.getString("auditorium"));
        tvContent.setText(bundle.getString("content"));
        tvType.setText(bundle.getString("type"));
        tvTime.setText(bundle.getString("time"));
        tvTokeMan.setText(bundle.getString("linMan"));
//        tvPhone.setText(bundle.getString("linphone"));


    }
}
