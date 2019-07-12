package com.gentop.ltgame.ltgamegoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.gentop.ltgame.ltgamenet.manager.LoginRealizeManager;
import com.gentop.ltgame.ltgamesdkcore.common.Target;
import com.gentop.ltgame.ltgamesdkcore.impl.OnLoginStateListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;


class GoogleLoginHelper {

    private static final String TAG = GoogleLoginHelper.class.getSimpleName();

    private int mLoginTarget;
    private WeakReference<Activity> mActivityRef;
    private OnLoginStateListener mListener;
    private String clientID;
    public int selfRequestCode;
    private String adID;

    GoogleLoginHelper(Activity activity, String clientID,  String adID,
                      int selfRequestCode, OnLoginStateListener listener) {
        this.mActivityRef = new WeakReference<>(activity);
        this.clientID = clientID;
        this.adID = adID;
        this.selfRequestCode = selfRequestCode;
        this.mListener = listener;
        this.mLoginTarget = Target.LOGIN_GOOGLE;
    }


    /**
     * 登录
     */
    void login() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(mActivityRef.get(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivityRef.get().startActivityForResult(signInIntent, selfRequestCode);
    }

    /**
     * 登录回调
     */
    void onActivityResult(int requestCode, Intent data, int selfRequestCode) {
        if (requestCode == selfRequestCode) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (!TextUtils.isEmpty(adID)) {
                handleSignInResult(task);
            }
        }
    }


    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            LoginRealizeManager.googleLogin(mActivityRef.get(), idToken, mListener);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     */
    void loginOut(Context context, String clientID) {
        Log.e(TAG, "退出登录");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                login();
            }
        });
    }

}
