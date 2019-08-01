package com.test.ydool.sanmen.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class VideoCaremerBean {
    private String cameraToken;
    private List<VideoToPeoBean> monitoringList;

    public String getCameraToken() {
        return cameraToken;
    }

    public void setCameraToken(String cameraToken) {
        this.cameraToken = cameraToken;
    }

    public List<VideoToPeoBean> getMonitoringList() {
        return monitoringList;
    }

    public void setMonitoringList(List<VideoToPeoBean> monitoringList) {
        this.monitoringList = monitoringList;
    }
}
