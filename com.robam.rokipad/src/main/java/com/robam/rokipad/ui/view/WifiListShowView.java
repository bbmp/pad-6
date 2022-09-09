package com.robam.rokipad.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.events.WifiConnectEvent;
import com.legent.plat.constant.PrefsKey;
import com.legent.services.TaskService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiNewUtils;
import com.legent.utils.api.WifiState;
import com.legent.utils.api.WifiUtils;
import com.robam.common.Utils;
import com.robam.rokipad.R;
import com.robam.rokipad.dialog.WifiAuthDialog;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.utils.OnItemSelectedListener;
import com.robam.rokipad.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.legent.utils.api.WifiUtils.getMng;

/**
 * Created by Dell on 2018/12/21.
 */

public class WifiListShowView extends FrameLayout {
    Context cx;
    @InjectView(R.id.wifi_cyc_list)
    RecyclerView wifiCycList;

    WifiUtils.WifiReceiver wifiReceiver;
    WifiAdapter wifiAdapter;

    //    boolean flag = false;
    @InjectView(R.id.wifi_control)
    ImageView wifiControl;
    @InjectView(R.id.wifi_name)
    TextView wifiName;
    @InjectView(R.id.wifi_lock)
    ImageView wifiLock;
    @InjectView(R.id.wifi_single)
    ImageView wifiSingle;

    @InjectView(R.id.pro)
    ImageView pro;
    @InjectView(R.id.show_select)
    ImageView showSelect;

    @InjectView(R.id.wifi_show)
    LinearLayout wifiShow;
    @InjectView(R.id.txt_desc)
    TextView txtDesc;
    @InjectView(R.id.selectS)
    RelativeLayout selectS;
    Animation imgAnimation;
    boolean isAnimationRun = false;
    private boolean mFlag;

    public interface OnCallBackSelect {
        void Select(ScanResult sr);
    }


    OnCallBackSelect onCallBackSelect;


    public void setOnCallBackSelect(OnCallBackSelect onCallBackSelect) {
        this.onCallBackSelect = onCallBackSelect;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LogUtils.e("20200324", "handleMessage case 0:");
                    ToastUtils.show(R.string.connectNetFail, Toast.LENGTH_SHORT);
                    stopPro();
                    break;
                case 1:
                    stopPro();
                    showSelect.setVisibility(View.GONE);
                    pro.setVisibility(View.GONE);
                    break;
                case 2:
                    setOnWifi();
                    break;
                case 3:
                    setOffWifi();
                    break;
                case 4:
                    ScanResult sr = (ScanResult) msg.obj;
                    selectShow(sr);
                    break;
                default:
                    break;
            }
        }
    };

    WifiNewUtils wifiNewUtils;

    public WifiListShowView(Context context) {
        super(context);
        init(context, null);
    }

    public WifiListShowView(Context context, OnWifiSetCallback callback) {
        super(context);
        this.callback = callback;
        init(context, null);
    }

    public WifiListShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
        wifiReceiver.startScaning();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        wifiReceiver.stopScanning();
    }


    private void init(final Context cx, AttributeSet attrs) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.wifi_list_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            wifiCycList.setLayoutManager(layoutManager);
            wifiAdapter = new WifiAdapter();
            wifiCycList.setAdapter(wifiAdapter);
            wifiReceiver = new WifiUtils.WifiReceiver(cx);
            wifiAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelect(final ScanResult sr, int position) {
                    if (wifiAdapter.isConnected(position))
                        return;

                    if (!WifiUtils.isEncrypted(sr)) {
                        startConnection(sr, "");
                        return;
                    }

                    WifiAuthDialog.show(cx, sr.SSID, new Callback2<String>() {
                        @Override
                        public void onCompleted(String s) {
                            startConnection(sr, s);
                        }
                    });
                }
            });

            wifiReceiver.setWifiStateListener(new WifiState() {
                @Override
                public void onScanResult(List<ScanResult> list) {
                    if (list != null) {
                        wifiAdapter.setData(list);
                    }
                }

                @Override
                public void onNetWorkStateChanged(NetworkInfo.DetailedState state) {
                    if (state == NetworkInfo.DetailedState.CONNECTED) {
                        TaskService.getInstance().postUiTask(new Runnable() {
                            @Override
                            public void run() {
                                ScanResult sr = WifiUtils.getCurrentScanResult(cx);
                                selectShow(sr);
                            }
                        }, 500);
                    }
                }

                @Override
                public void onWiFiStateChanged(int wifiState) {

                }

                @Override
                public void onWifiPasswordFault() {
//                    stopPro();
//                    ToastUtils.show(R.string.setting_network_wifi_failed);
//                    LogUtils.e("20200917", "onWifiPasswordFault:");
                }
            });
            onRefresh();
        }
    }

    @OnClick(R.id.wifi_control)
    public void wifiSwitch() {

        mFlag = PreferenceUtils.getBool(PageArgumentKey.IsWifi, false);
        if (mFlag) {
            Message msg = Message.obtain();
            msg.what = 2;
            handler.sendMessage(msg);
            ToastUtils.show("打开网络");
        } else {
            Message msg = Message.obtain();
            msg.what = 3;
            handler.sendMessage(msg);
            ToastUtils.show("关闭网络");
        }
    }

    private void onRefresh() {
        mFlag = PreferenceUtils.getBool(PageArgumentKey.IsWifi, false);
        if (WifiUtils.isEnabled(cx) && !mFlag) {
            setOnWifi();
        } else {
            setOffWifi();
        }
    }


    public void setOnWifi() {

        if (null != Utils.getDefaultFan()) {
            ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_wifi_switch) + "开",
                    cx.getString(R.string.google_screen_name));
        }

        WifiUtils.openWifi(cx);
        wifiReceiver.startScaning();
        wifiControl.setImageResource(R.mipmap.wifi_on);
        txtDesc.setVisibility(GONE);
        ScanResult sr = WifiUtils.getCurrentScanResult(cx);
        selectShow(sr);
        wifiShow.setVisibility(VISIBLE);
        PreferenceUtils.setBool(PageArgumentKey.IsWifi, false);

//        flag = false;
    }

    public void setOffWifi() {
        if (null != Utils.getDefaultFan()) {
            ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_wifi_switch) + "关",
                    cx.getString(R.string.google_screen_name));
        }

        wifiControl.setImageResource(R.mipmap.wifi_off);
        wifiShow.setVisibility(GONE);
        txtDesc.setVisibility(VISIBLE);
        WifiUtils.closeWifi(cx);
        PreferenceUtils.setBool(PageArgumentKey.IsWifi, true);

//        flag = true;
    }


    public void selectShow(ScanResult sr) {
        if (sr == null) {
            selectS.setVisibility(GONE);
        } else {
            selectS.setVisibility(VISIBLE);
            if (isAnimationRun) {
                showSelect.setVisibility(GONE);
            } else {
                showSelect.setVisibility(VISIBLE);

            }
            wifiName.setText(sr.SSID);
            boolean isEncrypted = WifiUtils.isEncrypted(sr);
            wifiLock.setVisibility(isEncrypted ? VISIBLE : GONE);
            switch (WifiManager.calculateSignalLevel(sr.level, 4)) {
                case 1:
                    wifiSingle.setImageResource(R.mipmap.wifi_img_one);
                    break;
                case 2:
                    wifiSingle.setImageResource(R.mipmap.wifi_img_two);
                    break;
                case 3:
                    wifiSingle.setImageResource(R.mipmap.wifi_img_three);
                    break;
                default:
                    wifiSingle.setImageResource(R.mipmap.wifi_img_no);
                    break;
            }
        }
    }


    @Subscribe
    public void onEvent(WifiConnectEvent event) {
        Log.e("20181228", "event::" + event.gate);
        if (StringUtils.isNullOrEmpty(event.gate))
            return;
        isConnected = true;
    }

    boolean isConnected;

    static private ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface OnWifiSetCallback {
        void onCompleted(String ssid, String pwd);
    }

    public OnWifiSetCallback callback;

    public void setCallback(OnWifiSetCallback callback) {
        this.callback = callback;
    }

    List<ScanResult> listSelect = new ArrayList<>();


    private void startConnection(final ScanResult sr, final String pwd) {
        LogUtils.e("20190330", "sr:" + sr);
        selectShow(sr);
        proRun();
        final int cipherType = WifiUtils.getCipherType(sr);
        WifiManager wifiManager = getMng(cx);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            WifiUtils.removeWifiBySsid(wifiManager, "\"" + sr.SSID + "\"");
        } else {
        }
        WifiConfiguration cfg = WifiUtils.createWifiInfo(cx, sr.SSID, pwd, cipherType);
        WifiUtils.addNetwork(cx, cfg);
        isConnected = false;

        executor.submit(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (true) {
                    if (index > 30000) {
                        wifiReceiver.startScaning();
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);

                        break;
                    }
                    if (isConnected) {
                        wifiReceiver.startScaning();
                        getMng(cx).saveConfiguration();
                        PreferenceUtils.setString(PrefsKey.Ssid, sr.SSID);
                        PreferenceUtils.setString(sr.SSID, pwd);
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);
//                        ToastUtils.show(R.string.wifiConnectSuccess, Toast.LENGTH_SHORT);
                        Log.e("20200324", "llll::" + callback);
                        if (callback != null) {
                            callback.onCompleted(sr.SSID, pwd);
                        }
                        break;
                    } else {
                        try {
                            Thread.sleep(500);
                            index = index + 500;
                        } catch (InterruptedException e) {
                            wifiReceiver.startScaning();
                            Message msg = Message.obtain();
                            msg.what = 0;
                            handler.sendMessage(msg);
                            ProgressDialogHelper.setRunning(cx, false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void proRun() {
        pro.setVisibility(View.VISIBLE);
        showSelect.setVisibility(View.GONE);
        if (imgAnimation == null) {
            imgAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.img_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            imgAnimation.setInterpolator(lin);
            pro.startAnimation(imgAnimation);
            isAnimationRun = true;
        }
    }

    private void stopPro() {
        pro.setVisibility(GONE);
        isAnimationRun = false;
        if (imgAnimation != null) {
            imgAnimation.cancel();
            pro.clearAnimation();
            imgAnimation = null;
        }
    }


    class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiListViewHolder> {
        List<ScanResult> list = new ArrayList<>();
        String currentBssid;
        public OnItemSelectedListener onItemSelectedListener;

        public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            this.onItemSelectedListener = onItemSelectedListener;
        }

        public boolean isConnected(int position) {
            ScanResult sr = list.get(position);
            if (sr == null)
                return false;
            boolean isConnected = Objects.equal(sr.BSSID, currentBssid);
            return isConnected;
        }

       /* WifiUtils.WifiScanCallback callabck = new WifiUtils.WifiScanCallback() {

            @Override
            public void onScanWifi(List<ScanResult> scanList) {
                currentBssid = WifiUtils.getCurrentWifiInfo(cx).getBSSID();
                list.clear();
                if (scanList != null) {
                    list.addAll(scanList);
                }
                notifyDataSetChanged();
                ProgressDialogHelper.setRunning(cx, false);
            }
        };*/

        public void setData(List<ScanResult> scanList) {
            this.list = scanList;
            notifyDataSetChanged();
        }


        @Override
        public WifiListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(cx).inflate(R.layout.wifi_item_show, parent, false);
            return new WifiListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(WifiListViewHolder holder, final int position) {
            final ScanResult sr = list.get(position);
            holder.setDataShow(sr, isConnected(position), position);
            holder.viewClick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onItemSelect(sr, position);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }


        public class WifiListViewHolder extends RecyclerView.ViewHolder {
            LinearLayout viewClick;
            TextView wifiName;
            ImageView wifiLock;
            ImageView wifiSingle;


            public WifiListViewHolder(View itemView) {
                super(itemView);
                viewClick = (LinearLayout) itemView.findViewById(R.id.view_click);
                wifiName = (TextView) itemView.findViewById(R.id.wifi_name);
                wifiLock = (ImageView) itemView.findViewById(R.id.wifi_lock);
                wifiSingle = (ImageView) itemView.findViewById(R.id.wifi_single);
            }

            public void setDataShow(ScanResult sr, boolean isConnected, int position) {
                Log.e("20181222", "isConnected::" + isConnected);
                wifiName.setText(sr.SSID);
                listSelect.clear();
                if (isConnected) {
                    if (onCallBackSelect != null) {
                        onCallBackSelect.Select(sr);
                    }
                }
                boolean isEncrypted = WifiUtils.isEncrypted(sr);
                wifiLock.setVisibility(isEncrypted ? VISIBLE : GONE);
                switch (WifiManager.calculateSignalLevel(sr.level, 4)) {
                    case 1:
                        wifiSingle.setImageResource(R.mipmap.wifi_img_one);
                        break;
                    case 2:
                        wifiSingle.setImageResource(R.mipmap.wifi_img_two);
                        break;
                    case 3:
                        wifiSingle.setImageResource(R.mipmap.wifi_img_three);
                        break;
                    default:
                        wifiSingle.setImageResource(R.mipmap.wifi_img_no);
                        break;
                }
            }

        }


    }


}
