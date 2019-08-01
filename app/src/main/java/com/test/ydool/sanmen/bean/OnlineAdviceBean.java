package com.test.ydool.sanmen.bean;

public class OnlineAdviceBean {

    public OnlineAdviceBean(Integer id, String content, String statePosition, String userName, String create_time, String status, String create_user_id, String operationHtml, String state_to) {
        this.id = id;
        this.content = content;
        this.statePosition = statePosition;
        this.userName = userName;
        this.create_time = create_time;
        this.status = status;
        this.create_user_id = create_user_id;
        this.operationHtml = operationHtml;
        this.state_to = state_to;
    }

    /**
     * 建言id
     */
    private Integer id;
    /**
     * 建言内容
     */
    private String content;
    /**
     * 建言对象（区域名字）
     */
    private String statePosition;
    /**
     * 发表建言的用户名字
     */
    private String userName;
    /**
     * 发表时间
     */
    private String create_time;
    /**
     *  审核状态（0--未审核 ，1--已审核，3--已驳回）
     */
    private String status;
    /**
     * 建言人所属区域名字
     */
    private String pname;
    private String create_user_id;
    private String operationHtml;
    private String state_to;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getOperationHtml() {
        return operationHtml;
    }

    public void setOperationHtml(String operationHtml) {
        this.operationHtml = operationHtml;
    }

    public String getState_to() {
        return state_to;
    }

    public void setState_to(String state_to) {
        this.state_to = state_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatePosition() {
        return statePosition;
    }

    public void setStatePosition(String statePosition) {
        this.statePosition = statePosition;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
