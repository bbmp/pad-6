package com.legent.events;

import com.legent.utils.LogUtils;

public class PageChangedEvent {

    public String pageKey;

    public PageChangedEvent(String pageKey) {
        LogUtils.e("20190115","pageKey：" + pageKey);
        this.pageKey = pageKey;
    }
}
