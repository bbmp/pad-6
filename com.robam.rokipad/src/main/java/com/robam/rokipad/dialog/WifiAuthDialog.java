package com.robam.rokipad.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.Callback2;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.rokipad.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WifiAuthDialog extends AbsDialog {


    @InjectView(R.id.main_back)
    ImageView mainBack;
    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.pwd)
    EditText pwd;
    @InjectView(R.id.connect)
    TextView connect;
    @InjectView(R.id.wifiauthdg_ll_back)
    LinearLayout wifiauthdgLlBack;
    @InjectView(R.id.btn_con)
    LinearLayout btn_con;
    @InjectView(R.id.eye_btn)
    ImageView eyeBtn;

    private Context mContext;

    private Callback2<String> callback;
    private boolean checked = false;
    static WifiAuthDialog dlg;

    static public void show(Context cx, String ssid, final Callback2<String> callabck) {
        dlg = new WifiAuthDialog(cx);
        dlg.setCallback(callabck);
        dlg.setTitle(ssid);
        Window win = dlg.getWindow();
        // win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(wl);
        dlg.show();
    }


    @Override
    protected int getViewResId() {
        return R.layout.dialog_input_wifi_pwd;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int length = s.length();
                if (length > 0) {
                    btn_con.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_blue));
                    btn_con.setClickable(true);
                } else {
                    btn_con.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.wifi_pad_bg));
                    btn_con.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public WifiAuthDialog(Context context) {
        super(context, R.style.Theme_Dialog_FullScreen);
        mContext = context;
    }

    public WifiAuthDialog(Context context, int theme) {
        super(context, theme);
    }

    private void setCallback(Callback2<String> callback) {
        this.callback = callback;
    }

    private void setEdtPwd(String ssid) {
        String savedPwd = PreferenceUtils.getString(ssid, null);
        pwd.setText(savedPwd);
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
    }

    private boolean btnChecked = true;

    @OnClick(R.id.eye_btn)
    public void onShowPwd() {
        if (TextUtils.isEmpty(pwd.getText())) return;
        if (btnChecked) {
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeBtn.setImageResource(R.mipmap.eye_close);
        } else {
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeBtn.setImageResource(R.mipmap.eye_open);
        }
        btnChecked = !btnChecked;
    }

    @OnClick(R.id.btn_con)
    public void onClickCon() {
        String pwdTxt = pwd.getText().toString();
        if (TextUtils.isEmpty(pwdTxt)) {
            ToastUtils.showShort("Wifi密码不能为空");
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
        callback.onCompleted(pwdTxt);
    }


    /*@OnClick(R.id.wifiauthdg_ll_back)
    public void onClickImg() {
        if (UIService.getInstance().isCurrentPage(PageKey.Setting)) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        dismiss();
    }*/


    // 隐藏当前输入法
    private void hideInputMethod(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) super.cx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    private void setTitle(String ti) {
        title.setText(ti);
    }

}
