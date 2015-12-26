package com.busradeniz.nightswatch.ui.home;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.ui.violationlist.ViolationListFragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {


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
        setupViewPager(viewPager);


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

            }
        });


        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        super.onCreateOptionsMenu(menu, inflater);

    }


    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ViolationListFragment(), "Recent");
        adapter.addFragment(new ViolationListFragment(), "Nearby");
        adapter.addFragment(new ViolationListFragment(), "Top");
        viewPager.setAdapter(adapter);
    }

}
