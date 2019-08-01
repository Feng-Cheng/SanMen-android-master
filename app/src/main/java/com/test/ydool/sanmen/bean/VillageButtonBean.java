package com.test.ydool.sanmen.bean;

/**
 * Created by Administrator on 2018/5/30.
 */

public class VillageButtonBean {
    private String id;
    private String villageName;

    public VillageButtonBean(String id,String villageName){
        this.id = id;
        this.villageName = villageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
