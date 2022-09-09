package com.robam.rokipad.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.net.Uri;
import android.text.TextUtils;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.Reponses;
import com.legent.services.RestfulService;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.dictionary.StoveAlarm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 14807 on 2018/7/27.
 */

public class AlarmDataUtils {

    public static String fileJson;
    static BroadcastReceiver broadcastReceiver;
    static Activity mActivity;
    private static final int ONE_ALARM = 1;
    private static final int TWO_ALARM = 2;
    static int downloadCount = 0;//失败时下载次数

    public static void init(Activity activity) {
        LogUtils.i("20180726", " activity:" + activity);
        mActivity = activity;
        initAlarmData();
    }


    public static void onMicroWaveAlarmEvent(AbsMicroWave microWave, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = microWave.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject microWaveDc = (JSONObject) object.get(dc);
            String deviceType = (String) microWaveDc.get("deviceType");
            JSONObject microWaveDcCode = (JSONObject) microWaveDc.get(alarmCode);
            Integer alertLevel = (Integer) microWaveDcCode.get("alertLevel");
            String alertName = (String) microWaveDcCode.get("alertName");
            String alertDescr = (String) microWaveDcCode.get("alertDescr");
            String alertCode = (String) microWaveDcCode.get("alertCode");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //消毒柜
    public static void onSteriAlarmEvent(AbsSterilizer sterilizer, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = sterilizer.getDc();
        String dt = sterilizer.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject sterilizerDc = (JSONObject) object.get(dc);
            String deviceType = (String) sterilizerDc.get("deviceType");
            JSONObject sterilizerDt = (JSONObject) sterilizerDc.get(dt);
            JSONObject sterilizerCode = (JSONObject) sterilizerDt.get(alarmCode);
            Integer alertLevel = (Integer) sterilizerCode.get("alertLevel");
            String alertName = (String) sterilizerCode.get("alertName");
            String alertDescr = (String) sterilizerCode.get("alertDescr");
            String alertCode = (String) sterilizerCode.get("alertCode");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onEvent(SteamCleanResetEvent event) {
        ToastUtils.show("长时间使用，您需要除垢清洁蒸汽炉");
    }

    //电烤箱
    public static void ovenAlarmStatus(short alarmId, AbsOven oven) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = oven.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject ovenDc = (JSONObject) object.get(dc);
            String deviceType = (String) ovenDc.get("deviceType");
            JSONObject ovenCode = (JSONObject) ovenDc.get(alarmCode);
            Integer alertLevel = (Integer) ovenCode.get("alertLevel");
            String alertName = (String) ovenCode.get("alertName");
            String alertDescr = (String) ovenCode.get("alertDescr");
            String alertCode = (String) ovenCode.get("alertCode");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //灶具报警分类
    public static void onStoveAlarmEvent(Stove stove, StoveAlarm alarm) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarm.getID());
        if (null == stove) return;
        String dc = stove.getDc();
        String dt = stove.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject stoveDc = (JSONObject) object.get(dc);
            String deviceType = (String) stoveDc.get("deviceType");
            JSONObject stoveDt = (JSONObject) stoveDc.get(dt);
            JSONObject stoveCode = (JSONObject) stoveDt.get(alarmCode);
            Integer alertLevel = (Integer) stoveCode.get("alertLevel");
            String alertName = (String) stoveCode.get("alertName");
            String alertDescr = (String) stoveCode.get("alertDescr");
            String alertCode = (String) stoveCode.get("alertCode");
            ToastUtils.show(alertDescr);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //灶具报警分类
    public static void onPotAlarmEvent(Pot pot, short alarm) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarm);
        String dc = pot.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject potDc = (JSONObject) object.get(dc);
            String deviceType = (String) potDc.get("deviceType");
            JSONObject potCode = (JSONObject) potDc.get(alarmCode);
            Integer alertLevel = (Integer) potCode.get("alertLevel");
            String alertName = (String) potCode.get("alertName");
            String alertDescr = (String) potCode.get("alertDescr");
            String alertCode = (String) potCode.get("alertCode");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 蒸汽炉报警分类
     *
     * @param steam   设备
     * @param alarmId 报警编码
     * @return
     */
    public static void SteamAlarmStatus(AbsSteamoven steam, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = steam.getDc();
        String dt = steam.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject steamDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamDc.get("deviceType");
            JSONObject steamDt = (JSONObject) steamDc.get(dt);
            JSONObject steamCode = (JSONObject) steamDt.get(alarmCode);
            Integer alertLevel = (Integer) steamCode.get("alertLevel");
            String alertName = (String) steamCode.get("alertName");
            String alertDescr = (String) steamCode.get("alertDescr");
            String alertCode = (String) steamCode.get("alertCode");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //一体机报警处理
    public static void steamOvenOneAlarmStatus(AbsSteameOvenOne steamOvenOne, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);

        String alarmCode = String.valueOf(alarmId);
        String dc = steamOvenOne.getDc();
        String dt = steamOvenOne.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject steamOvenOneDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamOvenOneDc.get("deviceType");
            JSONObject steamOvenOneDt = (JSONObject) steamOvenOneDc.get(dt);
            JSONObject steamOvenOneCode = (JSONObject) steamOvenOneDt.get(alarmCode);
            Integer alertLevel = (Integer) steamOvenOneCode.get("alertLevel");
            String alertName = (String) steamOvenOneCode.get("alertName");
            String alertDescr = (String) steamOvenOneCode.get("alertDescr");
            String alertCode = (String) steamOvenOneCode.get("alertCode");
            LogUtils.i("20180831", "alertLevel:" + alertLevel);
            LogUtils.i("20180831", "alertDescr:" + alertDescr);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onWaterPurifiyAlarmEvent(AbsWaterPurifier purifier, short alarmId) {
        List<Short> list = new ArrayList<>();
        list.add(purifier.filter_state_pp);
        list.add(purifier.filter_state_cto);
        list.add(purifier.filter_state_ro1);
        list.add(purifier.filter_state_ro2);
        Short min = Collections.min(list);
        LogUtils.i("20181130", "min:" + min);
        if (alarmId == 255 && min > 10) {
            return;
        }
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = purifier.getDc();
        String dt = purifier.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject purifierDc = (JSONObject) object.get(dc);
            String deviceType = (String) purifierDc.get("deviceType");
            JSONObject purifierCode = (JSONObject) purifierDc.get(alarmCode);
            Integer alertLevel = (Integer) purifierCode.get("alertLevel");
            String alertName = (String) purifierCode.get("alertName");
            String alertDescr = (String) purifierCode.get("alertDescr");
            String alertCode = (String) purifierCode.get("alertCode");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static void initAlarmData() {
        downloadJson();
    }

    private static void downloadJson() {

        final String url = PreferenceUtils.getString("downloadUrl", "");
        Plat.deviceService.getAllDeviceErrorInfo(new Callback<Reponses.ErrorInfoResponse>() {
            @Override
            public void onSuccess(Reponses.ErrorInfoResponse errorInfoResponse) {
                String downloadUrl = errorInfoResponse.url;
                LogUtils.e("20190614", " downloadUrl:" + downloadUrl);
                PreferenceUtils.setString("downloadUrl", downloadUrl);
                if (!TextUtils.isEmpty(downloadUrl) && !url.equals(downloadUrl)) {
                    RestfulService.getInstance().downFile(downloadUrl, "alarm.json", new Callback<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            LogUtils.e("20190614", " uri:" + uri);
                            if (uri != null) {
                                fileJson = AlarmDataUtils.getFileFromSD(uri.getPath());
                                PreferenceUtils.setString("alarm", fileJson);
                                FileUtils.deleteFile(uri.getPath());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20190614", " t:" + t.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                downloadCount++;
                if (downloadCount <= 3) {
                    downloadJson();
                }
            }
        });

    }

    public static String getFileFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
