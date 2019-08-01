package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/12.
 *
 * 礼堂风采审核
 */

public class AuditorPhotoShenShowActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.btn_report)
    Button btnReport;
    @BindView(R.id.rl_photothshow)
    RecyclerView rlPhotoMain;
    @BindView(R.id.iv_auditor_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.rl_photothshow2)
    RecyclerView rlPhotoMain2;

    private ListAdapter listAdapter=new ListAdapter(R.layout.item_auditing_photo_show);
    private ListAdapter2 listAdapter2=new ListAdapter2(R.layout.item_auditing_photo_show);

    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    protected static final int PERMISSION_CODE = 100;

    private static final int REQUEST_CODE_CHOOSE = 0;

    private static final int REQUEST_CODE_WEB = 1;

    private static final int REQUEST_CODE_BENDI = 2;

    private String delIds="";

    private UserRepository userRepository;

    private List<Uri> mSelected;

    private List<Uri> mSelected1;

    private SPUtils spUtils;

    private BuildBean loading;

    private CompositeSubscription subscriptions;

    private int checkPositin = 0;

    private int checkWebPositin = 0;

    private int selectWebPositin = 0;
    private List<AuditoriumBean.img> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_photo_show);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        userRepository=new UserRepository();
        subscriptions = new CompositeSubscription();
        tvTitle.setText(R.string.auditorium_show);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        spUtils=SPUtils.getInstance("sanMen");
        String token=spUtils.getString("token");
        String id=getIntent().getStringExtra("id");
        list= (List<AuditoriumBean.img>) getIntent().getSerializableExtra("imgList");
        loading = DialogUIUtils.showLoading(mContext,"正在提交...",true,false,false,true);
        updatePhoto(list);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivAddPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(mContext, permissions)) {
                    Matisse.from((Activity) mContext)
                            .choose(MimeType.of(MimeType.JPEG,MimeType.PNG)) // 选择 mime 的类型
                            .countable(true)
                            .maxSelectable(3) // 图片选择的最多数量
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.photo_big))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f) // 缩略图的比例
                            .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                            .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                } else {
                    EasyPermissions.requestPermissions((Activity) mContext, "请给予权限",
                            PERMISSION_CODE, permissions);
                }
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loading.show();
                if (mSelected1!=null){
                    if (mSelected==null){
                        mSelected = mSelected1;
                    }else {
                        mSelected.addAll(mSelected1);
                    }
                }
                Subscription subscription = null;
                subscription = userRepository.saveFiles(token,id,delIds,mSelected,mContext)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0){
                                ToastUtil.showLongMessage(mContext,"提交成功！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext,"提交失败！"+response.getMsg());
                                DialogUIUtils.dismiss(loading);
                            }
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                Subscription subscription2 = null;
                subscription2 = userRepository.approveAuditorium(token,id,"1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if(response.getCode()==0){
                                ToastUtil.showLongMessage(mContext,"提交成功！");
                                finish();
                            }else{
                                ToastUtil.showLongMessage(mContext,"提交失败！"+response.getMsg());
                            }
                            DialogUIUtils.dismiss(loading);
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                            DialogUIUtils.dismiss(loading);
                        });
                subscriptions.add(subscription);
                subscriptions.add(subscription2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_CHOOSE:
                    mSelected = new ArrayList<>();
                    mSelected = Matisse.obtainResult(data);
                    listAdapter2.setNewData(mSelected);
                    rlPhotoMain2.setLayoutManager(new LinearLayoutManager(mContext));
                    rlPhotoMain2.setAdapter(listAdapter2);
                    listAdapter2.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()){
                                case R.id.iv_auding_auditor_photo_del:
                                    mSelected.remove(position);
                                    listAdapter2.setNewData(mSelected);
                                    rlPhotoMain2.setAdapter(listAdapter2);
                                    break;
                                case R.id.iv_auding_auditor_photo:
                                    checkPositin = position;
                                    startUrcop(mSelected.get(position),REQUEST_CODE_BENDI);
                                    break;
                            }
                        }
                    });
                    break;
                case REQUEST_CODE_WEB:
                    Uri reslutUri1 = UCrop.getOutput(data);
                    AuditoriumBean.img img = list.get(checkWebPositin);
                    img.setUrl(reslutUri1.getPath().toString());
                    if (!delIds.contains(list.get(checkPositin).getId())){
                        if (mSelected1==null){
                            mSelected1 = new ArrayList<>();
                        }
                        mSelected1.add(reslutUri1);
                        delIds+=(","+list.get(checkPositin).getId());
                    }
                    list.set(checkWebPositin,img);
                    listAdapter.setNewData(list);
                    rlPhotoMain.setLayoutManager(new LinearLayoutManager(mContext));
                    rlPhotoMain.setAdapter(listAdapter);
                    listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()){
                                case R.id.iv_auding_auditor_photo_del:
                                    delIds+=(","+list.get(position).getId());
                                    list.remove(position);
                                    listAdapter.setNewData(list);
                                    rlPhotoMain.setAdapter(listAdapter);
                                    break;
                                case R.id.iv_auding_auditor_photo:
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkWebPositin = position;
                                            if (list.get(position).getUrl().contains("/upload/Auditorium/")){
                                                new getImageCacheAsyncTask(mContext).execute(TerminalInfo.BASE_URL+list.get(position).getUrl());
                                            }else {
                                                startUrcop(Uri.fromFile(new File(list.get(position).getUrl())),REQUEST_CODE_WEB);
                                            }
                                        }
                                    }).start();
                                    break;
                            }
                        }
                    });
                    break;
                case REQUEST_CODE_BENDI:
                    Uri reslutUri = UCrop.getOutput(data);
                    Log.d("requestUCrop",reslutUri.toString());
                    mSelected.set(checkPositin,reslutUri);
                    listAdapter2.setNewData(mSelected);
                    rlPhotoMain2.setLayoutManager(new LinearLayoutManager(mContext));
                    rlPhotoMain2.setAdapter(listAdapter2);
                    listAdapter2.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()){
                                case R.id.iv_auding_auditor_photo_del:
                                    mSelected.remove(position);
                                    listAdapter2.setNewData(mSelected);
                                    rlPhotoMain2.setAdapter(listAdapter2);
                                    break;
                                case R.id.iv_auding_auditor_photo:
                                    checkPositin = position;
                                    startUrcop(mSelected.get(position),REQUEST_CODE_BENDI);
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }

    private void updatePhoto(List<AuditoriumBean.img> list){
        listAdapter.setNewData(list);
        rlPhotoMain.setLayoutManager(new LinearLayoutManager(mContext));
        rlPhotoMain.setAdapter(listAdapter);
        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_auding_auditor_photo_del:
                        delIds+=(","+list.get(position).getId());
                        list.remove(position);
                        listAdapter.setNewData(list);
                        rlPhotoMain.setAdapter(listAdapter);
                        break;
                    case R.id.iv_auding_auditor_photo:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                checkWebPositin = position;
                                new getImageCacheAsyncTask(mContext).execute(TerminalInfo.BASE_URL+list.get(position).getUrl());
                            }
                        }).start();
                        break;
                }
            }
        });
    }

    class ListAdapter extends BaseQuickAdapter<AuditoriumBean.img,BaseViewHolder> {

        public ListAdapter(int layoutResId) {
            super(layoutResId);
        }
        @Override
        protected void convert(BaseViewHolder helper, AuditoriumBean.img item) {
            if (item.getUrl().contains("/upload/Auditorium/")){
                Glide.with(mContext)
                        .load(TerminalInfo.BASE_URL+item.getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade().into((ImageView) helper.getView(R.id.iv_auding_auditor_photo));
            }else {
                Glide.with(mContext).load(Uri.fromFile(new File(item.getUrl()))).into((ImageView) helper.getView(R.id.iv_auding_auditor_photo));
            }
            helper.addOnClickListener(R.id.iv_auding_auditor_photo);
            helper.addOnClickListener(R.id.iv_auding_auditor_photo_del);
        }
    }

    class ListAdapter2 extends BaseQuickAdapter<Uri,BaseViewHolder>{

        public ListAdapter2(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Uri item) {
            Glide.with(mContext)
                    .load(item)
                    .into((ImageView) helper.getView(R.id.iv_auding_auditor_photo));
            helper.addOnClickListener(R.id.iv_auding_auditor_photo);
            helper.addOnClickListener(R.id.iv_auding_auditor_photo_del);
        }
    }

    private void startUrcop(Uri uri,int REQUESTCODE){
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(),System.currentTimeMillis()+".jpg"));
        UCrop uCrop = UCrop.of(uri,destinationUri);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.NONE,UCropActivity.ALL,UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mContext, R.color.colorPrimaryDark));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mContext, R.color.colorPrimaryDark));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);

        uCrop.withOptions(options);
        uCrop.start((Activity) mContext,REQUESTCODE);
    }

    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Log.e("path", path);
            startUrcop(Uri.fromFile(result),REQUEST_CODE_WEB);
        }
    }
}
