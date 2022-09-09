package com.robam.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.legent.VoidCallback2;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.R;

/**
 * Created by as on 2017-06-01.
 */

public class OneKeyTaskCompleteDialog extends AbsDialog {
    public OneKeyTaskCompleteDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_pot_cookingcomplete;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Button btn = (Button) view.findViewById(R.id.pot_over_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback2 != null)
                    callback2.onCompleted();
                if (dl != null) {
                    dl.dismiss();
                    dl = null;
                }
            }
        });
    }

    static OneKeyTaskCompleteDialog dl;
    static VoidCallback2 callback2;

    public static Dialog show(Context ctx, VoidCallback2 ca) {
        if (dl != null && dl.isShowing())
            return dl;
        callback2 = ca;
        dl = null;
        dl = new OneKeyTaskCompleteDialog(ctx);
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = dl.getWindow().getAttributes();
        layoutParams.width = 650;
        layoutParams.height = 392;
        dl.getWindow().setAttributes(layoutParams);
        dl.show();
        return dl;
    }
}
