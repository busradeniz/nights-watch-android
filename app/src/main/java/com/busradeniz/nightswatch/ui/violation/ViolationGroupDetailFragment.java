package com.busradeniz.nightswatch.ui.violation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViolationGroupDetailFragment extends Fragment {

    public static final String VIOLATION_GROUP_ID_PARAM = "violationGroupId";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.violation_group_name)
    TextView violationGroupName;

    private int violationGroupId;
    private ViolationGroup violationGroup;


    public ViolationGroupDetailFragment() {
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

        ServiceProvider.getViolationGroupService().get(violationGroupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ViolationGroup>() {
                    @Override
                    public void call(ViolationGroup violationGroup) {
                        ViolationGroupDetailFragment.this.violationGroup = violationGroup;
                        violationGroupName.setText(violationGroup.getName());
                    }
                });
        return view;
    }


}
