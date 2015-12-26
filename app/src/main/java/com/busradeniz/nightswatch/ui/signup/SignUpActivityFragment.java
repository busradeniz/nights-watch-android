package com.busradeniz.nightswatch.ui.signup;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.signup.SignUpRequest;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignUpActivityFragment extends Fragment {

    @Bind(R.id.signup_txt_email)
    EditText signup_txt_email;
    @Bind(R.id.signup_txt_password) EditText signup_txt_password;
    @Bind(R.id.signup_txt_password_again) EditText signup_txt_password_again;
    @Bind(R.id.signup_txt_username) EditText signup_txt_username;
    @Bind(R.id.btn_signup)
    AppCompatButton btn_signup;
    public SignUpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this,view);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return view;

    }
    private void signUp(){
        //TODO add validation

        sendSignUpRequest();
    }

    private void sendSignUpRequest(){
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
                        //TODO add information box
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {
                        openHomeScreen();
                    }
                });
    }

    private void openHomeScreen(){
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }
}
