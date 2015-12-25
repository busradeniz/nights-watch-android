package com.busradeniz.nightswatch.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by busradeniz on 23/12/15.
 */
public class NightsWatchApplication extends Application {

    private static NightsWatchApplication application;

    public static NightsWatchApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }


}
