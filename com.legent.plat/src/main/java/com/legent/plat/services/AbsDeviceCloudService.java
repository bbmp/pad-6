package com.legent.plat.services;


import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.AbsService;

import java.util.List;

/**
 * Created by sylar on 15/7/24.
 */
abstract public class AbsDeviceCloudService extends AbsService{

    // -------------------------------------------------------------------------------
    // restful service start
    // -------------------------------------------------------------------------------

    public void getDeviceGroups(long userId,
                                final Callback<List<DeviceGroupInfo>> callback) {
        CloudHelper.getDeviceGroups(userId, callback);
    }

    public void addDeviceGroup(long userId, String groupName,
                               Callback<Long> callback) {
        CloudHelper.addDeviceGroup(userId, groupName, callback);
    }

    public void deleteDeviceGroup(long userId, long groupId,
                                  VoidCallback callback) {
        CloudHelper.deleteDeviceGroup(userId, groupId, callback);
    }

    public void updateDeviceGroupName(long userId, long groupId,
                                      String groupName, VoidCallback callback) {
        CloudHelper.updateDeviceGroupName(userId, groupId, groupName, callback);
    }

    public void addDeviceToGroup(long userId, long groupId, String guid,
                                 VoidCallback callback) {
        CloudHelper.addDeviceToGroup(userId, groupId, guid, callback);
    }

    public void deleteDeviceFromGroup(long userId, long groupId, String guid,
                                      VoidCallback callback) {
        CloudHelper.deleteDeviceFromGroup(userId, groupId, guid, callback);
    }

    public void clearDeviceByGroup(long userId, long groupId,
                                   VoidCallback callback) {
        CloudHelper.clearDeviceByGroup(userId, groupId, callback);
    }

    public void getDevices(long userId, final Callback<List<DeviceInfo>> callback) {
        CloudHelper.getDevices(userId, callback);
    }


    public void getDeviceById(long userId, String guid,
                              final Callback<DeviceInfo> callback) {
        CloudHelper.getDeviceById(guid, callback);
    }

    public void getDeviceBySn(String sn, Callback<DeviceInfo> callback) {
        CloudHelper.getDeviceBySn(sn, callback);
    }

    public void updateDeviceName(long userId, String guid, String name,
                                 VoidCallback callback) {
        CloudHelper.updateDeviceName(userId, guid, name, callback);
    }

    public void bindDevice(long userId, final String guid, String name,
                           boolean isOwner, final VoidCallback callback) {
        CloudHelper.bindDevice(userId, guid, name, isOwner, callback);
    }

    public void unbindDevice(long userId, final String guid,
                             final VoidCallback callback) {
        CloudHelper.unbindDevice(userId, guid, callback);
    }

    public void getSnForDevice(long userId, String guid,
                               Callback<String> callback) {
        CloudHelper.getSnForDevice(userId, guid, callback);
    }

    public void getDeviceUsers(long userId, String guid,
                               Callback<List<User>> callback) {
        CloudHelper.getDeviceUsers(userId, guid, callback);
    }

    public void getDeviceAllUsers(long userId, String guid,
                               Callback<Reponses.GetDeviceUsersAllResponse> callback) {
        CloudHelper.getDeviceAllUsers(userId, guid, callback);
    }

    public void deleteDeviceUsers(long userId, String guid, List<Long> userIds,
                                  VoidCallback callback) {
        CloudHelper.deleteDeviceUsers(userId, guid, userIds, callback);
    }

    public void getAllDeviceErrorInfo(Callback<Reponses.ErrorInfoResponse> callback) {
        CloudHelper.getAllDeviceErrorInfo(callback);
    }

    // -------------------------------------------------------------------------------
    // restful service end
    // -------------------------------------------------------------------------------
}
