package com.busradeniz.nightswatch.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.signup.SignUpRequest;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignUpActivityFragment extends Fragment {
    private static String TAG = "SignUpActivityFragment";

    @Bind(R.id.signup_txt_email)
    EditText signup_txt_email;
    @Bind(R.id.signup_txt_password)
    EditText signup_txt_password;
    @Bind(R.id.signup_txt_password_again)
    EditText signup_txt_password_again;
    @Bind(R.id.signup_txt_username)
    EditText signup_txt_username;

    @Bind(R.id.signup_txt_username_fail)
    TextView signup_txt_username_fail;
    @Bind(R.id.signup_txt_password_fail)
    TextView signup_txt_password_fail;
    @Bind(R.id.signup_txt_password_again_fail)
    TextView signup_txt_password_again_fail;
    @Bind(R.id.signup_txt_email_fail)
    TextView signup_txt_email_fail;

    @Bind(R.id.btn_signup)
    AppCompatButton btn_signup;


    private ProgressDialog progressDialog;

    public SignUpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);


        signup_txt_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailTExt();
                return false;
            }
        });
        signup_txt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailTExt();
                return false;
            }
        });
        signup_txt_password_again.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailTExt();
                return false;
            }
        });
        signup_txt_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailTExt();
                return false;
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return view;

    }

    private void signUp() {

        if (!validate()) {
            return;
        }
        sendSignUpRequest();
    }

    private void sendSignUpRequest() {
        showProgress();
        SignUpRequest signUpRequest = new SignUpRequest(signup_txt_email.getText().toString(), signup_txt_password.getText().toString(), signup_txt_username.getText().toString());
        ServiceProvider.getSignUpService().signup(signUpRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignUpResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "SignUpRequest failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.signup_failed_text), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {
                        Log.i(TAG,"SignUpRequest success");
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.signup_success_text), Toast.LENGTH_LONG).show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().finish();
                            }
                        }, 2000);
                    }
                });
    }

    private boolean validate() {
        String password = signup_txt_password.getText().toString();
        String newPasswordAgain = signup_txt_password_again.getText().toString();
        String username = signup_txt_username.getText().toString();
        String email = signup_txt_email.getText().toString();


        if (username.isEmpty()) {
            signup_txt_username_fail.setText(getString(R.string.reset_password_password_fail_text));
            signup_txt_username_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (email.isEmpty()) {
            signup_txt_email_fail.setText(getString(R.string.reset_password_email_fail_text));
            signup_txt_email_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (password.length() < Constants.PASSWORD_LENGTH) {
            signup_txt_password_fail.setText(getString(R.string.change_password_password_fail_text));
            signup_txt_password_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (!newPasswordAgain.equals(password)) {
            signup_txt_password_fail.setText(getString(R.string.change_password_not_match_fail_text));
            signup_txt_password_fail.setVisibility(View.VISIBLE);
            signup_txt_password_again_fail.setText(getString(R.string.change_password_not_match_fail_text));
            signup_txt_password_again_fail.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void resetFailTExt() {
        signup_txt_email_fail.setText("");
        signup_txt_password_fail.setText("");
        signup_txt_password_again_fail.setText("");
        signup_txt_username_fail.setText("");
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }
}
