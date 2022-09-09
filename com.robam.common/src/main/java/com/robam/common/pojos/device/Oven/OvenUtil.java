package com.robam.common.pojos.device.Oven;

/**
 * Created by Rent on 2016/7/15.
 */
public class OvenUtil {
    public static String getOvenModel(int index) {
        String str = new String();
        switch (index) {
            case 130:
                str = new String("快热");
                break;
            case 132:
                str = new String("风焙烤");
                break;
            case 134:
                str = new String("焙烤");
                break;
            case 136:
                str = new String("底加热");
                break;
            case 138:
                str = new String("解冻");
                break;
            case 140:
                str = new String("风扇烤");
                break;
            case 142:
                str = new String("烤烧");
                break;
            case 144:
                str = new String("强烤烧");
                break;
            default:break;
        }
        return str;
    }
}
