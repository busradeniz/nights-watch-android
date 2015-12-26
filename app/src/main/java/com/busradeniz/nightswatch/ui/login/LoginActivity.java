package com.busradeniz.nightswatch.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NightsWatchApplication.context = this;
    }

}
