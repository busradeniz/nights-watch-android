package com.busradeniz.nightswatch.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.Window;
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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

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

//        Set up navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        SharedPreferences preferences = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        TextView nav_view_txt = (TextView) findViewById(R.id.nav_view_txt);
        nav_view_txt.setText(preferences.getString("username", ""));
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

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.home_navigation_menu_item:
                                openHomeScreen();
                                break;
                            case R.id.statistics_navigation_menu_item:
                                openStatisticsScreen();
                                break;
                            case R.id.profile_navigation_menu_item:
                                openProfileScreen();
                                break;
                            case R.id.history_navigation_menu_item:
                                openHistoryScreen();
                                break;
                            case R.id.watchlist_navigation_menu_item:
                                openWatchListScreen();
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
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
        fragmentTransaction.replace(R.id.baseFrameContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openDisplayFragment(ViolationResponse violationResponse) {
        DisplayViolationFragment displayViolationFragment = new DisplayViolationFragment();
        displayViolationFragment.setSelectedViolation(violationResponse);
        openFragment(displayViolationFragment);
    }

}
