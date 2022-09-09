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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
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
 * Created by Dell on 2019/2/19.
 */

public class StoveView9B37 extends FrameLayout implements IStove {
    private Context cx;
    View viewRoot;
    @InjectView(R.id.view_left)
    Stove9B37ChildrenView viewLeft;
    @InjectView(R.id.view_right)
    Stove9B37ChildrenView viewRight;
    private IStoveSendCommand stoveCom = null;
    private Stove[] stove;
    private IRokiDialog mTimeDialog;
    private boolean leftPowerFlag;
    private boolean rightPowerFlag;

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

    public StoveView9B37(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StoveView9B37(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public StoveView9B37(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(R.layout.stove_view_9b37, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        stove = Utils.getDefaultStove();
        init();
    }

    private void init() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen((Activity) cx, stove[0].getDt() + ":" + cx.getString(R.string.google_screen_stove_home), null);
        stoveCom = new StoveCommand(cx);
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

    private void dealEvent(final short headId, String tag) {
        stove = Utils.getDefaultStove();
        if (null == stove[0]) return;
        switch (tag) {
            case IStoveTag.time:
                if (headId == Stove.StoveHead.LEFT_ID) {
                    if (stove != null) {

                        boolean connected = stove[0].isConnected();
                        if (!connected) {
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
                    if (stove[0] != null) {

                        boolean connected = stove[0].isConnected();
                        if (!connected) {
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
                    if (stove[0] != null) {

                        boolean connected = stove[0].isConnected();
                        if (!connected) {
                            ToastUtils.show(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].leftHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.dialog_ple_on_stove);
                            return;
                        }
                        if (stove[0].leftHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.dialog_ple_on_stove_level);
                            return;
                        }
                    }
                }
                if (headId == Stove.StoveHead.RIGHT_ID) {
                    if (stove != null) {
                        boolean connected = stove[0].isConnected();
                        if (!connected) {
                            ToastUtils.show(R.string.stove_invalid_error);
                            return;
                        }

                        if (stove[0].rightHead.status == StoveStatus.Off) {
                            ToastUtils.show(R.string.dialog_ple_on_stove);
                            return;
                        }
                        if (stove[0].rightHead.status == StoveStatus.StandyBy) {
                            ToastUtils.show(R.string.dialog_ple_on_stove_level);
                            return;
                        }
                    }
                }
                mTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
                mTimeDialog.setUnitName(R.string.setting_model_min_text);
                mTimeDialog.setWheelViewData(DataCreateUtils.create9b37StoveTimer(), false, 29, new OnItemSelectedListenerCenter() {
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
                            ToastUtils.showShort(R.string.device_stove_countDown_timing);
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
                viewLeft.setOffStatus();
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
                break;
            case StoveStatus.Working://工作
                viewRight.setRightWorkStatus(level);
                viewRight.setDevice(stove);
                break;
        }
    }

    @Override
    public void isConnected(boolean isCon) {
        if (!isCon) {
            viewLeft.disConnected();
            viewRight.disConnected();
        }
    }

    @Override
    public void setLockStatus(Stove pojo) {

    }

}
