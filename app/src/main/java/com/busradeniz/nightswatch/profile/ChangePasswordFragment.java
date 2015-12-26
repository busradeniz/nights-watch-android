package com.busradeniz.nightswatch.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by busradeniz on 25/12/15.
 */
public class ChangePasswordFragment extends Fragment {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.change_password_txt_new_password) EditText change_password_txt_new_password;
    @Bind(R.id.change_password_txt_new_password2) EditText change_password_txt_new_password2;
    @Bind(R.id.change_password_txt_old_password) EditText change_password_txt_old_password;
    @Bind(R.id.change_password_txt_old_password_fail) TextView change_password_txt_old_password_fail;
    @Bind(R.id.change_password_txt_new_password2_fail) TextView change_password_txt_new_password2_fail;
    @Bind(R.id.change_password_txt_new_password_fail) TextView change_password_txt_new_password_fail;

    public ChangePasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);


        ButterKnife.bind(this,view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getResources().getString(R.string.profile_txt_change_password));

        return view;
    }


}
