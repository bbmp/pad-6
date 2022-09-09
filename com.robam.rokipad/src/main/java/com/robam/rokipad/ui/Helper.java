package com.robam.rokipad.ui;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.utils.api.ToastUtils;

/**
 * Created by WZTCM on 2015/12/19.
 */
public class Helper {
    public static void login(String account, String pwdMd5) {
        Plat.accountService.login(account, pwdMd5, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                Helper.onLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    public static void onLoginCompleted(final User user) {
        UIService.getInstance().returnHome();
    }


}
