
package com.robam.rokipad.ui.view.wheelview;

import android.os.Handler;
import android.os.Message;

import com.legent.utils.LogUtils;

/**
 * final 接收消息去执行对应操作
 */
public final class MessageHandler2 extends Handler {
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;
    final LoopView2 mLoopview;

    public MessageHandler2(LoopView2 loopview) {
        mLoopview = loopview;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                mLoopview.invalidate();
                break;
            case WHAT_SMOOTH_SCROLL:
                mLoopview.smoothScroll(LoopView2.ACTION.FLING);
                break;
            case WHAT_ITEM_SELECTED:
                LogUtils.i("20170829","-----------WHAT_ITEM_SELECTED----------");
                mLoopview.onItemSelected();
                break;
        }
    }

}
