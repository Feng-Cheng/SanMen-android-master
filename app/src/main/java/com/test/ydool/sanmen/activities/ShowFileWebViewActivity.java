package com.test.ydool.sanmen.activities;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.TbsReaderView;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/21.
 *
 * 查看文档页面
 */

public class ShowFileWebViewActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    private static final String TAG = "ShowFileWebViewActivity";

//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_menu)
//    ImageView ivMenu;
    @BindView(R.id.rl_root)
    RelativeLayout rootRl;
    @BindView(R.id.tv_show_down)
    TextView tvShowDown;

    private TbsReaderView mTbsReaderView;
    private DownloadObserver mDownloadObserver;
    private DownloadManager mDownloadManager;
    private String mFileUrl;
    private String mFileName;
    private long mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_show_file_webview);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
//        ivMenu.setImageResource(R.drawable.ic_back);
//        ivMenu.setOnClickListener(v -> finish());
        String[] s = getIntent().getExtras().getString("fileUrl").split("/");
        String a = s[s.length-1];
        String b = null;
        try {
            b = URLEncoder.encode(a,"utf-8");
            b = b.replaceAll("\\+",  "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mFileUrl = TerminalInfo.BASE_URL +"/" + s[1] + "/" + s[2] + "/" + b;
//        mFileUrl = TerminalInfo.BASE_URL+getIntent().getExtras().getString("fileUrl");
        mFileName = getIntent().getExtras().getString("fileName");

        mTbsReaderView = new TbsReaderView(this, this);

//        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        rl.setMargins(0,88,0,0);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        onShow();
    }
    private void onShow(){
        if (isLocalExist()) {
            tvShowDown.setVisibility(View.GONE);
            displayFile();
        } else {
            startDownload();
        }
    }

    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        ToastUtil.showMessage(mContext,getLocalFile().getPath());
        boolean result = mTbsReaderView.preOpen(parseFormat(mFileName), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    private boolean isLocalExist() {
        return getLocalFile().exists();
    }

    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName);
    }

    private void startDownload() {
        mDownloadObserver = new DownloadObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownloadObserver);

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mFileUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mFileName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        mRequestId = mDownloadManager.enqueue(request);
    }
    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mRequestId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载的字节数
                int currentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                Log.i("downloadUpdate: ", currentBytes + " " + totalBytes + " " + status);
                tvShowDown.setText("正在下载：" + currentBytes + "/" + totalBytes);
                if (DownloadManager.STATUS_SUCCESSFUL == status && tvShowDown.getVisibility() == View.VISIBLE) {
                    tvShowDown.setVisibility(View.GONE);
                    onShow();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
        if (mDownloadObserver != null) {
            getContentResolver().unregisterContentObserver(mDownloadObserver);
        }
    }


    private class DownloadObserver extends ContentObserver {

        private DownloadObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.i("downloadUpdate: ", "onChange(boolean selfChange, Uri uri)");
            queryDownloadStatus();
        }
    }
}
