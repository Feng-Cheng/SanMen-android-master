package com.test.ydool.sanmen.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.PonitListAfterBean;
import com.test.ydool.sanmen.fragments.HomeFragment;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.DividerItemDecoration;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/20.
 *
 * 主题活动
 */

public class ActivityListHomeMainActivity  extends BaseActivity implements OnBannerListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_actiivty_list)
    RecyclerView rlList;
    @BindView(R.id.banner)
    Banner banner;

    private CompositeSubscription subscriptions;

    private UserRepository userRepository;

    private ListHomeMainAdapter mAdapter;

    private List<PonitListAfterBean> list;

    private String auditorium = "";
    private String content = "";
    private String type = "";
    private String time = "";
    private String linMan = "";
    private String linphone = "";

    private ArrayList<String> list_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_activity_list_home_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.them_activiy);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();
        SPUtils spUtils = SPUtils.getInstance("sanMen");

        Bundle bundle = getIntent().getExtras();
        String titleId = bundle.getString("id");

       Subscription subscription = userRepository.getHomePintAfterList(titleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                list = new ArrayList<>();
                list_path = new ArrayList<>();
                if (response!=null){
                    list = response;
                    for (PonitListAfterBean a : list){
                        if (list_path!=null && list_path.size()==5){
                            break;
                        }
                        List<PonitListAfterBean.imgUrl> imgUrlList = a.getDocument();
                        for (PonitListAfterBean.imgUrl b : imgUrlList){
                            if (b.getType().equals("1")&&list_path.size()<5){
                                list_path.add(TerminalInfo.BASE_URL+b.getUrl());
                            }
                        }
                    }
                    if (list_path!=null){
                        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        banner.setImageLoader(new MyLoader());
                        banner.setImages(list_path);
                        banner.setBannerAnimation(Transformer.Default);
                        banner.setDelayTime(3000);
                        banner.isAutoPlay(true);
                        banner.setIndicatorGravity(BannerConfig.CENTER)
                                .setOnBannerListener(this)
                                .start();
                    }

                    mAdapter = new ListHomeMainAdapter(R.layout.item_news,list);
                    rlList.setLayoutManager(new LinearLayoutManager(mContext));
                    rlList.addItemDecoration(new DividerItemDecoration(mContext,
                            DividerItemDecoration.VERTICAL_LIST));
                    rlList.setAdapter(mAdapter);
                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(mContext, ShowWebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", TerminalInfo.BASE_URL+"/img/img?id="+list.get(position).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

//                   mAdapter = new ListHomeMainAdapter(R.layout.item_home_list_main,list);
//                    rlList.setLayoutManager(new LinearLayoutManager(mContext));
//                    rlList.setAdapter(mAdapter);
//
//                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                        @Override
//                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                            Intent intent = new Intent(mContext,ThemeActivityMainActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("theme",list.get(position).getActivity_theme());
//                            bundle.putString("url",list.get(position).getUrl());
//                            bundle.putString("time",list.get(position).getTime());
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    });
//                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                        @Override
//                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                            Intent intent = new Intent(mContext,ListHomeOnewActivity.class);
//                            Bundle bundle = new Bundle();
//                            if (list.get(position).getAuditorium()!=null){
//                                auditorium = list.get(position).getAddr();
//                            }
//                            if (list.get(position).getActivity_form()!=null){
//                                content = list.get(position).getContent();
//                            }
//                            if (list.get(position).getActivity_form()!=null){
//                                type = list.get(position).getActivity_form();
//                            }
//                            if (list.get(position).getCreate_time()!=null){
//                                time = list.get(position).getCreate_time();
//                            }
//                            if (list.get(position).getLink_man()!=null){
//                                linMan = list.get(position).getLink_man();
//                            }
//                            if (list.get(position).getLink_phone()!=null){
//                                linphone = list.get(position).getLink_phone();
//                            }
//                            bundle.putString("auditorium",auditorium);
//                            bundle.putString("content",content);
//                            bundle.putString("type",type);
//                            bundle.putString("time",time);
//                            bundle.putString("linMan",linMan);
//                            bundle.putString("linphone",linphone);
//
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    });
                }
            },throwable -> {
                throwable.printStackTrace();
                ToastUtil.showLongMessage(mContext,"网络异常！");
            });
        subscriptions.add(subscription);


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



//    class ListHomeMainAdapter extends BaseQuickAdapter<PonitListAfterBean,BaseViewHolder> {
//
//        public ListHomeMainAdapter(int layoutResId, @Nullable List<PonitListAfterBean> data) {
//            super(layoutResId, data);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, PonitListAfterBean item) {
//            helper.setText(R.id.tv_list_title,item.getContent());
//            helper.addOnClickListener(R.id.tv_list_checkmore);
//        }
//    }
      class ListHomeMainAdapter extends BaseQuickAdapter<PonitListAfterBean,BaseViewHolder> {

        public ListHomeMainAdapter(int layoutResId, @Nullable List<PonitListAfterBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PonitListAfterBean item) {
            helper.setText(R.id.title, item.getActivity_theme());
            helper.setText(R.id.tv_put_time, item.getTime());
            helper.setText(R.id.tv_theme_title, item.getThe_subject());

            Glide.with(mContext)
                    .load(TerminalInfo.BASE_URL + item.getDocument().get(0).getUrl())
                    .crossFade()
                    .placeholder(R.drawable.error)
                    .thumbnail(0.1f)
                    .error(R.drawable.error)
                    .into((ImageView) helper.getView(R.id.pic));
            helper.addOnClickListener(R.id.item);
        }
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
//        Log.i("tag", "你点了第"+position+"张轮播图");
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }
}
