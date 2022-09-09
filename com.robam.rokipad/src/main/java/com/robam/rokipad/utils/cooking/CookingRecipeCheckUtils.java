package com.robam.rokipad.utils.cooking;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.fan.AbsFan;

import java.util.List;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 检查菜谱是否又对应的设备类.
 */
public class CookingRecipeCheckUtils {
    private static final String TAG = "CookingRecipeCheckUtils";

    private static boolean isHaveAllDevices = false;

    /**
     * 检查当前这道菜  所要用到的设备是否齐全
     */
    public static boolean checkRecipeIsDevice(Recipe recipe) {

        List<IDevice> devices = Plat.deviceService.queryDevices();  //目前有的设备
        if (null != recipe) {
            List<Dc> needDcs = recipe.getJs_dcs();                    //做这道菜需要的设备
            for (Dc dcItem : needDcs) {
                if (dcItem.dc.contains("||")) {
                    String dcs = dcItem.dc;
                    LogUtils.e(TAG, "dcs:" + dcs);
                    String[] split = dcs.split("\\|\\|");
                    for (int i = 0; i < split.length; i++) {
                        for (IDevice iDevice : devices) {
                            if (iDevice.getDc().equals(split[i])) {
                                isHaveAllDevices = true;
                                break;
                            }
                        }
                        if (devices.contains(split[i])) {
                            isHaveAllDevices = true;
                            break;
                        } else {
                            isHaveAllDevices = false;
                        }
                    }
                    if (!isHaveAllDevices) {
                        break;
                    }

                } else {
                    for (IDevice iDevice : devices) {
                        if (iDevice instanceof AbsFan) {
                            AbsFan absFan = (AbsFan) iDevice;
                            List<IDevice> childList = absFan.getChildList();
                            for (int i = 0; i < childList.size(); i++) {
                                LogUtils.e(TAG, "childList:" + childList.get(i).getDc());
                                if (dcItem.dc.equals(childList.get(i).getDc())) {
                                    isHaveAllDevices = true;
                                    break;
                                } else {
                                    isHaveAllDevices = false;
                                }
                            }
                        } else {
                            if (iDevice.getDc().equals(dcItem.dc)) {
                                isHaveAllDevices = true;
                                break;
                            } else {
                                isHaveAllDevices = false;
                            }
                        }
                    }
                }
            }
            return isHaveAllDevices;
        }
        return isHaveAllDevices;
    }
}
