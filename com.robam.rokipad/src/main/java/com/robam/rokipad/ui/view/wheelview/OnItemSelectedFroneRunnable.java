
package com.robam.rokipad.ui.view.wheelview;

import com.legent.utils.LogUtils;

/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedFroneRunnable implements Runnable {
    final LoopView mLoopViewFrone;

    OnItemSelectedFroneRunnable(LoopView loopviewFrone) {
        mLoopViewFrone = loopviewFrone;

    }

    @Override
    public final void run() {

        LogUtils.i("20170829","mOnItemSelectedListenerFrone:"+mLoopViewFrone.mOnItemSelectedListenerFront);
        mLoopViewFrone.mOnItemSelectedListenerFront.onItemSelectedFront(mLoopViewFrone.getItemsContent(mLoopViewFrone.getSelectedItem()));
    }
}
