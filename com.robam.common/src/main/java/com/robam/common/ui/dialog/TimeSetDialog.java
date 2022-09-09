package com.robam.common.ui.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.legent.events.PageChangedEvent;
import com.legent.ui.UI;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.ui.views.CountdownTimePicker;

public class TimeSetDialog {

	public static interface TimeSeletedCallback {
		void onTimeSeleted(int minute, int second);
	}

	public static void show(Context cx, String title, int minute, int second,
			int maxMin, int maxSec, final TimeSeletedCallback callback) {

		final CountdownTimePicker tpView = new CountdownTimePicker(cx);
		tpView.setTime(minute, second);
		tpView.setMaxTime(maxMin, maxSec);

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				tpView.clearFocus();
				ViewUtils.setDialogShowField(dialog, true);

				if (which == DialogInterface.BUTTON_POSITIVE) {

					if (callback != null) {
						callback.onTimeSeleted(tpView.getCurrentMinute(),
								tpView.getCurrentSecond());
					}
				}
			}
		};

		AlertDialog.Builder builder = new Builder(cx);
		builder.setTitle(title);
		builder.setView(tpView);
		builder.setNegativeButton(UI.getStr_Cancel(cx), clickListener);
		builder.setPositiveButton(UI.getStr_Ok(cx), clickListener);

		AlertDialog dlg = builder.create();
		dlg.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				EventUtils.postEvent(new PageChangedEvent(new String("dialogshow")));
			}
		});
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				EventUtils.postEvent(new PageChangedEvent(new String("dialogdismiss")));
			}
		});
		dlg.show();
	}
}
