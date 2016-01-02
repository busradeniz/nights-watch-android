package com.busradeniz.nightswatch.ui.violationlist;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.Response;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.google.android.gms.location.LocationServices;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;


public class ViolationListFragment extends Fragment {


    private static String TAG = "ViolationListFragment";
    @Bind(R.id.toolbar) Toolbar toolbar;
    private String listType;
    private OperationType operationType;
    private ProgressDialog progressDialog;
    private ViolationListAdapter violationListAdapter;
    private ListView violationListView;
    private HomeActivity homeActivity;
    private List<ViolationResponse> violationResponseList;


    public ViolationListFragment() {
        operationType = OperationType.DETAIL;
    }

    public void update() {
        sendRequest();
    }

    public void setListType(String violationListType, HomeActivity homeActivity) {
        listType = violationListType;
        this.homeActivity = homeActivity;
    }

    public String getListType() {
        return listType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_violation_list, container, false);
        ButterKnife.bind(this, view);

        if (operationType == OperationType.DELETE) {
            toolbar.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(getString(R.string.manage_violations_title));
        }

        violationListView = (ListView) view.findViewById(R.id.violationListView);
        violationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (operationType == OperationType.DETAIL) {
                    displayViolationDetail(violationResponseList.get(position));
                } else if (operationType == OperationType.DELETE) {
                    deleteViolation(violationResponseList.get(position));
                }

            }
        });
        violationListView.setTextFilterEnabled(true);

        setHasOptionsMenu(true);
        sendRequest();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (violationListAdapter != null) {
            violationListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    violationListView.clearTextFilter();
                } else {
                    violationListView.setFilterText(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }


    private void sendRequest() {
        Log.i(TAG, "selectedListType : " + listType);
        if (listType == null) {
            return;
        }

        if (listType.equals(getString(R.string.home_page_recent_text))) {
            sendGetRecentViolationListRequest();
        } else if (listType.equals(getString(R.string.home_page_nearby_text))) {
            sendGetNearbyViolationListRequest();
        } else if (listType.equals(getString(R.string.home_page_top_text))) {
            sendGetTopViolationListRequest();
        } else if (listType.equals(getString(R.string.history_page_open_text))) {
            sendGetMyOpenListRequest();
        } else if (listType.equals(getString(R.string.history_page_fixed_text))) {
            sendGetMyFixedListRequest();
        } else if (listType.equals(getString(R.string.history_page_all_text))) {
            sendGetMyFixedListRequest();
        }

    }

    private void sendGetNearbyViolationListRequest() {
        showProgress();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                NightsWatchApplication.mGoogleApiClient);

        if (mLastLocation != null) {
            NightsWatchApplication.latitude = mLastLocation.getLatitude();
            NightsWatchApplication.longitude = mLastLocation.getLongitude();
        }
        ServiceProvider.getViolationService().getNearbyViolations(NightsWatchApplication.longitude, NightsWatchApplication.latitude)
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

    private void sendGetMyOpenListRequest() {

        String[] types = new String[4];
        types[0] = "NEW";
        types[1] = "IN_PROGRESS";
        types[2] = "NOT_FIXED";
        types[3] = "NOT_VIOLATION";

        showProgress();
        ServiceProvider.getViolationService().getUserViolations(types)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserViolations request failed : " + e.getLocalizedMessage());
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.history_fail_text), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserViolations request success : " + violationResponses.size() + " violationResponses ");
                        if (violationResponses.size() == 0) {
                            Toast.makeText(NightsWatchApplication.context, getString(R.string.history_success_text), Toast.LENGTH_LONG);
                        }
                        updateScreen(violationResponses);
                    }
                });

    }

    private void sendGetMyFixedListRequest() {
        String[] types = new String[1];
        types[0] = "FIXED";

        showProgress();
        ServiceProvider.getViolationService().getUserViolations(types)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserViolations request failed : " + e.getLocalizedMessage());
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.history_fail_text), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(List<ViolationResponse> violationResponses) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getUserViolations request success : " + violationResponses.size() + " violationResponses ");
                        if (violationResponses.size() == 0) {
                            Toast.makeText(NightsWatchApplication.context, getString(R.string.history_success_text), Toast.LENGTH_LONG);
                        }
                        updateScreen(violationResponses);
                    }
                });
    }

    private void updateScreen(List<ViolationResponse> violationResponses) {

        violationResponseList = violationResponses;
        if (violationListAdapter != null) {
            violationListAdapter = null;

        }
        violationListAdapter = new ViolationListAdapter(getActivity(), violationResponses);
        violationListView.setAdapter(violationListAdapter);
        return;
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(NightsWatchApplication.context, getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void displayViolationDetail(ViolationResponse violationResponse) {
        homeActivity.openDisplayFragment(violationResponse);
    }

    private void deleteViolation(final ViolationResponse violationResponse) {
        new android.support.v7.app.AlertDialog.Builder(this.getActivity())
                .setTitle("Deleting Violation....")
                .setMessage("Selected (" + violationResponse.getTitle() + ") violation is going to be deleted. You cannot revert a delete operation!. Are you sure?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceProvider.getViolationService().deleteViolation(violationResponse.getId())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Response>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "Error while deleting selected violation. " + violationResponse.getTitle(), e);
                                    }

                                    @Override
                                    public void onNext(Response response) {
                                        Log.i(TAG, "Violation is deleted. Message: " + response.getMessage());
                                        sendRequest();
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel delete
                    }
                })
                .show();
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public enum OperationType {
        DETAIL, DELETE;
    }

}
