package com.robam.rokipad.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.legent.ui.UIService;
import com.legent.ui.ext.views.TitleBar;
import com.robam.common.ui.RokiLoader;

/**
 * Created by Administrator on 2016/5/16.
 */
public class RokiPadLoader extends RokiLoader {
    private static long lastClickTime;


    @Override
    protected View getMenuIcon(Context cx) {

        // 设置menu图标
        final boolean isMainForm = UIService.getInstance().isMainForm();
        final boolean isHome = UIService.getInstance().isHomePage(pageKey);
        int iconResid = (isMainForm && isHome) ? getMenuIconResid() : getBackIconResid();

        if (iconResid == 0) return null;
        ImageView view = TitleBar.newTitleIconView(cx, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFastDoubleClick()) {
                    if (isMainForm) {
                        if (isHome)
                            UIService.getInstance().getMain().toggleMenu();
                        else {
                            UIService.getInstance().popBack();
                        }
                    } else {
                        if (isHome)
                            UIService.getInstance().getMain().getActivity().finish();
                        else
                            UIService.getInstance().popBack();
                    }
                }
            }
        });


        view.setImageResource(iconResid);
        view.setLayoutParams(new AbsListView.LayoutParams(dip2px(cx, 65), dip2px(cx, 100)));
        view.setImageResource(iconResid);
        return view;
    }


   //防止短时间重复点击，防止应用切换太频繁
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
