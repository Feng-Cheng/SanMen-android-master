package com.test.ydool.sanmen.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 */

public class DayNewsBean {
    private String create_user_id;
    private String address;
    private String create_time;
    private String villages;
    private String modify_time;
    private String remark;
    private String content;
    private String activity_theme;
    private String the_subject;
    private String order_title_id;
    private String order_content_id;
    private String name;
    private String id;
    private String time;
    private String isdelete;
    private String modify_user_id;
    private String did;
    private String status;
    private List<imgUrl> document;

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getVillages() {
        return villages;
    }

    public void setVillages(String villages) {
        this.villages = villages;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getThe_subject() {
        return the_subject;
    }

    public void setThe_subject(String the_subject) {
        this.the_subject = the_subject;
    }

    public String getOrder_title_id() {
        return order_title_id;
    }

    public void setOrder_title_id(String order_title_id) {
        this.order_title_id = order_title_id;
    }

    public String getOrder_content_id() {
        return order_content_id;
    }

    public void setOrder_content_id(String order_content_id) {
        this.order_content_id = order_content_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<imgUrl> getDocument() {
        return document;
    }

    public void setDocument(List<imgUrl> document) {
        this.document = document;
    }

    public class imgUrl{
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
