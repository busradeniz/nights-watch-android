package com.busradeniz.nightswatch.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.home.HomeActivity;
import com.busradeniz.nightswatch.signup.SignUpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {


    @Bind(R.id.login_txt_username) EditText login_txt_username;
    @Bind(R.id.login_txt_password) EditText login_txt_password;
    @Bind(R.id.login_btn_login) AppCompatButton login_btn_login;
    @Bind(R.id.login_btn_signup) TextView login_btn_signup;
    @Bind(R.id.login_txt_username_fail) TextView login_txt_username_fail;
    @Bind(R.id.login_txt_password_fail) TextView login_txt_password_fail;

    public LoginActivityFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);

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
                 openSignUpPage();
            }
        });
        return view;
    }


    private void login(){

        if (!validate()){
         //   return;
        }


        //TODO integrate call service
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);

    }


    private boolean validate(){
        boolean valid = true;

        String username = login_txt_username.getText().toString();
        String password = login_txt_password.getText().toString();

        if (username.isEmpty()){
            login_txt_username_fail.setText("Enter valid username");
            login_txt_username_fail.setVisibility(View.VISIBLE);
            valid = false;

        }

        if (password.isEmpty() || password.length() < 6){
            login_txt_password_fail.setText("Enter valid password");
            login_txt_password_fail.setVisibility(View.VISIBLE);
            valid = false;
        }


        return valid;

    }

    private void openSignUpPage(){
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }
}
