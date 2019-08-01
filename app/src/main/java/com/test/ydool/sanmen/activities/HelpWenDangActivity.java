package com.test.ydool.sanmen.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Administrator on 2018/7/26.
 *
 * 帮助文档页面
 */

public class HelpWenDangActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.ll_wenDang)
    RelativeLayout llWenDang;
    @BindView(R.id.ll_wenDang1)
    RelativeLayout llWenDang1;
    @BindView(R.id.ll_wenDang2)
    RelativeLayout llWenDang2;
    @BindView(R.id.ll_wenDang3)
    RelativeLayout llWenDang3;
    @BindView(R.id.ll_wenDang4)
    RelativeLayout llWenDang4;
    @BindView(R.id.ll_wenDang5)
    RelativeLayout llWenDang5;
    @BindView(R.id.ll_wenDang6)
    RelativeLayout llWenDang6;
    @BindView(R.id.ll_wenDang7)
    RelativeLayout llWenDang7;

    String url = "";
    private String url1  = "http://zs.ydool.com:8082/upload/instruction/三门县管理云平台web端详细功能说明书（村级管理员）.pdf";
    private String url2 = "http://zs.ydool.com:8082/upload/instruction/三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf";
    private String url3 = "http://zs.ydool.com:8082/upload/instruction/工作小结.mp4";
    private String url4 = "http://zs.ydool.com:8082/upload/instruction/活动预告.mp4";
    private String url5 = "http://zs.ydool.com:8082/upload/instruction/活动展示.mp4";
    private String url6 = "http://zs.ydool.com:8082/upload/instruction/在线建言.mp4";
    private String url7 = "http://zs.ydool.com:8082/upload/instruction/三门县管理云平台手机端详细功能说明书（村级管理员）.pdf";
    private String url8 = "http://zs.ydool.com:8082/upload/instruction/三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_help_wendang);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.help_wendang_manager);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SPUtils spUtils=SPUtils.getInstance("sanMen");
        Set<String> resList=spUtils.getStringSet("resList");
        if (resList.contains("/auditoriumInfo/auditorium/village")){
            llWenDang.setVisibility(View.VISIBLE);
            llWenDang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        } else {
/*                            DialogUIUtils.showAlert((Activity) mContext,
                                    getResources().getString(R.string.alert_notice),
                                    "是否下载三门县管理云平台web端详细功能说明书（村级管理员）.pdf文件到本地文件夹/download/下", null, null,
                                    getResources().getString(R.string.cancel),
                                    getResources().getString(R.string.confirm),
                                    false, true, true, new DialogUIListener() {
                                        @Override
                                        public void onPositive() {

                                        }
                                        @Override
                                        public void onNegative() {
                                            downLoad(url1,"三门县管理云平台web端详细功能说明书（村级管理员）.pdf");
                                        }
                                    }).show();*/
                            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（村级管理员）.pdf","三门县管理云平台web端详细功能说明书（村级管理员）.pdf");
                        }
                    }

                }
            });
            llWenDang6.setVisibility(View.VISIBLE);
            llWenDang6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 700);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台手机端详细功能说明书（村级管理员）.pdf","三门县管理云平台手机端详细功能说明书（村级管理员）.pdf");
                        }
                    }

                }
            });
        }else if (resList.contains("/auditoriumlnfo/auditorium/town")){
            llWenDang1.setVisibility(View.VISIBLE);
            llWenDang1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf","三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf");
                        }
                    }

                }
            });
            llWenDang7.setVisibility(View.VISIBLE);
            llWenDang7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 800);
                        } else {
                            intoView("三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc","三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc");
                        }
                    }

                }
            });
        }else if (resList.contains("/auditoriumInfo/auditorium/county")){
            llWenDang.setVisibility(View.VISIBLE);
            llWenDang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（村级管理员）.pdf","三门县管理云平台web端详细功能说明书（村级管理员）.pdf");
                        }
                    }

                }
            });
            llWenDang1.setVisibility(View.VISIBLE);
            llWenDang1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf","三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf");
                        }
                    }

                }
            });
            llWenDang6.setVisibility(View.VISIBLE);
            llWenDang6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 700);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台手机端详细功能说明书（村级管理员）.pdf","三门县管理云平台手机端详细功能说明书（村级管理员）.pdf");
                        }
                    }

                }
            });
            llWenDang7.setVisibility(View.VISIBLE);
            llWenDang7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //6.0才用动态权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 800);
                        } else {
                            intoView("/upload/instruction/三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc","三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc");
                        }
                    }

                }
            });
        }
        llWenDang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                    } else {
                        DialogUIUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.alert_notice),
                                "是否下载工作小结.mp4文件到本地文件夹/download/下", null, null,
                                getResources().getString(R.string.cancel),
                                getResources().getString(R.string.confirm),
                                false, true, true, new DialogUIListener() {
                                    @Override
                                    public void onPositive() {

                                    }
                                    @Override
                                    public void onNegative() {
                                        downLoad(url3,"工作小结.mp4");
                                    }
                                }).show();
                    }
                }

            }
        });
        llWenDang3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 400);
                    } else {
                        DialogUIUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.alert_notice),
                                "是否下载活动预告.mp4文件到本地文件夹/download/下", null, null,
                                getResources().getString(R.string.cancel),
                                getResources().getString(R.string.confirm),
                                false, true, true, new DialogUIListener() {
                                    @Override
                                    public void onPositive() {

                                    }
                                    @Override
                                    public void onNegative() {
                                        downLoad(url4,"活动预告.mp4");
                                    }
                                }).show();
                    }
                }

            }
        });
        llWenDang4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
                    } else {
                        DialogUIUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.alert_notice),
                                "是否下载活动展示.mp4文件到本地文件夹/download/下", null, null,
                                getResources().getString(R.string.cancel),
                                getResources().getString(R.string.confirm),
                                false, true, true, new DialogUIListener() {
                                    @Override
                                    public void onPositive() {

                                    }
                                    @Override
                                    public void onNegative() {
                                        downLoad(url5,"活动展示.mp4");
                                    }
                                }).show();
                    }
                }

            }
        });
        llWenDang5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 600);
                    } else {
                        DialogUIUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.alert_notice),
                                "是否下载在线建言.mp4文件到本地文件夹/download/下", null, null,
                                getResources().getString(R.string.cancel),
                                getResources().getString(R.string.confirm),
                                false, true, true, new DialogUIListener() {
                                    @Override
                                    public void onPositive() {

                                    }
                                    @Override
                                    public void onNegative() {
                                        downLoad(url6,"在线建言.mp4");
                                    }
                                }).show();
                    }
                }

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意了我的权限
        if (requestCode == 100 && grantResults[0] == PERMISSION_GRANTED) {
//            dialogShow(url1,"三门县管理云平台web端详细功能说明书（村级管理员）.pdf");
            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（村级管理员）.pdf","三门县管理云平台web端详细功能说明书（村级管理员）.pdf");
        }else if (requestCode == 200 && grantResults[0] == PERMISSION_GRANTED){
//            dialogShow(url2,"三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf");
            intoView("/upload/instruction/三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf","三门县管理云平台web端详细功能说明书（乡镇街道和县级管理员）_.pdf");
        }else if (requestCode == 300 && grantResults[0] == PERMISSION_GRANTED){
            dialogShow(url3,"工作小结.mp4");
        }else if (requestCode == 400 && grantResults[0] == PERMISSION_GRANTED){
            dialogShow(url4,"活动预告.mp4");
        }else if (requestCode == 500 && grantResults[0] == PERMISSION_GRANTED){
            dialogShow(url5,"活动展示.mp4");
        } else if (requestCode == 600 && grantResults[0] == PERMISSION_GRANTED){
            dialogShow(url6,"在线建言.mp4");
        }else if(requestCode == 700 && grantResults[0] == PERMISSION_GRANTED){
            intoView("/upload/instruction/三门县管理云平台手机端详细功能说明书（村级管理员）.pdf","三门县管理云平台手机端详细功能说明书（村级管理员）.pdf");
        }else if(requestCode == 800 && grantResults[0] == PERMISSION_GRANTED){
            intoView("/upload/instruction/三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc","三门县管理云平台手机端详细功能说明书（乡镇街道和县管理员）.doc");
        }else {
            ToastUtil.showMessage(mContext,"请去手机应用管理开启存储权限才能下载！");
        }
    }

    private void dialogShow(String url,String fileName){
        DialogUIUtils.showAlert((Activity) mContext,
                getResources().getString(R.string.alert_notice),
                "是否下载"+fileName+"到本地文件夹/download/下", null, null,
                getResources().getString(R.string.cancel),
                getResources().getString(R.string.confirm),
                false, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }
                    @Override
                    public void onNegative() {
                        downLoad(url,fileName);
                    }
                }).show();
    }

    private void downLoad(String url,String name){
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", name);
        //获取下载管理器
        DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    };

    private void intoView(String fileUrl,String fileName){
        Intent intent = new Intent(mContext,ShowFileWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fileUrl",fileUrl);
        bundle.putString("fileName",fileName);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}