package com.shantanukamath.knockout;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class KnockOutApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(this);

    }
}