package com.legent.plat.io.cloud;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.RCRetrofitCallbackWithVoid;
import com.legent.plat.io.cloud.Reponses.AddDeviceGroupResponse;
import com.legent.plat.io.cloud.Reponses.ChatGetReponse;
import com.legent.plat.io.cloud.Reponses.ChatSendReponse;
import com.legent.plat.io.cloud.Reponses.ChatisExistResponse;
import com.legent.plat.io.cloud.Reponses.CheckAppVerReponse;
import com.legent.plat.io.cloud.Reponses.GetAppIdReponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceGroupsResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicePesponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceUsersResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicesResponse;
import com.legent.plat.io.cloud.Reponses.GetDynamicPwdRequestReponse;
import com.legent.plat.io.cloud.Reponses.GetSnForDeviceResponse;
import com.legent.plat.io.cloud.Reponses.GetStartImagesResponse;
import com.legent.plat.io.cloud.Reponses.GetUserReponse;
import com.legent.plat.io.cloud.Reponses.GetVerifyCodeReponse;
import com.legent.plat.io.cloud.Reponses.IsExistedResponse;
import com.legent.plat.io.cloud.Reponses.LoginReponse;
import com.legent.plat.io.cloud.Reponses.UpdateFigureReponse;
import com.legent.plat.io.cloud.Requests.AccountRequest;
import com.legent.plat.io.cloud.Requests.AddDeviceGroupRequest;
import com.legent.plat.io.cloud.Requests.AppTypeRequest;
import com.legent.plat.io.cloud.Requests.AppUserGuidRequest;
import com.legent.plat.io.cloud.Requests.Bind3rdRequest;
import com.legent.plat.io.cloud.Requests.BindDeviceRequest;
import com.legent.plat.io.cloud.Requests.ChatGetRequest;
import com.legent.plat.io.cloud.Requests.ChatSendRequest;
import com.legent.plat.io.cloud.Requests.ChatisExistRequest;
import com.legent.plat.io.cloud.Requests.DeleteDeviceUsersRequest;
import com.legent.plat.io.cloud.Requests.ExpressLoginRequest;
import com.legent.plat.io.cloud.Requests.GetAppIdRequest;
import com.legent.plat.io.cloud.Requests.GetDeviceBySnRequest;
import com.legent.plat.io.cloud.Requests.GetVerifyCodeRequest;
import com.legent.plat.io.cloud.Requests.GuidRequest;
import com.legent.plat.io.cloud.Requests.LoginFrom3rdRequest;
import com.legent.plat.io.cloud.Requests.LoginRequest;
import com.legent.plat.io.cloud.Requests.LogoutRequest;
import com.legent.plat.io.cloud.Requests.RegistByEmailRequest;
import com.legent.plat.io.cloud.Requests.RegistByPhoneRequest;
import com.legent.plat.io.cloud.Requests.ReportLogRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByEmailRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByPhoneRequest;
import com.legent.plat.io.cloud.Requests.Unbind3rdRequest;
import com.legent.plat.io.cloud.Requests.UnbindDeviceRequest;
import com.legent.plat.io.cloud.Requests.UpdateDeviceNameRequest;
import com.legent.plat.io.cloud.Requests.UpdateFigureRequest;
import com.legent.plat.io.cloud.Requests.UpdateGroupNameRequest;
import com.legent.plat.io.cloud.Requests.UpdatePasswordRequest;
import com.legent.plat.io.cloud.Requests.UpdateUserRequest;
import com.legent.plat.io.cloud.Requests.UserGroupGuidRequest;
import com.legent.plat.io.cloud.Requests.UserGroupRequest;
import com.legent.plat.io.cloud.Requests.UserGuidRequest;
import com.legent.plat.io.cloud.Requests.UserRequest;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.ChatMsg;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.RestfulService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;

import java.util.Date;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.legent.ContextIniter.cx;

/**
 * Created by sylar on 15/7/23.
 */
public class CloudHelper {

    static ICloudService svr = getRestfulApi(ICloudService.class);

    static public <T> T getRestfulApi(Class<T> apiClazz) {
        return RestfulService.getInstance().createApi(apiClazz);
    }

    // ==========================================================Common Start==========================================================

    static public String getAppGuid(String appType, String token, String versionName) {
        GetAppIdReponse res = svr.getAppId(new GetAppIdRequest(appType, token, versionName));
        return res != null ? res.appGuid : null;
    }

    static public void getAddDeviceList(String vendor, String dc, String dt, final Callback<Reponses.AddDeviceListResponse> callback) {
        svr.getAddDeviceList(new Requests.AddDeviceListRequest(vendor, dc, dt),
                new RCRetrofitCallback<Reponses.AddDeviceListResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.AddDeviceListResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }


    static public void getAppGuid(String appType, String token, String versionName,
                                  final Callback<String> callback) {
        svr.getAppId(new GetAppIdRequest(appType, token, versionName),
                new RCRetrofitCallback<GetAppIdReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetAppIdReponse result) {
                        callback.onSuccess(result.appGuid);
                    }

                    @Override
                    public void failure(RetrofitError e) {
//                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }

    static public void bindAppGuidAndUser(String appGuid, long userId,
                                          final VoidCallback callback) {
        svr.bindAppGuidAndUser(new AppUserGuidRequest(appGuid, userId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unbindAppGuidAndUser(String appGuid, long userId,
                                            final VoidCallback callback) {
        svr.unbindAppGuidAndUser(new AppUserGuidRequest(appGuid, userId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void checkAppVersion(String appType,
                                       final Callback<AppVersionInfo> callback) {
        AppTypeRequest appTypeRequest;
        if (IAppType.RKPBD.equals(appType))
            appTypeRequest = new AppTypeRequest(appType, Plat.getFanGuid().getDeviceTypeId());
        else
            appTypeRequest = new AppTypeRequest(appType);
        svr.checkAppVersion(appTypeRequest,
                new RCRetrofitCallback<CheckAppVerReponse>(callback) {
                    @Override
                    protected void afterSuccess(CheckAppVerReponse result) {
                        callback.onSuccess(result.verInfo);
                    }
                    @Override
                    public void failure(RetrofitError e) {
                        callback.onFailure(e);
                    }
                });
    }

    static public void reportLog(String appGuid, int logType, String log,
                                 VoidCallback callback) {
        String version = null;
        if (IAppType.RKDRD.equals(Plat.appType))
            version = PackageUtils.getVersionName(cx);
        else
            version = String.valueOf(PackageUtils.getAppVersionCode(Plat.app));
        svr.reportLog(new ReportLogRequest(appGuid, version, logType, log),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getStartImages(String appType,
                                      final Callback<List<String>> callback) {
        svr.getStartImages(new AppTypeRequest(appType),
                new RCRetrofitCallback<GetStartImagesResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetStartImagesResponse result) {
                        callback.onSuccess(result.images);
                    }
                });
    }

    static public void sendChatMsg(long userId, String msg,
                                   final Callback<ChatSendReponse> callback) {
        svr.sendChatMsg(new ChatSendRequest(userId, msg), new RCRetrofitCallback<ChatSendReponse>(callback) {
            @Override
            protected void afterSuccess(ChatSendReponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void getChatBefore(long userId, Date date, int count,
                                     final Callback<List<ChatMsg>> callback) {
        svr.getChatBefore(new ChatGetRequest(userId, date, count),
                new RCRetrofitCallback<ChatGetReponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatGetReponse result) {
                        callback.onSuccess(result.msgList);
                    }
                });
    }

    static public void getChatAfter(long userId, Date date, int count,
                                    final Callback<List<ChatMsg>> callback) {
        svr.getChatAfter(new ChatGetRequest(userId, date, count),
                new RCRetrofitCallback<ChatGetReponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatGetReponse result) {
                        callback.onSuccess(result.msgList);
                    }
                });
    }

    static public void isExistChatMsg(long userId, Date date,
                                      final Callback<Boolean> callback) {
        svr.isExistChatMsg(new ChatisExistRequest(userId, date),
                new RCRetrofitCallback<ChatisExistResponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatisExistResponse result) {
                        callback.onSuccess(result.existed);
                    }
                });
    }

    // ==========================================================User Start==========================================================
    static public void isExisted(String account,
                                 final Callback<Boolean> callback) {
        svr.isExisted(new AccountRequest(account),
                new RCRetrofitCallback<IsExistedResponse>(callback) {
                    @Override
                    protected void afterSuccess(IsExistedResponse result) {
                        callback.onSuccess(result.existed);
                    }
                });
    }

    static public void registByPhone(String phone, String nickname,
                                     String password, String figure, boolean gender, String verifyCode,
                                     VoidCallback callback) {
        svr.registByPhone(new RegistByPhoneRequest(phone, nickname, password,
                        figure, gender, verifyCode),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void registByEmail(String email, String nickname,
                                     String password, String figure, boolean gender,
                                     VoidCallback callback) {
        svr.registByEmail(new RegistByEmailRequest(email, nickname, password,
                figure, gender), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));

    }

    static public void login(String account, String password,
                             final Callback<User> callback) {

        svr.login(new LoginRequest(account, password),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        result.user.TGT = result.tgt;
                        callback.onSuccess(result.user);
                    }
                });

    }

    static public void expressLogin(String phone, String verifyCode,
                                    final Callback<User> callback) {

        svr.expressLogin(new ExpressLoginRequest(phone, verifyCode),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        result.user.TGT = result.tgt;
                        callback.onSuccess(result.user);
                    }
                });

    }

    static public void getCode(final Callback<Reponses.GetCode> callback) {
        svr.getCode(new RCRetrofitCallback<Reponses.GetCode>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetCode result) {
                super.afterSuccess(result);
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                callback.onFailure(e);
            }
        });
    }

    static public void getLoginStatus(String key, final retrofit.Callback<Reponses.GetLoginStatus> callback) {

        svr.getLoginStatus(key, new retrofit.Callback<Reponses.GetLoginStatus>() {
            @Override
            public void success(Reponses.GetLoginStatus getLoginStatus, Response response) {
                callback.success(getLoginStatus,response);
            }
            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    static public void logout(String tgt, VoidCallback callback) {
        svr.logout(new LogoutRequest(tgt),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getUser(long userId, final Callback<User> callback) {
        svr.getUser(new UserRequest(userId),
                new RCRetrofitCallback<GetUserReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetUserReponse result) {
                        callback.onSuccess(result.user);
                    }
                });
    }

    static public void updateUser(long id, String name, String phone,
                                  String email, boolean gender, VoidCallback callback) {
        svr.updateUser(new UpdateUserRequest(id, name, phone, email, gender),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void updatePassword(long userId, String oldPwd,
                                      String newPwd, VoidCallback callback) {
        svr.updatePassword(new UpdatePasswordRequest(userId, oldPwd, newPwd),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void updateFigure(long userId, String figure,
                                    final Callback<String> callback) {
        svr.updateFigure(
                new UpdateFigureRequest(userId, figure),
                new RCRetrofitCallback<UpdateFigureReponse>(callback) {
                    @Override
                    protected void afterSuccess(UpdateFigureReponse result) {
                        callback.onSuccess(result.figureUrl);
                    }
                });
    }

    static public void getVerifyCode(String phone,
                                     final Callback<String> callback) {
        svr.getVerifyCode(
                new GetVerifyCodeRequest(phone),
                new RCRetrofitCallback<GetVerifyCodeReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetVerifyCodeReponse result) {
                        callback.onSuccess(result.verifyCode);
                    }
                });
    }

    static public void getDynamicPwd(String phone, final Callback<String> callback) {
        svr.getDynamicPwd(new GetVerifyCodeRequest(phone),
                new RCRetrofitCallback<GetDynamicPwdRequestReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDynamicPwdRequestReponse result) {
                        callback.onSuccess(result.dynamicPwd);
                    }
                });
    }

    static public void resetPasswordByPhone(String phone, String newPwd,
                                            String verifyCode, VoidCallback callback) {
        svr.resetPasswordByPhone(new ResetPwdByPhoneRequest(phone, newPwd,
                verifyCode), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void resetPasswordByEmail(String email, VoidCallback callback) {
        svr.resetPasswordByEmail(new ResetPwdByEmailRequest(email),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void loginFrom3rd(int platId, String userId3rd,
                                    String nickname, String figureUrl, String token,
                                    final Callback<User> callback) {
        svr.loginFrom3rd(new LoginFrom3rdRequest(platId, userId3rd, nickname,
                        figureUrl, token),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        callback.onSuccess(result.user);
                    }
                });
    }

    static public void bind3rd(long userId, int platType, String thirdId,
                               String nickname, VoidCallback callback) {
        svr.bind3rd(new Bind3rdRequest(userId, platType, thirdId, nickname),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unbind3rd(long userId, int platType,
                                 VoidCallback callback) {
        svr.unbind3rd(new Unbind3rdRequest(userId, platType),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));

    }
    // ==========================================================Device Start==========================================================


    static public void getDeviceGroups(long userId,
                                       final Callback<List<DeviceGroupInfo>> callback) {
        svr.getDeviceGroups(new UserRequest(userId),
                new RCRetrofitCallback<GetDeviceGroupsResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetDeviceGroupsResponse result) {
                        callback.onSuccess(result.deviceGroups);
                    }
                });
    }

    static public void addDeviceGroup(long userId, String groupName,
                                      final Callback<Long> callback) {
        svr.addDeviceGroup(new AddDeviceGroupRequest(userId, groupName),
                new RCRetrofitCallback<AddDeviceGroupResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(AddDeviceGroupResponse result) {
                        callback.onSuccess(result.groupId);
                    }
                });

    }

    static public void deleteDeviceGroup(long userId, long groupId,
                                         final VoidCallback callback) {
        svr.deleteDeviceGroup(new UserGroupRequest(userId, groupId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));

    }

    static public void updateDeviceGroupName(long userId, long groupId,
                                             String groupName, VoidCallback callback) {
        svr.updateDeviceGroupName(new UpdateGroupNameRequest(userId, groupId,
                groupName), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void addDeviceToGroup(long userId, long groupId, String guid,
                                        VoidCallback callback) {
        svr.addDeviceToGroup(new UserGroupGuidRequest(userId, groupId, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteDeviceFromGroup(long userId, long groupId,
                                             String guid, VoidCallback callback) {
        svr.deleteDeviceFromGroup(new UserGroupGuidRequest(userId, groupId,
                guid), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void clearDeviceByGroup(long userId, long groupId,
                                          VoidCallback callback) {
        svr.clearDeviceByGroup(new UserGroupRequest(userId, groupId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // ----------------------------------------------------------------

    static public void getDevices(long userId,
                                  final Callback<List<DeviceInfo>> callback) {

        svr.getDevices(new UserRequest(userId),
                new RCRetrofitCallback<GetDevicesResponse>(callback) {
                    protected void afterSuccess(GetDevicesResponse result) {
                        if (Plat.DEBUG)
                            LogUtils.i("20170329", "result:" + result.toString());
                        callback.onSuccess(result.devices);
                    }

                });

    }

    static public void getDeviceById(String guid,
                                     final Callback<DeviceInfo> callback) {
        svr.getDeviceById(new GuidRequest(guid),
                new RCRetrofitCallback<GetDevicePesponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDevicePesponse result) {
                        callback.onSuccess(result.device);
                    }
                });
    }

    static public void getDeviceBySn(String sn, final Callback<DeviceInfo> callback) {
        svr.getDeviceBySn(new GetDeviceBySnRequest(sn),
                new RCRetrofitCallback<GetDevicePesponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDevicePesponse result) {
                        callback.onSuccess(result.device);
                    }
                });
    }

    static public void updateDeviceName(long userId, String guid, String name,
                                        VoidCallback callback) {
        svr.updateDeviceName(new UpdateDeviceNameRequest(userId, guid, name),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void bindDevice(long userId, String guid, String name,
                                  boolean isOwner, VoidCallback callback) {
        svr.bindDevice(new BindDeviceRequest(userId, guid, name, isOwner),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unbindDevice(long userId, String guid,
                                    VoidCallback callback) {
        svr.unbindDevice(new UnbindDeviceRequest(userId, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSnForDevice(long userId, String guid,
                                      final Callback<String> callback) {
        svr.getSnForDevice(new UserGuidRequest(userId, guid),
                new RCRetrofitCallback<GetSnForDeviceResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetSnForDeviceResponse result) {
                        callback.onSuccess(result.sn);
                    }
                });
    }

    static public void getDeviceUsers(long userId, String guid,
                                      final Callback<List<User>> callback) {
        svr.getDeviceUsers(new UserGuidRequest(userId, guid),
                new RCRetrofitCallback<GetDeviceUsersResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetDeviceUsersResponse result) {
                        callback.onSuccess(result.users);
                    }
                });
    }

    static public void getDeviceAllUsers(long userId, String guid,
                                         final Callback<Reponses.GetDeviceUsersAllResponse> callback) {
        svr.getDeviceAllUsers(new Requests.UserGuidAllRequest(userId, guid),
                new RCRetrofitCallback<Reponses.GetDeviceUsersAllResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(Reponses.GetDeviceUsersAllResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }


    static public void deleteDeviceUsers(long userId, String guid,
                                         List<Long> userIds, VoidCallback callback) {
        svr.deleteDeviceUsers(new DeleteDeviceUsersRequest(userId, guid,
                userIds), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getAllDeviceErrorInfo(final Callback callback) {
        svr.getAllDeviceErrorInfo(new RCRetrofitCallback<Reponses.ErrorInfoResponse>(callback) {

            @Override
            protected void afterSuccess(Reponses.ErrorInfoResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                LogUtils.i("20180612", "e:" + e);
            }
        });
    }
}
