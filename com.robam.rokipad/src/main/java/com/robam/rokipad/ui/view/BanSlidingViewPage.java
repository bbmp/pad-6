package com.robam.rokipad.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/27.
 * PS: 禁止滑动的ViewPage.
 */
public class BanSlidingViewPage extends ViewPager {

    private boolean scroll = true;

    public BanSlidingViewPage(Context context) {
        super(context);
    }



    public BanSlidingViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if(scroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(scroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }


}
