
package com.robam.rokipad.ui.view.wheelview;

import android.text.TextUtils;

/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedCenterRunnable2 implements Runnable {
    final LoopView2 mLoopViewCenter;

    OnItemSelectedCenterRunnable2(LoopView2 loopviewCenter) {
        mLoopViewCenter = loopviewCenter;
    }

    @Override
    public final void run() {
        if (null != mLoopViewCenter) {

            int selectedItem = mLoopViewCenter.getSelectedItem();
            String itemsContent = mLoopViewCenter.getItemsContent(selectedItem);
            if (!TextUtils.isEmpty(itemsContent)) {
                mLoopViewCenter.mOnItemSelectedListenerCenter.onItemSelectedCenter(itemsContent);
            }
        }
    }
}
