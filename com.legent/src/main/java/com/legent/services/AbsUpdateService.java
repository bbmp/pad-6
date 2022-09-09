package com.legent.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.legent.DeleteFileUtils;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;

abstract public class AbsUpdateService {

	protected Object[] params;
	private SharedPreferences sp;

	public synchronized void start(Context cx, Object... params) {
		this.params = params;
		sp = PreferenceManager.getDefaultSharedPreferences(cx);

		//每次升级前删除之前下载好的apk
		String  apkPath =  sp.getString("newVersionPath",null);
		LogUtils.e("20190115","apkPath:" + apkPath);
		if(null != apkPath){
			FileUtils.deleteFile(apkPath);
			DeleteFileUtils.rmoveFile(apkPath);
		}

		checkVersion(cx, new CheckVersionListener() {

			@Override
			public void onWithoutNewest() {
				onWithout();
			}

			@Override
			public void onWithNewest(String downUrl, Object... params) {
				sp.edit().putBoolean("isDowning",true).commit();
				sp.edit().putBoolean("isDownComplete",false).commit();
				onNewest(downUrl, params);
			}

			@Override
			public void onCheckFailure(Exception ex) {
				onFailure(ex);
			}
		});
	}

	protected void onNewest(String downUrl, Object... params) {
		// 有新版本
		download(downUrl,"");

	}

	protected void onWithout() {
		// 没有新版本
	}

	protected void onFailure(Exception ex) {
		// 检查版本出错
	}

	abstract public void checkVersion(Context cx, CheckVersionListener listener);

	abstract protected void download(String downUrl,String newVersionDes);

	public interface CheckVersionListener {
		void onWithNewest(String downUrl, Object... params);

		void onWithoutNewest();

		void onCheckFailure(Exception ex);
	}
}
