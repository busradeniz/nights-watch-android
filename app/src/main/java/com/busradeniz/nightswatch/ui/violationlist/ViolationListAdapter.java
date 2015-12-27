package com.busradeniz.nightswatch.ui.violationlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.violation.Violation;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.DateFormatter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by busradeniz on 22/12/15.
 */
public class ViolationListAdapter extends RecyclerView.Adapter<ViolationListAdapter.ViewHolder> {


    private List<Violation> violations;


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView imgViolationThumbnail;
        public TextView txtViolationTitle;
        public TextView txtViolationType;
        public TextView txtViolationLocation;
        public TextView txtViolationDate;
        public TextView txtViolationLikeNumber;
        public TextView txtViolationCommentNumber;
        public TextView txtViolationFollowerNumber;

        public ViewHolder(View v) {
            super(v);
            imgViolationThumbnail = (ImageView) v.findViewById(R.id.imgViolationThumbnail);
            txtViolationTitle = (TextView) v.findViewById(R.id.txtViolationTitle);
            txtViolationType = (TextView) v.findViewById(R.id.txtViolationType);
            txtViolationLocation = (TextView) v.findViewById(R.id.txtViolationLocation);
            txtViolationDate = (TextView) v.findViewById(R.id.txtViolationDate);
            txtViolationLikeNumber = (TextView) v.findViewById(R.id.txtViolationLikeNumber);
            txtViolationCommentNumber = (TextView) v.findViewById(R.id.txtViolationCommentNumber);
            txtViolationFollowerNumber = (TextView) v.findViewById(R.id.txtViolationFollowerNumber);
        }
    }

    public ViolationListAdapter(List<Violation> list) {
        violations = list;
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

        Violation violation = violations.get(position);
        holder.txtViolationTitle.setText(violation.getTitle());
        holder.txtViolationType.setText(violation.getViolationGroupName());
        holder.txtViolationLocation.setText(violation.getAddress());
        holder.txtViolationDate.setText(" / " + DateFormatter.dateFormatToString(violation.getViolationDate()));
        holder.txtViolationLikeNumber.setText(violation.getUserLikeCount()+"");
        holder.txtViolationCommentNumber.setText(violation.getCommentCount() +"");
        holder.txtViolationFollowerNumber.setText(violation.getUserWatchCount()+"");

        Picasso.with(NightsWatchApplication.context)
                .load(Constants.IMAGE_URL).transform(new CircleTransformation()).into(holder.imgViolationThumbnail);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return violations.size();
    }



}
