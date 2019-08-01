package com.test.ydool.sanmen.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/19.
 */

public class DaySignBean {
    //签到
    private String user_id;
    private String name;//签到者
    private String time;//签到时间

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
