package com.busradeniz.nightswatch.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.user.ChangePasswordRequest;
import com.busradeniz.nightswatch.service.user.ChangePasswordResponse;
import com.busradeniz.nightswatch.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by busradeniz on 25/12/15.
 */
public class ChangePasswordFragment extends Fragment {

    private String TAG  = "ChangePasswordFragment";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.change_password_txt_new_password)
    EditText change_password_txt_new_password;
    @Bind(R.id.change_password_txt_new_password2)
    EditText change_password_txt_new_password2;
    @Bind(R.id.change_password_txt_old_password)
    EditText change_password_txt_old_password;
    @Bind(R.id.change_password_txt_old_password_fail)
    TextView change_password_txt_old_password_fail;
    @Bind(R.id.change_password_txt_new_password2_fail)
    TextView change_password_txt_new_password2_fail;
    @Bind(R.id.change_password_txt_new_password_fail)
    TextView change_password_txt_new_password_fail;
    @Bind(R.id.change_password_btn_submit)
    AppCompatButton change_password_btn_submit;


    private ProgressDialog progressDialog;

    public ChangePasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);


        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getResources().getString(R.string.profile_txt_change_password));


        change_password_txt_new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    resetFailText();
                return false;
            }
        });

        change_password_txt_new_password2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailText();
                return false;
            }
        });

        change_password_txt_old_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFailText();
                return false;
            }
        });

        change_password_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        return view;
    }

    private void changePassword(){
        if (!validate())
            return;

        sendChangePasswordRequest();
    }

    private void sendChangePasswordRequest(){
        showProgress();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(change_password_txt_new_password.getText().toString() , change_password_txt_old_password.getText().toString());
        ServiceProvider.getUserService().changePassword(changePasswordRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG , "Change Password request failed : " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ChangePasswordResponse changePasswordResponse) {
                        progressDialog.dismiss();
                        Log.i(TAG , "Change Password request success : " + changePasswordResponse.toString());
                    }
                });
    }

    private boolean validate (){

        String newPassword  = change_password_txt_new_password.getText().toString();
        String newPasswordAgain = change_password_txt_new_password2.getText().toString();
        String oldPassword = change_password_txt_old_password.getText().toString();

        if (oldPassword.length() < Constants.PASSWORD_LENGTH){
            change_password_txt_old_password_fail.setText("Enter valid old password");
            change_password_txt_old_password_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (newPassword.length() < Constants.PASSWORD_LENGTH){
            change_password_txt_new_password_fail.setText("Enter valid new password");
            change_password_txt_new_password_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if(newPasswordAgain.length() < Constants.PASSWORD_LENGTH){
            change_password_txt_new_password2_fail.setText("Enter valid new password");
            change_password_txt_new_password2_fail.setVisibility(View.VISIBLE);
            return false;
        }

        if (!newPassword.equals(newPasswordAgain)){
            change_password_txt_new_password_fail.setText("Password does not match the confirm password");
            change_password_txt_new_password_fail.setVisibility(View.VISIBLE);
            change_password_txt_new_password2_fail.setText("Password does not match the confirm password");
            change_password_txt_new_password2_fail.setVisibility(View.VISIBLE);

            return false;
        }
        return true;
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void resetFailText(){
        change_password_txt_old_password_fail.setText("");
        change_password_txt_new_password_fail.setText("");
        change_password_txt_new_password2_fail.setText("");

    }

}
