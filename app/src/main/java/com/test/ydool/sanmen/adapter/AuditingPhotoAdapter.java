package com.test.ydool.sanmen.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.bean.AuditingPhotoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */

public class AuditingPhotoAdapter extends BaseQuickAdapter<AuditingPhotoBean,BaseViewHolder>{
    public AuditingPhotoAdapter(int layoutResId, @Nullable List<AuditingPhotoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuditingPhotoBean item) {
        helper.setText(R.id.tv_audtiorname, item.getName());
        helper.setText(R.id.tv_admin_name, item.getAdminName());

        if (item.getImgList()!=null && !item.getImgList().isEmpty()){
            for (int i = 0 ; i < item.getImgList().size() ; i++){
                if (i == 0){
                    System.out.println(TerminalInfo.BASE_URL+item.getImgList().get(0).getUrl());
                    Glide.with(mContext).load(TerminalInfo.BASE_URL+item.getImgList().get(0).getUrl()).crossFade().into((ImageView) helper.getView(R.id.iv_iv1));
                }else if(i==1){
                    System.out.println(TerminalInfo.BASE_URL+item.getImgList().get(0).getUrl());
                    Glide.with(mContext).load(TerminalInfo.BASE_URL+item.getImgList().get(1).getUrl()).crossFade().into((ImageView) helper.getView(R.id.iv_iv2));
                }else if(i==2){
                    System.out.println(TerminalInfo.BASE_URL+item.getImgList().get(0).getUrl());
                    Glide.with(mContext).load(TerminalInfo.BASE_URL+item.getImgList().get(2).getUrl()).crossFade().into((ImageView) helper.getView(R.id.iv_iv3));
                }
            }
        }

        if (item.getState() == 0){
            helper.setText(R.id.tv_auditing_phtoto_status, R.string.weishen);
            helper.setTextColor(R.id.tv_auditing_phtoto_status, Color.RED);
        }
        if (item.getState() == 1){
            helper.setText(R.id.tv_auditing_phtoto_status, R.string.yishen);
            helper.setTextColor(R.id.tv_auditing_phtoto_status, Color.GREEN);
        }
        if (item.getState() == 3){
            helper.setText(R.id.tv_auditing_phtoto_status, R.string.bohui);
            helper.setTextColor(R.id.tv_auditing_phtoto_status, Color.BLACK);
        }

        helper.addOnClickListener(R.id.iv_iv4);
    }
}
