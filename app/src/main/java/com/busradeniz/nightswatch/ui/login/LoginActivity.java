package com.busradeniz.nightswatch.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

    }

    @Override
    public void onBackPressed(){

    }
}
