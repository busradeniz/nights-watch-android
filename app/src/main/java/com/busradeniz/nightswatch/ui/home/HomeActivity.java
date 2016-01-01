package com.busradeniz.nightswatch.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.login.LoginRequest;
import com.busradeniz.nightswatch.service.login.LoginResponse;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.history.HistoryActivityFragment;
import com.busradeniz.nightswatch.ui.profile.ProfileActivityFragment;
import com.busradeniz.nightswatch.ui.statistics.StatisticsActivityFragment;
import com.busradeniz.nightswatch.ui.violation.DisplayViolationFragment;
import com.busradeniz.nightswatch.ui.watchlist.WatchListActivityFragment;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.google.android.gms.location.LocationServices;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout left_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }




        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        mDrawerList = (ListView) findViewById(R.id.listview_left_drawer);
        left_view = (LinearLayout) findViewById(R.id.left_drawer);

        String[] drawler_menu_user = getResources().getStringArray(R.array.drawler_menu_user);
        mDrawerList.setAdapter(new DrawlerAdapter(this,R.layout.drawer_list_item, drawler_menu_user));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        openHomeScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }
    }


    private void openHomeScreen() {
        HomeActivityFragment homeActivityFragment = new HomeActivityFragment();
        openFragment(homeActivityFragment);
    }

    private void openStatisticsScreen() {
        StatisticsActivityFragment statisticsActivityFragment = new StatisticsActivityFragment();
        openFragment(statisticsActivityFragment);
    }

    private void openProfileScreen() {
        ProfileActivityFragment profileActivityFragment = new ProfileActivityFragment();
        openFragment(profileActivityFragment);

    }

    private void openHistoryScreen() {
        HistoryActivityFragment historyActivityFragment = new HistoryActivityFragment();
        openFragment(historyActivityFragment);

    }

    private void openWatchListScreen() {
        WatchListActivityFragment watchListActivityFragment = new WatchListActivityFragment();
        openFragment(watchListActivityFragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openDisplayFragment(ViolationResponse violationResponse) {
        DisplayViolationFragment displayViolationFragment = new DisplayViolationFragment();
        displayViolationFragment.setSelectedViolation(violationResponse);
        openFragment(displayViolationFragment);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(left_view);

       if (position == 0){
           openHomeScreen();
       }else if (position == 1){
           openProfileScreen();
       }else if(position == 2){
           openHistoryScreen();
       }else if(position == 3){
           openWatchListScreen();
       }else if(position == 4){
           openStatisticsScreen();
       }

        // Highlight the selected item, update the title, and close the drawer

    }



}
