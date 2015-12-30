package com.busradeniz.nightswatch.ui.violationlist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.busradeniz.nightswatch.util.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ViolationListFragment extends Fragment {


    private static String TAG = "ViolationListFragment";
    private String listType;
    private ProgressDialog progressDialog;
    private ViolationListAdapter violationListAdapter;
    private ListView violationListView;
    private HomeActivity homeActivity;
    private List<ViolationResponse> violationResponseList;


    public ViolationListFragment() {

    }

    public void setListType(String violationListType , HomeActivity homeActivity){
        listType = violationListType;
        this.homeActivity = homeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_violation_list, container, false);
        ButterKnife.bind(getActivity(), view);

        violationListView = (ListView) view.findViewById(R.id.violationListView);
        violationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayViolationDetail(violationResponseList.get(position));
            }
        });

        sendRequest();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void sendRequest() {
        Log.i(TAG, "selectedListType : " + listType);
        if (listType != null){
            if (listType.equals(getString(R.string.home_page_recent_text))) {
                sendGetRecentViolationListRequest();
            } else if (listType.equals(getString(R.string.home_page_nearby_text))) {
                sendGetNearbyViolationListRequest();
            } else if (listType.equals(getString(R.string.home_page_top_text))) {
                sendGetTopViolationListRequest();
            }
        }

    }

    private void sendGetNearbyViolationListRequest() {
        showProgress();
        ServiceProvider.getViolationService().getNearbyViolations(0, 0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getNearbyViolations request failed : " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getNearbyViolations request success : " + violationResponses.size() + " violationResponses ");
                        updateScreen(violationResponses);
                    }
                });
    }

    private void sendGetTopViolationListRequest() {
        showProgress();
        ServiceProvider.getViolationService().getTopViolations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getTopViolations request failed : " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getTopViolations request success : " + violationResponses.size() + " violationResponses ");
                        updateScreen(violationResponses);
                    }
                });
    }

    private void sendGetRecentViolationListRequest() {
        showProgress();
        ServiceProvider.getViolationService().getNewestViolations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getNewestViolations request failed : " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getNewestViolations request success : " + violationResponses.size() + " violationResponses ");
                        updateScreen(violationResponses);
                    }
                });
    }

    private void updateScreen(List<ViolationResponse> violationResponses) {

        violationResponseList = violationResponses;
        if (violationListAdapter == null) {
            violationListAdapter = new ViolationListAdapter(getActivity(),violationResponses);
            violationListView.setAdapter(violationListAdapter);
            return;
        }
        violationListAdapter.notifyDataSetChanged();
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(NightsWatchApplication.context, getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void displayViolationDetail(ViolationResponse violationResponse){
        homeActivity.openDisplayFragment(violationResponse);
    }


}
