package com.busradeniz.nightswatch.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.login.LoginRequest;
import com.busradeniz.nightswatch.service.login.LoginResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        NightsWatchApplication.context = this;

        SharedPreferences preferences = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("username" , "").length() == 0){
            Intent intent = new Intent(Splash.this , LoginActivity.class);
            startActivity(intent);
        }else {

            final Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    sendLoginRequest();
                    timer.cancel();
                }
            }, 1000);


        }
    }


    private void sendLoginRequest() {
        final SharedPreferences preferences = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);

        final LoginRequest loginRequest = new LoginRequest(preferences.getString("username",""), preferences.getString("password",""));
        ServiceProvider.getLoginService().signin(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LOGIN", "login failed :" + e.getLocalizedMessage());
                        finish();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.i("HomeActivity", "login success");

                        NightsWatchApplication.token = loginResponse.getToken();
                        NightsWatchApplication.userId = loginResponse.getUserId();
                        NightsWatchApplication.username= preferences.getString("username","");

                        Intent intent = new Intent(Splash.this , HomeActivity.class);
                        startActivity(intent);
                    }
                });
    }

}
