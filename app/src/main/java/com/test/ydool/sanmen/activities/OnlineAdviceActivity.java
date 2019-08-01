package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.VillageButtonBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.utils.utile;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 *
 * 在线建言提交
 */

public class OnlineAdviceActivity  extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ed_adm_name)
    TextView tvXiangzheng;
    @BindView(R.id.btn_report)
    Button btnReport;
    @BindView(R.id.tv_work_statics_village)
    TextView tvViView;
    @BindView(R.id.tv_work_statics_check_type)
    TextView tvCtView;
    @BindView(R.id.ed_auditor_intdro)
    TextView edCtView;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private Integer town=-1;

    private Integer village=-1;

    private List<PositionRootBean.row> jsonArray;
    private List<PositionRootBean.row> jsonArray2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_online_advice);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.Online_advice);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");
        tvXiangzheng.setText(spUtils.getString("pname"));

        //设置乡镇列表
        tvViView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Subscription subscription = null;
                subscription = userRepository.getPositionRoot()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            //List<VillageButtonBean> list = new ArrayList<>();
                            jsonArray = response.getRows();
                            String[] words2 = new String[jsonArray.size()+1];

                            words2[0] = "三门县";
                            for (int i = 0 ; i < jsonArray.size() ; i++){
                                words2[i+1] = jsonArray.get(i).getPosition_name();
                            }
                            DialogUIUtils.showSingleChoose((Activity) mContext, "选择建言的县或所属乡镇", town+1, words2, new DialogUIItemListener() {
                                @Override
                                public void onItemClick(CharSequence text, int position) {
                                    town =position-1;
                                    tvViView.setText(text);
                                    village=-1;
                                    //设置村列表
                                    tvCtView.setText("");
                                    if(town==-1) {
                                        tvCtView.setOnClickListener(null);
                                        return;
                                    }
                                    tvCtView.setOnClickListener(new View.OnClickListener(){

                                        @Override
                                        public void onClick(View v) {
                                            Subscription subscription = null;
                                            subscription = userRepository.getPositionRoot(jsonArray.get(town).getId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(response ->{
                                                        List<VillageButtonBean> list = new ArrayList<>();
                                                        jsonArray2 = response.getRows();
                                                        String[] words2 = new String[jsonArray2.size()+1];

                                                        words2[0] = "三门县";
                                                        for (int i = 0 ; i < jsonArray2.size() ; i++){
                                                            words2[i+1] = jsonArray2.get(i).getPosition_name();
                                                        }
                                                        DialogUIUtils.showSingleChoose((Activity) mContext, "选择村", village+1, words2, new DialogUIItemListener() {
                                                            @Override
                                                            public void onItemClick(CharSequence text, int position) {
                                                                village =position-1;
                                                                tvCtView.setText(text);
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
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });

                subscriptions.add(subscription);
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String townId=null;
                String villageId=null;
                if(jsonArray!=null&&town!=-1){
                    townId=jsonArray.get(town).getId();
                }
                if(jsonArray2!=null&&village!=-1){
                    villageId=jsonArray2.get(village).getId();
                }
                String content= utile.delHTMLTag(edCtView.getText().toString());
                if("".equals(content)){
                    ToastUtil.showLongMessage(mContext,"请输入内容后再提交！");
                    return;
                }
                Subscription subscription = userRepository.saveOnlineAdvice(token,content,"331022000000",townId,villageId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0){
                                ToastUtil.showLongMessage(mContext,"提交成功！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext,response.getMsg());
                            }
                        },throwable -> {
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });
            }
        });

    }
}
