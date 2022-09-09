package com.robam.rokipad.view;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.StoveAlarmManager;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.service.pidcontroltemperature.MiniPID;
import com.robam.rokipad.ui.page.StoveCommand;
import com.robam.rokipad.utils.CallBackCommand;
import com.robam.rokipad.utils.DataCreateUtils;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.IClickStoveSelectListener;
import com.robam.rokipad.utils.IStove;
import com.robam.rokipad.utils.IStoveSendCommand;
import com.robam.rokipad.utils.IStoveTag;
import com.robam.rokipad.utils.ToolUtils;
import com.robam.rokipad.view.commonview.ProgressLayout;

import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2019/2/12.
 */

//此类主要进行业务逻辑的编写（因为每个灶具的业务逻辑都不相同，因此进行分开处理，方便理解）
public class StoveView9W70_PID extends FrameLayout implements IStove {
    private static final String TAG = "StoveView9W70_PID";
    Context cx;
    View viewRoot;
    @InjectView(R.id.view_right)
    Stove9W70ChildrenView viewRight;
    @InjectView(R.id.set_temperature_value)
    SeekBar setTemperature;
    @InjectView(R.id.tv_temperature)
    TextView tv_temperature;
    IStoveSendCommand stoveCom = null;
    Stove[] stove;
    private IRokiDialog mTimeDialog;
    private ProgressLayout mProgress;
    private MiniPID miniPID = new MiniPID(0.25f, 0.01f, 0.04f);
    private float targetTemperature = 60;

    private float actualTemperature = 0;
    private float output = 0;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setStoveTime((short) msg.arg1, (String) msg.obj);
                    break;
            }

        }
    };

    public StoveView9W70_PID(Context context) {
        super(context);
        initView(context);

    }

    public StoveView9W70_PID(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoveView9W70_PID(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView(Context context) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_view_9w70_pid, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        stove = Utils.getDefaultStove();
        mProgress = viewRoot.findViewById(R.id.mProgress);
        init();
        short level = stove[0].getHeadById(Stove.StoveHead.RIGHT_ID).level;
        mProgress.setTemperature(0, 0);
        setTemperature.setOnSeekBarChangeListener(progressChangedListener);
        miniPID.setOutputLimits(3);
        miniPID.setSetpointRange(40);
        miniPID.setSetpoint(0);
        miniPID.setSetpoint(targetTemperature);
    }

    private SeekBar.OnSeekBarChangeListener progressChangedListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            LogUtils.e(TAG, "onProgressChanged progress:" + progress);
            tv_temperature.setText(String.valueOf(progress) + "℃");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            int temperature = seekBar.getProgress();
            LogUtils.e(TAG, "onStartTrackingTouch temperature:" + temperature);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            LogUtils.e(TAG, "onStopTrackingTouch progress:" + progress);
            targetTemperature = progress;

        }

    };

    private void init() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen((Activity) cx, stove[0].getDt() + ":" + cx.getString(R.string.google_screen_stove_home), null);
        stoveCom = new StoveCommand(cx);
        stove = Utils.getDefaultStove();

        viewRight.setHeadName(R.string.stove_head_right);

        viewRight.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                dealEvent(Stove.StoveHead.RIGHT_ID, tag);
            }
        });

    }

    private void dealEvent(final Short headId, String tag) {
        stove = Utils.getDefaultStove();
        if (null == stove[0]) return;
        LogUtils.e(TAG, "dealEvent stove[]" + stove.hashCode() + " isConnected:" + stove[0].isConnected());
        //模拟童锁开启
        if (stove[0].isLock) {

            return;
        }
        if (Utils.getDefaultStove() != null) {
            if (Utils.getDefaultStove()[0].isLock) {
                return;
            }
        }
        switch (tag) {
            case IStoveTag.add:
                Stove.StoveHead headById = stove[0].getHeadById(headId);
                if (9 == headById.level) {
                    ToastUtils.show(R.string.dialog_stove_big_level);
                    return;
                }
                stoveCom.add(headId, new CallBackCommand() {
                    @Override
                    public void success() {
                        ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头" + cx.getString(R.string.google_screen_stove_level_add)
                                , cx.getString(R.string.google_screen_name));
                    }

                    @Override
                    public void fail(Throwable t) {

                    }
                });
                break;
            case IStoveTag.decrease:
                Stove.StoveHead headByIdDecrease = stove[0].getHeadById(headId);
                if (1 == headByIdDecrease.level) {
                    ToastUtils.show(R.string.dialog_stove_small_level);
                    return;
                }
                stoveCom.decrease(headId, new CallBackCommand() {
                    @Override
                    public void success() {
                        ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头" + cx.getString(R.string.google_screen_stove_level_decrease)
                                , cx.getString(R.string.google_screen_name));
                    }

                    @Override
                    public void fail(Throwable t) {

                    }
                });
                break;
            case IStoveTag.power:
                stoveCom.setPower(headId, new CallBackCommand() {
                    @Override
                    public void success() {
                        powerStatus(headId);
                    }

                    @Override
                    public void fail(Throwable t) {

                    }
                });
                break;
            case IStoveTag.time:

                if (headId == Stove.StoveHead.LEFT_ID) {
                    if (stove != null) {

                        if (!stove[0].isConnected()) {
                            ToastUtils.show(R.string.device_off_line);
                            return;
                        }
                        if (stove[0].leftHead.level == 0) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }


                    }
                }

                if (headId == Stove.StoveHead.RIGHT_ID) {
                    if (stove != null) {
                        if (!stove[0].isConnected()) {
                            ToastUtils.show(R.string.device_off_line);
                            return;
                        }
                        if (stove[0].rightHead.level == 0) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                    }
                }

                if (headId == Stove.StoveHead.LEFT_ID) {
                    if (stove != null) {
                        if (!stove[0].isConnected()) {
                            ToastUtils.showShort(R.string.device_off_line);
                            return;
                        }
                        if (stove[0].leftHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                        if (stove[0].leftHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.dialog_ple_on_stove_level);
                            return;
                        }

                        if (potIsLeaveStove(stove[0], headId)) {
                            return;
                        }
                    }
                }

                if (headId == Stove.StoveHead.RIGHT_ID) {
                    if (stove != null) {
                        if (!stove[0].isConnected()) {
                            ToastUtils.showShort(R.string.device_off_line);
                            return;
                        }

                        if (stove[0].rightHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                        if (stove[0].rightHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.dialog_ple_on_stove_level);
                            return;
                        }
                        if (potIsLeaveStove(stove[0], headId)) {
                            return;
                        }
                    }
                }
                mTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
                mTimeDialog.setUnitName(R.string.setting_model_min_text);
                mTimeDialog.setWheelViewData(DataCreateUtils.createStoveTimer(), false, 29, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.arg1 = headId;
                        msg.obj = contentCenter;
                        mHandler.sendMessage(msg);
                    }
                });
                mTimeDialog.show();
                break;
        }
    }

    /**
     * 判断是否是锅灶分离状态
     */
    protected boolean potIsLeaveStove(Stove stove, int stoveHeadIndex) {
        short id = stove.getHeadById(stoveHeadIndex).alarmId;
        if (id == StoveAlarmManager.Key_None || id < 0)
            return false;

        StoveAlarm alarm = StoveAlarmManager.getInstance().queryById
                (stove.getHeadById(stoveHeadIndex).alarmId);
        if (alarm != null) {
            alarm.src = stove.getHeadById(stoveHeadIndex);
            if (id <= 6 || id == 8 || id == 9 || id == 13) {
                ToastUtils.showShort(alarm.getName());
                return true;
            }
        }
        return false;
    }

    boolean leftPowerFlag;
    boolean rightPowerFlag;

    private void powerStatus(short headId) {

        switch (headId) {
            case Stove.StoveHead.LEFT_ID:
                ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头"
                                + cx.getString(R.string.google_screen_stove_close) + (leftPowerFlag ? "开" : "关")
                        , cx.getString(R.string.google_screen_name));
                LogUtils.e("20190518", "viewLeft:");
                break;
            case Stove.StoveHead.RIGHT_ID:
                ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头"
                                + cx.getString(R.string.google_screen_stove_close) + (rightPowerFlag ? "开" : "关")
                        , cx.getString(R.string.google_screen_name));
                viewRight.switchPowerStatus(rightPowerFlag);
                LogUtils.e("20190518", "viewRight:");
                break;
            default:
                break;
        }
    }


    @Override
    public void setLeftStatus(Stove stove) {
        if (stove == null) return;
        short status = stove.getHeadById(Stove.StoveHead.LEFT_ID).status;
        short level = stove.getHeadById(Stove.StoveHead.LEFT_ID).level;
        this.stove[0] = stove;
        stoveCom.setDevice(stove);
        switch (status) {
            case StoveStatus.Off://关机
                leftPowerFlag = false;
                break;
            case StoveStatus.StandyBy://待机
                leftPowerFlag = true;
                break;
            case StoveStatus.Working://工作

                break;
        }

    }

    @Override
    public void setRightStatus(Stove stove) {
        if (stove == null) return;
        short status = stove.getHeadById(Stove.StoveHead.RIGHT_ID).status;
        short level = stove.getHeadById(Stove.StoveHead.RIGHT_ID).level;
        this.stove[0] = stove;
        stoveCom.setDevice(stove);
        switch (status) {
            case StoveStatus.Off://关机
                rightPowerFlag = false;
                viewRight.setOffStatus();
                viewRight.setDevice(stove);
                break;
            case StoveStatus.StandyBy://待机
                rightPowerFlag = true;
                viewRight.setStandyByStatus();
                viewRight.setDevice(stove);
                break;
            case StoveStatus.Working://工作
                viewRight.setRightWorkStatus(level);
                viewRight.setDevice(stove);
                break;
        }
    }

    @Override
    public void setLockStatus(Stove pojo) {
        this.stove[0] = pojo;
        stoveCom.setDevice(stove[0]);

    }


    //灶具定时的指令
    private void setStoveTime(final short headId, final String data) {
        mTimeDialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeDialog != null && mTimeDialog.isShow()) {
                    mTimeDialog.dismiss();
                    short seconds = Short.parseShort(data);
                    ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头"
                                    + cx.getString(R.string.google_screen_stove_time_close) + seconds
                            , cx.getString(R.string.google_screen_name));
                    stove[0].setStoveShutdown(headId, (short)
                            (seconds * (short) 60), new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.show(R.string.device_stove_countDown_timing);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            }
        });

        mTimeDialog.setCancelBtn(R.string.cancel, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeDialog != null && mTimeDialog.isShow()) {
                    mTimeDialog.dismiss();
                }
            }
        });

    }

    //网络连接状况
    @Override
    public void isConnected(boolean isCon) {
        if (!isCon) {
            viewRight.disConnected();
        }
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }
        mProgress.setTemperature(event.pojo.tempUp, 180);
        actualTemperature = event.pojo.tempUp;
        output = miniPID.getOutput(actualTemperature, targetTemperature);
        actualTemperature = actualTemperature + output;
        LogUtils.e(TAG, "target:" + targetTemperature + " actual：" + actualTemperature + " output:" + output + " error:" + (targetTemperature - actualTemperature));
        if (output >= 0) {
            if (stove[0].getHeadById(Stove.StoveHead.RIGHT_ID).level != 5 && stove[0].getHeadById(Stove.StoveHead.RIGHT_ID).status != StoveStatus.Off) {
                stove[0].setStoveLevel(false, Stove.StoveHead.RIGHT_ID, (short) 5, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        } else if (output < 0) {
            if (stove[0].getHeadById(Stove.StoveHead.RIGHT_ID).level != 0 && stove[0].getHeadById(Stove.StoveHead.RIGHT_ID).status != StoveStatus.Off) {
                stove[0].setStoveLevel(false, Stove.StoveHead.RIGHT_ID, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }


    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        LogUtils.e(TAG, "onViewAdded");
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        LogUtils.e(TAG, "onViewRemoved");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.e(TAG, "onAttachedToWindow");
        EventUtils.regist(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        LogUtils.e(TAG, "onDetachedFromWindow");

    }


}
