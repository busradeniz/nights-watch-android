package com.busradeniz.nightswatch.ui.history;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.ui.home.HomeViewPagerAdapter;
import com.busradeniz.nightswatch.ui.violationlist.ViolationListFragment;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryActivityFragment extends Fragment {

    private static String TAG = "HistoryActivityFragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeViewPagerAdapter adapter;


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
        toolbar.setTitle(getString(R.string.history_title));


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager();

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        return view;
    }


    private void setupViewPager() {

        ViolationListFragment recentViolationListFragment = new ViolationListFragment();
        recentViolationListFragment.setListType(getString(R.string.history_page_open_text),(HomeActivity) getActivity() );


        ViolationListFragment nearbyViolationListFragment = new ViolationListFragment();
        nearbyViolationListFragment.setListType(getString(R.string.history_page_fixed_text),(HomeActivity) getActivity());

        adapter = new HomeViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(recentViolationListFragment, getString(R.string.history_page_open_text));
        adapter.addFragment(nearbyViolationListFragment, getString(R.string.history_page_fixed_text));

        viewPager.setAdapter(adapter);
    }





}
