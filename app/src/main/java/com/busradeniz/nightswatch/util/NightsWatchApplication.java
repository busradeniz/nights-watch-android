package com.busradeniz.nightswatch.util;

import android.app.Application;
import android.content.Context;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

/**
 * Created by busradeniz on 23/12/15.
 */
public class NightsWatchApplication extends Application {

    public static Context context;
    public static String token;
    public static int userId;
    public static String username;
    public static GoogleApiClient mGoogleApiClient;
    public static double latitude;
    public static double longitude;
    public static SignUpResponse user;

    public static boolean hasRole(String roleName) {
        if (user == null
                || user.getRoles() == null
                || user.getRoles().isEmpty()) {
            return false;
        }

        return user.getRoles().contains(roleName) || user.getRoles().contains(roleName.toLowerCase(Locale.ENGLISH));
    }


}
