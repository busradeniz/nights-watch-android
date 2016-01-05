package com.busradeniz.nightswatch.ui.statistics;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.busradeniz.nightswatch.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatisticsActivityFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private LinearLayout llBase;

    public StatisticsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        llBase = (LinearLayout) view.findViewById(R.id.llBase);
        addChart(view);


        return view;
    }

    public void setDrawlerActionProvider(DrawerLayout drawlerActionProvider) {
        this.drawerLayout = drawlerActionProvider;
    }

    private void addChart(View view) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(13f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(2f, 2));
        entries.add(new BarEntry(6f, 3));
        entries.add(new BarEntry(9f, 4));

        BarDataSet dataset = new BarDataSet(entries, "# of Violations");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("New");
        labels.add("Not Violation");
        labels.add("Fixed");
        labels.add("In Progress");
        labels.add("Not Fixed");


        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.animateY(1000);

    }
}
