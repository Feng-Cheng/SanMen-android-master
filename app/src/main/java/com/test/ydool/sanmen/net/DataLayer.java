package com.test.ydool.sanmen.net;


import android.content.Context;
import android.net.Uri;

import com.test.ydool.sanmen.bean.*;


import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/4/19.
 */

public interface DataLayer {

    interface UserService{
        //更新检查
        Observable<HttpResponse<AdminBean>> loginCheck(String username, String password);

        //每日签到
        Observable<HttpResponse> signDay(String userId, String year, String month, String day);

        //获取礼堂信息
        Observable<HttpResponse> getListData(String name);

        //获取三门县区域信息
        Observable<PositionRootBean> getPositionRoot();

        //获取下级区域信息
        Observable<PositionRootBean> getPositionRoot(String pid);

        //获取街道信息
        Observable<AuditoriumBean> getAuditorium(String poid,String token);

        //更新礼堂信息
        Observable<HttpResponse> adsaveAuditorium(String id,String name,String town,String village,String address,String lishihui
                ,String wentiduiwu,String proportion,String build_year,String admin_id,String build_introduce,String ps
                ,String survey,String status, String token, List<Uri> path, Context context,String ids);

        //手机签到
        Observable<HttpResponse> saveSign(String userId,String token);

        //工作小结
        Observable<HttpResponse<List<WorkBaseBean>>> getWorkListData(String token);

        //上传工作小结
        Observable<HttpResponse> saveWorkReport(String token,String name,String time, String summary,String issue,String solution
                ,String activity_describe, String remark);

        //获取签到列表
        Observable<HttpResponse<List<DaySignBean>>> getSignList(String token);

        //获取流量统计
        Observable<HttpResponse<List<FlowStatisticsBean>>> getFlowStatisticsList(String token);

        //获取工作统计
        Observable<HttpResponseBean<List<WorkStatisticsBean>>> getWorkStatisticsListData(String token,int genre,String area);

        //获取工作统计
        Observable<List<InformBaseBean>> getInformMainList(String token);

        //信息发布
        Observable<HttpResponse> InformReport(String token, String title, String detail, int type, String remarks,Uri path,Context context,String names);

        //获取审核列表
        Observable<HttpResponseBean<List<AuditoriumBean>>> getAuditoriumShenInfoList(String token);

        //获取活动预告通过列表
        Observable<List<ActivityPreviewBean>> getActivityPreviewAccepterList(String token);

        //获取活动预告未通过列表
        Observable<List<ActivityPreviewBean>> getActivityPreviewNoPassList(String token);

        //获取今日热点
        Observable<List<NewsBean>> getNewsList(String token);

        //获取首页轮播图
        Observable<List<String>> getHomePhotoList();

        //获取首页活动预告
        Observable<List<ActivityPreviewBean>> getpreviewList();

        //上传活动预告
        Observable<HttpResponse> ActivityPreViewadSave(String token,String aud,String town,String title,String detail,String delIds,String date,String organizer,String remarks, List<Uri> path,Context context);


        //获取点单大类
        Observable<List<PonitActivityTypeBean>> getPonitActivityTypeList(String token);

        //获取点单内的内容
        Observable<HttpResponseBean<List<PointActivityMainBean>>> getPonitActivityMainList(String token, String titleId);

        //获取点单内的内容
        Observable<HttpResponseBean<List<PointActivityMainBean>>> getHomePonitActivityMainList(String titleId);

        //提交点单的
        Observable<HttpResponse> adSaveOrderMeall(String token,String titleId,String orderContentId);

        //获取在线建言
        Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(String token);

        Observable<HttpResponseBean<List<OnlineAdviceBean>>> getOnlineAdviceList(String token, String town, String village);

        //获取文档列表
        Observable<HttpResponseBean<List<WenDangBean>>> getWenDangList(String token);

        //获取
        Observable<List<ShareBean>> getHomeShareList();

        //通过在线建言
        Observable<HttpResponse> approveOnlineAdvice(String token,String id);

        //驳回在线建言
        Observable<HttpResponse> rejectOnlineAdvice(String token,String id);

        //提交在线建言


        //获取回复列表
        Observable<List<ShareHuifuBean>> getHomeReplyShareList(String sid);

        //回复资源共享
        Observable<HttpResponse> replyZiyuan(String token,String sid,String detail);

        //获取预告图片
        Observable<List<UrlBean>> getAnnocePicture();

        //上传文件
        Observable<HttpResponse> saveaReportve(String token,String user_name,String position_id,String time,String content,Uri uri,Context context);

        //无需登录的通知公告列表
        Observable<List<InformBaseBean>> getAnnoListNoLogin();

        //获取审核通过列表
        Observable<HttpResponseBean<List<ActivityExhibitionBean>>> getExhibitionList(String token);

        //获取我发布的
        Observable<List<ActivityExhibitionBean>> getActivityListDataNotAudit(String token);

        //获取视频列表
        Observable<List<VedioBean>> getViedos();

        //获取活动清单列表
        Observable<List<PonitListAfterBean>> getHomePintAfterList(String titleId);

        //保存活动展示
        Observable<HttpResponse> saveActivityExhibition(String token,String time,String content,String activity_theme,String address,String remark,String villages,List<Uri> path,Context context,String VedioPath,String order_title_id,String order_content_id,String the_subject,int level);

        //保存在线建言
        Observable<HttpResponse> saveOnlineAdvice(String token,String content,String area,String town,String village);

        //修改密码
        Observable<HttpResponse> savePwdUpdate(String token,String pwd);

        //获取类型列表
        Observable<List<OrderTypeBean>> getOrderTypeList(String token);

        //获取活动展示具体内容
        Observable<List<PonitListAfterBean>> getOrderManageContentList(String token,String titleId);

        //获取活动展示不通过列表
        Observable<List<ActivityExhibitionBean>> getExhibitionNoAccepterList(String token);

        //获取首页活动
        Observable<List<ActivityExhibitionBean>> getHomeExhibitionList(String positionId);

        //上传礼堂图片接口
        Observable<HttpResponse> saveFiles(String token,String id,String delIds,List<Uri> paths,Context context);

        //礼堂审核接口
        Observable<HttpResponse> approveAuditorium(String token,String id,String status);

        //手机端注册接口
        Observable<HttpResponse> saveUser(String name,String adminName
                ,String password,String town,String village, String phoneCode);

        //查找在该礼堂所在区域内的礼堂管理员
        Observable<List<AuditoriaAdminBean>> getLocalAdmin(String village,String token);

        //获取通知栏数量
        Observable<String> getNotifiSize(String token);

        //获取通知栏具体
        Observable<NotificationBean> getNotifiMain(String token);

        //获取手机验证码
        Observable<HttpResponse> getPhoneCode(String phone,String type);

        //找回密码
        Observable<HttpResponse> findPwd(String phone,String phoneCode,String pwd);

        //获取日常活动接口
        Observable<List<DayNewsBean>> getEverydayActivtes(int start,int length);

        //测试获取日常活动接口
        Observable<List<DayNewsBean>> getEverydayActivtesTest(int start,int length);

        //通过活动预告
        Observable<HttpResponse> passActivityPreview(String token,String id);

        //不通过活动预告
        Observable<HttpResponse> noPassActivityPreview(String token,String id);

        //获取点单信息
        Observable<List<OrderMealBean>> getOrderMeal();

        //获取点单权限人列表
        Observable<HttpResponseBean<List<PonitPeopleBean>>> getOrderMealPermissionsList(String token);

        //更新存管理点单权限
        Observable<HttpResponse> upPeopleDataStatus(String token,String permissions,String ids);

        //获取此活动主题已点数量
        Observable<NumBean> getOrderMealIsNum(String token,String titleId);

        //获取此内容主题活动是否已点
        Observable<PonitListAfterBean> getOrderMealTheThemeIsNum(String token,String contentId);

        //获取今日看点头部图片
        Observable<List<HomePhotoBean>> getDayHotPhotoBean();

        //通过活动展示
        Observable<HttpResponse> postActivityExhibitionPass(String token,String id);

        //不通过活动展示
        Observable<HttpResponse> postActivityExhibitionNoPass(String token, String id);

        //更新方式
        Observable<Version> getAppVersionCode();

        //获取七牛toknen
        Observable<UptokenBean> getQiNiuToken();

        //获取是否开启视频互动模块
        Observable<String> getModuleOpen();

        //获取视频互动列表
        Observable<VideoCaremerBean> findMonitors();

        //获取积分数排行榜
        Observable<List<ScoreBean>> getScoreOrder(String token);
    }

}
