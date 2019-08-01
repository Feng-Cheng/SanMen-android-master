package com.test.ydool.sanmen.net.api;


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

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/4/19.
 */

public interface UserApi {

    @FormUrlEncoded
    @POST("/dologin")
    Observable<HttpResponse<AdminBean>> LoginCheck(
            @Field("username") String username,
            @Field("password") String password);

    @GET("/management/work/getSignlist")
    Observable<HttpResponse> signDay(
            @Query("userId") String userId,
            @Query("year") String year,
            @Query("month") String month,
            @Query("day") String day);

    @GET("/auditoriumInfo/auditorium/getListData")
    Observable<HttpResponse> getListData(
            @Query("name") String name);

    @GET("/data/position/getPositionRoot")
    Observable<PositionRootBean> getPositionRoot(
            @Query("pid") String pid);

    @GET("/auditoriumInfo/auditorium/getInfo")
    Observable<AuditoriumBean> getAuditorium(
            @Query("poid") String poid,
            @Query("token") String token);

    @Multipart
    @POST("/auditoriumInfo/auditorium/save")
    Observable<HttpResponse> adSave(@Query("area") String area, @Query("town") String town
            , @Query("village") String village,@PartMap Map<String, RequestBody> params, @Query("token") String token,@Query("ids")
            String ids, @Part List<MultipartBody.Part> file);

    @POST("/saveSign")
    Observable<HttpResponse> saveSign(
            @Query("userId") String userId,
            @Query("token") String token);

    @GET("/management/work/getListData")
    Observable<HttpResponse<List<WorkBaseBean>>> getWorkListData(
            @Query("token") String token,
            @Query("sidx") String sidx,
            @Query("sord") String sord);

    @GET("/info/anno")
    Observable<HttpResponse> getInformPutListData(
            @Query("token") String token);

    @POST("/management/work/savePhone")
    Observable<HttpResponse> saveWorkReport(
            @Query("token") String token,
            @Query("name") String name,
            @Query("time") String time,
            @Query("summary") String summary,
            @Query("issue") String issue,
            @Query("solution") String solution,
            @Query("activity_describe") String activity_describe,
            @Query("remark") String remark);


    @GET("/getSignAll")
    Observable<HttpResponse<List<DaySignBean>>> getSignList(
            @Query("token") String token);

    @GET("/statistics/flowStatistics/getListData")
    Observable<HttpResponse<List<FlowStatisticsBean>>> getFlowStatisticsList(
            @Query("token") String token);

    @GET("/statistics/rating/getListData")
    Observable<HttpResponseBean<List<WorkStatisticsBean>>> getWorkStatisticsListData(
            @Query("token") String token,
            @Query("genre") int genre,
            @Query("area") String area);

    @GET("/info/anno/androidList")
    Observable<List<InformBaseBean>> getInformMainList(
            @Query("token") String token);


    @GET("/auditoriumInfo/auditorium/getListData")
    Observable<HttpResponseBean<List<AuditoriumBean>>> getAuditoriumShenInfoList(
            @Query("token") String token);

    @Multipart
    @POST("/info/anno/saveAd")
    Observable<HttpResponse> InformReport(
            @Query("token") String token,
            @PartMap Map<String, RequestBody> params,
            @Part MultipartBody.Part file);

    @GET("/activity/preview/androidList")
    Observable<List<ActivityPreviewBean>> getActivityPreviewAccepterList(
            @Query("token") String token);

    @GET("/activity/preview/androidListNoPass")
    Observable<List<ActivityPreviewBean>> getActivityPreviewNoPassList(
            @Query("token") String token);

    @Multipart
    @POST("/activity/preview/adSave")
    Observable<HttpResponse> ActivityPreViewadSave(
            @PartMap Map<String, RequestBody> params,
            @Query("token") String token,
            @Query("aud") String aud,
            @Query("town") String town,
            @Query("detail") String detail,
            @Query("date") String date,
            @Part List<MultipartBody.Part> file);

    @GET("/getProcessor")
    Observable<List<NewsBean>> getNewsList(
            @Query("token") String token);

    @GET("/auditoriumInfo/auditorium/getShowImg")
    Observable<List<String>> getHomePhotoList();

    @GET("/activity/preview/previewList")
    Observable<List<ActivityPreviewBean>> getpreviewList();

    @GET("/ordermeal/manage/getTitle")
    Observable<List<PonitActivityTypeBean>> getPonitActivityTypeList();

    @GET("/ordermeal/manageInfo/getListData")
    Observable<HttpResponseBean<List<PointActivityMainBean>>> getPonitActivityMainList(
            @Query("token") String token,
            @Query("titleId") String titleId);

    @POST("/ordermeal/manage/savePhone")
    Observable<HttpResponse> adSaveOrderMeall(
            @Query("token") String token,
            @Query("titleId") String titleId,
            @Query("orderContentId") String orderContentId);


    @GET("/state/state/getListData")
    Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(
            @Query("token") String token,
            @Query("sord") String sord,
            @Query("sidx") String sidx);

    @GET("/state/state/getListData")
    Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(
            @Query("token") String token,
            @Query("town")String town,
            @Query("village")String village,
            @Query("sord") String sord,
            @Query("sidx") String sidx);

    @GET("/report/report/getListData")
    Observable<HttpResponseBean<List<WenDangBean>>> getWenDangList(
            @Query("token") String token,
            @Query("sord") String sord,
            @Query("sidx") String sidx);

    @GET("/activity/share/androidList")
    Observable<List<ShareBean>> getHomeShareList();

    @GET("/activity/preview/picture")
    Observable<List<UrlBean>> getAnnocePicture();


    @GET("/activity/share/androidReplyList")
    Observable<List<ShareHuifuBean>> getHomeReplyShareList(
            @Query("sid") String sid);

    @POST("/activity/share/androidReply")
    Observable<HttpResponse> replyZiyuan(
            @Query("token") String token,
            @Query("id") String sid,
            @Query("detail") String detail);

    @Multipart
    @POST("/report/report/save")
    Observable<HttpResponse> saveaReportve(
            @Query("token") String token,
            @PartMap Map<String, RequestBody> params,
            @Part MultipartBody.Part file);

    @GET("/info/anno/androidListNoLogin")
    Observable<List<InformBaseBean>> getAnnoListNoLogin();

    @GET("/activity/exhibition/getListData")
    Observable<HttpResponseBean<List<ActivityExhibitionBean>>> getExhibitionList(
            @Query("token") String token);

    @GET("/activity/exhibition/PhonePassAct")
    Observable<List<ActivityExhibitionBean>> getActivityListDataNotAudit(
            @Query("token") String token);

    @GET("/ordermeal/manageInfo/getListData")
    Observable<HttpResponseBean<List<PointActivityMainBean>>> getHomePonitActivityMainList(
            @Query("titleId") String titleId);

/*    @GET("/ordermeal/manage/getListData")
    Observable<HttpResponseBean<List<PonitListAfterBean>>> getHomePintAfterList(
            @Query("titleId") String titleId);*/
    @POST("/state/state/approve")
    Observable<HttpResponse> approveOnlineAdvice(@Query("token")String token,@Query("ids")String ids);

    @POST("/state/state/reject")
    Observable<HttpResponse> rejectOnlineAdvice(@Query("token")String token,@Query("ids")String ids);

    @POST("/state/state/save")
    Observable<HttpResponse> saveOnlineAdvice(@Query("token")String token,@Query("bean.content")String content
                                        ,@Query("area")String area,@Query("town")String town,@Query("village")String village);

    @POST("/sys/user/savePwdUpdate")
    Observable<HttpResponse> savePwdUpdate(@Query("token")String token,@Query("pwd")String pwd);

    @GET("/activity/exhibition/getViedos")
    Observable<List<VedioBean>> getViedos();

    @GET("/ordermeal/manage/getOrderManage")
    Observable<List<PonitListAfterBean>> getHomePintAfterList(
            @Query("titleId")String titleId);

    @GET("/ordermeal/manage/getOrderType")
    Observable<List<OrderTypeBean>> getOrderTypeList(
            @Query("token") String token);

    @GET("/ordermeal/manage/getOrderManageContent")
    Observable<List<PonitListAfterBean>> getOrderManageContentList(
            @Query("token") String token,
            @Query("titleId")String titleId);

    @Multipart
    @POST("/activity/exhibition/savePhone")
    Observable<HttpResponse> saveActivityExhibition(
            @Query("token") String token,
            @PartMap Map<String, RequestBody> params,
            @Part List<MultipartBody.Part> file);

    @GET("/activity/exhibition/PhoneNoPassAct")
    Observable<List<ActivityExhibitionBean>> getExhibitionNoAccepterList(
            @Query("token") String token);

    @GET("/activity/exhibition/getExhibition")
    Observable<List<ActivityExhibitionBean>> getHomeExhibitionList(
            @Query("positionId") String positionId);

    @Multipart
    @POST("/auditoriumInfo/auditorium/saveFiles")
    Observable<HttpResponse> saveFiles(
            @Query("token") String token,
            @Query("id") String id,
            @Query("delIds")String delIds,
            @Part List<MultipartBody.Part> file);

    @POST("/auditoriumInfo/auditorium/approve")
    Observable<HttpResponse> approveAuditorium(@Query("token") String token,@Query("id") String id,@Query("status") String status);

    @POST("/sys/user/save")
    Observable<HttpResponse> saveUser(@Query("name") String name,@Query("adminName") String adminName
            ,@Query("password") String password,@Query("area") String area
            ,@Query("town") String town,@Query("village") String village,@Query("phoneCode") String phoneCode);

    @POST("/auditoriumInfo/auditorium/getLocalAdmin")
    Observable<List<AuditoriaAdminBean>> getLocalAdmin(@Query("village")String village,@Query("token") String token);

    @GET("/sys/notify/getNotificationPhone")
    Observable<String> getNotifiSize(@Query("token") String token);

    @GET("/sys/notify/getNotification")
    Observable<NotificationBean> getNotifiMain(@Query("token") String token);

    @GET("/sys/user/getPhoneCode")
    Observable<HttpResponse> getPhoneCode(@Query("phone") String phone,@Query("type") String type);

    @POST("/sys/user/findPwd")
    Observable<HttpResponse> findPwd(@Query("phone") String phone,@Query("phoneCode") String phoneCode,@Query("pwd") String pwd);

    @GET("/activity/exhibition/getEverydayActivte")
    Observable<List<DayNewsBean>> getEverydayActivtes();

    @GET("/activity/exhibition/getEverydayActivte")
    Observable<List<DayNewsBean>> getEverydayActivtesTest(
            @Query("start") int start,
            @Query("length") int length);

    @GET("/activity/preview/pass")
    Observable<HttpResponse> passActivityPreview(@Query("token") String token,@Query("id") String id);

    @GET("/activity/preview/noPass")
    Observable<HttpResponse> noPassActivityPreview(@Query("token") String token,@Query("id") String id);

    @GET("/ordermeal/manage/getFindOrderManane")
    Observable<List<OrderMealBean>> getOrderMeal();

    @GET("/order/permissions/getListData")
    Observable<HttpResponseBean<List<PonitPeopleBean>>> getOrderMealPermissionsList(@Query("token") String token);

    @POST("/order/permissions/updataStatus")
    Observable<HttpResponse> upPeopleDataStatus(
            @Query("token") String token,
            @Query("permissions") String permissions,
            @Query("ids") String ids);

    @GET("/ordermeal/manage/getCount")
    Observable<NumBean> getOrderMealIsNum(@Query("token") String token,@Query("titleId") String titleId);

    @GET("/ordermeal/manageInfo/getCount")
    Observable<PonitListAfterBean> getOrderMealTheThemeIsNum(@Query("token") String token,@Query("contentId") String contentId);

    @GET("/activity/exhibition/pic")
    Observable<List<HomePhotoBean>> getDayHotPhotoBean();

    @GET("/activity/exhibition/pass")
    Observable<HttpResponse> postActivityExhibitionPass(
            @Query("token") String token,
            @Query("id") String id);

    @GET("/activity/exhibition/noPass")
    Observable<HttpResponse> postActivityExhibitionNoPass(
            @Query("token") String token,
            @Query("id") String id);

    @GET("/addApp/getFindVersion")
    Observable<Version> getAppVersionCode();

    @GET("/activity/exhibition/getQiniuUptoken")
    Observable<UptokenBean> getQiNiuToken();

    @GET("/monitoring/moduleOpen")
    Observable<String> getModuleOpen();

    @GET("/monitoring/findMonitors")
    Observable<VideoCaremerBean> findMonitors();

    @GET("/getScoreOrder")
    Observable<List<ScoreBean>> getScoreOrder(
            @Query("token") String token);
}
