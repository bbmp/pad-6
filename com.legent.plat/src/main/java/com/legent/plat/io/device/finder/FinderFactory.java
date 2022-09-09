package com.legent.plat.io.device.finder;

import com.legent.plat.io.device.IDeviceFinder;

public class FinderFactory {

	/**
	 * 庆科模块
	 */
	static public final int MODULE_MXCHIP = 0;

	/**
	 * 汉丰模块
	 */
	static public final int MODULE_HF = 1;

	static public IDeviceFinder getDefault() {
		return getFinder(MODULE_MXCHIP);
	}

	static public IDeviceFinder getFinder() {
		return getFinder(MODULE_MXCHIP);
	}

	/**
	 * 根据wifi模块型号获取相应的激活器
	 * 
	 * @param moduleCode
	 * @return
	 */
	static public IDeviceFinder getFinder(int moduleCode) {

		switch (moduleCode) {
		case MODULE_MXCHIP:
			return new MxchipFinder();
		case MODULE_HF:
			return new MxchipFinder();

		default:
			return new MxchipFinder();
		}
	}
}
