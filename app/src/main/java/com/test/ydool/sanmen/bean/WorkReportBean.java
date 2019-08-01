package com.test.ydool.sanmen.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class WorkReportBean {

    private String name;//姓名
    private Date date;//日期
    private String auditorium;//所属礼堂
    private List<work> work;
    class work{
        Date date;//时间
        String work;//工作简述
        int workGood;//执行情况 0一般 1良好 2优
        String execution;//完成情况

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public int getWorkGood() {
            return workGood;
        }

        public void setWorkGood(int workGood) {
            this.workGood = workGood;
        }

        public String getExecution() {
            return execution;
        }

        public void setExecution(String execution) {
            this.execution = execution;
        }
    }

}
