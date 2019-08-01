package com.test.ydool.sanmen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class AuditoriumBean implements Serializable{
    //礼堂信息
    private String id;
    private String admin_id;
    private String create_user_id;
    private String name;//礼堂名称
    private String address;//地址
    private String ps;//备注
    private String proportion;//大小
    private String service_introduce;
    private String create_time;
    private String modify_time;
    private String lishihui;
    private String adminName;
    private List<img> imgList;
    private String build_introduce;//村概况
    private String build_year;
    private String delete_status;
    private String survey;//礼堂介绍
    private String position;
    private String modify_user_id;
    private String wentiduiwu;
    private String position_id;
    private int status;
    private String villageid;

    public String getVillageid() {
        return villageid;
    }

    public void setVillageid(String villageid) {
        this.villageid = villageid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getService_introduce() {
        return service_introduce;
    }

    public void setService_introduce(String service_introduce) {
        this.service_introduce = service_introduce;
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

    public String getLishihui() {
        return lishihui;
    }

    public void setLishihui(String lishihui) {
        this.lishihui = lishihui;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<img> getImgList() {
        return imgList;
    }

    public void setImgList(List<img> imgList) {
        this.imgList = imgList;
    }

    public String getBuild_introduce() {
        return build_introduce;
    }

    public void setBuild_introduce(String build_introduce) {
        this.build_introduce = build_introduce;
    }

    public String getBuild_year() {
        return build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }

    public String getWentiduiwu() {
        return wentiduiwu;
    }

    public void setWentiduiwu(String wentiduiwu) {
        this.wentiduiwu = wentiduiwu;
    }

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class img implements Serializable{
        private String create_user_id;
        private String name;
        private String createdate;
        private String id;
        private String type;
        private String table_name;
        private String url;
        private String tid;

        public String getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(String create_user_id) {
            this.create_user_id = create_user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
    }

}
