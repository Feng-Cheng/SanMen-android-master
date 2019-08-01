package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.OrderMealBean;
import com.test.ydool.sanmen.bean.PonitPeopleBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.DividerItemDecoration;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/7/25.
 *
 *
 * 给点单权限
 */

public class GiveQuanXianActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rlOrderMealActivity)
    RecyclerView rlPeople;


    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;

    private int pageIndex = 0;

    private QuanXianAdapter mAdapter;
    private List<PonitPeopleBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_give_quanxain);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("点单权限");
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        spUtils = SPUtils.getInstance("sanMen");
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String token = spUtils.getString("token");

        Subscription subscription = userRepository
                .getOrderMealPermissionsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBean -> {
                    list = new ArrayList<>();
                    if (responseBean!=null){
                        list = responseBean.getData();
                        mAdapter = new QuanXianAdapter(R.layout.tv_add_delet,list);
                        rlPeople.setLayoutManager(new LinearLayoutManager(mContext));
                        rlPeople.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
                        rlPeople.setAdapter(mAdapter);
                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                String text = "";
                                String CheckPermissions = "";
                                if (list.get(position).getPermissions()==null || list.get(position).getPermissions().equals("0")){
                                    text += "是否确认开启账号:"+list.get(position).getName() + "  所属村:" + list.get(position).getPosName() +" 的点单权限?";
                                    CheckPermissions = "1";
                                }else {
                                    text += "是否确认关闭账号:"+list.get(position).getName() +  "  所属村:" + list.get(position).getPosName()  + " 的点单权限?";
                                    CheckPermissions = "0";
                                }
                                String finalCheckPermissions = CheckPermissions;
                                DialogUIUtils.showAlert((Activity) mContext,
                                        getResources().getString(R.string.alert_notice),
                                        text, null, null,
                                        getResources().getString(R.string.cancel),
                                        getResources().getString(R.string.confirm),
                                        false, true, true, new DialogUIListener() {
                                            @Override
                                            public void onPositive() {
                                                //取消
                                            }

                                            @Override
                                            public void onNegative() {
                                                //确定
                                                SPUtils spUtils = SPUtils.getInstance("sanMen");
                                                upPeopleDataStatus(spUtils.getString("token"), finalCheckPermissions,list.get(position).getId(),position);
                                            }
                                        }).show();
                            }
                        });
                    }
                },throwable -> {
                    throwable.printStackTrace();
                });
        subscriptions.add(subscription);

    }


    private void getPonitList(String token){
        Subscription subscription = userRepository
                .getOrderMealPermissionsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBean -> {
                    loadList(responseBean.getData());
                },throwable -> {
                    throwable.printStackTrace();
                });
        subscriptions.add(subscription);
    }
    private void loadList(List<PonitPeopleBean> data){
        if (data != null && data.size() > 0){
            if (pageIndex != 0){//上拉加载
                mAdapter.addData(data);
            }else {//下拉刷新
                mAdapter.setNewData(data);
            }
//            pageIndex += data.size();
        }else {
            mAdapter.loadMoreComplete();
        }
    }
    class QuanXianAdapter extends BaseQuickAdapter<PonitPeopleBean,BaseViewHolder> {

        public QuanXianAdapter(int layoutResId, @Nullable List<PonitPeopleBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PonitPeopleBean item) {
            helper.setText(R.id.tv_aud_name,item.getName());
            helper.setText(R.id.tv_cun,item.getPosName());
            if (item.getPermissions() ==null || item.getPermissions().equals("0")){
                helper.setText(R.id.tv_click,"未开启");
            }else {
                helper.setText(R.id.tv_click,"已开启");
            }
            helper.addOnClickListener(R.id.ll_back);
        }
    }


    private void upPeopleDataStatus(String token, String permissions, String ids,int postion){
        Subscription subscription = userRepository
                .upPeopleDataStatus(token,permissions,ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBean -> {
                    if (responseBean.getCode()==0){
                        String text = "";
                        if (permissions==null || permissions.equals("0")){
                            text += "关闭成功！";
                            list.get(postion).setPermissions("0");
                        }else {
                            text += "开启成功！";
                            list.get(postion).setPermissions("1");
                        }
                        ToastUtil.showMessage(mContext,text);
                        loadList(list);
                    }else {
                        ToastUtil.showMessage(mContext,"开启失败！"+responseBean.getMsg());
                    }
                },throwable -> {
                    throwable.printStackTrace();
                });
        subscriptions.add(subscription);
    }

}
