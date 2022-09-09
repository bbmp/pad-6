package com.robam.rokipad.ui.page;

import android.content.Context;
import android.content.res.Resources;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.StoveAlarmManager;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.CallBackCommand;
import com.robam.rokipad.utils.IStoveSendCommand;

/**
 * Created by Dell on 2019/2/25.
 */

public class StoveCommand implements IStoveSendCommand {

    Stove[] stove;
    Context cx;

    public StoveCommand(Context cx) {
        this.cx = cx;
        stove = Utils.getDefaultStove();
    }

    @Override
    public void setDevice(Stove stove) {
        this.stove[0] = stove;
    }

    //火力增加
    @Override
    public void add(int headIndex, CallBackCommand callback) {

        if (potIsLeaveStove(stove[0], headIndex)) {
            return;
        }
        if (stove[0].getHeadById(headIndex).status == StoveStatus.StandyBy) {
            onSetLevel(Stove.PowerLevel_5, headIndex, callback);
        } else {
            if (!checkDeviceIsOffLine())
                return;
            if (stove[0].getHeadById(headIndex).status == StoveStatus.Off) {
                ToastUtils.showShort(R.string.device_open_stove);
                return;
            }
            short level = stove[0].getHeadById(headIndex).level;
            if (stove[0].getStoveModel().equals(IRokiFamily._9W851)) {
                if (level < 10) {
                    level++;
                    onSetLevel(level, headIndex, callback);
                }
            } else {
                if (level < 9) {
                    level++;
                    onSetLevel(level, headIndex, callback);
                }
            }

        }
    }

    //火力减少
    @Override
    public void decrease(int headIndex, CallBackCommand callback) {
        if (potIsLeaveStove(stove[0], headIndex)) {
            return;
        }
        if (stove[0].getHeadById(headIndex).status == StoveStatus.StandyBy) {
            onSetLevel(Stove.PowerLevel_5, headIndex, callback);
        } else {
            if (!checkDeviceIsOffLine())
                return;

            if (stove[0].getHeadById(headIndex).status == StoveStatus.Off) {
                ToastUtils.showShort(R.string.device_open_stove);
                return;
            }
            short level = stove[0].getHeadById(headIndex).level;
            if (level > 1) {
                level--;
                onSetLevel(level, headIndex, callback);
            }
        }
    }

    //设置定时
    @Override
    public void setTime(short headId, short time, final CallBackCommand callBack) {
        stove[0].setStoveShutdown(headId, time, new VoidCallback() {
            @Override
            public void onSuccess() {
                callBack.success();
            }

            @Override
            public void onFailure(Throwable t) {
                callBack.fail(t);
            }
        });

    }

    //设置开关的档位
    @Override
    public void setPower(int stoveHeadIndex, CallBackCommand callBack) {
        onSetStatus(stoveHeadIndex, callBack);
    }

    //童锁状态设置
    @Override
    public void setLockStatus(boolean status, CallBackCommand callback) {
        if (!checkDeviceIsOffLine())
            return;
        stove[0].setStoveLock(status, null);
    }

    protected void onSetStatus(final int stoveHeadIndex, final CallBackCommand callBack) {
        if (!checkDeviceIsOffLine())
            return;
        Stove.StoveHead stoveHead = stove[0].getHeadById(stoveHeadIndex);
        short status;

        if (IRokiFamily.R9B39.equals(stove[0].getDt()) || IRokiFamily._9B39E.equals(stove[0].getDt())) {
            status = stoveHead.status == StoveStatus.Off ? StoveStatus.Working : StoveStatus.Off;
        } else {
            status = stoveHead.status == StoveStatus.Off ? StoveStatus.StandyBy : StoveStatus.Off;
        }
        stove[0].setStoveStatus(false, stove[0].getHeadById(stoveHeadIndex).ihId, status, new VoidCallback() {

            @Override
            public void onSuccess() {
                callBack.success();
            }

            @Override
            public void onFailure(Throwable t) {
                callBack.fail(t);
            }
        });
    }


    /**
     * 设置灶具档位
     */
    protected void onSetLevel(final short level, final int headIndex, final CallBackCommand callback) {
        if (!checkDeviceIsOffLine())
            return;

        stove[0].setStoveLevel(false, stove[0].getHeadById(headIndex).ihId, level, new VoidCallback() {

            @Override
            public void onSuccess() {
                callback.success();
            }

            @Override
            public void onFailure(Throwable t) {
                //ToastUtils.showThrowable(t);
                callback.fail(t);
            }
        });
    }


    /**
     * 检查设备是否离线
     */
    protected boolean checkDeviceIsOffLine() {
        try {
            Resources resources = cx.getResources();
            Preconditions.checkNotNull(stove[0], resources.getString(R.string.dev_invalid_error));
            Preconditions.checkState(stove[0].isConnected(), resources.getString(R.string.stove_invalid_error));
            return true;
        } catch (Exception e) {
            ToastUtils.showException(e);
            return false;
        }
    }

    /**
     * 判断是否是锅灶分离状态
     */
    protected boolean potIsLeaveStove(Stove stove, int stoveHeadIndex) {
        short id = stove.getHeadById(stoveHeadIndex).alarmId;
        if (id == StoveAlarmManager.Key_None || id < 0)
            return false;

        StoveAlarm alarm = StoveAlarmManager.getInstance().queryById(stove.getHeadById(stoveHeadIndex).alarmId);
        if (alarm != null) {
            alarm.src = stove.getHeadById(stoveHeadIndex);
            if (id <= 6 || id == 8 || id == 9 || id == 13) {
                ToastUtils.showShort(alarm.getName());
                return true;
            }
        }
        return false;
    }

}
