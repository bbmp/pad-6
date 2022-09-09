package com.robam.common.ui;

import com.legent.ui.ext.loaders.SimpleTitleBarLoader;
import com.robam.common.R;

/**
 * Created by sylar on 15/7/26.
 */
public class RokiLoader extends SimpleTitleBarLoader{

    @Override
    protected int getBackIconResid() {
        return R.mipmap.ic_titlebar_return;
    }

    @Override
    protected int getMenuIconResid() {
        return super.getMenuIconResid();
    }
}
