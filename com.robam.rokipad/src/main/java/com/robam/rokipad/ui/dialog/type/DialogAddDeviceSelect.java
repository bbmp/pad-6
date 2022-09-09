package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.legent.utils.api.ToastUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;
import com.robam.rokipad.utils.DeviceDcToNameUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;


/**
 * 类型0 Dialog
 * 中部文本内容，底部两个按钮，居中弹出
 */
public class DialogAddDeviceSelect extends BaseDialog {

    private TagFlowLayout mTfl_add_device;
    private TextView mCancelTv;
    private TextView mTvAffirm;
    private String selectDeviceCategory = null;


    public DialogAddDeviceSelect(Context context) {
        super(context);
        mDialog.setPosition(Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout_add_device_select, null);
        mTfl_add_device = (TagFlowLayout) rootView.findViewById(R.id.tfl_add_device);
        mCancelTv = (TextView) rootView.findViewById(R.id.tv_cancel);
        mTvAffirm = (TextView) rootView.findViewById(R.id.tv_affirm);
        createDialog(rootView);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvAffirm.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setData(final String[] deviceCategory) {
        super.setData(deviceCategory);
        mTfl_add_device.setAdapter(new TagAdapter<String>(deviceCategory) {
            @Override
            public View getView(FlowLayout parent, int position, String deviceCategory) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.add_device_select_tv,
                        mTfl_add_device, false);
                String deviceName = DeviceDcToNameUtil.getDcToName(deviceCategory);
                tv.setText(deviceName);
                return tv;
            }
        });

        mTfl_add_device.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer index : selectPosSet) {
                    selectDeviceCategory = deviceCategory[index];
                }
            }
        });
    }



    @Override
    public void bindAllListeners() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mTvAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(selectDeviceCategory)){
                    v.setTag(selectDeviceCategory);
                    onOkClick(v);
                }else {
                    ToastUtils.showShort(R.string.setting_model_device_select_text);
                }

            }
        });
    }

}
