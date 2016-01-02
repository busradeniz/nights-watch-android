package com.busradeniz.nightswatch.ui.violation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement
 * interface.
 */
public class ViolationGroupListFragment extends Fragment
        implements ViolationGroupRecyclerAdapter.OnViolationGroupButtonsListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.violation_group_recycler_view)
    RecyclerView violationGroupRecyclerView;

    @Bind(R.id.fab_create_violation_group)
    FloatingActionButton createViolationGroup;

    private ViolationGroupRecyclerAdapter violationGroupRecyclerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViolationGroupListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_violation_group_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.violation_gorup_list));

        violationGroupRecyclerAdapter = new ViolationGroupRecyclerAdapter();
        violationGroupRecyclerAdapter.setOnViolationGroupButtonsListener(this);
        violationGroupRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        violationGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());
        violationGroupRecyclerView.setHasFixedSize(true);
        violationGroupRecyclerView.setAdapter(violationGroupRecyclerAdapter);

        createViolationGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Creating a new Violation Group", Toast.LENGTH_LONG).show();
            }
        });

        readViolationGroups();

        return view;
    }

    private void readViolationGroups() {
        ServiceProvider.getViolationGroupService().getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ViolationGroup>>() {
                    @Override
                    public void call(List<ViolationGroup> violationGroups) {
                        violationGroupRecyclerAdapter.addAll(violationGroups);
                    }
                });
    }

    @Override
    public void onDetailButtonClicked(ViolationGroup violationGroup) {
        Toast.makeText(getActivity(), violationGroup.getName() + " detail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteButtonClicked(ViolationGroup violationGroup) {
        Toast.makeText(getActivity(), violationGroup.getName() + " delete", Toast.LENGTH_LONG).show();
    }
}
