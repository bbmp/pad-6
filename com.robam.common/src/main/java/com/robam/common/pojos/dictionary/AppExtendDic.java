package com.robam.common.pojos.dictionary;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.pojos.dictionary.ResultCode;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.plat.services.ResultCodeManager;
import com.legent.pojos.IJsonPojo;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.R;
import com.robam.common.services.StoveAlarmManager;

import java.util.List;

public class AppExtendDic implements IJsonPojo {


    @JsonProperty("deviceTypes")
    public List<DeviceType> deviceTypes;

    @JsonProperty("resultCodes")
    public List<ResultCode> resultCodes;

    @JsonProperty("stoveAlarms")
    public List<StoveAlarm> stoveAlarms;

    static public void init(Context cx) {

        AppExtendDic dic = null;
        String dicContent = ResourcesUtils.raw2String(R.raw.app_dic_extend);
        Preconditions.checkNotNull(dicContent);

        try {
            dic = JsonUtils.json2Pojo(dicContent, AppExtendDic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Preconditions.checkNotNull(dic, "加载 app dic 失败");

        // common
        DeviceTypeManager.getInstance().batchAdd(dic.deviceTypes);
        ResultCodeManager.getInstance().batchAdd(dic.resultCodes);
        StoveAlarmManager.getInstance().batchAdd(dic.stoveAlarms);

    }

}