package com.test.ydool.sanmen.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 */

public class NotificationBean {
    private List<AuditoriumBean> auditoriumPreviews;
    private List<NoticationInformBean> announcement;
    private List<share> shareReplies;
    private List<AuditoriaAdminBean> auditoriaAdminPreviews;
    private List<activity> activityPreviews;
    private List<OnlineAdviceBean> statePreviews;
    private JSONObject workReports;

    public List<AuditoriumBean> getAuditoriumPreviews() {
        return auditoriumPreviews;
    }

    public void setAuditoriumPreviews(List<AuditoriumBean> auditoriumPreviews) {
        this.auditoriumPreviews = auditoriumPreviews;
    }

    public List<NoticationInformBean> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(List<NoticationInformBean> announcement) {
        this.announcement = announcement;
    }

    public List<share> getShareReplies() {
        return shareReplies;
    }

    public void setShareReplies(List<share> shareReplies) {
        this.shareReplies = shareReplies;
    }

    public List<AuditoriaAdminBean> getAuditoriaAdminPreviews() {
        return auditoriaAdminPreviews;
    }

    public void setAuditoriaAdminPreviews(List<AuditoriaAdminBean> auditoriaAdminPreviews) {
        this.auditoriaAdminPreviews = auditoriaAdminPreviews;
    }

    public List<activity> getActivityPreviews() {
        return activityPreviews;
    }

    public void setActivityPreviews(List<activity> activityPreviews) {
        this.activityPreviews = activityPreviews;
    }

    public List<OnlineAdviceBean> getStatePreviews() {
        return statePreviews;
    }

    public void setStatePreviews(List<OnlineAdviceBean> statePreviews) {
        this.statePreviews = statePreviews;
    }

    public JSONObject getWorkReports() {
        return workReports;
    }

    public void setWorkReports(JSONObject workReports) {
        this.workReports = workReports;
    }

    class share{
        private String rtime;
        private String rdetail;
        private String stitle;
        private String sid;
        private String rid;

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public String getRdetail() {
            return rdetail;
        }

        public void setRdetail(String rdetail) {
            this.rdetail = rdetail;
        }

        public String getStitle() {
            return stitle;
        }

        public void setStitle(String stitle) {
            this.stitle = stitle;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }
    }

    class activity{
        private String auditorium;
        private String create_user_id;
        private String create_time;
        private String modify_time;
        private String title;
        private String organizer;
        private String name;
        private String passtime;
        private String id;
        private String time;
        private String detail;
        private String isdelete;
        private String village;
        private String modify_user_id;
        private String remarks;
        private String status;

        public String getAuditorium() {
            return auditorium;
        }

        public void setAuditorium(String auditorium) {
            this.auditorium = auditorium;
        }

        public String getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(String create_user_id) {
            this.create_user_id = create_user_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrganizer() {
            return organizer;
        }

        public void setOrganizer(String organizer) {
            this.organizer = organizer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIsdelete() {
            return isdelete;
        }

        public void setIsdelete(String isdelete) {
            this.isdelete = isdelete;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getModify_user_id() {
            return modify_user_id;
        }

        public void setModify_user_id(String modify_user_id) {
            this.modify_user_id = modify_user_id;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPosition_id() {
            return position_id;
        }

        public void setPosition_id(String position_id) {
            this.position_id = position_id;
        }

        private String position_id;

    }

}
