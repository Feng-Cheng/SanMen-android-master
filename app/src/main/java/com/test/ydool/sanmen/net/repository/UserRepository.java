package com.test.ydool.sanmen.net.repository;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import com.test.ydool.sanmen.bean.ActivityExhibitionBean;
import com.test.ydool.sanmen.bean.ActivityPreviewBean;
import com.test.ydool.sanmen.bean.AdminBean;
import com.test.ydool.sanmen.bean.AuditoriaAdminBean;
import com.test.ydool.sanmen.bean.AuditoriumBean;
import com.test.ydool.sanmen.bean.DayNewsBean;
import com.test.ydool.sanmen.bean.DaySignBean;
import com.test.ydool.sanmen.bean.FlowStatisticsBean;
import com.test.ydool.sanmen.bean.HomePhotoBean;
import com.test.ydool.sanmen.bean.HttpResponse;
import com.test.ydool.sanmen.bean.HttpResponseBean;
import com.test.ydool.sanmen.bean.InformBaseBean;
import com.test.ydool.sanmen.bean.NewsBean;
import com.test.ydool.sanmen.bean.NotificationBean;
import com.test.ydool.sanmen.bean.NumBean;
import com.test.ydool.sanmen.bean.OnlineAdviceBean;
import com.test.ydool.sanmen.bean.OrderMealBean;
import com.test.ydool.sanmen.bean.OrderTypeBean;
import com.test.ydool.sanmen.bean.PointActivityMainBean;
import com.test.ydool.sanmen.bean.PonitActivityTypeBean;
import com.test.ydool.sanmen.bean.PonitListAfterBean;
import com.test.ydool.sanmen.bean.PonitPeopleBean;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.ScoreBean;
import com.test.ydool.sanmen.bean.ShareBean;
import com.test.ydool.sanmen.bean.ShareHuifuBean;
import com.test.ydool.sanmen.bean.UptokenBean;
import com.test.ydool.sanmen.bean.UrlBean;
import com.test.ydool.sanmen.bean.VedioBean;
import com.test.ydool.sanmen.bean.Version;
import com.test.ydool.sanmen.bean.VideoCaremerBean;
import com.test.ydool.sanmen.bean.VideoToPeoBean;
import com.test.ydool.sanmen.bean.WenDangBean;
import com.test.ydool.sanmen.bean.WorkBaseBean;
import com.test.ydool.sanmen.bean.WorkStatisticsBean;
import com.test.ydool.sanmen.net.DataLayer;
import com.test.ydool.sanmen.net.RxService;
import com.test.ydool.sanmen.net.api.UserApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2018/4/19.
 */

public class UserRepository implements DataLayer.UserService {
    private static final String AREA="331022000000";

    private UserApi userApi;

    public UserRepository() {
        userApi = RxService.createApi(UserApi.class);
    }


    @Override
    public Observable<HttpResponse<AdminBean>> loginCheck(String username, String password) {
        return userApi.LoginCheck(username,password);
    }

    @Override
    public Observable<HttpResponse> signDay(String userId, String year, String month, String day) {
        return userApi.signDay(userId,year,month,day);
    }

    @Override
    public Observable<HttpResponse> getListData(String name) {
        return userApi.getListData(name);
    }

    @Override
    public Observable<PositionRootBean> getPositionRoot() {
        return userApi.getPositionRoot(AREA);
    }

    @Override
    public Observable<PositionRootBean> getPositionRoot(String pid) {
        return userApi.getPositionRoot(pid);
    }

    @Override
    public Observable<AuditoriumBean> getAuditorium(String poid,String token) {
        return userApi.getAuditorium(poid,token);
    }



    @Override
    public Observable<HttpResponse> adsaveAuditorium(String id,String name,String town,String village,String address,String lishihui
            ,String wentiduiwu,String proportion,String build_year,String admin_id,String build_introduce,String ps
            ,String survey,String status, String token, List<Uri> path, Context context,String ids) {

        List<String> imgPath = new ArrayList<>();
        List<File> files = new ArrayList<>();

        if (path!=null){
            for (int i = 0 ; i< path.size() ; i++){
                if (path.get(i)!=null)
                    imgPath.add(getPath(context,path.get(i)));
            }
            for (int i = 0; i< imgPath.size();i++){
                try {
//            file = new File(new URI(path.toString()));
//                file = new File(path);
                    files.add(new File(imgPath.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        if (files.size()>0){
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        }else{
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),"");
            builder.addFormDataPart("file", "", requestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("bean.id", RequestBody.create(null, id));
        map.put("bean.name", RequestBody.create(null,name));
        map.put("bean.address", RequestBody.create(null, address));
        map.put("bean.lishihui", RequestBody.create(null, lishihui));
        map.put("bean.wentiduiwu", RequestBody.create(null, wentiduiwu));
        map.put("bean.proportion", RequestBody.create(null, proportion));
        map.put("bean.build_year", RequestBody.create(null, build_year));
        map.put("bean.admin_id", RequestBody.create(null, admin_id));
        map.put("bean.build_introduce", RequestBody.create(null, build_introduce));
        map.put("bean.ps", RequestBody.create(null, ps));
        map.put("bean.survey", RequestBody.create(null, survey));
        map.put("bean.status", RequestBody.create(null, status));


//        return userApi.adSave(id,name,AREA,town,village,address,lishihui,wentiduiwu
//                    ,proportion,build_year,admin_id,build_introduce
//                    ,ps,survey,status,token,ids,parts);
        return userApi.adSave(AREA,town,village,map,token,ids,parts);
    }

    @Override
    public Observable<HttpResponse> saveSign(String userId,String token) {
        return userApi.saveSign(userId,token);
    }

    @Override
    public Observable<HttpResponse<List<WorkBaseBean>>> getWorkListData(String token) {
        return userApi.getWorkListData(token,"id","desc");
    }

    @Override
    public Observable<HttpResponse> saveWorkReport(String token, String name, String time, String summary, String issue, String solution, String activity_describe, String remark) {
        return userApi.saveWorkReport(token,name,time,summary,issue,solution,activity_describe,remark);
    }

    @Override
    public Observable<HttpResponse<List<DaySignBean>>> getSignList(String token) {
        return userApi.getSignList(token);
    }

    @Override
    public Observable<HttpResponse<List<FlowStatisticsBean>>> getFlowStatisticsList(String token) {
        return userApi.getFlowStatisticsList(token);
    }

    @Override
    public Observable<HttpResponseBean<List<WorkStatisticsBean>>> getWorkStatisticsListData(String token,int genre,String area) {
        return userApi.getWorkStatisticsListData(token,genre,area);
    }

    @Override
    public Observable<List<InformBaseBean>> getInformMainList(String token) {
        return userApi.getInformMainList(token);
    }

    @Override
    public Observable<HttpResponse> InformReport(String token, String title, String detail, int type, String remarks,Uri path,Context context,String names) {
        String sPath1 = null;
        File file=null;
        if ( null != path ) {
            sPath1 = getPath(context, path);
            file = new File(sPath1);
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        if (file!=null){
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),"");
            builder.addFormDataPart("file", "", requestBody);
        }

        MultipartBody.Part parts = builder.build().part(0);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("title", RequestBody.create(null, title));
        map.put("detail", RequestBody.create(null,detail));
        map.put("type", RequestBody.create(null, String.valueOf(type)));
        map.put("remarks", RequestBody.create(null, remarks));
        map.put("names",RequestBody.create(null,names));

        return userApi.InformReport(token,map,parts);
    }

    @Override
    public Observable<HttpResponseBean<List<AuditoriumBean>>> getAuditoriumShenInfoList(String token) {
        return userApi.getAuditoriumShenInfoList(token);
    }

    @Override
    public Observable<List<ActivityPreviewBean>> getActivityPreviewAccepterList(String token) {
        return userApi.getActivityPreviewAccepterList(token);
    }

    @Override
    public Observable<List<ActivityPreviewBean>> getActivityPreviewNoPassList(String token) {
        return userApi.getActivityPreviewNoPassList(token);
    }

    @Override
    public Observable<List<NewsBean>> getNewsList(String token) {
        return userApi.getNewsList(token);
    }

    @Override
    public Observable<List<String>> getHomePhotoList() {
        return userApi.getHomePhotoList();
    }

    @Override
    public Observable<List<ActivityPreviewBean>> getpreviewList() {
        return userApi.getpreviewList();
    }

    @Override
    public Observable<HttpResponse> ActivityPreViewadSave(String token, String aud, String town, String title, String detail, String delIds, String date, String organizer, String remarks,List<Uri> path,Context context) {

        List<String> imgPath = new ArrayList<>();
        List<File> files = new ArrayList<>();

        if (path!=null){
            for (int i = 0 ; i< path.size() ; i++){
                if (path.get(i)!=null)
                    imgPath.add(getPath(context,path.get(i)));
            }
            for (int i = 0; i< imgPath.size();i++){
                try {
//            file = new File(new URI(path.toString()));
//                file = new File(path);
                    files.add(new File(imgPath.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        if (files.size()>0){
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        }else{
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),"");
            builder.addFormDataPart("file", "", requestBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();

        Map<String, RequestBody> map = new HashMap<>();
        map.put("title", RequestBody.create(null, title));
        map.put("detail", RequestBody.create(null,detail));
        map.put("organizer", RequestBody.create(null, organizer));
        map.put("remarks", RequestBody.create(null, remarks));

        return userApi.ActivityPreViewadSave(map,token,aud,town,delIds,date,parts);
    }

    @Override
    public Observable<List<PonitActivityTypeBean>> getPonitActivityTypeList(String token) {
        return userApi.getPonitActivityTypeList();
    }

    @Override
    public Observable<HttpResponseBean<List<PointActivityMainBean>>> getPonitActivityMainList(String token, String titleId) {
        return userApi.getPonitActivityMainList(token,titleId);
    }

    @Override
    public Observable<HttpResponseBean<List<PointActivityMainBean>>> getHomePonitActivityMainList(String titleId) {
        return userApi.getHomePonitActivityMainList(titleId);
    }

    @Override
    public Observable<HttpResponse> adSaveOrderMeall(String token, String titleId,String orderContentId) {
        return userApi.adSaveOrderMeall(token,titleId,orderContentId);
    }

    @Override
    public Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(String token) {
        return userApi.getOnlineAdviceList(token,"desc","create_time");
    }

    @Override
    public Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(String token, String town, String village) {
        return userApi.getOnlineAdviceList(token,town,village,"desc","create_time");
    }

    @Override
    public Observable<HttpResponseBean<List<WenDangBean>>> getWenDangList(String token) {
        return userApi.getWenDangList(token,"desc","time");
    }

    @Override
    public Observable<List<ShareBean>> getHomeShareList() {
        return userApi.getHomeShareList();
    }

    @Override
    public Observable<List<ShareHuifuBean>> getHomeReplyShareList(String sid) {
        return userApi.getHomeReplyShareList(sid);
    }

    @Override
    public Observable<HttpResponse> replyZiyuan(String token, String sid, String detail) {
        return userApi.replyZiyuan(token,sid,detail);
    }

    @Override
    public Observable<List<UrlBean>> getAnnocePicture() {
        return userApi.getAnnocePicture();
    }

    @Override
    public Observable<HttpResponse> saveaReportve(String token, String user_name, String position_id, String time, String content, Uri uri,Context context) {
        String sPath1 = null;
        if ( null != uri ) {
            sPath1 = getPath(context, uri);
        }
        File file = new File(sPath1);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        builder.addFormDataPart("file", file.getName(), requestBody);

        MultipartBody.Part parts = builder.build().part(0);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("bean.user_name", RequestBody.create(null, user_name));
        map.put("bean.position_id", RequestBody.create(null, position_id));
        map.put("bean.time", RequestBody.create(null,time));
        map.put("bean.content", RequestBody.create(null, content));

        return userApi.saveaReportve(token,map,parts);
    }

    @Override
    public Observable<List<InformBaseBean>> getAnnoListNoLogin() {
        return userApi.getAnnoListNoLogin();
    }

    @Override
    public Observable<HttpResponseBean<List<ActivityExhibitionBean>>> getExhibitionList(String token) {
        return userApi.getExhibitionList(token);
    }

    @Override
    public Observable<List<ActivityExhibitionBean>> getActivityListDataNotAudit(String token) {
        return userApi.getActivityListDataNotAudit(token);
    }

    @Override
    public Observable<List<VedioBean>> getViedos() {
        return userApi.getViedos();
    }

    @Override
    public Observable<List<PonitListAfterBean>> getHomePintAfterList(String titleId) {
        return userApi.getHomePintAfterList(titleId);
    }



    @Override
    public Observable<HttpResponse> saveOnlineAdvice(String token, String content,String area, String town, String village) {
        return userApi.saveOnlineAdvice(token,content,area,town,village);
    }

    @Override
    public Observable<HttpResponse> savePwdUpdate(String token, String pwd) {
        return userApi.savePwdUpdate(token,pwd);
    }

    @Override
    public Observable<List<OrderTypeBean>> getOrderTypeList(String token) {
        return userApi.getOrderTypeList(token);
    }

    @Override
    public Observable<List<PonitListAfterBean>> getOrderManageContentList(String token, String titleId) {
        return userApi.getOrderManageContentList(token,titleId);
    }

    @Override
    public Observable<List<ActivityExhibitionBean>> getExhibitionNoAccepterList(String token) {
        return userApi.getExhibitionNoAccepterList(token);
    }

    @Override
    public Observable<List<ActivityExhibitionBean>> getHomeExhibitionList(String positionId) {
        return userApi.getHomeExhibitionList(positionId);
    }

    @Override
    public Observable<HttpResponse> saveFiles(String token, String id, String delIds, List<Uri> path, Context context) {
        List<String> imgPath = new ArrayList<>();
        List<File> files = new ArrayList<>();

        if (path!=null){
            for (int i = 0 ; i< path.size() ; i++){
                if (path.get(i)!=null)
                    imgPath.add(getPath(context,path.get(i)));
            }
            for (int i = 0; i< imgPath.size();i++){
                try {
//            file = new File(new URI(path.toString()));
//                file = new File(path);
                    files.add(new File(imgPath.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        if (files.size()>0){
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        }else{
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),"");
            builder.addFormDataPart("file", "", requestBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return userApi.saveFiles(token,id, delIds,parts);
    }

    @Override
    public Observable<HttpResponse> approveAuditorium(String token, String id, String status) {
        return userApi.approveAuditorium(token,id,status);
    }

    @Override
    public Observable<HttpResponse> saveUser(String name, String adminName, String password, String town, String village,String phoneCode) {
        return userApi.saveUser(name,adminName,password,AREA,town,village,phoneCode);
    }

    @Override
    public Observable<List<AuditoriaAdminBean>> getLocalAdmin(String village,String token) {
        return userApi.getLocalAdmin(village,token);
    }

    @Override
    public Observable<String> getNotifiSize(String token) {
        return userApi.getNotifiSize(token);
    }

    @Override
    public Observable<NotificationBean> getNotifiMain(String token) {
        return userApi.getNotifiMain(token);
    }

    @Override
    public Observable<HttpResponse> getPhoneCode(String phone, String type) {
        return userApi.getPhoneCode(phone,type);
    }

    @Override
    public Observable<HttpResponse> findPwd(String phone, String phoneCode, String pwd) {
        return userApi.findPwd(phone,phoneCode,pwd);
    }

    @Override
    public Observable<List<DayNewsBean>> getEverydayActivtes(int start,int length) {
        return userApi.getEverydayActivtes();
    }

    @Override
    public Observable<List<DayNewsBean>> getEverydayActivtesTest(int start, int length) {
        return userApi.getEverydayActivtesTest(start,length);
    }

    @Override
    public Observable<HttpResponse> passActivityPreview(String token,String id) {
        return userApi.passActivityPreview(token,id);
    }

    @Override
    public Observable<HttpResponse> noPassActivityPreview(String token,String id) {
        return userApi.noPassActivityPreview(token,id);
    }

    @Override
    public Observable<List<OrderMealBean>> getOrderMeal() {
        return userApi.getOrderMeal();
    }

    @Override
    public Observable<HttpResponseBean<List<PonitPeopleBean>>> getOrderMealPermissionsList(String token) {
        return userApi.getOrderMealPermissionsList(token);
    }

    @Override
    public Observable<HttpResponse> upPeopleDataStatus(String token, String permissions, String ids) {
        return userApi.upPeopleDataStatus(token,permissions,ids);
    }

    @Override
    public Observable<NumBean> getOrderMealIsNum(String token, String titleId) {
        return userApi.getOrderMealIsNum(token,titleId);
    }

    @Override
    public Observable<PonitListAfterBean> getOrderMealTheThemeIsNum(String token, String contentId) {
        return userApi.getOrderMealTheThemeIsNum(token,contentId);
    }

    @Override
    public Observable<List<HomePhotoBean>> getDayHotPhotoBean() {
        return userApi.getDayHotPhotoBean();
    }

    @Override
    public Observable<HttpResponse> postActivityExhibitionPass(String token, String id) {
        return userApi.postActivityExhibitionPass(token,id);
    }

    @Override
    public Observable<HttpResponse> postActivityExhibitionNoPass(String token, String id) {
        return userApi.postActivityExhibitionNoPass(token,id);
    }

    @Override
    public Observable<Version> getAppVersionCode() {
        return userApi.getAppVersionCode();
    }

    @Override
    public Observable<UptokenBean> getQiNiuToken() {
        return userApi.getQiNiuToken();
    }

    @Override
    public Observable<String> getModuleOpen() {
        return userApi.getModuleOpen();
    }

    @Override
    public Observable<VideoCaremerBean> findMonitors() {
        return userApi.findMonitors();
    }

    @Override
    public Observable<List<ScoreBean>> getScoreOrder(String token) {
        return userApi.getScoreOrder(token);
    }

    @Override
    public Observable<HttpResponse> approveOnlineAdvice(String token, String id) {
        return userApi.approveOnlineAdvice(token,id);
    }

    @Override
    public Observable<HttpResponse> rejectOnlineAdvice(String token, String id) {
        return userApi.rejectOnlineAdvice(token,id);
    }
    @Override
    public Observable<HttpResponse> saveActivityExhibition(String token,String time,String content,String activity_theme
            ,String address,String remark,String villages,List<Uri> path,Context context,String VedioPath
            ,String order_title_id,String order_content_id,String the_subject,int level) {
        List<String> imgPath = new ArrayList<>();
        Cursor cursor = null;
        List<File> files = new ArrayList<>();

        if (path!=null){
            for (int i = 0 ; i< path.size() ; i++){
                if (path.get(i)!=null)
                    imgPath.add(getPath(context,path.get(i)));
            }
            for (int i = 0; i< imgPath.size();i++){
                try {
                    files.add(new File(imgPath.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        if (!VedioPath.equals("")){
//            files.add(new File(VedioPath));
//        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型

        if (files.size()>0){
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        }else{
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),"");
            builder.addFormDataPart("file", "", requestBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();

        Map<String, RequestBody> map = new HashMap<>();
        map.put("time", RequestBody.create(null, time));
        map.put("activity_theme", RequestBody.create(null, activity_theme));
        map.put("address", RequestBody.create(null,address));
        map.put("content", RequestBody.create(null, content));
        map.put("remark", RequestBody.create(null, remark));
        map.put("villages", RequestBody.create(null, villages));
        map.put("order_title_id", RequestBody.create(null, order_title_id));
        map.put("order_content_id", RequestBody.create(null, order_content_id));
        map.put("the_subject", RequestBody.create(null, the_subject));
        map.put("VedioPath", RequestBody.create(null, VedioPath));
        map.put("level", RequestBody.create(null, level+""));

        return userApi.saveActivityExhibition(token,map,parts);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
