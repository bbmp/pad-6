
package com.robam.rokipad.ui.view.wheelview;


/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedRearRunnable2 implements Runnable {
    final LoopView2 mLoopViewRear;

    OnItemSelectedRearRunnable2(LoopView2 loopViewRear) {
        mLoopViewRear = loopViewRear;
    }

    @Override
    public final void run() {
        mLoopViewRear.mOnItemSelectedListenerRear.onItemSelectedRear(mLoopViewRear.getItemsContent(mLoopViewRear.getSelectedItem()));
    }
}
