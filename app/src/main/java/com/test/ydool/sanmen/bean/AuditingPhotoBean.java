package com.test.ydool.sanmen.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */

public class AuditingPhotoBean {
    private String id;
    private String name;
    private String adminName;
    private List<AuditoriumBean.img> ImgList;
    private int state;//审批状态（0—未审批，1—已审批，3—已驳回）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }


    public List<AuditoriumBean.img> getImgList() {
        return ImgList;
    }

    public void setImgList(List<AuditoriumBean.img> imgList) {
        ImgList = imgList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
