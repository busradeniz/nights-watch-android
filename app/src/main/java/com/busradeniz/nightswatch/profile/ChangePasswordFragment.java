package com.busradeniz.nightswatch.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busradeniz.nightswatch.R;

/**
 * Created by busradeniz on 25/12/15.
 */
public class ChangePasswordFragment extends Fragment {

    public ChangePasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);



        return view;
    }


}
