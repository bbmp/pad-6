package com.robam.common.pojos.device.Oven;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

/**
 * Created by Administrator on 2016/11/8.
 */

public class Oven028 extends AbsOven {
    public Oven028(DeviceInfo devInfo) {
        super(devInfo);
    }


    /**
     * 设置多段
     */
    public void setOvenMoreMode(short prflag,short recipeId,short recipeStep,short argument,
                                short stage,short step1_mode,short step1_temp,short step1_time,
                                short step2_mode, short step2_temp,short step2_time,
                                final VoidCallback callback){
        try{
            Msg msg = newReqMsg(MsgKeys.Set_Oven_More_Cook);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.PreFlag,prflag);
            msg.putOpt(MsgParams.OvenRecipeId,recipeId);
            msg.putOpt(MsgParams.ArgumentNumber,argument);

            msg.putOpt(MsgParams.OvenStagekey,(short)1);
            msg.putOpt(MsgParams.OvenStageLength,(short)1);
            msg.putOpt(MsgParams.OvenStageValue,stage);

            msg.putOpt(MsgParams.OvenStep1Modekey,(short)2);
            msg.putOpt(MsgParams.OvenStep1ModeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1ModeValue,step1_mode);

            msg.putOpt(MsgParams.OvenStep1SetTempkey ,(short)3);
            msg.putOpt(MsgParams.OvenStep1SetTempLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1SetTempValue,step1_temp);

            msg.putOpt(MsgParams.OvenStep1SetTimekey ,(short)4);
            msg.putOpt(MsgParams.OvenStep1SetTimeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1SetTimeValue,step1_time);

            msg.putOpt(MsgParams.OvenStep2Modekey,(short)5);
            msg.putOpt(MsgParams.OvenStep2ModeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2ModeValue,step2_mode);

            msg.putOpt(MsgParams.OvenStep2SetTempkey,(short)6);
            msg.putOpt(MsgParams.OvenStep2SetTempLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2SetTempValue,step2_temp);

            msg.putOpt(MsgParams.OvenStep2SetTimekey,(short)7);
            msg.putOpt(MsgParams.OvenStep2SetTimeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2SetTimeValue,step2_time);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if(Plat.DEBUG)
                        LogUtils.i("20171030","resMsg:"+resMsg.toString());
                }
            });
        }catch (Exception e){

        }
    }
}
