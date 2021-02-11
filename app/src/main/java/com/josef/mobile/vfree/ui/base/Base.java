package com.josef.mobile.vfree.ui.base;

public interface Base {

    int SPLASH_ACTIVITY_ID = 9001;
    int AUTH_ACTIVITY_ID = 9002;
    int MAIN_ACTIVITY_ID = 9003;
    int PLAYER_ACTIVITY_ID = 9004;
    int ERROR_ACTIVITY_ID = 9005;
    int SIGN_ACTIVITY_ID = 9006;
    int VERIFICATION_ACTIVITY_ID = 9007;

    String SPLASH_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.splash.SplashActivity";
    String AUTH_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.auth.AuthActivity";
    String MAIN_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.main.MainActivity";
    String PLAYER_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.player.PlayerActivity";
    String ERROR_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.err.ErrorActivity";
    String SIGN_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.auth.option.account.SignActivity";
    String VERIFICATION_ACTIVITY_NAME = "com.josef.mobile.vfree.ui.auth.option.verification.VerificationActivity";

   static String REQUEST_INDEX = "request_url";
   static String REQUEST_ENDPOINT = "request_endpoint";
}
