package com.test.ydool.sanmen.customview;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.test.ydool.sanmen.R;

/**
 * Created by Administrator on 2018/7/23.
 */

public class CustomLoadMoreView extends LoadMoreView{
    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
