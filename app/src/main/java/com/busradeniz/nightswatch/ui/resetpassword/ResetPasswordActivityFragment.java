package com.busradeniz.nightswatch.ui.resetpassword;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.login.ResetPasswordRequest;
import com.busradeniz.nightswatch.service.login.ResetPasswordResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResetPasswordActivityFragment extends Fragment {

    @Bind(R.id.reset_password_txt_email) EditText reset_password_txt_email;
    @Bind(R.id.reset_password_txt_username) EditText reset_password_txt_username;
    @Bind(R.id.reset_password_btn_title) AppCompatButton reset_password_btn;


    public ResetPasswordActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this,view);

        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordRequest();
            }
        });
        return view;
    }

    private void sendResetPasswordRequest(){

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(reset_password_txt_email.getText().toString(), reset_password_txt_username.getText().toString());
        ServiceProvider.getLoginService().resetPassword(resetPasswordRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResetPasswordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResetPasswordResponse resetPasswordResponse) {

                    }
                });
    }
}
