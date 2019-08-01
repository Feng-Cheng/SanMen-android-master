package com.test.ydool.sanmen.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/5/29.
 *
 * 工作小结提交页面
 */

public class WorkReportActivity extends BaseActivity{
    private static final String TAG = "WorkReportActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_all)
    TextView tvTitleAll;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_pickTime)
    TextView tvPickTime;
    @BindView(R.id.et_work_report_summary)
    EditText edSummary;
    @BindView(R.id.et_work_report_solution)
    EditText edSolutin;
    @BindView(R.id.et_work_report_activity_describe)
    EditText edActivityDescribe;
    @BindView(R.id.et_work_report_issue)
    EditText edIssue;
    @BindView(R.id.et_work_report_remark)
    EditText edRemark;
    @BindView(R.id.rl_check_time)
    RelativeLayout rlCheckTime;

    private String nowTime;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;
    private SPUtils spUtils;

    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_work_report);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("工作小结");
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitleAll.setText("提交");
        tvTitleAll.setTextColor(0xfffaa8ae);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");

        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        nowTime = mYear + "-" + (mMonth+1) + "-" + mDay;

        tvPickTime.setText(nowTime);



        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = edSummary.getText().toString();
                if (summary!=null && summary!=""){
                    SPUtils spUtils = SPUtils.getInstance("sanMen");
                    if (spUtils.getInt("quanxian") == 2){
                        if (summary.length()<300){
                            ToastUtil.showMessage(mContext,"村管理员工作内容最少300字！");
                            return;
                        }
                    }
                    if (spUtils.getInt("quanxian") == 1){
                        if (summary.length()<500){
                            ToastUtil.showMessage(mContext,"镇管理员工作内容最少500字！");
                            return;
                        }
                    }
                    String soulution = edSolutin.getText().toString();
                    String issue = edIssue.getText().toString();
                    String activitydes = edActivityDescribe.getText().toString();
                    String remark = edRemark.getText().toString();
                    Subscription subscription = userRepository.saveWorkReport(spUtils.getString("token"),spUtils.getString("Adm_name"),nowTime
                            ,summary,issue,soulution,activitydes,remark)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if (response.getCode() == 0){
                                    ToastUtil.showLongMessage(mContext,"提交成功！");
                                    finish();
                                }else {
                                    ToastUtil.showLongMessage(mContext,"提交失败！"+response.getMsg());
                                }
                            },throwable -> {
                                Log.e(TAG,throwable.toString());
                                ToastUtil.showLongMessage(mContext,"网络异常！");
                            });
                    subscriptions.add(subscription);
                }else {
                    ToastUtil.showLongMessage(mContext,"工作内容不能为空");
                }
            }
        });


        rlCheckTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, onDateSetListener, mYear, mMonth, mDay).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
            }
            nowTime=days;
            tvPickTime.setText(days);
        }
    };

}
