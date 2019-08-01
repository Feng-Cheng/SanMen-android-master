package com.test.ydool.sanmen.activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.fragments.DayNews1Fragment;
import com.test.ydool.sanmen.fragments.FragmentLifecycle;
import com.test.ydool.sanmen.fragments.MovieListFragment;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.widget.ScrollEnableViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.design.widget.TabLayout.Tab;

/**
 * Created by Administrator on 2018/6/20.
 *
 * 首页日常活动
 */

public class DayActivityMainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tl_seletor)
    TabLayout mTabLayout;
    @BindView(R.id.vp_Show)
    ScrollEnableViewPager mViewPager;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private Tab tab1;
    private Tab tab2;
    private List<Fragment> fragments = new ArrayList<>();

    private MyFragmentPagerAdapter mPagerAdapter;

//    private ScrollEnableViewPager scrollEnableViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_day_activity_main);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText(R.string.day_activity);
        tvTitle.setTextColor(0xfffaa8ae);
        ivMenu.setImageResource(R.drawable.ic_back);

        fragments.add(new DayNews1Fragment());
        fragments.add(new MovieListFragment());
//        fragments.add(new OnlineVedioFragment());

        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager, true);//将tablayout和viewpager绑定
        tab1 = mTabLayout.getTabAt(0);
        tab2 = mTabLayout.getTabAt(1);

        tab1.setText("新闻");
        tab2.setText("视频");

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                for (int i = 0; i < mTabLayout.getChildCount(); i++) {
                    if (tab == mTabLayout.getTabAt(i)) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPagerAdapter != null && mPagerAdapter.getCurrentFragment() != null){
            mPagerAdapter.getCurrentFragment().onActivityDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPagerAdapter != null) {
            mPagerAdapter.getCurrentFragment().onBackPressed();
        }else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPagerAdapter != null && mPagerAdapter.getCurrentFragment() != null) {
            mPagerAdapter.getCurrentFragment().onActivityPause();
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private FragmentLifecycle mCurrentFragment;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            {
                if (getCurrentFragment() != object) {
                    if (mCurrentFragment != null) {
                        mCurrentFragment.onFragmentPause();
                    }
                    mCurrentFragment = ((FragmentLifecycle) object);
                    mCurrentFragment.onFragmentResume();
                }
                super.setPrimaryItem(container, position, object);
            }
        }

        public FragmentLifecycle getCurrentFragment() {
            return mCurrentFragment;
        }
    }


    public void setTabViewVisible(boolean isVisible) {
        mTabLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        rlTitle.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isVisible){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        }else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        }
        setmSwipeBackHelper(isVisible);
        mViewPager.setScrollEnable(isVisible);
    }

}