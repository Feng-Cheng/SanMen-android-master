package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PointActivityMainBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/14.
 *
 * 点单页面
 */

public class PonitActivityChooseActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_ponit_activity_choose_danwei)
    TextView tvDanwei;
    @BindView(R.id.tv_ponit_activity_choose_content)
    TextView tvContent;
    @BindView(R.id.tv_ponit_activity_choose_theme)
    TextView tvTheme;
    @BindView(R.id.tv_ponit_activity_choose_type)
    TextView tvType;
    @BindView(R.id.tv_activty_time)
    TextView tvTime;
    @BindView(R.id.tv_phonenum_peopele)
    TextView tvPeople;
    @BindView(R.id.tv_phonenumber)
    TextView tvPhone;
    @BindView(R.id.btn_report)
    Button btnReport;


    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private List<PointActivityMainBean> list;



    List<String> list5 = new ArrayList<>();
    List<String> listContent = new ArrayList<>();
    List<String> listType = new ArrayList<>();
    List<String> listTheme = new ArrayList<>();
    private int genre = 0;
    private int genre1 = 0;
    private int genre2 = 0;
    private int genre3 = 0;
    String[] word2;
    String[] word3;
    String[] word4;
    String[] word5;

    String id = "";

    String titleId;

    private boolean select = false;
    private boolean select1 = false;
    private boolean select2 = false;

    private BuildBean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_choose);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.activty_ponit);
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
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        titleId = bundle.getString("titleId");
        Subscription subscription = userRepository.getPonitActivityMainList(spUtils.getString("token"),bundle.getString("titleId"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response!=null){
                        list= response.getData();
                    }
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常");
                });
        subscriptions.add(subscription);


        tvDanwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (list5.size() == 0){
                    list5 = new ArrayList<>();
                    int k =0;
                    int j = 0;
                    for (int i = 0 ;i <list.size() ; i++){
                        j=0;
                        if (k ==0){
                            list5.add(list.get(1).getActivity_unit());
                            k++;
                        }else {
                            for (j = 0 ; j <list5.size() ; j++){
                                if (list.get(i).getActivity_unit().equals(list5.get(j)))
                                    break;
                            }
                            if (j == list5.size()){
                                list5.add(list.get(i).getActivity_unit());
                                k++;
                            }
                        }
                    }
                    word2 = new String[list5.size()];
                    for (int i = 0 ; i < list5.size() ; i++){
                        word2[i] = list5.get(i);
                    }
//                }
                DialogUIUtils.showSingleChoose((Activity) mContext, "活动单位", genre, word2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        genre=position;
                        select = true;
                        tvDanwei.setText(text);
//                            tvDanwei.setEnabled(false);
                        tvContent.setText("");
                        tvType.setText("");
                        tvTheme.setText("");
                        tvTime.setText("");
                        tvPeople.setText("");
                        tvPhone.setText("");
                        id = "";
                        select1 = false;
                        select2 = false;
                        genre1 = 0;
                        genre2 = 0;
                        genre3 = 0;
                    }
                }).show();
            }
        });

        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDanwei.getText().toString().equals("")&&!select){
                    ToastUtil.showMessage(mContext,"请选择活动单位");
                }else {
//                    if (listContent.size() == 0){
                        listContent = new ArrayList<>();
                        int k = 0;
                        for (int i = 0 ;i <list.size() ; i++){
                            int j = 0;
                            if (list.get(i).getActivity_unit().equals(word2[genre])){
                                if (k == 0){
                                    listContent.add(list.get(i).getActivity_content());
                                    k++;
                                }else {
                                    for ( j = 0 ; j < listContent.size() ; j++){
                                        if (list.get(i).getActivity_content().equals(listContent.get(j)))
                                            break;
                                    }
                                    if ( j == listContent.size()){
                                        listContent.add(list.get(i).getActivity_content());
                                    }
                                }
                            }
                        }
                        word3 = new String[listContent.size()];
                        for (int i = 0 ; i < listContent.size() ; i++){
                            word3[i] = listContent.get(i);
                        }
//                    }
                    DialogUIUtils.showSingleChoose((Activity) mContext, "活动内容", genre1, word3, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            genre1=position;
                            select1 = true;
                            tvContent.setText(text);
//                                tvContent.setEnabled(false);
                            tvType.setText("");
                            tvTheme.setText("");
                            tvTime.setText("");
                            tvPeople.setText("");
                            tvPhone.setText("");
                            select2 = false;
                            id = "";
                            genre2 = 0;
                            genre3 = 0;
                        }
                    }).show();
                }
            }
        });

        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvContent.getText().toString().equals("")&&!select1){
                    ToastUtil.showMessage(mContext,"请选择活动内容");
                }else {
//                    if (listType.size() == 0){
                    listType = new ArrayList<>();
                        int k = 0;
                        for (int i = 0 ;i <list.size() ; i++){
                            int j = 0;
                            if (list.get(i).getActivity_unit().equals(word2[genre]) && list.get(i).getActivity_content().equals(word3[genre1])){
                                if (k == 0){
                                    listType.add(list.get(i).getActivity_form());
                                    k++;
                                }else {
                                    for (j = 0 ; j < listType.size() ; j++){
                                        if (listType.get(j).equals(list.get(i).getActivity_form()))
                                            break;
                                    }
                                    if (j == listType.size()){
                                        listType.add(list.get(i).getActivity_form());
                                        k++;
                                    }
                                }
                            }
                        }
                        word4 = new String[listType.size()];
                        for (int i = 0 ; i < listType.size() ; i++){
                            word4[i] = listType.get(i);
                        }
//                    }
                    DialogUIUtils.showSingleChoose((Activity) mContext, "活动形式", genre2, word4, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            genre2=position;
                            select2 = true;
                            tvType.setText(text);
//                                tvType.setEnabled(false);
                            tvTheme.setText("");
                            tvTime.setText("");
                            tvPeople.setText("");
                            tvPhone.setText("");
                            id = "";
                            genre3 = 0;
                        }
                    }).show();
                }
            }
        });

        tvTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvType.getText().toString().equals("")&&!select2){
                    ToastUtil.showMessage(mContext,"请选择活动形式");
                }else {
//                    if (listTheme.size() == 0){
                    listTheme = new ArrayList<>();
                        int k = 0;
                        for (int i = 0 ;i <list.size() ; i++){
                            int j = 0;
                            if (list.get(i).getActivity_unit().equals(word2[genre]) && list.get(i).getActivity_content().equals(word3[genre1]) && list.get(i).getActivity_form().equals(word4[genre2])){
                                if (k == 0){
                                    listTheme.add(list.get(i).getActivity_theme());
                                    k++;
                                }else {
                                    for (j = 0 ; j < listTheme.size() ; j++){
                                        if (listTheme.get(j).equals(list.get(i).getActivity_theme()))
                                            break;
                                    }
                                    if (j == listTheme.size()){
                                        listTheme.add(list.get(i).getActivity_theme());
                                        k++;
                                    }
                                }
                            }
                        }
                        word5 = new String[listTheme.size()];
                        for (int i = 0 ; i < listTheme.size() ; i++){
                            word5[i] = listTheme.get(i);
                        }
//                    }
                    DialogUIUtils.showSingleChoose((Activity) mContext, "活动形式", genre3, word5, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            genre3=position;
                            tvTheme.setText(text);
//                                tvTheme.setEnabled(false);
                            for (int i = 0 ; i< list.size() ; i++){
                                if (list.get(i).getActivity_unit().equals(word2[genre]) && list.get(i).getActivity_content().equals(word3[genre1]) && list.get(i).getActivity_form().equals(word4[genre2])&&list.get(i).getActivity_theme().equals(text)){
                                    tvTime.setText(list.get(i).getTime());
                                    tvPeople.setText(list.get(i).getLink_man());
                                    tvPhone.setText(list.get(i).getLink_phone());
                                    id = list.get(i).getId();
                                }
                            }
                        }
                    }).show();
                }
            }
        });


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("")){
                    ToastUtil.showMessage(mContext,"请选择点单内容");
                }else {
                    loading = DialogUIUtils.showLoading(mContext,"正在点单中...",true,false,false,true);
                    loading.show();
                    SPUtils spUtils1 = SPUtils.getInstance("sanMen");
                    String token = spUtils1.getString("token");
                    Subscription subscription1 = userRepository.getOrderMealIsNum(token,titleId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if (response.getNumber()<3){
                                    Subscription subscription2 = userRepository.getOrderMealTheThemeIsNum(token,id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(ponitListAfterBean -> {
                                                if (ponitListAfterBean==null){
                                                    Subscription subscription3 = userRepository.adSaveOrderMeall(token,titleId,id)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(httpResponse -> {
                                                                if (httpResponse.getCode() == 0){
                                                                    ToastUtil.showMessage(mContext,"点单成功");
                                                                    finish();
                                                                }else {
                                                                    ToastUtil.showLongMessage(mContext,"点单失败!"+httpResponse.getMsg());
                                                                }
                                                                DialogUIUtils.dismiss(loading);
                                                            },throwable ->{
                                                                throwable.printStackTrace();
                                                                ToastUtil.showLongMessage(mContext,"网络异常");
                                                                DialogUIUtils.dismiss(loading);
                                                            });
                                                    subscriptions.add(subscription3);
                                                }else {
                                                    ToastUtil.showLongMessage(mContext,"点单失败,此活动已点，无需在点！");
                                                }
                                                DialogUIUtils.dismiss(loading);
                                            },throwable ->{
                                                throwable.printStackTrace();
                                                ToastUtil.showLongMessage(mContext,"网络异常");
                                                DialogUIUtils.dismiss(loading);
                                            });
                                    subscriptions.add(subscription2);
                                }else {
                                    ToastUtil.showLongMessage(mContext,"点单失败，此主题的活动点单数量已达上限(3个)！");
                                }
                                DialogUIUtils.dismiss(loading);
                            },throwable ->{
                                throwable.printStackTrace();
                                ToastUtil.showLongMessage(mContext,"网络异常");
                                DialogUIUtils.dismiss(loading);
                            });
                    subscriptions.add(subscription1);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
}
