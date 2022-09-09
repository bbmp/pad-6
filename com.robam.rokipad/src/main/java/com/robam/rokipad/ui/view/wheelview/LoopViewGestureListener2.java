

package com.robam.rokipad.ui.view.wheelview;

import android.view.MotionEvent;

/**
 * 控件手势监听器
 */
final class LoopViewGestureListener2 extends android.view.GestureDetector.SimpleOnGestureListener {

    final LoopView2 mLoopView;

    LoopViewGestureListener2(LoopView2 loopview) {
        mLoopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mLoopView.scrollBy(velocityY);
        return true;
    }
}
