package com.legent.io.buses;

import android.util.Log;

import com.legent.LogTags;
import com.legent.VoidCallback;
import com.legent.io.AbsIONode;
import com.legent.services.TaskService;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

abstract public class AbsBus extends AbsIONode implements IBus {

	protected final static String TAG = LogTags.TAG_IO;
	protected final static long RECONNECT_DELAY = 1000 * 10;
	protected final static long RECONNECT_PERIOD = 1000 * 3;

	protected ScheduledFuture<?> reconnectFuture;

	@Override
	public synchronized void open(final VoidCallback callback) {
		if (isConnected) {
			onCallSuccess(callback);
		} else {
			onOpen(new VoidCallback() {

				@Override
				public void onSuccess() {
					Log.d(TAG, "open bus success:" + this);

					onCallSuccess(callback);
					onConnectionChanged(true);
				}

				@Override
				public void onFailure(Throwable t) {
					if(null != t){
						Log.e(TAG, "open bus failure: t" + t.toString());
						onCallFailure(callback, t);
					}
					startReconnect();
				}
			});
		}
	}

	@Override
	public void close(final VoidCallback callback) {

		stopReconnect();

		if (!isConnected) {
			onCallSuccess(callback);
		} else {
			onClose(new VoidCallback() {

				@Override
				public void onSuccess() {
					Log.d(TAG, "close bus success:" + this);

					onCallSuccess(callback);
					onConnectionChanged(false);
				}

				@Override
				public void onFailure(Throwable t) {
					Log.e(TAG, "close bus failure:" + this);

					onCallFailure(callback, t);
				}
			});
		}
	}

	protected void startReconnect() {

		if (reconnectFuture != null)
			return;

		reconnectFuture = TaskService.getInstance().scheduleWithFixedDelay(
				new Runnable() {

					@Override
					public void run() {
						if (isConnected) {
							Log.e(TAG, "bus重连成功，退出重连定时器");
							stopReconnect();
						} else {
							Log.e(TAG, "bus尝试重连");
							open(null);
						}
					}
				}, RECONNECT_DELAY, RECONNECT_PERIOD, TimeUnit.MILLISECONDS);
	}

	protected void stopReconnect() {
		if (reconnectFuture != null) {
			reconnectFuture.cancel(true);
			reconnectFuture = null;
		}
	}
}
