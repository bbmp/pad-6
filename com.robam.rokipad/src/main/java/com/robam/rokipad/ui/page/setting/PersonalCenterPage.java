package com.robam.rokipad.ui.page.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceDeleteBluetoothSubsetSuccessEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.dialog.FullScreenImageDialog;
import com.robam.rokipad.ui.from.LoginActivity;
import com.robam.rokipad.ui.recycler.RecyclerAdapter;
import com.robam.rokipad.utils.DeviceDcToNameUtil;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.GlideCircleTransform;
import com.robam.rokipad.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/21.
 * PS: 个人中心页面.
 */
public class PersonalCenterPage extends BasePage {

    public static final String personalCenter = "personalCenter";
    @InjectView(R.id.iv_portrait_center)
    ImageView iv_portrait_center;
    @InjectView(R.id.tv_nickname)
    TextView mTvNickname;
    @InjectView(R.id.tv_phone)
    TextView mTvPhone;
    @InjectView(R.id.tv_login)
    TextView mTvLogin;
    @InjectView(R.id.iv_add_device)
    ImageView mIvAddDevice;
    @InjectView(R.id.recycler_device)
    RecyclerView mRecyclerDevice;
    RecyclerAdapter<IDevice> mAdapterDevice;
    RecyclerAdapter<User> mAdapterUser;
    private List<IDevice> mAllDevices = new ArrayList<>();
    private long userId;
    private Map<String, List<User>> mDataMap;

    @Subscribe
    public void onEvent(DeviceLoadCompletedEvent event) {
        LogUtils.e("20190603", "DeviceLoadCompletedEvent event:" + event);
        initData();
    }

//    @Subscribe
//    public void onEvent(UserLogoutEvent event) {
//        LogUtils.e("20190603", "UserLogoutEvent event:" + event.pojo.name);
//        initData();
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_personal_center), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    @OnClick(R.id.tv_login)
    public void onMTvLoginClicked() {
        if (cx.getString(R.string.setting_model_out_login_text).equals(mTvLogin.getText().
                toString()) && Plat.accountService.isLogon()) {
            ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_logout),
                    cx.getString(R.string.google_screen_name));
            final IRokiDialog outLoginDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
            outLoginDialog.setContentText(cx.getString(R.string.setting_model_out_login_content));
            outLoginDialog.show();
            outLoginDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outLoginDialog.dismiss();
                }
            });
            outLoginDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outLoginDialog.dismiss();
                    Plat.accountService.logout(new VoidCallback() {
                        @Override
                        public void onSuccess() {
//                            EventUtils.postEvent(new UserLogoutEvent(null));
//                            logoutSucceedUpdateData();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.show("退出登录失败");
                        }
                    });

                }
            });

        } else {
            ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_login),
                    cx.getString(R.string.google_screen_name));
            LoginActivity.start(activity);
        }
    }

    @OnClick(R.id.iv_add_device)
    public void onMIvAddDeviceClicked() {
//        ToastUtils.showShort("敬请期待");
        ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_add_device),
                cx.getString(R.string.google_screen_name));
        String[] deviceCategory = {"RRQZ", "RDKX", "RZQL", "RZKY", "RJSQ", "RWBL", "RXDG"};
        final IRokiDialog deviceAddDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_ADD_DEVICE_SELECT);
        deviceAddDialog.setData(deviceCategory);
        deviceAddDialog.show();
        deviceAddDialog.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceAddDialog != null && deviceAddDialog.isShow()) {
                    deviceAddDialog.dismiss();
                }
                String deviceCategory = v.getTag().toString();
                if (TextUtils.isEmpty(deviceCategory))
                    return;


                if (IDeviceType.RRQZ.equals(deviceCategory) || IDeviceType.RDCZ.equals(deviceCategory)) {

                    if (Utils.getDefaultStove()[0] != null) {
                        ToastUtils.show(R.string.device_stove_not_add);
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(PageArgumentKey.deviceCategory, deviceCategory);
                    UIService.getInstance().postPage(PageKey.DeviceBluetoothAdd, bundle);

                } else {
                    //TODO wifi添加页面
                    ToastUtils.show("暂未开发，敬请期待");
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (cx == null) return;
        if (mAllDevices != null || mAllDevices.size() > 0) {
            mAllDevices.clear();
        }
        ProgressDialogHelper.setRunning(cx, true);
        mAllDevices = Plat.deviceService.queryAll();
        LogUtils.e("20190603", " size:" + mAllDevices.size());
        if (null == mAllDevices || 0 == mAllDevices.size()) {
            ToastUtils.show("设备加载失败，请重新加载");
            ProgressDialogHelper.setRunning(cx, false);
            return;
        }
        for (int i = 0; i < mAllDevices.size(); i++) {
            if (mAllDevices.get(i) instanceof AbsFan) {
                List<IDevice> childList = ((AbsFan) mAllDevices.get(i)).getChildList();
                if (childList != null && childList.size() > 0) {
                    mAllDevices.addAll(childList);
                }
            }
        }
        IDevice defaultDevice = Utils.getDefaultFan();
        mAllDevices.remove(defaultDevice);
        mAllDevices.add(0, defaultDevice);

        getUserData();
        User user = Plat.accountService.getCurrentUser();
        boolean logon = Plat.accountService.isLogon();
        LogUtils.e("20190603", "logon:" + logon + " user:" + user);
        if (user != null && logon) {
            userId = user.getID();
            Glide.with(cx).load(user.figureUrl).
                    transform(new GlideCircleTransform(cx)).error(R.mipmap.ic_not_login).into(iv_portrait_center);
            mTvNickname.setText(user.name);
            mTvPhone.setText(user.phone);
            mTvLogin.setText(R.string.setting_model_out_login_text);
            mTvLogin.setTextSize(16);
            mTvLogin.setBackground(getResources().getDrawable(R.drawable.shape_item_bg));
        } else {
            logoutSucceedUpdateData();
        }
    }

    /**
     * 退出登录时更新UI
     */
    private void logoutSucceedUpdateData() {
        iv_portrait_center.setImageResource(R.mipmap.ic_not_login);
        mTvNickname.setText(R.string.setting_model_not_login_text);
        mTvPhone.setText("");
        mTvLogin.setText(R.string.setting_model_login_text);
        mTvLogin.setTextSize(24);
        mTvLogin.setBackgroundResource(R.drawable.shape_item_blue_bg);
//        LogUtils.e("20190603", "mAdapterDevice:" + mAdapterDevice);
//        if (mAdapterDevice != null) {
//            mAdapterDevice.clear();
//            List<IDevice> notLogList = new ArrayList<>();
//            LogUtils.e("20190603", "mAllDevices size:" + mAllDevices.size());
//            if (mAllDevices.size() > 0) {
//                for (int i = 0; i < mAllDevices.size(); i++) {
//                    String dt = mAllDevices.get(i).getDt();
//                    if (IRokiFamily._8236S.equals(dt) || IRokiFamily._5916S.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily.R9B37.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily._9B515.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily.R9B39.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily.R9W70.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily.R9B30C.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    } else if (IRokiFamily.R0001.equals(dt)) {
//                        notLogList.add(mAllDevices.get(i));
//                    }
//                }
//                LogUtils.e("20190603", "notLogList:" + notLogList + " size:" + notLogList.size());
//                mAdapterDevice.replace(notLogList);
//            }
//        }
    }

    private synchronized void initAdapter() {
        //设备信息
        mRecyclerDevice.setAdapter(mAdapterDevice = new RecyclerAdapter<IDevice>() {
            @Override
            protected int getItemViewType(int position, IDevice t) {
                return R.layout.cell_personal_center;
            }

            @Override
            protected ViewHolder<IDevice> onCreateViewHolder(View root, int viewType) {
                return new DeviceViewHolder(root);
            }
        });

        if (null == mAllDevices || mAllDevices.size() == 0) {

            initData();
        }
        mAdapterDevice.replace(mAllDevices);
        mRecyclerDevice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void getUserData() {
        StringBuffer sb = new StringBuffer();
        if (null != mAllDevices && mAllDevices.size() > 0) {
            for (int i = 0; i < mAllDevices.size(); i++) {
                IDevice iDevice = mAllDevices.get(i);
                if (iDevice != null) {
                    DeviceGuid deviceGuid = iDevice.getGuid();
                    if (deviceGuid != null) {
                        String guid = deviceGuid.getGuid();
                        sb.append(",").append(guid);
                    }
                }
            }
            String guids = sb.toString();
            LogUtils.e("20200928", "guids:" + guids);
            if (!TextUtils.isEmpty(guids)) {
                String substring = guids.substring(1, guids.length());
                Plat.deviceService.getDeviceAllUsers(userId, substring, new Callback<Reponses.GetDeviceUsersAllResponse>() {
                    @Override
                    public void onSuccess(Reponses.GetDeviceUsersAllResponse response) {
                        mDataMap = response.mMap;
                        initAdapter();
                        ProgressDialogHelper.setRunning(cx, false);
                        LogUtils.e("20200928", "mDataMap:" + mDataMap);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        initAdapter();
                        ProgressDialogHelper.setRunning(cx, false);
                    }
                });
            }
        } else {
            ToastUtils.show("获取设备信息失败");
        }
    }

    public void upDate() {

        initData();
    }


    class DeviceViewHolder extends RecyclerAdapter.ViewHolder<IDevice> {
        TextView tv_device_name;
        TextView tv_share_control_power;
        ImageView iv_device_ic;
        TextView tv_device_dt;
        TextView tv_device_code;
        TextView tv_device_version;
        RecyclerView recycler_device;
        String deviceGuid = null;
        String deviceDc = null;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            iv_device_ic = (ImageView) itemView.findViewById(R.id.iv_device_ic);
            tv_device_name = (TextView) itemView.findViewById(R.id.tv_device_name);
            tv_share_control_power = (TextView) itemView.findViewById(R.id.tv_share_control_power);
            tv_device_dt = (TextView) itemView.findViewById(R.id.tv_device_dt);
            tv_device_code = (TextView) itemView.findViewById(R.id.tv_device_code);
            tv_device_version = (TextView) itemView.findViewById(R.id.tv_device_version);
            recycler_device = (RecyclerView) itemView.findViewById(R.id.recycler_device);

            mAdapterDevice.setListener(new RecyclerAdapter.AdapterListenerImpl<IDevice>() {
                @Override
                public void onItemClick(final RecyclerAdapter.ViewHolder holder, final IDevice deviceInfo) {
                    super.onItemClick(holder, deviceInfo);
                    DeviceViewHolder deviceViewHolder = (DeviceViewHolder) holder;
                    deviceViewHolder.tv_share_control_power.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (IDeviceType.RYYJ.equals(deviceInfo.getDc())) {
                                //烟机分享控制权
                                Log.e("20181228", "烟机分享控制权");
                                addAccount(deviceInfo, deviceInfo.getName());
                            } else {
                                //其他删除设备
                                Log.e("20181228", "其他删除设备");
                                final String dc = deviceInfo.getDc();
                                String deviceName = DeviceDcToNameUtil.getDcToName(dc);
                                final IRokiDialog deleteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
                                deleteDialog.setContentText(cx.getString(R.string.setting_model_delete_content) + '"' + deviceName + '"');
                                deleteDialog.show();
                                deleteDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteDialog.dismiss();
                                    }
                                });
                                deleteDialog.setOkBtn(R.string.delete, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteDialog.dismiss();
                                        LogUtils.e("20190625", "userId:" + userId);
                                        LogUtils.e("20190625", "device_guid:" + deviceInfo.getGuid().getGuid());
                                        Plat.deviceService.deleteWithUnbind(userId, deviceInfo.getGuid().getGuid(),
                                                new VoidCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        if (IDeviceType.RRQZ.equals(dc) || IDeviceType.RDCZ.equals(dc) || IDeviceType.RPOT.equals(dc)) {
                                                            AbsFan defaultFan = Utils.getDefaultFan();
                                                            defaultFan.delPotDevice(deviceInfo.getGuid().getGuid(), true, new MsgCallback() {
                                                                @Override
                                                                public void onSuccess(Msg msg) {
                                                                    if (dc.equals(IDeviceType.RRQZ) || dc.equals(IDeviceType.RDCZ)) {
                                                                        EventUtils.postEvent(new DeviceDeleteBluetoothSubsetSuccessEvent());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Throwable t) {

                                                                }
                                                            });
                                                        }
                                                        LogUtils.e("20190902", "count:" + mAdapterDevice.getItemCount() + " position:" + holder.getAdapterPosition());
                                                        mAdapterDevice.clear(holder.getAdapterPosition());
                                                        ToastUtils.showShort(R.string.setting_model_delete_device_success_text);
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        LogUtils.e("20190625", "t:" + t.toString());
                                                    }
                                                });
                                    }
                                });

                            }
                        }
                    });
                }
            });

            //用户控制权
            recycler_device.setAdapter(mAdapterUser = new RecyclerAdapter<User>() {
                @Override
                protected int getItemViewType(int position, User user) {
                    return R.layout.cell_personal_center_user;
                }

                @Override
                protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                    return new UserViewHolder(root);
                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(cx,
                    LinearLayoutManager.VERTICAL, false);
            recycler_device.setLayoutManager(layoutManager);
            mAdapterUser.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
                @Override
                public void onItemClick(final RecyclerAdapter.ViewHolder holder, final User user) {
                    super.onItemClick(holder, user);
                    final UserViewHolder userViewHolder = (UserViewHolder) holder;
                    final Long userId = user.getID();
//                    LogUtils.e("20190102", "userId:" + userId);
//                    Log.e("20190102", "deviceGuid:" + deviceGuid);

                    userViewHolder.tv_delete_control_power.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final IRokiDialog deleteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
                            String deviceName = DeviceDcToNameUtil.getDcToName(deviceDc);
                            deleteDialog.setContentText(cx.getString(R.string.setting_model_delete_content)
                                    + '"' + deviceName + '"' + cx.getString(R.string.setting_model_control_power_text));
                            deleteDialog.show();
                            deleteDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDialog.dismiss();
                                }
                            });
                            deleteDialog.setOkBtn(R.string.delete, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDialog.dismiss();
                                    if (!Plat.accountService.isLogon()) {
                                        ToastUtils.show(getContext().getString(R.string.canNotLogDevices));
                                        return;
                                    }

                                    if (Plat.accountService.getCurrentUserId() == userId) {
                                        ToastUtils.show(getContext().getString(R.string.canNotDeleteDevices));
                                        return;
                                    }
                                    if (IDeviceType.RRQZ.equals(deviceDc) || IDeviceType.RDCZ.equals(deviceDc)) {
                                        ToastUtils.show(R.string.device_stove_not_delete_user);
                                        return;
                                    }
                                    if (IDeviceType.RPOT.equals(deviceDc)) {
                                        ToastUtils.show(R.string.device_pot_not_delete_user);
                                        return;
                                    }
                                    List<Long> users = new ArrayList<>();
                                    users.add(userId);
                                    Plat.deviceService.deleteDeviceUsers(Plat.accountService.getCurrentUserId(), deviceGuid, users, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.e("20190102", "deviceGuid:" + deviceGuid);
                                            initData();
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Log.e("20190102", "t:" + t.toString());
                                        }
                                    });

//                                    Plat.deviceService.deleteDeviceUsers(userId, deviceGuid, new VoidCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            ToastUtils.showShort(R.string.setting_model_delete_device_success_text);
////                                            getUserData();
////                                            if (deviceDc.equals(IDeviceType.RYYJ)){
////                                                mAdapterUser.clear(holder.getAdapterPosition());
////                                            }
//                                            LogUtils.e("20190102", "userId:" + userId);
//                                            Log.e("20190102", "deviceGuid:" + deviceGuid);
//                                            initData();
//                                        }
//
//                                        @Override
//                                        public void onFailure(Throwable t) {
//                                            ToastUtils.showThrowable(t);
//                                        }
//                                    });
                                }
                            });
                        }
                    });
                }
            });
        }

        private void addAccount(IDevice device, final String dc) {
            if (!Plat.accountService.isLogon()) {
                ToastUtils.show(R.string.device_not_login);
                return;
            }

            if (device == null) {
                ToastUtils.show(getContext().getResources().getString(
                        R.string.unconnect_toast));
                return;
            }

            Plat.deviceService.getSnForDevice(userId, device.getID(),
                    new Callback<String>() {

                        @Override
                        public void onSuccess(String sn) {
                            if (Strings.isNullOrEmpty(sn)) {
                                ToastUtils.showShort(getContext().getString(R.string.failedTheDeviceCode));
                                return;
                            }
                            FullScreenImageDialog.show(getContext(), sn, dc);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            if (t.toString().contains("DeviceIOException")) {
                                ToastUtils.showShort(getContext().getString(R.string.setting_model_no_net_work_text));
                            } else {
                                ToastUtils.showShort(getContext().getString(R.string.deviceIsNotYou));
                            }
                        }
                    });
        }

        @Override
        protected void onBind(IDevice device) {
            if (device == null) return;
            tv_device_dt.setText(device.getDt());
            tv_device_code.setText(device.getBid());
            tv_device_version.setText(String.valueOf(device.getVersion()));
            deviceGuid = device.getID();
            deviceDc = device.getDc();
            selectedImg(deviceDc);
            if (null != mDataMap && mDataMap.size()>0){
                if (!TextUtils.isEmpty(deviceGuid)){
                    List<User> userList = mDataMap.get(deviceGuid);
                    mAdapterUser.replace(userList);
                }

            }

        }

        private void selectedImg(String dc) {
            if (TextUtils.isEmpty(dc)) return;
            switch (dc) {
                case IDeviceType.RYYJ:
                    tv_device_name.setText(R.string.setting_model_device_fan_name_text);
                    tv_share_control_power.setText(R.string.setting_model_share_control_power_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_fan_device);
                    break;
                case IDeviceType.RRQZ:
                case IDeviceType.RDCZ:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_stove_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_stove_device);
                    break;
                case IDeviceType.RPOT:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_pot_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_pot_device);
                    break;
                case IDeviceType.RXDG:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_disinfection_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_disinfection_device);
                    break;
                case IDeviceType.RZQL:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_steam_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_steam_device);
                    break;
                case IDeviceType.RWBL:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_wave_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_microwave_device);
                    break;
                case IDeviceType.RDKX:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_oven_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_oven_device);
                    break;
                case IDeviceType.RJSQ:
                    tv_share_control_power.setText(R.string.setting_model_delete_device_text);
                    tv_device_name.setText(R.string.setting_model_device_water_name_text);
                    iv_device_ic.setImageResource(R.mipmap.ic_water_device);
                    break;
                default:

                    break;
            }
        }
    }

    class UserViewHolder extends RecyclerAdapter.ViewHolder<User> {

        TextView tv_user_nickname;
        TextView tv_user_phone;
        TextView tv_delete_control_power;

        public UserViewHolder(View itemView) {
            super(itemView);
            tv_user_nickname = (TextView) itemView.findViewById(R.id.tv_user_nickname);
            tv_user_phone = (TextView) itemView.findViewById(R.id.tv_user_phone);
            tv_delete_control_power = (TextView) itemView.findViewById(R.id.tv_delete_control_power);

        }

        @Override
        protected void onBind(User user) {
            LogUtils.e("20190419", "onBind user:" + user.getName());
            tv_user_nickname.setText(user.getName());
            tv_user_phone.setText(user.phone);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
