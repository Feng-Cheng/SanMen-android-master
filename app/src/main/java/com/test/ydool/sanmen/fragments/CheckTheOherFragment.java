package com.test.ydool.sanmen.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseFragment;
import com.test.ydool.sanmen.bean.OrderTypeBean;
import com.test.ydool.sanmen.bean.PonitActivityTypeBean;
import com.test.ydool.sanmen.bean.PonitListAfterBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/6/20.
 */

public class CheckTheOherFragment extends Fragment implements EasyPermissions.PermissionCallbacks{
    protected static final int PERMISSION_CODE = 100;

    @BindView(R.id.ll_report_photo)
    LinearLayout llReportPhoto;
    @BindView(R.id.ll_report_vedio)
    LinearLayout llReportVedio;
    @BindView(R.id.tv_inform_report_notice_time)
    TextView tvActivityTitle;
    @BindView(R.id.tv_chose_main_name)
    TextView tvChooseMainName;
    @BindView(R.id.ll_check_order_type)
    LinearLayout llOrderType;
    @BindView(R.id.ll_check_content)
    LinearLayout llCheckContent;
    @BindView(R.id.btn_oher_report)
    Button btnOhterReport;
    @BindView(R.id.et_inform_report_remarks)
    EditText edRemark;
    @BindView(R.id.tv_inform_report_type)
    TextView tvArea;
    @BindView(R.id.tv_inform_report_putman)
    TextView tvTime;
    @BindView(R.id.ed_content_theme)
    EditText edTheSubject;
    @BindView(R.id.tv_photo_num)
    TextView tvNum;
    @BindView(R.id.tv_vedio_name)
    TextView tvVedio;
    @BindView(R.id.et_inform_report_notice_content)
    EditText etContent;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    //    @BindView(R.id.tv_inform_report_tonotice_pepole)
//    EditText edorganizer;

    private BuildBean loading;

    private CompositeSubscription subscriptions;
    private UserRepository userRepository;
    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static int REQUEST_CODE_CHOOSE = 0;
    private static int REQUEST_CODE_CHOOSE_VEDIO = 1;
    private String selectvideoPath = "";


    public static CheckTheOherFragment newInstance(){
        return new CheckTheOherFragment();
    }

    private List<Uri> mSelected;

    private List<PonitActivityTypeBean> list;
    private List<PonitListAfterBean> list1;
    String[] word1;
    String[] word2;

    String[] levelStr = {"可选","村级","乡级","县级"};
    private int levelGenre = 0;

    int mYear;
    int mMonth;
    int mDay;
    private int genre = 0;
    private int genre1 = 0;

    private String address = "";
    private String villages = "";
    private String activity_theme  = "";
    private String content  = "";
    private String remark  = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_theohter, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(){
        SPUtils spUtils = SPUtils.getInstance("sanMen");
        tvArea.setText(spUtils.getString("pname"));
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        tvTime.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        tvLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("okokok");
                DialogUIUtils.showSingleChoose(getActivity(), "活动级别", levelGenre, levelStr, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        levelGenre = position;
                        tvLevel.setText(text);
                    }
                }).show();
            }
        });


        llOrderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils spUtils = SPUtils.getInstance("sanMen");
                Subscription subscription = userRepository.getPonitActivityTypeList(spUtils.getString("token"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response->{
                            list = new ArrayList<>();
                            if (response!=null){
                                list = response;
                                word1 = new String[list.size()];
                                for (int i = 0 ; i < list.size() ; i++){
                                    word1[i] = list.get(i).getTitle();
                                }
                                DialogUIUtils.showSingleChoose(getActivity(), "活动主题", genre, word1, new DialogUIItemListener() {
                                    @Override
                                    public void onItemClick(CharSequence text, int position) {
                                        genre=position;
                                        tvActivityTitle.setText(text);
                                        tvActivityTitle.setEnabled(false);
                                        tvChooseMainName.setText("");
                                        edTheSubject.setText("");
                                        genre1=0;
                                    }
                                }).show();
                            }
                        },throwable -> {
                            ToastUtil.showLongMessage(getActivity(),"网络异常！");
                        });
                subscriptions.add(subscription);

            }
        });

        llCheckContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvActivityTitle.getText().toString().equals("")){
                    ToastUtil.showMessage(getActivity(),"请选择活动主题");
                }else {
                    SPUtils spUtils = SPUtils.getInstance("sanMen");
                    Subscription subscription = userRepository.getOrderManageContentList(spUtils.getString("token"),list.get(genre).getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response->{
                                list1 = new ArrayList<>();
                                if (response!=null){
                                    list1 = response;
                                    word2 = new String[list1.size()];
                                    for (int i = 0 ; i < list1.size() ; i++){
                                        word2[i] = list1.get(i).getActivity_content();
                                    }
                                    DialogUIUtils.showSingleChoose(getActivity(), "选择活动", genre1, word2, new DialogUIItemListener() {
                                        @Override
                                        public void onItemClick(CharSequence text, int position) {
                                            genre1=position;
                                            tvChooseMainName.setText(text);
                                            tvChooseMainName.setEnabled(false);
                                            edTheSubject.setText(list1.get(genre1).getActivity_theme());
                                            etContent.setText(list1.get(genre1).getActivity_content());
                                        }
                                    }).show();
                                }
                            },throwable -> {
                                ToastUtil.showLongMessage(getActivity(),"网络异常！");
                            });
                    subscriptions.add(subscription);
                }
            }
        });

        btnOhterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvChooseMainName.getText().toString().equals("")){
                    ToastUtil.showMessage(getActivity(),"请选择活动再提交！");
                }else if (mSelected == null || mSelected.size()<=0){
                    ToastUtil.showMessage(getActivity(),"请选择图片再提交！");
                }else if (edTheSubject.getText().toString().equals("")){
                    ToastUtil.showMessage(getActivity(),"内容主题不能为空！");
                }else if (etContent.getText().toString().length()<50){
                    ToastUtil.showMessage(getActivity(),"活动内容最少50个字！");
                }else if(levelGenre == 0){
                    ToastUtil.showMessage(getActivity(),"请选择活动级别！");
                }else {
                    loading = DialogUIUtils.showLoading(getActivity(),"正在发布中...",true,false,false,true);
                    loading.show();
                    SPUtils spUtils = SPUtils.getInstance("sanMen");
                    String time = list1.get(genre1).getCreate_time();
                    activity_theme = list.get(genre).getTitle();
                    if (spUtils.getInt("quanxian") == 2){
                        address = spUtils.getString("auditorid");
                    }else{
                        villages = spUtils.getString("pname");
                    }
                    System.out.println();
//                    content = list1.get(genre1).getActivity_content();
                    content = etContent.getText().toString();
                    remark = edRemark.getText().toString();

                    String the_subject = edTheSubject.getText().toString();

                    Subscription subscription = userRepository.getQiNiuToken()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response ->{
                                if (response!=null){
                                    Configuration config = new Configuration.Builder()
                                            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                                            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                                            .connectTimeout(10)           // 链接超时。默认10秒
                                            .useHttps(false)               // 是否使用https上传域名
                                            .responseTimeout(60)          // 服务器响应超时。默认60秒
                                            .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
//                                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                                            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                            .build();
                                    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                                    UploadManager uploadManager = new UploadManager(config);
                                    if (selectvideoPath!=null && selectvideoPath.length()>0){
                                        File file = new File(selectvideoPath);
                                        String key = UUID.randomUUID().toString()+"."+file.getName().substring(file.getName().lastIndexOf(".") + 1);
                                        String token = response.getUptoken();
                                        uploadManager.put(file, key, token,
                                                new UpCompletionHandler() {
                                                    @Override
                                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                                                        if(info.isOK()) {
                                                            uploadFrom(spUtils.getString("token"),time,content,activity_theme,address,remark,villages,key,list.get(genre).getId(),the_subject,list1.get(genre1).getId());
                                                            Log.i("qiniu", "Upload Success");
                                                        } else {
                                                            Log.i("qiniu", "Upload Fail");
                                                            ToastUtil.showMessage(getActivity(),"上传失败！");
                                                            DialogUIUtils.dismiss(loading);
                                                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                                        }
                                                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                                    }
                                                }, null);
                                    }else {
                                        uploadFrom(spUtils.getString("token"),time,content,activity_theme,address,remark,villages,"",list.get(genre).getId(),the_subject,list1.get(genre1).getId());
                                    }
                                }else {
                                    ToastUtil.showMessage(getActivity(),"上传失败！");
                                    DialogUIUtils.dismiss(loading);
                                }
                            },throwable -> {
                                ToastUtil.showMessage(getActivity(),"上传失败！");
                                DialogUIUtils.dismiss(loading);
                            });
                    subscriptions.add(subscription);
                }
            }
        });

        llReportPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(getContext(), permissions)) {
                    Matisse.from(CheckTheOherFragment.this)
                            .choose(MimeType.of(MimeType.JPEG,MimeType.PNG)) // 选择 mime 的类型
                            .countable(true)
                            .maxSelectable(3) // 图片选择的最多数量
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.photo_big))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f) // 缩略图的比例
                            .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                            .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "请给予权限",
                            PERMISSION_CODE, permissions);
                }
            }
        });

        llReportVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(getContext(), permissions)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_VEDIO);
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "请给予权限",
                            PERMISSION_CODE, permissions);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK){
                mSelected = new ArrayList<>();
                mSelected = Matisse.obtainResult(data);
                tvNum.setText(mSelected.size()+"张图片");
                Log.d("Matisse", "mSelected: " + mSelected);
            }
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (!EasyPermissions.hasPermissions(getActivity(), permissions)) {
                //这里响应的是AppSettingsDialog点击取消按钮的效果
                getActivity().finish();
            }
        }
        if (requestCode == REQUEST_CODE_CHOOSE_VEDIO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = getActivity().getContentResolver();
                /** 数据库查询操作。
                 * 第一个参数 uri：为要查询的数据库+表的名称。
                 * 第二个参数 projection ： 要查询的列。
                 * 第三个参数 selection ： 查询的条件，相当于SQL where。
                 * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                 * 第四个参数 sortOrder ： 结果排序。
                 */
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频ID:MediaStore.Audio.Media._ID
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        // 视频名称：MediaStore.Audio.Media.TITLE
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        // 视频路径：MediaStore.Audio.Media.DATA
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        tvVedio.setText(title);
                        selectvideoPath = videoPath;
                        Log.d("CheckTheOherFragment", "selectvideoPath+++"+selectvideoPath);
                    }
                    cursor.close();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这里需要重新设置Rationale和title，否则默认是英文格式
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        } else if (!EasyPermissions.hasPermissions(getActivity(), permissions)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            getActivity().finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    /**
     * 日期选择器对话框监听
     */
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
            tvTime.setText(days);
        }
    };

    private void uploadFrom(String token,String time,String content,String activity_theme
            ,String address,String remark,String villages,String videoName,String order_title_id,String the_subject,String orderContentId){
        Subscription subscription = userRepository.saveActivityExhibition(token,time,content
                ,activity_theme,address,remark,villages,mSelected,getActivity(),videoName,order_title_id,orderContentId,the_subject,levelGenre)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
//                    ToastUtil.showMessage(getActivity(),response.getMsg());
                    if (response.getCode()==0){
                        ToastUtil.showMessage(getActivity(),"上传成功！");
                        getActivity().finish();
                    }else {
                        ToastUtil.showMessage(getActivity(),"上传失败！操作有误");
                    }
                    DialogUIUtils.dismiss(loading);
                },throwable -> {
                    ToastUtil.showMessage(getActivity(),"上传失败！操作有误");
                    DialogUIUtils.dismiss(loading);
                });
        subscriptions.add(subscription);
    }
}
