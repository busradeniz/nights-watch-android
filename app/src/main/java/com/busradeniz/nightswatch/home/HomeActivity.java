package com.busradeniz.nightswatch.home;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.history.HistoryActivityFragment;
import com.busradeniz.nightswatch.profile.ProfileActivityFragment;
import com.busradeniz.nightswatch.statistics.StatisticsActivityFragment;
import com.busradeniz.nightswatch.watchlist.WatchListActivityFragment;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private SearchView searchView;

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


    private void openHomeScreen(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeActivityFragment homeActivityFragment = new HomeActivityFragment();
        fragmentManager.beginTransaction().replace(R.id.baseFrameContainer, homeActivityFragment).commit();
    }

    private void openStatisticsScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StatisticsActivityFragment statisticsActivityFragment = new StatisticsActivityFragment();
        statisticsActivityFragment.setDrawlerActionProvider(mDrawerLayout);
        fragmentManager.beginTransaction().replace(R.id.baseFrameContainer, statisticsActivityFragment).commit();

    }

    private void openProfileScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProfileActivityFragment profileActivityFragment = new ProfileActivityFragment();
        fragmentManager.beginTransaction().replace(R.id.baseFrameContainer, profileActivityFragment).commit();

    }

    private void openHistoryScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HistoryActivityFragment historyActivityFragment = new HistoryActivityFragment();
        historyActivityFragment.setDrawlerActionProvider(mDrawerLayout);
        fragmentManager.beginTransaction().replace(R.id.baseFrameContainer, historyActivityFragment).commit();

    }

    private void openWatchListScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        WatchListActivityFragment watchListActivityFragment = new WatchListActivityFragment();
        fragmentManager.beginTransaction().replace(R.id.baseFrameContainer, watchListActivityFragment).commit();

    }


}
