package com.test.ydool.sanmen.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/29.
 *
 * 工作小结查看页面
 */

public class WorkOnewActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_base_time)
    TextView tvBaseTime;
    @BindView(R.id.tv_base_name)
    TextView tvBaseName;
    @BindView(R.id.tv_auditorName)
    TextView tvAuditorName;
    @BindView(R.id.tv_work_content)
    TextView tvWorkContent;
    @BindView(R.id.tv_problem_report)
    TextView tvIssue;
    @BindView(R.id.tv_problem_advice)
    TextView tvSolution;
    @BindView(R.id.tv_activity_inform_main)
    TextView tvActivity_describe;
    @BindView(R.id.tv_ps)
    TextView tvRemark;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_work_onew);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("工作小结");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        Bundle bundle = getIntent().getExtras();

        tvAuditorName.setText(bundle.getString("auditorName"));
        tvBaseName.setText(bundle.getString("name"));
        tvBaseTime.setText(bundle.getString("time"));
        tvWorkContent.setText(bundle.getString("content"));
        tvIssue.setText(bundle.getString("issue"));
        tvSolution.setText(bundle.getString("solution"));
        tvActivity_describe.setText(bundle.getString("activity_describe"));
        tvRemark.setText(bundle.getString("remark"));


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
