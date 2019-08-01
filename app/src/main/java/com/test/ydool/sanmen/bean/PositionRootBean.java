package com.test.ydool.sanmen.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

public class PositionRootBean {
    private int total;
    private boolean flag;
    private String message;
    private List<row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<row> getRows() {
        return rows;
    }

    public void setRows(List<row> rows) {
        this.rows = rows;
    }

    public class row{
        private String id;
        private String operationHtml;
        private boolean _children;
        private int level;
        private String position_name;
        private String pid;
        private String url;

        public boolean is_children() {
            return _children;
        }

        public void set_children(boolean _children) {
            this._children = _children;
        }

        public String getOperationHtml() {
            return operationHtml;
        }

        public void setOperationHtml(String operationHtml) {
            this.operationHtml = operationHtml;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getPosition_name() {
            return position_name;
        }

        public void setPosition_name(String position_name) {
            this.position_name = position_name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
