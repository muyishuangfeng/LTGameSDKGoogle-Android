package com.sdk.ltgame.ltgoogle.model;

import com.gentop.ltgame.ltgamesdkcore.common.Target;
import com.gentop.ltgame.ltgamesdkcore.model.token.AccessToken;

public class GoogleAccessToken extends AccessToken {

    private String access_token;

    @Override
    public String getAccess_token() {
        return access_token;
    }

    @Override
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public int getLoginTarget() {
        return Target.LOGIN_GOOGLE;
    }
}
