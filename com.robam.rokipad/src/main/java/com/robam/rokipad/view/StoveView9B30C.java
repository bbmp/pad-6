package com.robam.rokipad.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.ui.page.StoveCommand;
import com.robam.rokipad.utils.CallBackCommand;
import com.robam.rokipad.utils.DataCreateUtils;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.IClickStoveSelectListener;
import com.robam.rokipad.utils.IStove;
import com.robam.rokipad.utils.IStoveSendCommand;
import com.robam.rokipad.utils.IStoveTag;
import com.robam.rokipad.utils.ToolUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2019/2/12.
 */

//此类主要进行业务逻辑的编写（因为每个灶具的业务逻辑都不相同，因此进行分开处理，方便理解）
public class StoveView9B30C extends FrameLayout implements IStove {
    Context cx;
    View viewRoot;
    @InjectView(R.id.view_left)
    Stove9B30ChildrenView viewLeft;
    @InjectView(R.id.view_right)
    Stove9B30ChildrenView viewRight;
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
    Stove[] stove;
    private IRokiDialog mTimeDialog;

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


    public StoveView9B30C(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StoveView9B30C(Context context) {
        super(context);
        initView(context, null);
    }


    public StoveView9B30C(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_view_9b30, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        LogUtils.e("20190528", "!viewRoot.isInEditMode():" + !viewRoot.isInEditMode());
        stove = Utils.getDefaultStove();
        init();
    }


    private void init() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen((Activity) cx, stove[0].getDt() + ":" + cx.getString(R.string.google_screen_stove_home), null);
        stoveCom = new StoveCommand(cx);
        lockStart.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("20190311", "view++");
                lockStart.startWaveTwo();
                return true;
            }
        });
        lockStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stove = Utils.getDefaultStove();
                if (null == stove[0]) return;
                lockStart.startWaveOne();
                ToolUtils.logEvent(stove[0].getDt(), cx.getString(R.string.google_screen_stove_lock) + "关"
                        , cx.getString(R.string.google_screen_name));
                stoveCom.setLockStatus(false, null);
            }
        });
        lockLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stove = Utils.getDefaultStove();
                if (null == stove[0]) return;
                ToolUtils.logEvent(stove[0].getDt(), cx.getString(R.string.google_screen_stove_lock) + "开"
                        , cx.getString(R.string.google_screen_name));
                stoveCom.setLockStatus(true, null);
            }
        });
        viewLeft.setHeadName(R.string.stove_head_left);
        viewRight.setHeadName(R.string.stove_head_right);
        viewLeft.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                Log.e("20190212", "tag:::" + tag);
                dealEvent(Stove.StoveHead.LEFT_ID, tag);
            }
        });

        viewRight.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                Log.e("20190212", "tag:::" + tag);
                dealEvent(Stove.StoveHead.RIGHT_ID, tag);
            }
        });

    }


    private void dealEvent(final Short headId, String tag) {
        stove = Utils.getDefaultStove();
        if (null == stove[0]) return;
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
        switch (tag) {
            case IStoveTag.add:
                Stove.StoveHead headById = stove[0].getHeadById(headId);
                if (5 == headById.level) {
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
                    if (stove[0] != null) {
                        if (!stove[0].isConnected()) {
                            ToastUtils.showShort(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].leftHead.level == 0) {
                            ToastUtils.show(R.string.dialog_not_level);
                            return;
                        }
                    }
                }

                if (headId == Stove.StoveHead.RIGHT_ID) {
                    if (stove != null) {

                        if (!stove[0].isConnected()) {
                            ToastUtils.show(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].rightHead.level == 0) {
                            ToastUtils.show(R.string.dialog_not_level);
                            return;
                        }
                    }
                }

                if (headId == Stove.StoveHead.LEFT_ID) {
                    if (stove != null) {

                        if (!stove[0].isConnected()) {
                            ToastUtils.showShort(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].leftHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                        if (stove[0].leftHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                    }
                }

                if (headId == Stove.StoveHead.RIGHT_ID) {
                    if (stove != null) {

                        if (!stove[0].isConnected()) {
                            ToastUtils.showShort(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].rightHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.device_open_stove);
                            return;
                        }
                        if (stove[0].rightHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.device_open_stove);
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

    boolean leftPowerFlag;
    boolean rightPowerFlag;

    private void powerStatus(short headId) {


        switch (headId) {
            case Stove.StoveHead.LEFT_ID:
                ToolUtils.logEvent(stove[0].getDt(), headId == 0 ? "左炉头" : "右炉头"
                                + cx.getString(R.string.google_screen_stove_close) + (leftPowerFlag ? "开" : "关")
                        , cx.getString(R.string.google_screen_name));
                viewLeft.switchPowerStatus(leftPowerFlag);
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
                viewLeft.disConnected();
                viewLeft.setDevice(stove);
                break;
            case StoveStatus.StandyBy://待机
                leftPowerFlag = true;

                break;
            case StoveStatus.Working://工作
                viewLeft.setLeftWorkStatus(level);
                viewLeft.setDevice(stove);
                break;
        }

    }

    @Override
    public void setRightStatus(Stove stove) {
        LogUtils.e("20190518", "stove:" + stove);
        if (stove == null) return;
        short status = stove.getHeadById(Stove.StoveHead.RIGHT_ID).status;
        short level = stove.getHeadById(Stove.StoveHead.RIGHT_ID).level;
        this.stove[0] = stove;
        stoveCom.setDevice(stove);
        switch (status) {
            case StoveStatus.Off://关机
                rightPowerFlag = false;
                viewRight.disConnected();
                viewRight.setDevice(stove);
                break;
            case StoveStatus.StandyBy://待机
                rightPowerFlag = true;
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
        if (pojo.isLock) {
            lockLayout.setVisibility(GONE);
            lockStartLayout.setVisibility(VISIBLE);
        } else {
            lockLayout.setVisibility(VISIBLE);
            stoveLock.setImageResource(R.mipmap.stove_lock_white);
            lockStartLayout.setVisibility(GONE);
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
            lockLayout.setVisibility(VISIBLE);
            stoveLock.setImageResource(R.mipmap.stove_lock_gray);
            viewLeft.disConnected();
            viewRight.disConnected();
        }
    }


}
