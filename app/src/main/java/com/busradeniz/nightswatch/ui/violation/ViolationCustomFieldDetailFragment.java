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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationCustomField;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViolationCustomFieldDetailFragment extends Fragment implements ViolationCustomFieldRecyclerAdapter.OnViolationCustomFieldButtonsListener {

    public static final String VIOLATION_GROUP_ID_PARAM = "violationGroupId";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.violation_group_name)
    TextView violationGroupName;

    @Bind(R.id.violation_property_recycler_view)
    RecyclerView violationPropertyRecyclerView;

    @Bind(R.id.fab_create_violation_property)
    FloatingActionButton createViolationProperty;

    private int violationGroupId;
    private ViolationCustomFieldRecyclerAdapter violationGroupRecyclerAdapter;


    public ViolationCustomFieldDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.violationGroupId = getArguments().getInt(VIOLATION_GROUP_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_violation_group_detail, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.violation_gorup_detail));

        violationGroupRecyclerAdapter = new ViolationCustomFieldRecyclerAdapter();
        violationGroupRecyclerAdapter.setOnViolationGroupButtonsListener(this);
        violationPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        violationPropertyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        violationPropertyRecyclerView.setHasFixedSize(true);
        violationPropertyRecyclerView.setAdapter(violationGroupRecyclerAdapter);

        createViolationProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Create button for custom field ", Toast.LENGTH_LONG).show();
            }
        });

        getViolationGroupDetail();
        return view;
    }

    private void getViolationGroupDetail() {
        ServiceProvider.getViolationGroupService().get(violationGroupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ViolationGroup>() {
                    @Override
                    public void call(ViolationGroup violationGroup) {
                        violationGroupName.setText(violationGroup.getName());
                        violationGroupRecyclerAdapter.addAll(violationGroup.getViolationPropertyDtos());
                    }
                });
    }


    @Override
    public void onDetailButtonClicked(ViolationCustomField violationCustomField) {
        Toast.makeText(getActivity(), "Edit button for property " + violationCustomField.getProperty(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteButtonClicked(ViolationCustomField violationCustomField) {
        Toast.makeText(getActivity(), "Delete button for property " + violationCustomField.getProperty(), Toast.LENGTH_LONG).show();
    }
}
