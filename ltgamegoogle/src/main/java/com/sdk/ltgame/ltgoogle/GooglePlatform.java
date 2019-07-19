package com.sdk.ltgame.ltgoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sdk.ltgame.ltgoogle.uikit.GoogleLoginActivity;
import com.gentop.ltgame.ltgamesdkcore.common.LTGameOptions;
import com.gentop.ltgame.ltgamesdkcore.common.LTGameSdk;
import com.gentop.ltgame.ltgamesdkcore.common.Target;
import com.gentop.ltgame.ltgamesdkcore.impl.OnLoginStateListener;
import com.gentop.ltgame.ltgamesdkcore.impl.OnRechargeListener;
import com.gentop.ltgame.ltgamesdkcore.model.LoginObject;
import com.gentop.ltgame.ltgamesdkcore.model.RechargeObject;
import com.gentop.ltgame.ltgamesdkcore.platform.AbsPlatform;
import com.gentop.ltgame.ltgamesdkcore.platform.IPlatform;
import com.gentop.ltgame.ltgamesdkcore.platform.PlatformFactory;
import com.gentop.ltgame.ltgamesdkcore.uikit.BaseActionActivity;
import com.gentop.ltgame.ltgamesdkcore.util.LTGameUtil;

public class GooglePlatform extends AbsPlatform {

    private GoogleLoginHelper mGoogleHelper;

    private GooglePlatform(Context context, String baseUrl, String appId, String appKey, String clientID,
                           String adID, String packageID, int selfRequestCode, int target) {
        super(context, baseUrl, appId, appKey, clientID, adID, packageID, selfRequestCode, target);
    }

    /**
     * 工厂
     */
    public static class Factory implements PlatformFactory {


        @Override
        public IPlatform create(Context context, int target) {
            IPlatform platform = null;
            LTGameOptions options = LTGameSdk.options();
            if (!LTGameUtil.isAnyEmpty(options.getGoogleClientID(), options.getAdID(),
                    options.getLtAppId(), options.getLtAppKey(),
                    options.getPackageID(), options.getBaseUrl()) && options.getSelfRequestCode() != -1) {
                platform = new GooglePlatform(context, options.getBaseUrl(), options.getLtAppId(),
                        options.getLtAppKey(), options.getGoogleClientID(), options.getAdID(),
                        options.getPackageID(), options.getSelfRequestCode(), target);
            }
            return platform;
        }

        @Override
        public int getPlatformTarget() {
            return Target.PLATFORM_GOOGLE;
        }

        @Override
        public boolean checkLoginPlatformTarget(int target) {
            return target == Target.LOGIN_GOOGLE;
        }

        @Override
        public boolean checkRechargePlatformTarget(int target) {
            return false;
        }
    }

    @Override
    public Class getUIKitClazz() {
        return GoogleLoginActivity.class;
    }

    @Override
    public void onActivityResult(BaseActionActivity activity, int requestCode, int resultCode, Intent data) {
        super.onActivityResult(activity, requestCode, resultCode, data);
        mGoogleHelper.onActivityResult(requestCode, data, mGoogleHelper.selfRequestCode);
    }


    @Override
    public void login(Activity activity, int target, LoginObject object, OnLoginStateListener listener) {
        mGoogleHelper = new GoogleLoginHelper(activity, object.getmGoogleClient(), object.getmAdID(),
                object.getSelfRequestCode(), listener);
        if (object.isLoginOut()) {
            mGoogleHelper.loginOut(activity, object.getmGoogleClient());
        } else {
            mGoogleHelper.login();
        }


    }

    @Override
    public void recharge(Activity activity, int target, RechargeObject object, OnRechargeListener listener) {

    }
}
