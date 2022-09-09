package com.legent.ui.ext;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.legent.ui.AbsPage;
import com.legent.ui.R;

public class BasePage extends AbsPage{

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Drawable dr = rootView.getBackground();
        Log.e("201080102","dr::::"+dr);
        if (dr == null) {
            setRootBg();
        }
    }

    public void setRootBg() {
        setRootBgRes(R.drawable.default_background);

    }

    protected void setRootBgColor(final int color) {
        if (rootView != null) {
            rootView.setBackgroundColor(color);
        }
    }

    protected void setRootBgRes(final int resid) {
        if (rootView != null) {
            rootView.setBackgroundResource(resid);
        }
    }

}
