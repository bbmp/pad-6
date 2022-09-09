package com.legent.plat.services;

import android.content.Context;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.PlatformUser;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.User3rd;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.services.account.AuthCallback;
import com.legent.plat.services.account.IAppOAuthService;
import com.legent.utils.EventUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class AccountService extends AbsAccountCloudService {

    private final static String AutoLogin = "AutoLogin";
    private final static String IsOwnUser = "IsOwnUser";
    private final static String LastUserId = "LastUserId";
    private final static String LastAccount = "LastAccount";
    private final static String LastOwnAccount = "LastOwnAccount";
    private final static String LastPlatId = "LastPlatId";
    private final static String LastPlat3rd_Nickname = "LastPlat3rd_Nickname";
    private final static String LastPlat3rd_FaceUrl = "LastPlat3rd_FaceUrl";
    private final static String LastPlat3rd_Cert = "LastPlat3rd_Cert";

    private static AccountService instance = new AccountService();

    synchronized public static AccountService getInstance() {
        return instance;
    }

    private AccountService() {
    }

    private User curUser;
    private Map<Long, User> mapUsers = Maps.newHashMap();

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.init(cx, params);
        }

        boolean autoLogin = PreferenceUtils.getBool(AutoLogin, true);

        if (autoLogin) {
            autoLogin(null);
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.dispose();
        }
    }

    // -------------------------------------------------------------------------------
    // IAccountService
    // -------------------------------------------------------------------------------

    public boolean isLogon() {
        return curUser != null;
    }

    public User getCurrentUser() {
        return curUser;
    }

    public long getCurrentUserId() {
        if (curUser != null) {
            return curUser.id;
        }
        return PreferenceUtils.getLong(LastUserId, 0);
    }

    public String getLastAccount() {
        return PreferenceUtils.getString(LastOwnAccount, null);
    }

    public void setAutoLogin(boolean autoFlag) {
        PreferenceUtils.setBool(AutoLogin, autoFlag);
    }

    public void autoLogin(Callback<User> callback) {
        if (curUser != null) {
            Helper.onSuccess(callback, curUser);
        } else {
            loginByLastAccount(callback);
        }
    }

    public void login3rd(Context cx, final int platId,
                         final Callback<User> callback) {

        authorizeBy3rd(cx, platId, new AuthCallback() {

            @Override
            public void onAuthCompleted(final PlatformUser pu) {
                loginFrom3rd(platId, pu.userId, pu.nickname, pu.figureUrl, pu.token,
                        new Callback<User>() {
                            @Override
                            public void onSuccess(User user) {
                                onLoginForThirdParty(user, platId, pu.userId,
                                        pu.nickname, pu.figureUrl, pu.token);
                                Helper.onSuccess(callback, user);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Helper.onFailure(callback, t);
                            }
                        });
            }

            @Override
            public void onError(Throwable t) {
                Helper.onFailure(callback, t);
            }

            @Override
            public void onCancel() {
                Helper.onFailure(callback, ExceptionHelper
                        .newPlatException(ResultCodeManager.EC_CancelAuth3rd));
            }
        });

    }

    public void bind3rd(Context cx, final int platId,
                        final VoidCallback callback) {
        bindThirdPlatAccount(cx, platId, new Callback<PlatformUser>() {
            @Override
            public void onSuccess(final PlatformUser pu) {
                final User3rd u3 = new User3rd(pu.userId, pu.nickname);

                bind3rd(curUser.id, platId, pu.userId, pu.nickname, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        curUser.bind3rd(platId, u3);

                        EventUtils.postEvent(new UserUpdatedEvent(curUser));
                        Helper.onSuccess(callback);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void unbind3rd(final int platId, final VoidCallback callback) {
        unbind3rd(curUser.id, platId, new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.unbind3rd(platId);

                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void logout(final VoidCallback callback) {
        if (curUser != null) {
            logout(curUser.TGT, new VoidCallback() {
                @Override
                public void onSuccess() {
                    onLogout(curUser);
                    PreferenceUtils.setBool(PrefsKey.Guided, false);
                    if (null != callback) {
                        callback.onSuccess();
                    }
                    //          clearLoginTimeOut();
                }

                @Override
                public void onFailure(Throwable t) {
                    if (null != callback) {
                        callback.onFailure(t);
                    }
                }
            });
        }
    }

    // -------------------------------------------------------------------------------
    // override
    // -------------------------------------------------------------------------------

    @Override
    public void login(final String account, final String password, final Callback<User> callback) {
        CloudHelper.login(account, password, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                user.password = password;
                onLoginForOwnUser(account, user);
                Helper.onSuccess(callback, user);
                PreferenceUtils.setBool(PrefsKey.Guided, true);
                // setLoginTimeOut(user);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    Timer timer = null;

    private void setLoginTimeOut(final User user) {
        try {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Plat.deviceService.getDevices(user.id, new Callback<List<DeviceInfo>>() {
                    @Override
                    public void onSuccess(List<DeviceInfo> deviceInfos) {
                        try {
                            if (timer != null) {
                                timer.cancel();
                                timer.purge();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            timer = null;
                            setLoginTimeOut(user);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        try {
                            if (timer != null) {
                                timer.cancel();
                                timer.purge();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            timer = null;
                            setLoginTimeOut(user);
                        }
                    }
                });
            }
        }, 1000 * 60 * 60 * 12);
    }

    private void clearLoginTimeOut() {
        try {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
        } catch (Exception e) {
        } finally {
            timer = null;
        }

    }

    @Override
    public void expressLogin(final String phone, String verifyCode, final Callback<User> callback) {
        CloudHelper.expressLogin(phone, verifyCode, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                onLoginForOwnUser(phone, user);
                Helper.onSuccess(callback, user);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    @Override
    public void getUser(final long userId, final Callback<User> callback) {
        if (mapUsers.containsKey(userId)) {
            User user = mapUsers.get(userId);
            Helper.onSuccess(callback, user);
        } else {
            CloudHelper.getUser(userId, new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    mapUsers.put(userId, user);
                    Helper.onSuccess(callback, user);
                }

                @Override
                public void onFailure(Throwable t) {
                    Helper.onFailure(callback, t);
                }
            });
        }
    }

    @Override
    public void updateUser(long id, final String name, final String phone, final String email,
                           final boolean gender, final VoidCallback callback) {
        CloudHelper.updateUser(id, name, phone, email, gender, new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.name = name;
                curUser.phone = phone;
                curUser.email = email;
                curUser.gender = gender;
                saveUser2Preference(curUser);
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    @Override
    public void updateFigure(long userId, String figure,
                             final Callback<String> callback) {
        CloudHelper.updateFigure(userId, figure, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                curUser.figureUrl = s;
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback, s);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });

    }

    @Override
    public void updatePassword(long userId, String oldPwd, final String newPwd,
                               final VoidCallback callback) {
        CloudHelper.updatePassword(userId, oldPwd, newPwd, new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.password = newPwd;
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    //-------------------------------------------------------------------------------
    // override
    //-------------------------------------------------------------------------------

    private void loginByLastAccount(final Callback<User> callback) {

        String account = PreferenceUtils.getString(LastAccount, null);
        if (Strings.isNullOrEmpty(account)) {
            Helper.onFailure(callback, new Exception("not found any user"));
            return;
        }

        boolean isOwnUser = PreferenceUtils.getBool(IsOwnUser, true);
        if (isOwnUser) {
            String pwd = PreferenceUtils.getString(account, null);
            login(account, pwd, callback);
        } else {
            int platId = PreferenceUtils.getInt(LastPlatId, 0);
            String nickname = PreferenceUtils.getString(LastPlat3rd_Nickname,
                    null);
            String faceUrl = PreferenceUtils.getString(LastPlat3rd_FaceUrl,
                    null);
            String cert = PreferenceUtils.getString(LastPlat3rd_Cert, null);

            loginFrom3rd(platId, account, nickname, faceUrl, cert,
                    new Callback<User>() {

                        @Override
                        public void onSuccess(User user) {
                            Helper.onSuccess(callback, user);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Helper.onFailure(callback, t);
                        }
                    });
        }
    }

    private void bindThirdPlatAccount(Context cx, final int platId,
                                      final Callback<PlatformUser> callback) {

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.removeAuth(platId);
        }

        authorizeBy3rd(cx, platId, new AuthCallback() {

            @Override
            public void onError(Throwable t) {
                Helper.onFailure(callback, t);
            }

            @Override
            public void onAuthCompleted(PlatformUser platUser) {
                Helper.onSuccess(callback, platUser);
            }

            @Override
            public void onCancel() {
                Helper.onFailure(callback, ExceptionHelper
                        .newPlatException(ResultCodeManager.EC_CancelAuth3rd));
            }
        });
    }


    private void authorizeBy3rd(Context cx, int platId,
                                AuthCallback callback) {

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.authorize(cx, platId, callback);
        } else {
            if (callback != null) {
                callback.onError(new Throwable("Plat.appOAuthService is null"));
            }
        }
    }

    private void onLoginForOwnUser(String account, User user) {
        user.loginPlatId = IAppOAuthService.PlatId_Self;

        PreferenceUtils.setBool(IsOwnUser, true);
        PreferenceUtils.setString(LastOwnAccount, account);
        PreferenceUtils.setString(account, user.password);

        onLogin(user);
    }

    private void onLoginForThirdParty(User user, int platId, String platUserId,
                                      String nickname, String faceUrl, String cert) {

        user.loginPlatId = platId;

        PreferenceUtils.setBool(IsOwnUser, false);
        PreferenceUtils.setInt(LastPlatId, platId);
        PreferenceUtils.setString(LastPlat3rd_Nickname, nickname);
        PreferenceUtils.setString(LastPlat3rd_FaceUrl, faceUrl);
        PreferenceUtils.setString(LastPlat3rd_Cert, cert);

        onLogin(user);
    }

    private void onLogin(User user) {
        curUser = user;
        saveUser2Preference(user);
        EventUtils.postEvent(new UserLoginEvent(user));
    }

    private void onLogout(User user) {
        if (!StringUtils.isNullOrEmpty(Plat.appType) && IAppType.RKDRD.equals(Plat.appType)) {
            EventUtils.postEvent(new UserLogoutEvent(user));
            saveUser2Preference(null);
            curUser = null;
        } else {
            curUser = null;
            saveUser2Preference(null);
            EventUtils.postEvent(new UserLogoutEvent(user));
        }
    }

    private void saveUser2Preference(User user) {
        PreferenceUtils.setLong(LastUserId, user != null ? user.id : 0);
        PreferenceUtils.setString(LastAccount, user != null ? user.getAccount() : null);
        PreferenceUtils.setString(LastOwnAccount, user != null ? user.getAccount() : null);

    }


}
