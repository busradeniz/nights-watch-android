package com.busradeniz.nightswatch.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.service.login.LoginRequest;
import com.busradeniz.nightswatch.service.login.LoginResponse;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.ui.resetpassword.ResetPasswordActivity;
import com.busradeniz.nightswatch.ui.signup.SignUpActivity;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {


    @Bind(R.id.login_txt_username)
    EditText login_txt_username;
    @Bind(R.id.login_txt_password)
    EditText login_txt_password;
    @Bind(R.id.login_btn_login)
    AppCompatButton login_btn_login;
    @Bind(R.id.login_btn_signup)
    TextView login_btn_signup;
    @Bind(R.id.login_txt_username_fail)
    TextView login_txt_username_fail;
    @Bind(R.id.login_txt_password_fail)
    TextView login_txt_password_fail;
    @Bind(R.id.login_forgot_password)
    TextView login_forgot_password;

    private ProgressDialog progressDialog;

    public LoginActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        login_txt_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                login_txt_username_fail.setText("");
                login_txt_password_fail.setText("");
                return false;
            }
        });
        login_txt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                login_txt_username_fail.setText("");
                login_txt_password_fail.setText("");
                return false;
            }
        });

        login_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpScreen();
            }
        });

        login_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordScreen();
            }
        });


        login_txt_password.setText("12345");
        login_txt_username.setText("test");
        return view;
    }


    private void login() {
        if (!validate()) {
            return;
        }
        sendLoginRequest();
    }

    private void sendLoginRequest() {
        showProgress();
        final LoginRequest loginRequest = new LoginRequest(login_txt_username.getText().toString(), login_txt_password.getText().toString());
        ServiceProvider.getLoginService().signin(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i("LOGIN", "login failed :" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        progressDialog.dismiss();
                        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        //TODO  add remember me
                        editor.putInt("userId", loginResponse.getUserId());
                        editor.putString("username" , login_txt_username.getText().toString());
                        editor.commit();
                        NightsWatchApplication.token = loginResponse.getToken();
                        NightsWatchApplication.userId = loginResponse.getUserId();
                        NightsWatchApplication.username= login_txt_username.getText().toString();
                        openHomeScreen();
                    }
                });
    }

    private boolean validate() {

        String username = login_txt_username.getText().toString();
        String password = login_txt_password.getText().toString();

        if (username.isEmpty()) {
            login_txt_username_fail.setText("Enter valid username");
            login_txt_username_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (password.length() < Constants.PASSWORD_LENGTH) {
            login_txt_password_fail.setText("Enter valid password");
            login_txt_password_fail.setVisibility(View.VISIBLE);
            return false;
        }


        return true;

    }

    private void openSignUpScreen() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }

    private void openHomeScreen() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private void openForgotPasswordScreen() {
        Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void onRememberMeClicked(){

    }
}
