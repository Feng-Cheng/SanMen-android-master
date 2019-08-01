package com.test.ydool.sanmen.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */

public class OrderMealBean {

    private String create_user_id;
    private String activity_form;
    private String create_time;
    private int surplus;
    private String link_man;
    private String modify_time;
    private String link_phone;
    private String activity_unit;
    private String title;
    private String activity_theme;
    private String tid;
    private String order_title_id;
    private String id;
    private String time;
    private String activity_content;
    private String isdelete;
    private String modify_user_id;
    private int activity_number;
    private List<auditorium> auditoriums;

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getActivity_form() {
        return activity_form;
    }

    public void setActivity_form(String activity_form) {
        this.activity_form = activity_form;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }



    public String getLink_man() {
        return link_man;
    }

    public void setLink_man(String link_man) {
        this.link_man = link_man;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getLink_phone() {
        return link_phone;
    }

    public void setLink_phone(String link_phone) {
        this.link_phone = link_phone;
    }

    public String getActivity_unit() {
        return activity_unit;
    }

    public void setActivity_unit(String activity_unit) {
        this.activity_unit = activity_unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivity_theme() {
        return activity_theme;
    }

    public void setActivity_theme(String activity_theme) {
        this.activity_theme = activity_theme;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOrder_title_id() {
        return order_title_id;
    }

    public void setOrder_title_id(String order_title_id) {
        this.order_title_id = order_title_id;
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

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public int getActivity_number() {
        return activity_number;
    }

    public void setActivity_number(int activity_number) {
        this.activity_number = activity_number;
    }

    public List<auditorium> getAuditoriums() {
        return auditoriums;
    }

    public void setAuditoriums(List<auditorium> auditoriums) {
        this.auditoriums = auditoriums;
    }

    public class auditorium{
        private String auditorium;
        private String oid;
        private String street;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAuditorium() {
            return auditorium;
        }

        public void setAuditorium(String auditorium) {
            this.auditorium = auditorium;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }
    }

}
