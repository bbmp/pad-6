package com.legent.plat.io.cloud;

import com.legent.plat.R;
import com.legent.plat.io.cloud.Reponses.AddDeviceGroupResponse;
import com.legent.plat.io.cloud.Reponses.ChatGetReponse;
import com.legent.plat.io.cloud.Reponses.ChatSendReponse;
import com.legent.plat.io.cloud.Reponses.ChatisExistResponse;
import com.legent.plat.io.cloud.Reponses.CheckAppVerReponse;
import com.legent.plat.io.cloud.Reponses.GetAppIdReponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceGroupsResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicePesponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceUsersAllResponse;
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
import com.legent.plat.io.cloud.Requests.UserGuidAllRequest;
import com.legent.plat.io.cloud.Requests.UserGuidRequest;
import com.legent.plat.io.cloud.Requests.UserRequest;
import com.legent.plat.pojos.RCReponse;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by sylar on 15/7/23.
 */
public interface ICloudService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());

    // ==========================================================Url Start==========================================================
    String getAppId = "/rest/dms/api/app/guid/get";
    String bindAppGuidAndUser = "/rest/api/app/user/bind";
    String unbindAppGuidAndUser = "/rest/api/app/user/unbind";
    String checkAppVersion = "/rest/api/app/version/check";
    String reportLog = "/rest/dms/api/app/log/report";
    String getStartImages = "/rest/api/app/start-image/get";
    String sendChatMsg = "/rest/api/chat/send";
    String getChatBefore = "/rest/api/chat/before/get";
    String getChatAfter = "/rest/api/chat/after/get";
    String isExistChatMsg = "/rest/api/chat/after/is-exist";
    //-----------------------------------------------------------------------------------------------------------------------------
    String isExisted = "/rest/ums/api/account/is-existed";
    String registByPhone = "/rest/api/cas/account/regist-by-phone";
    String registByEmail = "/rest/api/cas/account/regist-by-email";
    String login = "/rest/api/cas/app/login";
    String logout = "/rest/api/cas/app/logout";
    String expressLogin = "/rest/api/cas/quick-login";
    //---------------
    String code = "/rest/gateway/api/auth/scan/key";
    String getLoginStatus = "/rest/gateway/api/auth/scan/login";

    //-------------------
    String getUser = "/rest/api/cas/user/get";
    String updateUser = "/rest/ums/api/user/update";
    String updatePassword = "/rest/api/cas/user/pwd/update";
    String updateFigure = "/rest/api/cas/user/figure/update";
    String getVerifyCode = "/rest/api/cas/verify-code/get";
    String getDynamicPwd = "/rest/api/cas/dynamic-pwd/get";
    String resetPasswordByPhone = "/rest/api/cas/pwd/reset-by-phone";
    String resetPasswordByEmail = "/rest/api/cas/pwd/reset-by-email";
    String loginFrom3rd = "/rest/api/cas/login3rd";
    String bind3rd = "/rest/api/cas/bind3rd";
    String unbind3rd = "/rest/api/cas/unbind3rd";
    //-----------------------------------------------------------------------------------------------------------------------------
    String getDeviceGroups = "/rest/api/device-group/get";
    String addDeviceGroup = "/rest/api/device-group/add";
    String deleteDeviceGroup = "/rest/api/device-group/delete";
    String updateDeviceGroupName = "/rest/api/device-group/name/update";
    String addDeviceToGroup = "/rest/api/device-group/device/add";
    String deleteDeviceFromGroup = "/rest/api/device-group/device/delete";
    String clearDeviceByGroup = "/rest/api/device-group/device/clear";
    String getDevices = "/rest/dms/api/device/get";
    //    String getDeviceById = "/rest/api/device/get-by-guid";
    String getDeviceById = "/rest/dms/api/device/get-by-guid";
    String getDeviceBySn = "/rest/api/device/get-by-sn";
    String updateDeviceName = "/rest/api/device/name/update";
    String bindDevice = "/rest/dms/api/device/bind";
    String unbindDevice = "/rest/dms/api/device/unbind";
    String getSnForDevice = "/rest/api/device/sn/get";
    String getDeviceUsers = "/rest/api/device/user/get";
    String getDeviceAllUsers = "/rest/api/device/user/gets";
    String getAllDeviceErrorInfo = "/rest/ops/api/error/config/get";
    String deleteDeviceUsers = "/rest/api/device/user/delete";
    /*根据条件返回设备联网说明列表*/
    String getAddDeviceList = "/rest/api/device/instruction-book/get";


    // ==========================================================Common Start==========================================================

    @POST(getAddDeviceList)
    void getAddDeviceList(@Body Requests.AddDeviceListRequest reqBody,
                          Callback<Reponses.AddDeviceListResponse> callback);

    @POST(getAppId)
    GetAppIdReponse getAppId(@Body GetAppIdRequest reqBody);

    @POST(getAppId)
    void getAppId(@Body GetAppIdRequest reqBody,
                  Callback<GetAppIdReponse> callback);

    @POST(bindAppGuidAndUser)
    void bindAppGuidAndUser(@Body AppUserGuidRequest reqBody,
                            Callback<RCReponse> callback);

    @POST(unbindAppGuidAndUser)
    void unbindAppGuidAndUser(@Body AppUserGuidRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(checkAppVersion)
    void checkAppVersion(@Body AppTypeRequest reqBody,
                         Callback<CheckAppVerReponse> callback);

    @POST(reportLog)
    void reportLog(@Body ReportLogRequest reqBody, Callback<RCReponse> callback);

    @POST(getStartImages)
    void getStartImages(@Body AppTypeRequest reqBody,
                        Callback<GetStartImagesResponse> callback);

    @POST(sendChatMsg)
    void sendChatMsg(@Body ChatSendRequest reqBody, Callback<ChatSendReponse> callback);

    @POST(getChatBefore)
    void getChatBefore(@Body ChatGetRequest reqBody,
                       Callback<ChatGetReponse> callback);

    @POST(getChatAfter)
    void getChatAfter(@Body ChatGetRequest reqBody,
                      Callback<ChatGetReponse> callback);

    @POST(isExistChatMsg)
    void isExistChatMsg(@Body ChatisExistRequest reqBody,
                        Callback<ChatisExistResponse> callback);

    // ==========================================================User Start==========================================================

    @POST(isExisted)
    void isExisted(@Body AccountRequest reqBody,
                   Callback<IsExistedResponse> callback);

    @POST(registByPhone)
    void registByPhone(@Body RegistByPhoneRequest reqBody,
                       Callback<RCReponse> callback);

    @POST(registByEmail)
    void registByEmail(@Body RegistByEmailRequest reqBody,
                       Callback<RCReponse> callback);

    @POST(login)
    void login(@Body LoginRequest reqBody, Callback<LoginReponse> callback);

    @POST(logout)
    void logout(@Body LogoutRequest reqBody, Callback<RCReponse> callback);

    @POST(expressLogin)
    void expressLogin(@Body ExpressLoginRequest reqBody, Callback<LoginReponse> callback);

    @GET(code)
    void getCode(Callback<Reponses.GetCode> callback);

    @GET(getLoginStatus)
    void getLoginStatus(@Query("key") String key, Callback<Reponses.GetLoginStatus> callback);

    @POST(getUser)
    void getUser(@Body UserRequest reqBody, Callback<GetUserReponse> callback);

    @POST(updateUser)
    void updateUser(@Body UpdateUserRequest reqBody,
                    Callback<RCReponse> callback);

    @POST(updatePassword)
    void updatePassword(@Body UpdatePasswordRequest reqBody,
                        Callback<RCReponse> callback);

    @POST(updateFigure)
    void updateFigure(@Body UpdateFigureRequest reqBody,
                      Callback<UpdateFigureReponse> callback);

    @POST(getVerifyCode)
    void getVerifyCode(@Body GetVerifyCodeRequest reqBody,
                       Callback<GetVerifyCodeReponse> callback);

    @POST(getDynamicPwd)
    void getDynamicPwd(@Body GetVerifyCodeRequest reqBody,
                       Callback<GetDynamicPwdRequestReponse> callback);

    @POST(resetPasswordByPhone)
    void resetPasswordByPhone(@Body ResetPwdByPhoneRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(resetPasswordByEmail)
    void resetPasswordByEmail(@Body ResetPwdByEmailRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(loginFrom3rd)
    void loginFrom3rd(@Body LoginFrom3rdRequest reqBody,
                      Callback<LoginReponse> callback);

    @POST(bind3rd)
    void bind3rd(@Body Bind3rdRequest reqBody, Callback<RCReponse> callback);

    @POST(unbind3rd)
    void unbind3rd(@Body Unbind3rdRequest reqBody, Callback<RCReponse> callback);

    // ==========================================================Device Start==========================================================

    @POST(getDeviceGroups)
    void getDeviceGroups(@Body UserRequest reqBody,
                         Callback<GetDeviceGroupsResponse> callback);

    @POST(addDeviceGroup)
    void addDeviceGroup(@Body AddDeviceGroupRequest reqBody,
                        Callback<AddDeviceGroupResponse> callback);

    @POST(deleteDeviceGroup)
    void deleteDeviceGroup(@Body UserGroupRequest reqBody,
                           Callback<RCReponse> callback);

    @POST(updateDeviceGroupName)
    void updateDeviceGroupName(@Body UpdateGroupNameRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(addDeviceToGroup)
    void addDeviceToGroup(@Body UserGroupGuidRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(deleteDeviceFromGroup)
    void deleteDeviceFromGroup(@Body UserGroupGuidRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(clearDeviceByGroup)
    void clearDeviceByGroup(@Body UserGroupRequest reqBody,
                            Callback<RCReponse> callback);

    // ----------------------------------------------------------------

    @POST(getDevices)
    void getDevices(@Body UserRequest reqBody,
                    Callback<GetDevicesResponse> callback);

    @POST(getDeviceById)
    void getDeviceById(@Body GuidRequest reqBody,
                       Callback<GetDevicePesponse> callback);

    @POST(getDeviceBySn)
    void getDeviceBySn(@Body GetDeviceBySnRequest reqBody,
                       Callback<GetDevicePesponse> callback);

    @POST(updateDeviceName)
    void updateDeviceName(@Body UpdateDeviceNameRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(bindDevice)
    void bindDevice(@Body BindDeviceRequest reqBody,
                    Callback<RCReponse> callback);

    @POST(unbindDevice)
    void unbindDevice(@Body UnbindDeviceRequest reqBody,
                      Callback<RCReponse> callback);

    @POST(getSnForDevice)
    void getSnForDevice(@Body UserGuidRequest reqBody,
                        Callback<GetSnForDeviceResponse> callback);

    @POST(getDeviceUsers)
    void getDeviceUsers(@Body UserGuidRequest reqBody,
                        Callback<GetDeviceUsersResponse> callback);

    @POST(getDeviceAllUsers)
    void getDeviceAllUsers(@Body UserGuidAllRequest reqBody,
                           Callback<GetDeviceUsersAllResponse> callback);


    @POST(deleteDeviceUsers)
    void deleteDeviceUsers(@Body DeleteDeviceUsersRequest reqBody,
                           Callback<RCReponse> callback);

    @POST(getAllDeviceErrorInfo)
    void getAllDeviceErrorInfo(Callback<Reponses.ErrorInfoResponse> callback);
}
