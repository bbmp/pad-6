package com.legent.ui.ext;

import android.content.ComponentName;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.legent.ui.AbsActivity;
import com.legent.ui.R;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AbsActivity {


    @Override
    protected void setContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.abs_activity);
    }



    HashMap<String, Integer> map = new HashMap();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int value = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < 100 && event.getY() < 100) {
                if (map.size() <= 0) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (map != null)
                                map.clear();
                        }
                    }, 15000);
                }
                if (map.containsKey("lefttop")) {
                    value = map.get("lefttop");
                }
                if (value > 18) {
                    onToDestop();
                } else
                    map.put("lefttop", ++value);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    void onToDestop() {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.android.launcher3",
                "com.android.launcher3.Launcher");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        startActivity(mIntent);

    }





}
