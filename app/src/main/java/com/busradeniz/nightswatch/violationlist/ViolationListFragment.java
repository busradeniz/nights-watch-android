package com.busradeniz.nightswatch.violationlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.util.SimpleDividerItemDecoration;

import butterknife.ButterKnife;


public class ViolationListFragment extends Fragment {


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    public ViolationListFragment() {
    }

    public static ViolationListFragment newInstance() {
        return new ViolationListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_violation_list, container, false);


        ButterKnife.bind(getActivity(), view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view) ;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        String[] data = new String[200];

        for (int i = 0 ; i < 200 ; i++){
            data[i] = "REcycler view";
        }

        mAdapter = new ViolationListAdapter(data);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

       mAdapter.notifyDataSetChanged();

    }



}
