package com.busradeniz.nightswatch.ui.resetpassword;

import android.app.ProgressDialog;
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
import com.busradeniz.nightswatch.service.login.ResetPasswordRequest;
import com.busradeniz.nightswatch.service.login.ResetPasswordResponse;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResetPasswordActivityFragment extends Fragment {

    private static String TAG = "ResetPasswordActivityFragment";


    @Bind(R.id.reset_password_txt_email)
    EditText reset_password_txt_email;
    @Bind(R.id.reset_password_txt_username)
    EditText reset_password_txt_username;
    @Bind(R.id.reset_password_btn_title)
    AppCompatButton reset_password_btn;
    @Bind(R.id.reset_password_txt_username_fail)
    TextView reset_password_txt_username_fail;
    @Bind(R.id.reset_password_txt_email_fail)
    TextView reset_password_txt_email_fail;


    private ProgressDialog progressDialog;


    public ResetPasswordActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);

        reset_password_txt_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                reset_password_txt_username_fail.setText("");
                reset_password_txt_email_fail.setText("");
                return false;
            }
        });
        reset_password_txt_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                reset_password_txt_username_fail.setText("");
                reset_password_txt_email_fail.setText("");
                return false;
            }
        });

        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    sendResetPasswordRequest();
                }
            }
        });
        return view;
    }

    private void sendResetPasswordRequest() {
        showProgress();
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
                        Log.i(TAG, "resetPassword request failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context,getString(R.string.reset_password_failed_text),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ResetPasswordResponse resetPasswordResponse) {
                        Log.i(TAG, "resetPassword request success");
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context,getString(R.string.reset_password_success_text),Toast.LENGTH_LONG).show();
                    }
                });
    }


    private boolean validate() {

        String email = reset_password_txt_email.getText().toString();
        String password = reset_password_txt_username.getText().toString();

        if (email.isEmpty()) {
            reset_password_txt_email_fail.setText(getString(R.string.reset_password_email_fail_text));
            reset_password_txt_email_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (password.isEmpty()) {
            reset_password_txt_username_fail.setText(getString(R.string.reset_password_password_fail_text));
            reset_password_txt_username_fail.setVisibility(View.VISIBLE);
            return false;
        }

        return true;

    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }


}
