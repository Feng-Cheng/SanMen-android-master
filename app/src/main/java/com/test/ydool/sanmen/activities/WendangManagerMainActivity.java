package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.NewsAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.TodayHotBean;
import com.test.ydool.sanmen.bean.WenDangBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
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
 * Created by Administrator on 2018/6/19.
 *
 * 文档下载页面
 * */

public class WendangManagerMainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvAll;
    @BindView(R.id.rlPonitActivity)
    RecyclerView rlWendang;
    @BindView(R.id.gank_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private List<WenDangBean> list;

    private WendangAdapter mAdapter;

    private String fileUrl = "";
    private String fileName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_ponit_activity_main);
        ButterKnife.bind(this);
        refreshLayout.setEnabled(false);
        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.wendang_manager);
        tvTitle.setTextColor(0xfffaa8ae);
        tvAll.setText("上传");
        tvAll.setTextColor(0xfffaa8ae);

        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        String token = spUtils.getString("token");
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WendangReportActivity.class);
                startActivity(intent);
            }
        });
        updatedata(token);


    }

    private void updatedata(String token){

        Subscription subscription = userRepository.getWenDangList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    list = new ArrayList<>();
                    if (response!=null){
                        list = response.getData();
                        mAdapter = new WendangAdapter(R.layout.item_wendang_manager,list);
                        rlWendang.setLayoutManager(new LinearLayoutManager(mContext));
                        rlWendang.setAdapter(mAdapter);
                        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                fileName = list.get(position).getName();
                                fileUrl = list.get(position).getId();
                                myPermission();

                            }
                        });
                    }
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
    }

    class WendangAdapter extends BaseQuickAdapter<WenDangBean,BaseViewHolder>{

        public WendangAdapter(int layoutResId, @Nullable List<WenDangBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WenDangBean item) {
            helper.setText(R.id.tv_base_time,item.getTime());
//            helper.setText(R.id.tv_base_true_time,item.getTime());
            helper.setText(R.id.tv_base_name,item.getPositionStr());
            helper.setText(R.id.tv_conten_title,item.getUser_name());
            helper.setText(R.id.tv_base_main,item.getContent());
            helper.setText(R.id.tv_ohter_name,item.getName());

            helper.addOnClickListener(R.id.ll_wenDang);
        }
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            ToastUtil.showMessage(mContext,"请开启存储权限！");
        }else {
            DialogUIUtils.showAlert((Activity) mContext,
                    getResources().getString(R.string.alert_notice),
                    "是否下载"+fileName+"文件到/download文件下", null, null,
                    getResources().getString(R.string.cancel),
                    getResources().getString(R.string.confirm),
                    false, true, true, new DialogUIListener() {
                        @Override
                        public void onPositive() {

                        }

                        @Override
                        public void onNegative() {
                            try{
                                //创建下载任务,downloadUrl就是下载链接
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(TerminalInfo.BASE_URL+"/report/report/getReportFile?id="+ fileUrl));
                                //指定下载路径和下载文件名
                                request.setDestinationInExternalPublicDir("/download/", fileName);
                                //获取下载管理器
                                DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                //将下载任务加入下载队列，否则不会进行下载
                                downloadManager.enqueue(request);
                            }catch (Exception e){
                                ToastUtil.showMessage(mContext,"下载失败,开启存储权限！");
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //权限已授予
//                    intoView(fileUrl,fileName);
                    DialogUIUtils.showAlert((Activity) mContext,
                            getResources().getString(R.string.alert_notice),
                            "是否下载"+fileName+"文件到/download文件下", null, null,
                            getResources().getString(R.string.cancel),
                            getResources().getString(R.string.confirm),
                            false, true, true, new DialogUIListener() {
                                @Override
                                public void onPositive() {

                                }

                                @Override
                                public void onNegative() {
                                    try{
                                        //创建下载任务,downloadUrl就是下载链接
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(TerminalInfo.BASE_URL+"/report/report/getReportFile?id="+ fileUrl));
                                        //指定下载路径和下载文件名
                                        request.setDestinationInExternalPublicDir("/download/", fileName);
                                        //获取下载管理器
                                        DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                        //将下载任务加入下载队列，否则不会进行下载
                                        downloadManager.enqueue(request);
                                    }catch (Exception e){
                                        ToastUtil.showMessage(mContext,"下载失败,开启存储权限！");
                                    }
                                }
                            }).show();
                }else{
                    //权限未授予
                    Toast.makeText(this,"请开启存储权限才能访问！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void intoView(String fileUrl,String fileName){
        Intent intent = new Intent(mContext,ShowFileWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fileUrl",fileUrl);
        bundle.putString("fileName",fileName);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
