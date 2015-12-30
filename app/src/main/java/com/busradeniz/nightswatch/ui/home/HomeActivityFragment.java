package com.busradeniz.nightswatch.ui.home;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.login.LoginRequest;
import com.busradeniz.nightswatch.service.login.LoginResponse;
import com.busradeniz.nightswatch.ui.violation.CreateViolationActivity;
import com.busradeniz.nightswatch.ui.violationlist.ViolationListFragment;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {

    private static String TAG = "HomeActivityFragment";
    private ProgressDialog progressDialog;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    public HomeActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Set up the toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


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

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_add_notes);
        floatingActionButton.setImageResource(R.drawable.ic_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateViolationScreen();
            }
        });

        return view;
    }




    private void setupViewPager() {

        ViolationListFragment recentViolationListFragment = new ViolationListFragment();
        recentViolationListFragment.setListType(getString(R.string.home_page_recent_text),(HomeActivity) getActivity() );


        ViolationListFragment nearbyViolationListFragment = new ViolationListFragment();
        nearbyViolationListFragment.setListType(getString(R.string.home_page_nearby_text),(HomeActivity) getActivity());

        ViolationListFragment topViolationListFragment = new ViolationListFragment();
        topViolationListFragment.setListType(getString(R.string.home_page_top_text),(HomeActivity) getActivity());


        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(recentViolationListFragment, getString(R.string.home_page_recent_text));
        adapter.addFragment(nearbyViolationListFragment, getString(R.string.home_page_nearby_text));
        adapter.addFragment(topViolationListFragment, getString(R.string.home_page_top_text));
        viewPager.setAdapter(adapter);
    }



    private void openCreateViolationScreen(){
        Intent intent = new Intent(getActivity(), CreateViolationActivity.class);
        startActivity(intent);
    }
}
