package com.busradeniz.nightswatch.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.busradeniz.nightswatch.ui.login.LoginActivity;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    private String TAG = "ProfileActivityFragment";
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.profile_img_user) ImageView profile_img_user;
    @Bind(R.id.profile_txt_fullname) TextView profile_txt_fullname;
    @Bind(R.id.profile_txt_username) TextView profile_txt_username;
    @Bind(R.id.profile_txt_email) TextView profile_txt_email;
    @Bind(R.id.profile_txt_bio_title) TextView profile_txt_bio_title;
    @Bind(R.id.profile_txt_bio) TextView profile_txt_bio;
    @Bind(R.id.profile_btn_change_password) AppCompatButton profile_btn_change_password;
    @Bind(R.id.profile_btn_edit_profile) AppCompatButton profile_btn_edit_profile;
    @Bind(R.id.profile_btn_logout) AppCompatButton profile_btn_logout;

    private ProgressDialog progressDialog;

    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this,view);

        // Set up the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getResources().getString(R.string.profile_title));

        if (NightsWatchApplication.user == null){
            profile_img_user.setBackground(getActivity().getDrawable(R.drawable.user));

        }

        profile_btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordScreen();
            }
        });

        profile_btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileScreen();
            }
        });

        profile_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        sendGetUserInfoRequest();
        return view;
    }


    private void sendGetUserInfoRequest(){
        showProgress();
        ServiceProvider.getUserService().getUserInfo(NightsWatchApplication.userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignUpResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG , "getUserInfo service failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                            //TODO hata mesajı ver
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {
                        progressDialog.dismiss();
                        if (signUpResponse != null)
                            NightsWatchApplication.user = signUpResponse;
                            updateScreen(signUpResponse);
                    }
                });
    }


    private void updateScreen(SignUpResponse signUpResponse){
        profile_txt_fullname.setText(signUpResponse.getFullName());
        profile_txt_username.setText(signUpResponse.getUsername());
        profile_txt_email.setText(signUpResponse.getEmail());
        profile_txt_bio_title.setText("About");
        profile_txt_bio.setText(signUpResponse.getBio());

        if (signUpResponse.getPhoto().getUrl().length() > 0){
            Picasso.with(getActivity())
                    .load(signUpResponse.getPhoto().getUrl()).resize(300,300).transform(new CircleTransformation()).into(profile_img_user);
        }else {
            profile_img_user.setBackground(getActivity().getDrawable(R.drawable.user));
        }
    }
    private void openChangePasswordScreen(){
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        openFragment(changePasswordFragment);
    }

    private void openEditProfileScreen(){
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        openFragment(editProfileFragment);

    }

    private void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void showProgress(){
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text) , getResources().getString(R.string.progress_message_text), true);
    }




}
