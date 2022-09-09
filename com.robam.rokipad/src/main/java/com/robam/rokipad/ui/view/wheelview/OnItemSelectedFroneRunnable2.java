
package com.robam.rokipad.ui.view.wheelview;

import com.legent.utils.LogUtils;

/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedFroneRunnable2 implements Runnable {
    final LoopView2 mLoopViewFrone;

    OnItemSelectedFroneRunnable2(LoopView2 loopviewFrone) {
        mLoopViewFrone = loopviewFrone;

    }

    @Override
    public final void run() {

        LogUtils.i("20170829","mOnItemSelectedListenerFrone:"+mLoopViewFrone.mOnItemSelectedListenerFront);
        mLoopViewFrone.mOnItemSelectedListenerFront.onItemSelectedFront(mLoopViewFrone.getItemsContent(mLoopViewFrone.getSelectedItem()));
    }
}
