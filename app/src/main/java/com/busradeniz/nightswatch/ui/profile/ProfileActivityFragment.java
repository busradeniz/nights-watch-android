package com.busradeniz.nightswatch.ui.profile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {


    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.profile_img_user) ImageView profile_img_user;
    @Bind(R.id.profile_txt_fullname) TextView profile_txt_fullname;
    @Bind(R.id.profile_txt_username) TextView profile_txt_username;
    @Bind(R.id.profile_txt_email) TextView profile_txt_email;
    @Bind(R.id.profile_txt_bio_title) TextView profile_txt_bio_title;
    @Bind(R.id.profile_txt_bio) TextView profile_txt_bio;
    @Bind(R.id.profile_btn_change_password) AppCompatButton profile_btn_change_password;
    @Bind(R.id.profile_btn_edit_profile) AppCompatButton profile_btn_edit_profile;


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

        Picasso.with(getActivity())
                .load(Constants.IMAGE_URL).transform(new CircleTransformation()).into(profile_img_user);


        profile_txt_fullname.setText("Busra Deniz");
        profile_txt_username.setText("busradeniz");
        profile_txt_email.setText("busradeniz@gmail.com");
        profile_txt_bio_title.setText("About");
        profile_txt_bio.setText("Busra Deniz is a Software Engineer who has a keen interest in developing high-quality software by applying agile principles and methodologies, especially Scrum. She is a Certificated Scrum Master and mobile engineer in Netas. Currently, working on an international project that provides iOS and Android SDKs those allow other programmers to develop their applications, which are capable of voice call, video call and IM using WebRTC.");


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

        return view;
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
        fragmentTransaction.replace(R.id.baseFrameContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }






}
