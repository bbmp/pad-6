package com.robam.rokipad.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.StoveAlarmManager;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.service.ConvenientCookService;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.page.StoveCommand;
import com.robam.rokipad.utils.CallBackCommand;
import com.robam.rokipad.utils.DataCreateUtils;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.IClickStoveSelectListener;
import com.robam.rokipad.utils.ILongClickStoveSelectListener;
import com.robam.rokipad.utils.IStove;
import com.robam.rokipad.utils.IStoveSendCommand;
import com.robam.rokipad.utils.IStoveTag;
import com.robam.rokipad.utils.ToolUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.rokipad.NewPadApp.isRecipeRun;

public class StoveView9W851 extends FrameLayout implements IStove {
    private static final String TAG = "StoveView9W851";

    private Context context;
    private View viewRoot;

    @InjectView(R.id.view_left)
    Stove9W851ChildrenView viewLeft;
    @InjectView(R.id.view_right)
    Stove9W851ChildrenView viewRight;
    @InjectView(R.id.stove_lock)
    ImageView stoveLock;
    IStoveSendCommand stoveCom = null;
    @InjectView(R.id.lock_txt)
    TextView lockTxt;
    @InjectView(R.id.lock_layout)
    RelativeLayout lockLayout;
    @InjectView(R.id.lock_start)
    RippleImageView lockStart;
    @InjectView(R.id.lock_start_layout)
    RelativeLayout lockStartLayout;
    @InjectView(R.id.iv_array_menu)
    ImageView array_menu;
    @InjectView(R.id.tv_convenient_menu)
    TextView tvConverientMenu;
    Stove[] stove;
    private IRokiDialog mTimeDialog;
    private IRokiDialog mConveniengDialog;
    private IRokiDialog mCancelTimeDialog;
    private static final int SET_TIME_COUNT = 1001;
    private ConvenientCookService convenientCookService = ConvenientCookService.getInstance();

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_TIME_COUNT:
                    setStoveTime((short) msg.arg1, (String) msg.obj);
                    break;
            }

        }
    };


    public StoveView9W851(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StoveView9W851(Context context) {
        super(context);
        initView(context, null);
    }


    public StoveView9W851(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        viewRoot = LayoutInflater.from(this.context).inflate(
                R.layout.stove_view_9w851, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        stove = Utils.getDefaultStove();
        initEvent();
    }


    private void initEvent() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen((Activity) context, stove[0].getDt() + ":" + context.getString(R.string.google_screen_stove_home), null);
        stoveCom = new StoveCommand(context);
        lockStart.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtils.e(TAG, "LongClick lockStart");
                lockStart.startWaveTwo();
                return true;
            }
        });
        lockStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lockStart.startWaveOne();
                ToolUtils.logEvent(stove[0].getDt(), context.getString(R.string.google_screen_stove_lock) + context.getString(R.string.children_lock_close)
                        , context.getString(R.string.google_screen_name));
                stove[0].setStoveLock(false, null);
            }
        });
        lockLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stove[0].isConnected()) {
                    ToastUtils.show(R.string.device_off_line);
                    return;
                }

                if (stove[0].leftHead.status == StoveStatus.Off && stove[0].rightHead.status == StoveStatus.Off) {
                    ToastUtils.show(R.string.device_open_stove);
                    return;
                }
                stoveCom.setLockStatus(true, null);
            }
        });
        viewLeft.setHeadName(R.string.stove_head_left);
        viewRight.setHeadName(R.string.stove_head_right);
        viewLeft.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                dealEvent(Stove.StoveHead.LEFT_ID, tag);
            }
        });

        viewLeft.setLongClickStoveSelectListener(new ILongClickStoveSelectListener() {
            @Override
            public void longClickStoveSelectListener(String tag) {
                dealLongClick(Stove.StoveHead.LEFT_ID,tag);
            }
        });

        viewRight.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                dealEvent(Stove.StoveHead.RIGHT_ID, tag);
            }
        });

        viewRight.setLongClickStoveSelectListener(new ILongClickStoveSelectListener() {
            @Override
            public void longClickStoveSelectListener(String tag) {
                dealLongClick(Stove.StoveHead.RIGHT_ID, tag);
            }
        });

        array_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toConvenientMenu();
            }
        });

        tvConverientMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toConvenientMenu();
            }
        });

    }

    private void toConvenientMenu() {
        //童锁开启
        if (stove[0].isLock) {
            lockStart.startWaveOne();
            return;
        }

        if (!stove[0].isConnected()) {
            ToastUtils.show("灶具已离线");
        }

        if (Utils.getDefaultStove() != null) {
            if (Utils.getDefaultStove()[0].isLock) {
                lockStart.startWaveOne();
                return;
            }
        }
        if (convenientCookService.isConvenientCook()) {
            ToastUtils.show("便捷菜谱烹饪中");
        } else {
            UIService.getInstance().postPage(PageKey.ConvenientMenu);
        }
    }

    private void dealLongClick(final Short headId, String tag) {
        //童锁开启
        if (stove[0].isLock) {
            lockStart.startWaveOne();
            return;
        }
        if (Utils.getDefaultStove() != null) {
            if (Utils.getDefaultStove()[0].isLock) {
                lockStart.startWaveOne();
                return;
            }
        }
        if (isConveniengMode()) {
            return;
        }
        switch (tag) {
            case IStoveTag.STOVE_MAX_LEVEL:
                Stove.StoveHead headById = stove[0].getHeadById(headId);
                LogUtils.e(TAG, "add headById:" + headById.toString());
                if (10 == headById.level) {
                    ToastUtils.show(R.string.dialog_stove_big_level);
                    return;
                }

                if (stove[0].getHeadById(headId).status == StoveStatus.Off) {
                    ToastUtils.showShort(R.string.device_open_stove);
                    return;
                }

                stove[0].setStoveLevel(false, headById.ihId, (short) 10, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.e(TAG, "MAX Level set success");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.e(TAG, "max Level set onFailure");
                    }
                });

                break;
            case IStoveTag.STOVE_MIN_LEVEL:
                Stove.StoveHead headByIdDecrease = stove[0].getHeadById(headId);
                LogUtils.e(TAG, "decrease headById:" + headByIdDecrease.toString());
                if (1 == headByIdDecrease.level) {
                    ToastUtils.show(R.string.dialog_stove_small_level);
                    return;
                }
                if (stove[0].getHeadById(headId).status == StoveStatus.Off) {
                    ToastUtils.showShort(R.string.device_open_stove);
                    return;
                }
                stove[0].setStoveLevel(false, headByIdDecrease.ihId, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.e(TAG, "Min Level set success");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.e(TAG, "Min Level set onFailure");
                    }
                });
                break;
        }

    }

    private void dealEvent(final Short headId, String tag) {
        //模拟童锁开启
        if (stove[0].isLock) {
            lockStart.startWaveOne();
            return;
        }
        if (Utils.getDefaultStove() != null) {
            if (Utils.getDefaultStove()[0].isLock) {
                lockStart.startWaveOne();
                return;
            }
        }
        if (isConveniengMode()) {
            return;
        }
        switch (tag) {
            case IStoveTag.add:
                Stove.StoveHead headById = stove[0].getHeadById(headId);
                LogUtils.e(TAG, "add headById:" + headById.toString());
                if (10 == headById.level) {
                    ToastUtils.show(R.string.dialog_stove_big_level);
                    return;
                }
                stoveCom.add(headId, new CallBackCommand() {
                    @Override
                    public void success() {
                        ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? context.getString(R.string.left_stovehead) : context.getString(R.string.right_stovehead) + context.getString(R.string.google_screen_stove_level_add)
                                , context.getString(R.string.google_screen_name));
                    }

                    @Override
                    public void fail(Throwable t) {

                    }
                });
                break;
            case IStoveTag.decrease:
                Stove.StoveHead headByIdDecrease = stove[0].getHeadById(headId);
                LogUtils.e(TAG,"decrease headById:"+headByIdDecrease.toString());
                if (1 == headByIdDecrease.level) {
                    ToastUtils.show(R.string.dialog_stove_small_level);
                    return;
                }
                stoveCom.decrease(headId, new CallBackCommand() {
                    @Override
                    public void success() {
                        ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? context.getString(R.string.left_stovehead) : context.getString(R.string.right_stovehead) + context.getString(R.string.google_screen_stove_level_decrease)
                                , context.getString(R.string.google_screen_name));
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
                mTimeDialog = RokiDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_01);
                mTimeDialog.setUnitName(R.string.setting_model_min_text);
                mTimeDialog.setWheelViewData(DataCreateUtils.create9w851StoveTimer(), false, 29, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = SET_TIME_COUNT;
                        msg.arg1 = headId;
                        msg.obj = contentCenter;
                        mHandler.sendMessage(msg);
                    }
                });
                mTimeDialog.show();
                break;
            case IStoveTag.time_cancel:
                LogUtils.e(TAG, "time_cancel");
                mCancelTimeDialog = RokiDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_0);
                mCancelTimeDialog.setContentText(R.string.cancel_time_text);
                mCancelTimeDialog.setOkBtn(R.string.continue_time, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCancelTimeDialog.isShow()) {
                            mCancelTimeDialog.dismiss();
                            stoveCom.setTime(headId, (short) 0, new CallBackCommand() {
                                @Override
                                public void success() {
                                    LogUtils.e(TAG, "cancel time success");
                                }

                                @Override
                                public void fail(Throwable t) {
                                    LogUtils.e(TAG, "cancel time fail");
                                }
                            });

                        }
                    }
                });
                mCancelTimeDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCancelTimeDialog.isShow()) {
                            mCancelTimeDialog.dismiss();
                        }
                    }
                });
                mCancelTimeDialog.show();
                break;
        }
    }

    private boolean isConveniengMode() {
        LogUtils.e(TAG, "ConvenientCook:" + convenientCookService.isConvenientCook());
        if (convenientCookService.isConvenientCook()) {
            if (mConveniengDialog == null) {
                mConveniengDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
            }
            mConveniengDialog.setOkBtn(R.string.dialog_sure, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mConveniengDialog.isShow()) {
                        mConveniengDialog.dismiss();
                    }
                }
            });
            mConveniengDialog.setCancelBtn(R.string.dialog_exit, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    convenientCookService.stop();
                    LogUtils.e(TAG, "stop convenientCook Service");
                    if (mConveniengDialog.isShow()) {
                        mConveniengDialog.dismiss();
                    }
                }
            });
            mConveniengDialog.show();
        }
        return convenientCookService.isConvenientCook();
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
                ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? context.getString(R.string.left_stovehead) : context.getString(R.string.right_stovehead)
                                + context.getString(R.string.google_screen_stove_close) + (leftPowerFlag ? context.getString(R.string.power_open) : context.getString(R.string.power_close))
                        , context.getString(R.string.google_screen_name));
                viewLeft.switchPowerStatus(leftPowerFlag);
                LogUtils.e(TAG, "viewLeft:");
                break;
            case Stove.StoveHead.RIGHT_ID:
                ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? context.getString(R.string.left_stovehead) : context.getString(R.string.right_stovehead)
                                + context.getString(R.string.google_screen_stove_close) + (rightPowerFlag ? context.getString(R.string.power_open) : context.getString(R.string.power_close))
                        , context.getString(R.string.google_screen_name));
                viewRight.switchPowerStatus(rightPowerFlag);
                LogUtils.e(TAG, "viewRight:");
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
        LogUtils.e(TAG,"LeftStatus level:"+level+" status:"+status);
        this.stove[0] = stove;
        stoveCom.setDevice(stove);
        switch (status) {
            case StoveStatus.Off://关机
                leftPowerFlag = false;
                viewLeft.setOffStatus();
                viewLeft.setDevice(stove);
                break;
            case StoveStatus.StandyBy://待机
                leftPowerFlag = true;
                viewLeft.setStandyByStatus();
                viewLeft.setDevice(stove);
                break;
            case StoveStatus.Working://工作
                viewLeft.setLeftWorkStatus(level);
                viewLeft.setDevice(stove);
                break;
        }

    }

    @Override
    public void setRightStatus(Stove stove) {
        if (stove == null) return;
        short status = stove.getHeadById(Stove.StoveHead.RIGHT_ID).status;
        short level = stove.getHeadById(Stove.StoveHead.RIGHT_ID).level;
        LogUtils.e(TAG,"rightstatus level:"+level+" status:"+status);
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
        if (pojo.leftHead.status == StoveStatus.Off && pojo.rightHead.status == StoveStatus.Off) {
            lockLayout.setVisibility(VISIBLE);
            stoveLock.setImageResource(R.mipmap.stove_lock_gray);
            lockStartLayout.setVisibility(GONE);
        } else {
            if (pojo.isLock) {
                lockLayout.setVisibility(GONE);
                lockStartLayout.setVisibility(VISIBLE);
            } else {
                lockLayout.setVisibility(VISIBLE);
                stoveLock.setImageResource(R.mipmap.stove_unlock_gray);
                lockStartLayout.setVisibility(GONE);
            }
        }
    }

    //灶具定时的指令
    private void setStoveTime(final short headId, final String data) {
        mTimeDialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeDialog != null && mTimeDialog.isShow()) {
                    mTimeDialog.dismiss();
                    short seconds = Short.parseShort(data);
                    LogUtils.e(TAG, "seconds:" + seconds);
                    ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? context.getString(R.string.left_stovehead) : context.getString(R.string.right_stovehead)
                                    + context.getString(R.string.google_screen_stove_time_close) + seconds
                            , context.getString(R.string.google_screen_name));
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
            lockLayout.setVisibility(VISIBLE);
            stoveLock.setImageResource(R.mipmap.stove_unlock_gray);
            viewLeft.disConnected();
            viewRight.disConnected();
        }
    }


}
