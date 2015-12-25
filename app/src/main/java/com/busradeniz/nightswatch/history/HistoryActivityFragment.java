package com.busradeniz.nightswatch.history;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busradeniz.nightswatch.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryActivityFragment extends Fragment {

    private DrawerLayout drawerLayout;

    public HistoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    public void setDrawlerActionProvider(DrawerLayout drawlerActionProvider) {
        this.drawerLayout = drawlerActionProvider;
    }
}
