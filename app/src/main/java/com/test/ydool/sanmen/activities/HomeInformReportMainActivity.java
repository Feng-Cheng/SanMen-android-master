package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.InformPutBaseAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.InformBaseBean;
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
import tbsplus.tbs.tencent.com.tbsplus.TbsPlus;

/**
 * Created by Administrator on 2018/6/4.
 *
 * 信息发布页面
 */

public class HomeInformReportMainActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_inform_main)
    RecyclerView rlInformMain;
    @BindView(R.id.query_et)
    EditText etQuery;
    @BindView(R.id.btn_inform_report_search)
    Button btnSearch;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;


    private InformPutBaseAdapter mAdapter;

    private List<InformBaseBean> list;

    private List<InformBaseBean> list1;

    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_home_inform_report_main);
        ButterKnife.bind(this);

        initView();
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mAdapter.setNewData(list);
                rlInformMain.setAdapter(mAdapter);
            }
        }
    };

    private void initView() {
        tvTitle.setText(R.string.inform_put);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.getInstance().popActivity((Activity) mContext);
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        updateData();
    }

    private void updateData(){
        Subscription subscription = userRepository.getAnnoListNoLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list = response;
                    list1 = response;
                    mAdapter = new InformPutBaseAdapter(R.layout.item_inform_put_base,list);
                    rlInformMain.setLayoutManager(new LinearLayoutManager(mContext));
                    rlInformMain.setAdapter(mAdapter);
                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            check = position;
                            myPermission(position);

//                            TbsPlus.openUrl(mContext, "/storage/emulated/0/Download/三宣〔2018〕35号(关于开展“最美文化礼堂人”评选工作的通知.doc)11.doc", TbsPlus.eTBSPLUS_SCREENDIR.eTBSPLUS_SCREENDIR_SENSOR);
//                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent.setType("*/*");
//                            intent.addCategory(Intent.CATEGORY_OPENABLE);
//                            Intent chooser = Intent.createChooser(intent, "/storage/emulated/0/Download/三宣〔2018〕35号(关于开展“最美文化礼堂人”评选工作的通知.doc)11.doc");
//                            startActivityForResult(chooser, 1);

//                                TbsPlus.openUrl(mContext,TerminalInfo.BASE_URL + list.get(position).getUrl());

/*                            myPermission();
                            DialogUIUtils.showAlert((Activity) mContext,
                                    getResources().getString(R.string.alert_notice),
                                    "是否下载"+list.get(position).getDname()+"文件", null, null,
                                    getResources().getString(R.string.cancel),
                                    getResources().getString(R.string.confirm),
                                    false, true, true, new DialogUIListener() {
                                        @Override
                                        public void onPositive() {

                                        }
                                        @Override
                                        public void onNegative() {
                                            try {
                                                //创建下载任务,downloadUrl就是下载链接
                                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(TerminalInfo.BASE_URL+list.get(position).getUrl()));
                                                //指定下载路径和下载文件名
                                                request.setDestinationInExternalPublicDir("/download/", list.get(position).getDname());
                                                //获取下载管理器
                                                DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                                //将下载任务加入下载队列，否则不会进行下载
                                                downloadManager.enqueue(request);
                                            }catch (Exception e){
                                                Log.i("aaa",e.toString());
                                                ToastUtil.showMessage(mContext,"下载失败,开启存储权限！");
                                            }
                                        }
                                    }).show();*/
                        }
                    });
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });
        subscriptions.add(subscription);
    }

    private void search() {
        String data = etQuery.getText().toString().trim();
        System.out.println("data=========>"+data);
        if (data.equals("")){
            if (list != list1){
                list = new ArrayList<>();
                list = list1;
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }else {
            if (list1!=null){
                list = new ArrayList<>();
                for (int i = 0; i < list1.size(); i++) {
                    InformBaseBean informBaseBean = list1.get(i);
                    String ageStr = informBaseBean.getPname()+"";
                    //原理很简单,只要检索的字符,被数据库包含,即可展示出来
                    if(ageStr.contains(data)){
                        list.add(informBaseBean);
                    }
                }
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission(int position) {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED&&permission1 != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(HomeInformReportMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //提示用户这一权限的重要性
                Toast.makeText(mContext,"请开启存储权限才能访问!",
                        Toast.LENGTH_SHORT).show();
            }
            //请求权限
            ActivityCompat.requestPermissions(HomeInformReportMainActivity.this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }else{
            into(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //权限已授予
                    into(check);
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

    private void into(int position){
        Intent intent = new Intent(mContext,ShowFileWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fileUrl",list.get(position).getUrl());
        bundle.putString("fileName",list.get(position).getDname());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
