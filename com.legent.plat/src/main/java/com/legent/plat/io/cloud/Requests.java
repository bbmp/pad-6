package com.legent.plat.io.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.pojos.AbsPostRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sylar on 15/7/23.
 */
public interface Requests {

    // ==========================================================Common Start==========================================================

    class AppTypeRequest extends AbsPostRequest {

        @JsonProperty("appType")
        public String appType;
        @JsonProperty("deviceType")
        public String deviceType;

        public AppTypeRequest(String appType) {
            this.appType = appType;
        }

        public AppTypeRequest(String appType, String deviceType) {
            this.appType = appType;
            this.deviceType = deviceType;
        }

    }

    class GetAppIdRequest extends AppTypeRequest {

        @JsonProperty("token")
        public String token;


        @JsonProperty("versionName")
        public String versionName;

        public GetAppIdRequest(String appType, String token,String versionName) {
            super(appType);
            this.token = token;
            this.versionName = versionName;
        }
    }

    class AppUserGuidRequest extends AbsPostRequest {

        @JsonProperty("appGuid")
        public String appGuid;

        @JsonProperty("userId")
        public long userId;

        public AppUserGuidRequest(String appId, long userId) {
            this.appGuid = appId;
            this.userId = userId;
        }
    }


    class ReportLogRequest extends AbsPostRequest {

        @JsonProperty("appGuid")
        public String appGuid;

        @JsonProperty("verCode")
        public String verCode;

        @JsonProperty("logType")
        public int logType;

        @JsonProperty("log")
        public String log;

        public ReportLogRequest(String appGuid, String verCode, int logType,
                                String log) {

            this.appGuid = appGuid;
            this.verCode = verCode;
            this.logType = logType;
            this.log = log;
        }

    }


    class ChatisExistRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("time")
        public long time;

        public ChatisExistRequest(long userId, long time) {
            this.userId = userId;
            this.time = time;
        }

        public ChatisExistRequest(long userId, Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            this.userId = userId;
            this.time = c.getTimeInMillis();
        }

    }

    class ChatSendRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("msg")
        public String msg;

        public ChatSendRequest(long userId, String msg) {
            this.userId = userId;
            this.msg = msg;
        }
    }

    class ChatGetRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("time")
        public long time;

        @JsonProperty("count")
        public int count;

        public ChatGetRequest(long userId, long time, int count) {
            this.userId = userId;
            this.time = time;
            this.count = count;
        }

        public ChatGetRequest(long userId, Date date, int count) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            this.userId = userId;
            this.time = c.getTimeInMillis();
            this.count = count;
        }

    }

    // ==========================================================Common End==========================================================
    // ==========================================================User Start==========================================================

    class UserRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;

        public UserRequest(long userId) {
            this.userId = userId;
        }
    }

    class AccountRequest extends AbsPostRequest {
        @JsonProperty("account")
        public String account;

        public AccountRequest(String account) {
            this.account = account;
        }
    }

    class RegistRequest extends AbsPostRequest {

        @JsonProperty("nickname")
        public String name;

        @JsonProperty("pwd")
        public String password;

        @JsonProperty("gender")
        public boolean gender;

        @JsonProperty("figure")
        public String figure;

        public RegistRequest(String nickname, String password, String figure,
                             boolean gender) {
            this.name = nickname;
            this.password = password;
            this.gender = gender;
            this.figure = figure;
        }

    }

    class RegistByPhoneRequest extends RegistRequest {

        @JsonProperty("phone")
        public String phone;

        @JsonProperty("verifyCode")
        public String verifyCode;

        public RegistByPhoneRequest(String phone, String nickname,
                                    String password, String figure, boolean gender,
                                    String verifyCode) {
            super(nickname, password, figure, gender);
            this.phone = phone;
            this.verifyCode = verifyCode;
        }

    }

    class RegistByEmailRequest extends RegistRequest {

        @JsonProperty("email")
        public String email;

        public RegistByEmailRequest(String email, String nickname,
                                    String password, String figure, boolean gender) {
            super(nickname, password, figure, gender);
            this.email = email;
        }

    }

    class LoginRequest extends AccountRequest {
        @JsonProperty("pwd")
        public String pwd;

        public LoginRequest(String account, String pwd) {
            super(account);
            this.pwd = pwd;
        }
    }

    class LogoutRequest extends AbsPostRequest {

        @JsonProperty("tgt")
        public String tgt;

        public LogoutRequest(String tgt) {
            this.tgt = tgt;
        }
    }

    class ExpressLoginRequest extends AbsPostRequest {

        @JsonProperty("phone")
        public String phone;

        @JsonProperty("verifyCode")
        public String verifyCode;

        public ExpressLoginRequest(String phone, String verifyCode) {
            this.phone = phone;
            this.verifyCode = verifyCode;
        }
    }

    class UpdateUserRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long id;

        @JsonProperty("nickname")
        public String name;

        @JsonProperty("phone")
        public String phone;

        @JsonProperty("email")
        public String email;

        @JsonProperty("gender")
        public boolean gender;

        public UpdateUserRequest(long id, String name, String phone,
                                 String email, boolean gender) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
        }

    }

    class UpdatePasswordRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long id;

        @JsonProperty("oldPwd")
        public String oldPassword;

        @JsonProperty("newPwd")
        public String newPassword;

        public UpdatePasswordRequest(long userId, String oldPassword,
                                     String newPassword) {
            this.id = userId;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
    }

    class UpdateFigureRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long id;

        @JsonProperty("figure")
        public String figure;

        public UpdateFigureRequest(long userId, String figure) {
            this.id = userId;
            this.figure = figure;
        }
    }

    class GetVerifyCodeRequest extends AbsPostRequest {

        @JsonProperty("phone")
        public String phone;

        public GetVerifyCodeRequest(String phone) {
            this.phone = phone;
        }

    }

    class ResetPwdByEmailRequest extends AbsPostRequest {

        @JsonProperty("email")
        public String email;

        public ResetPwdByEmailRequest(String email) {
            this.email = email;
        }

    }

    class ResetPwdByPhoneRequest extends AbsPostRequest {

        @JsonProperty("phone")
        public String phone;

        @JsonProperty("newPwd")
        public String password;

        @JsonProperty("verifyCode")
        public String verifyCode;

        public ResetPwdByPhoneRequest(String phone, String newPwd,
                                      String verifyCode) {
            this.phone = phone;
            this.password = newPwd;
            this.verifyCode = verifyCode;
        }

    }

    class LoginFrom3rdRequest extends AbsPostRequest {

        @JsonProperty("platType")
        public int platId;

        @JsonProperty("account")
        public String account;

        @JsonProperty("nickname")
        public String name;

        @JsonProperty("figureUrl")
        public String figureUrl;

        @JsonProperty("cert")
        public String token;

        public LoginFrom3rdRequest(int platId, String account, String nickname,
                                   String figureUrl, String token) {
            this.platId = platId;
            this.account = account;
            this.name = nickname;
            this.figureUrl = figureUrl;
            this.token = token;
        }
    }

    class Bind3rdRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("platType")
        public int platType;

        @JsonProperty("thirdId")
        public String thirdId;

        @JsonProperty("thirdName")
        public String name;

        public Bind3rdRequest(long userId, int platType, String thirdId,
                              String nickname) {
            this.userId = userId;
            this.platType = platType;
            this.thirdId = thirdId;
            this.name = nickname;
        }
    }

    class Unbind3rdRequest extends AbsPostRequest {

        @JsonProperty("userId")
        public long id;

        @JsonProperty("platType")
        public int platType;

        public Unbind3rdRequest(long userId, int platType) {
            this.id = userId;
            this.platType = platType;
        }
    }


    // ==========================================================User End==========================================================
    // ==========================================================Device Start==========================================================


    class GuidRequest extends AbsPostRequest {
        @JsonProperty("guid")
        public String guid;

        public GuidRequest(String guid) {
            this.guid = guid;
        }
    }

    class UserGuidRequest extends UserRequest {
        @JsonProperty("guid")
        public String guid;

        public UserGuidRequest(long userId, String guid) {
            super(userId);
            this.guid = guid;
        }
    }

    class UserGuidAllRequest extends UserRequest {
        @JsonProperty("guids")
        public String guids;

        public UserGuidAllRequest(long userId, String guid) {
            super(userId);
            this.guids = guid;
        }
    }

    class UserGroupRequest extends UserRequest {
        @JsonProperty("groupId")
        long groupId;

        public UserGroupRequest(long userId, long groupId) {
            super(userId);
            this.groupId = groupId;
        }
    }

    class UserGroupGuidRequest extends UserGroupRequest {

        @JsonProperty("guid")
        public String guid;

        public UserGroupGuidRequest(long userId, long groupId, String guid) {
            super(userId, groupId);
            this.guid = guid;
        }
    }

    class AddDeviceGroupRequest extends UserRequest {
        @JsonProperty("name")
        String name;

        public AddDeviceGroupRequest(long userId, String name) {
            super(userId);
            this.name = name;
        }
    }

    class GetDeviceBySnRequest extends AbsPostRequest {
        @JsonProperty("sn")
        public String sn;

        public GetDeviceBySnRequest(String sn) {
            this.sn = sn;
        }
    }

    class UpdateGroupNameRequest extends UserGroupRequest {
        @JsonProperty("name")
        String name;

        public UpdateGroupNameRequest(long userId, long groupId, String name) {
            super(userId, groupId);
            this.name = name;
        }
    }

    class UpdateDeviceNameRequest extends UserGuidRequest {
        @JsonProperty("name")
        String name;

        public UpdateDeviceNameRequest(long userId, String guid, String name) {
            super(userId, guid);
            this.name = name;
        }
    }

    class BindDeviceRequest extends UserGuidRequest {

        @JsonProperty("name")
        public String name;

        @JsonProperty("isOwner")
        public boolean isOwner;

        public BindDeviceRequest(long userId, String guid, String name,
                                 boolean isOwner) {
            super(userId, guid);
            this.name = name;
            this.isOwner = isOwner;
        }
    }

    class UnbindDeviceRequest extends UserRequest {

        @JsonProperty("guid")
        public String guid;

        public UnbindDeviceRequest(long userId, String guid) {
            super(userId);
            this.guid = guid;
        }
    }

    class UpdateDeviceGroupNameRequest extends UserRequest {

        @JsonProperty("groupId")
        public long groupId;

        @JsonProperty("groupName")
        public String groupName;

        public UpdateDeviceGroupNameRequest(long userId, long groupId,
                                            String groupName) {
            super(userId);
            this.groupId = groupId;
            this.groupName = groupName;
        }
    }

    class DeleteDeviceUsersRequest extends UserGuidRequest {

        @JsonProperty("userIds")
        public List<Long> userIds;

        public DeleteDeviceUsersRequest(long userId, String guid,
                                        List<Long> userIds) {
            super(userId, guid);
            this.userIds = userIds;
        }
    }

    class AddDeviceListRequest extends AbsPostRequest{

        @JsonProperty("vendor")
        public String vendor;

        @JsonProperty("dc")
        public String dc;

        @JsonProperty("dt")
        public String dt;

        public AddDeviceListRequest(String vendor, String dc, String dt) {
            this.vendor = vendor;
            this.dc = dc;
            this.dt = dt;
        }
    }
    // ==========================================================Device End==========================================================

}
