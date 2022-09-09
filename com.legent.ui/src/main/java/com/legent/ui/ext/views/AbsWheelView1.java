package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * WheelView滚轮 rent
 * <p/>
 */
public class AbsWheelView1 extends AbsWheelView2 {

    public AbsWheelView1(Context context) {
        super(context);
    }

    public AbsWheelView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取返回的内容
     *
     * @return
     */
    @Override
    protected synchronized void initData() {
        isClearing = true;
        itemList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            ItemObject itmItemObject = getItemObject();
            itmItemObject.id = i;
            itmItemObject.tag = dataList.get(i);
            if(String.valueOf(dataList.get(i)).length() == 1){
                itmItemObject.itemText = "0"+String.valueOf(dataList.get(i));
            }else{
                itmItemObject.itemText = String.valueOf(dataList.get(i));
            }
            itmItemObject.x = 0;
            itmItemObject.y = i * unitHeight;
            itemList.add(itmItemObject);
        }
        isClearing = false;
    }
}
