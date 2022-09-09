package com.robam.rokipad.factory;

import android.content.Context;

import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.dialog.type.DialogAddDeviceSelect;
import com.robam.rokipad.ui.dialog.type.DialogCookingFinish;
import com.robam.rokipad.ui.dialog.type.DialogType_0;
import com.robam.rokipad.ui.dialog.type.DialogType_01;
import com.robam.rokipad.ui.dialog.type.DialogType_0100;
import com.robam.rokipad.ui.dialog.type.DialogType_02;
import com.robam.rokipad.ui.dialog.type.DialogType_03;
import com.robam.rokipad.ui.dialog.type.DialogType_04;
import com.robam.rokipad.ui.dialog.type.DialogType_05;
import com.robam.rokipad.ui.dialog.type.DialogType_06;
import com.robam.rokipad.ui.dialog.type.DialogType_07;
import com.robam.rokipad.ui.dialog.type.DialogType_08;
import com.robam.rokipad.ui.dialog.type.DialogType_09;
import com.robam.rokipad.ui.dialog.type.DialogType_10;
import com.robam.rokipad.ui.dialog.type.DialogType_11;
import com.robam.rokipad.utils.DialogUtil;


/**
 * 公共对话框创建工厂
 * 创建对象的时候就会初始化对话框布局了，
 * 请在需要弹对话框的地方才执行创建操作
 */

public class RokiDialogFactory {

    public static IRokiDialog createDialogByType(Context context, int dialogType) {
        IRokiDialog rokiDialog = null;
        switch (dialogType) {
            case DialogUtil.DIALOG_TYPE_0:
                rokiDialog = new DialogType_0(context);
                break;
            case DialogUtil.DIALOG_TYPE_01:
                rokiDialog = new DialogType_01(context);
                break;
            case DialogUtil.DIALOG_TYPE_0100:
                rokiDialog = new DialogType_0100(context);
                break;
            case DialogUtil.DIALOG_TYPE_02:
                rokiDialog = new DialogType_02(context);
                break;
            case DialogUtil.DIALOG_TYPE_03:
                rokiDialog = new DialogType_03(context);
                break;
            case DialogUtil.DIALOG_TYPE_04:
                rokiDialog = new DialogType_04(context);
                break;
            case DialogUtil.DIALOG_TYPE_05:
                rokiDialog = new DialogType_05(context);
                break;
            case DialogUtil.DIALOG_TYPE_06:
                rokiDialog = new DialogType_06(context);
                break;
            case DialogUtil.DIALOG_TYPE_07:
                rokiDialog = new DialogType_07(context);
                break;
            case DialogUtil.DIALOG_TYPE_08:
                rokiDialog = new DialogType_08(context);
                break;
            case DialogUtil.DIALOG_TYPE_09:
                rokiDialog = new DialogType_09(context);
                break;
            case DialogUtil.DIALOG_TYPE_10:
                rokiDialog = new DialogType_10(context);
                break;
            case DialogUtil.DIALOG_TYPE_11:
                rokiDialog = new DialogType_11(context);
                break;
            case DialogUtil.DIALOG_ADD_DEVICE_SELECT:
                rokiDialog = new DialogAddDeviceSelect(context);
                break;
            case DialogUtil.DIALOG_COOKING_FINISH:
                rokiDialog = new DialogCookingFinish(context);
                break;
        }
        return rokiDialog;
    }
}
