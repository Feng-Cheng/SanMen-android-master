package com.test.ydool.sanmen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.SignAdapater;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.DaySignBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/4.
 */

public class SignTimeFragment extends BaseFragment{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_sign)
    RecyclerView rlSign;

    private List<DaySignBean> list;

    private UserRepository userRepository;

    private CompositeSubscription subscriptions;


    private SignAdapater signAdapater;

    SPUtils spUtils;

    public static SignTimeFragment newInstance(){
        return new SignTimeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_time, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText(R.string.day_sign);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        spUtils = SPUtils.getInstance("sanMen");
        String userId = spUtils.getString("userId");
        String token = spUtils.getString("token");
        Subscription subscription2 = userRepository.saveSign(userId,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 0 ){
                        ToastUtil.showLongMessage(getActivity(),"签到成功！");
                    }else{
                        ToastUtil.showLongMessage(getActivity(),response.getMsg());
                    }
                },throwable ->{
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription2);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Subscription subscription = userRepository.getSignList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = response.getData();
                    signAdapater = new SignAdapater(R.layout.item_sign,list);
                    rlSign.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rlSign.setAdapter(signAdapater);
                },throwable -> {
                    ToastUtil.showLongMessage(getActivity(),"网络异常！");
                });
        subscriptions.add(subscription);

    }



}
