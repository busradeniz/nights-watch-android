package com.busradeniz.nightswatch.ui.violation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.Response;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import com.busradeniz.nightswatch.ui.home.HomeActivity;
import com.busradeniz.nightswatch.util.RecyclerItemTouchToClickListener;
import rx.Observer;
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

        violationGroupRecyclerView.addOnItemTouchListener(new RecyclerItemTouchToClickListener(getActivity()) {
            @Override
            public void onClick(View view, int position) {
                toDetailFragment(violationGroupRecyclerAdapter.getItem(position).getId());
            }
        });

        createViolationGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateViolationGroupDialog();

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
    public void onDetailButtonClicked(final ViolationGroup violationGroup) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.violation_group_name);
        input.setText(violationGroup.getName());

        TextInputLayout textInputLayout = new TextInputLayout(getActivity());
        textInputLayout.addView(input);
        textInputLayout.setHint(getActivity().getResources().getString(R.string.violation_group_name));

        // Set up the input
        builder.setView(textInputLayout);
        builder.setTitle("Update Violation Group...");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                violationGroup.setName(input.getText().toString());
                ServiceProvider.getViolationGroupService().update(violationGroup.getId(), violationGroup)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ViolationGroup>() {
                            @Override
                            public void call(ViolationGroup violationGroup) {
                                readViolationGroups();
                                com.busradeniz.nightswatch.util.AlertDialog.showAlertWithPositiveButton(getActivity(), "Successful!", "Violation Group is updated!");
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onDeleteButtonClicked(final ViolationGroup violationGroup) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Deleting Violation....")
                .setMessage("Selected (" + violationGroup.getName() + ") violation group is going to be deleted. You cannot revert a delete operation!. Are you sure?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceProvider.getViolationGroupService().delete(violationGroup.getId())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Response>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        com.busradeniz.nightswatch.util.AlertDialog.showAlertWithPositiveButton(getActivity(), "Error!",
                                                "Violation Group cannot be deleted! " +
                                                        "You should first delete or update all the violations with this group");
                                    }

                                    @Override
                                    public void onNext(Response response) {
                                        com.busradeniz.nightswatch.util.AlertDialog.showAlertWithPositiveButton(getActivity(), "Success!",
                                                "Violation Group is deleted! ");
                                        readViolationGroups();
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();


    }

    public void showCreateViolationGroupDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.violation_group_name);

        TextInputLayout textInputLayout = new TextInputLayout(getActivity());
        textInputLayout.addView(input);
        textInputLayout.setHint(getActivity().getResources().getString(R.string.violation_group_name));

        // Set up the input
        builder.setView(textInputLayout);
        builder.setTitle("Create Violation Group...");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ViolationGroup violationGroup = new ViolationGroup();
                violationGroup.setName(input.getText().toString());
                ServiceProvider.getViolationGroupService().create(violationGroup)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ViolationGroup>() {
                            @Override
                            public void call(ViolationGroup violationGroup) {
                                readViolationGroups();
                                com.busradeniz.nightswatch.util.AlertDialog.showAlertWithPositiveButton(getActivity(), "Successful!", "Violation Group is created!");
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public HomeActivity getHomeActivity() {
        return (HomeActivity) getActivity();
    }

    public void toDetailFragment(int violationGroupId) {
        getHomeActivity().openViolationGroupDetailFragment(violationGroupId);
    }
}
