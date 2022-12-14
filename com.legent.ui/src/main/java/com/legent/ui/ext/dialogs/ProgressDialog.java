package com.legent.ui.ext.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.R;
import com.legent.utils.api.ViewUtils;

public class ProgressDialog extends AbsDialog {

	public ProgressDialog(Context cx) {
		this(cx, R.style.Theme_Dialog_FullScreen);
		ViewUtils.setFullScreen(cx,this, Gravity.FILL);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected int getViewResId() {
		return R.layout.dialog_progress;
	}

	private TextView txtMsg;
	private ImageView imgAnim;
	private Animation anim;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && imgAnim != null && anim != null)
			imgAnim.setAnimation(anim);
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (imgAnim != null) {
			imgAnim.setBackgroundDrawable(null);
			imgAnim = null;
			anim = null;
		}
	}

	@Override
	protected void initView(View view) {
		super.initView(view);

		txtMsg = (TextView) view.findViewById(R.id.txtMsg);
		imgAnim = (ImageView) view.findViewById(R.id.loadingImageView);
		anim = AnimationUtils.loadAnimation(getContext(), R.anim.common_anim_loading);

	}

	public void setMsg(String msg) {
		txtMsg.setText(msg);
	}

}
