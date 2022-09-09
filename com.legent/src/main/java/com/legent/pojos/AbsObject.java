package com.legent.pojos;

import android.content.Context;

import com.legent.IDispose;
import com.legent.Initialization;
import com.legent.utils.EventUtils;

abstract public class AbsObject implements Initialization, IDispose {
	protected Context cx;
	protected Object[] params;

	@Override
	public void dispose() {
	}

	@Override
	public void init(Context cx, Object... params) {
		this.cx = cx;
		this.params = params;
	}

	protected void postEvent(Object event) {
		EventUtils.postEvent(event);
	}

}
