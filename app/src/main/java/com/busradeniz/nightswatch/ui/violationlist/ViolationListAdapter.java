package com.busradeniz.nightswatch.ui.violationlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created by busradeniz on 22/12/15.
 */
public class ViolationListAdapter extends RecyclerView.Adapter<ViolationListAdapter.ViewHolder> {


    private String[] mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView imgViolationThumbnail;

        public TextView txtViolationTitle;
        public TextView txtViolationType;
        public TextView txtViolationLocation;


        public ViewHolder(View v) {
            super(v);
            imgViolationThumbnail = (ImageView) v.findViewById(R.id.imgViolationThumbnail);
            txtViolationTitle = (TextView) v.findViewById(R.id.txtViolationTitle);
            txtViolationType = (TextView) v.findViewById(R.id.txtViolationType);
            txtViolationLocation = (TextView) v.findViewById(R.id.txtViolationLocation);

        }
    }

    public ViolationListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ViolationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_violation, parent, false);
        ButterKnife.bind(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtViolationTitle.setText("Violation Title buraya gelecek");
        holder.txtViolationType.setText("Kaldırım taşları engeli");
        holder.txtViolationLocation.setText("Beşiktaş, Istanbul");

        Picasso.with(NightsWatchApplication.context)
                .load(Constants.IMAGE_URL).transform(new CircleTransformation()).into(holder.imgViolationThumbnail);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }



}
