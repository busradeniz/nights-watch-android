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

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.violationlist.ViolationListAdapter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.busradeniz.nightswatch.util.SimpleDividerItemDecoration;

import java.util.List;

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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;


    @Bind(R.id.watchlist_recycler_view)
    RecyclerView watchlist_recycler_view;
    @Bind(R.id.toolbar) Toolbar toolbar;
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


        watchlist_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        watchlist_recycler_view.setLayoutManager(mLayoutManager);
        watchlist_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));


        sendGetUserWatchedViolationsRequest();

        return view;
    }

    private void sendGetUserWatchedViolationsRequest() {
        showProgress();
        ServiceProvider.getViolationService().getUserWatchedViolations(null)
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
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserWatchedViolations request success : " + violationResponses.size() + " violationResponses ");
                        updateScreen(violationResponses);
                    }
                });
    }

    private void updateScreen(List<ViolationResponse> violationResponses) {
        if (mAdapter == null) {
            mAdapter = new ViolationListAdapter(violationResponses);
            watchlist_recycler_view.setAdapter(mAdapter);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(NightsWatchApplication.context, getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }
}
