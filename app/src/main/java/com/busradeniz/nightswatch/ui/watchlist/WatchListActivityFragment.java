package com.busradeniz.nightswatch.ui.watchlist;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.ui.violationlist.ViolationListAdapter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.busradeniz.nightswatch.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class WatchListActivityFragment extends Fragment {


    private static String TAG = "WatchListActivityFragment";


    @Bind(R.id.violationListView)
    ListView watch_violation_list_view;
    @Bind(R.id.toolbar) Toolbar toolbar;


    private List<ViolationResponse> violationResponseList;
    private ViolationListAdapter violationListAdapter;
    private ProgressDialog progressDialog;


    public WatchListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);
        ButterKnife.bind(this,view);

        // Set up the toolbar.
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.watch_title));

        watch_violation_list_view = (ListView) view.findViewById(R.id.violationListView);
        watch_violation_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayViolationDetail(violationResponseList.get(position));
            }
        });



        sendGetUserWatchedViolationsRequest();

        return view;
    }

    private void sendGetUserWatchedViolationsRequest() {

        String[] types= new String[2];
        types[0] = "NEW";
        types[1] = "IN_PROGRESS";

        showProgress();
        ServiceProvider.getViolationService().getUserWatchedViolations(types)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserWatchedViolations request failed : " + e.getLocalizedMessage());
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.watchlist_fail_text) , Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserWatchedViolations request success : " + violationResponses.size() + " violationResponses ");
                        if (violationResponses.size() == 0){
                            Toast.makeText(NightsWatchApplication.context, getString(R.string.watchlist_success_text) , Toast.LENGTH_LONG);
                        }
                        updateScreen(violationResponses);
                    }
                });
    }

    private void updateScreen(List<ViolationResponse> violationResponses) {
        violationResponseList = violationResponses;
        if (violationListAdapter == null) {
            violationListAdapter = new ViolationListAdapter(getActivity(),violationResponses);
            watch_violation_list_view.setAdapter(violationListAdapter);
            return;
        }
        violationListAdapter.notifyDataSetChanged();

    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void displayViolationDetail(ViolationResponse violationResponse){
        ((HomeActivity) getActivity()).openDisplayFragment(violationResponse);
    }

}
