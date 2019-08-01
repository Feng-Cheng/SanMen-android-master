package com.test.ydool.sanmen.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class AssetsJsonManager {
    private static AssetsJsonManager mAssetsJsonManager;
    private Context mContext;
    private List<villageBean> listVillage;

    private AssetsJsonManager(Context context) {
        mContext = context;
    }

    public static AssetsJsonManager getInstance(Context mContext) {
        if (mAssetsJsonManager == null) {
            mAssetsJsonManager = new AssetsJsonManager(mContext);
        }
        return mAssetsJsonManager;
    }

    public List<villageBean> getListVillage() {
        if (listVillage == null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open("villageintroduce.json")));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                listVillage = JSON.parseArray(sb.toString(), villageBean.class);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listVillage;
    }
    public String getVillageIntroduce(String id){
        if (listVillage == null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open("villageintroduce.json")));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                listVillage = JSON.parseArray(sb.toString(), villageBean.class);
                for (villageBean a : listVillage){
                    if (a.getId().equals(id)){
                        return a.getDesc();
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            for (villageBean a : listVillage){
                if (a.getId().equals(id)){
                    return a.getDesc();
                }
            }
        }
        return "";
    }

    public static class villageBean{
        private String id;
        private String desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
