package com.robam.rokipad.ui.recycler;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by 14807 on 2020/3/5.
 * PS:
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    /**
     * 禁止滑动
     * canScrollHorizontally（禁止横向滑动）
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollVertically();
    }
    /**
     * 禁止滑动
     * canScrollVertically（禁止竖向滑动）
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}