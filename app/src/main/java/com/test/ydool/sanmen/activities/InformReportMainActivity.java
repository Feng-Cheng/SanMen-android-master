package com.test.ydool.sanmen.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.adapter.InformPutBaseAdapter;
import com.test.ydool.sanmen.adapter.WorkStatisticsAdapter;
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

/**
 * Created by Administrator on 2018/6/4.
 *
 * 信息发布列表
 */

public class InformReportMainActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_title_all)
    TextView tvTitleAll;
    @BindView(R.id.rl_inform_main)
    RecyclerView rlInformMain;
    @BindView(R.id.query_et)
    EditText etQuery;
    @BindView(R.id.btn_inform_report_search)
    Button btnSearch;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private SPUtils spUtils;

    private InformPutBaseAdapter mAdapter;

    private List<InformBaseBean> list;

    private List<InformBaseBean> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_inform_report_main);
        ButterKnife.bind(this);

        initView();
    }



    private void initView() {
        tvTitle.setText(R.string.inform_put);
        ivMenu.setImageResource(R.drawable.ic_back);
        tvTitle.setTextColor(0xfffaa8ae);
        subscriptions = new CompositeSubscription();
        userRepository = new UserRepository();
        spUtils = SPUtils.getInstance("sanMen");

        String token = spUtils.getString("token");
        if (spUtils.getInt("quanxian") == 1 || spUtils.getInt("quanxian") == 0){
            tvTitleAll.setText(R.string.put);
            tvTitleAll.setTextColor(0xfffaa8ae);
        }else {
            tvTitleAll.setVisibility(View.INVISIBLE);
        }
        updateData(token);


        tvTitleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),InformReportActivity.class);
                startActivity(intent);
            }
        });

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


    }

    private void updateData(String token ){
        Subscription subscription = userRepository.getInformMainList(token)
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
                                            //创建下载任务,downloadUrl就是下载链接
                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(TerminalInfo.BASE_URL+list.get(position).getUrl()));
                                            //指定下载路径和下载文件名
                                            request.setDestinationInExternalPublicDir("/download/", list.get(position).getDname());
                                            //获取下载管理器
                                            DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                            //将下载任务加入下载队列，否则不会进行下载
                                            downloadManager.enqueue(request);
                                        }
                                    }).show();
                        }
                    });
                },throwable -> {
                    ToastUtil.showLongMessage(mContext,"网络异常！");
                });

        subscriptions.add(subscription);
    }

    private void search() {
        String data = etQuery.getText().toString().trim();
        System.out.println("data=========>"+data);
        list=new ArrayList<>();
        if (data == ""){
            list = list1;
        }else {
            if (list1!=null){
                for (int i = 0; i < list1.size(); i++) {
                    InformBaseBean informBaseBean = list1.get(i);
                    String ageStr = informBaseBean.getPname()+"";
                    //原理很简单,只要检索的字符,被数据库包含,即可展示出来
                    if(ageStr.contains(data)){
                        list.add(informBaseBean);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


}
