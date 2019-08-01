package com.test.ydool.sanmen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class ActivityExhibitionBean implements Serializable{
    private String create_user_id;
    private String address;
    private String villages;
    private String create_time;
    private String operationHtml;
    private String modify_time;
    private String update;
    private String remark;
    private String delete;
    private String content;
    private String activity_theme;
    private String id;
    private String time;
    private String isdelete;
    private String status;
    private String modify_user_id;
    private String addr;
    private List<UrlBean> pic;
    private String video;

    public List<UrlBean> getPic() {
        return pic;
    }

    public void setPic(List<UrlBean> pic) {
        this.pic = pic;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVillages() {
        return villages;
    }

    public void setVillages(String villages) {
        this.villages = villages;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOperationHtml() {
        return operationHtml;
    }

    public void setOperationHtml(String operationHtml) {
        this.operationHtml = operationHtml;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActivity_theme() {
        return activity_theme;
    }

    public void setActivity_theme(String activity_theme) {
        this.activity_theme = activity_theme;
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

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }
}
