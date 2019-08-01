package com.test.ydool.sanmen.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.activities.ChangePeopleActivity;
import com.test.ydool.sanmen.activities.HomeOnlineAdvicesMainActivity;
import com.test.ydool.sanmen.activities.LoginActivity;
import com.test.ydool.sanmen.activities.OnlineAdviceListCunActivity;
import com.test.ydool.sanmen.activities.VersionDownActivity;
import com.test.ydool.sanmen.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/19.
 */

public class NormalFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_adm_name)
    TextView tvAdmnName;
    @BindView(R.id.iv_adm_photo)
    ImageView ivAdmPhoto;
    @BindView(R.id.tv_adm_tele)
    TextView tvAdmTele;
    @BindView(R.id.ll_Online_advice)
    LinearLayout llOnlineAdvice;
    @BindView(R.id.ll_inform_change)
    LinearLayout llInformChange;
    @BindView(R.id.iv_exit)
    ImageView ivexit;
    @BindView(R.id.ll_download)
    LinearLayout llDownLoad;

    private SPUtils spUtils;

    private BuildBean exitDialog;

    private FragmentInteraction listterner;

    public static NormalFragment newInstance() {
        return new NormalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_mine, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    // 1 定义了所有activity必须实现的接口方法
    public interface FragmentInteraction {
        void close(boolean is);
    }
    @Override
    public void onResume() {
        super.onResume();
        spUtils = SPUtils.getInstance("sanMen");
    }


    @Override
    public void onStart() {
        super.onStart();
        tvAdmTele.setText(TerminalInfo.phone);
        Glide.with(getActivity()).load(TerminalInfo.photoUrl).into(ivAdmPhoto);
    }

    private void initView() {
        tvTitle.setText(R.string.mine);
        tvTitle.setTextColor(0xfffaa8ae);
        spUtils = SPUtils.getInstance("sanMen");

        System.out.println(spUtils.getString("icon"));
        tvAdmnName.setText(spUtils.getString("Adm_name"));

        tvAdmTele.setText(spUtils.getString("phone"));

        Glide.with(getActivity()).load(spUtils.getString("icon")).into(ivAdmPhoto);

        exitDialog = DialogUIUtils.showAlert((Activity) getActivity(),
                getResources().getString(R.string.alert_notice),
                "是否确认注销当前账号", null, null,
                getResources().getString(R.string.cancel),
                getResources().getString(R.string.confirm),
                false, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                    }

                    @Override
                    public void onNegative() {
                        SPUtils spUtilas = SPUtils.getInstance("sanMen");
                        spUtilas.remove("name");
                        spUtilas.put("isLogin", false);
                        spUtilas.clear();
                        listterner.close(true);
//                        AppUtils.exitApp();
                    }
                });

        ivexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.show();
                getFragmentManager().popBackStack();
            }
        });


        llOnlineAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = spUtils.getBoolean("isLogin");
                if (isLogin) {
                    Intent intent = new Intent(getActivity(), HomeOnlineAdvicesMainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        llInformChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePeopleActivity.class);
                startActivity(intent);
            }
        });

        llDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VersionDownActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NormalFragment.FragmentInteraction){
            listterner = (FragmentInteraction) context; // 2.2 获取到宿主activity并赋值
        }else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

}
