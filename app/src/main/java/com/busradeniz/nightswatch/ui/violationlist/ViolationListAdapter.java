package com.busradeniz.nightswatch.ui.violationlist;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.DateFormatter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by busradeniz on 22/12/15.
 */
public class ViolationListAdapter extends ArrayAdapter<ViolationResponse> {


    private List<ViolationResponse> violationResponses;
    private Context context;
    public List<ViolationResponse> orig;


    public ViolationListAdapter(Context context, List<ViolationResponse> values) {
        super(context, -1, values);
        violationResponses = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_violation, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgViolationThumbnail = (ImageView) convertView.findViewById(R.id.imgViolationThumbnail);
            viewHolder.txtViolationTitle = (TextView) convertView.findViewById(R.id.txtViolationTitle);
            viewHolder.txtViolationType = (TextView) convertView.findViewById(R.id.txtViolationType);
            viewHolder.txtViolationLocation = (TextView) convertView.findViewById(R.id.txtViolationLocation);
            viewHolder.txtViolationDate = (TextView) convertView.findViewById(R.id.txtViolationDate);
            viewHolder.txtViolationLikeNumber = (TextView) convertView.findViewById(R.id.txtViolationLikeNumber);
            viewHolder.txtViolationCommentNumber = (TextView) convertView.findViewById(R.id.txtViolationCommentNumber);
            viewHolder.txtViolationFollowerNumber = (TextView) convertView.findViewById(R.id.txtViolationFollowerNumber);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ViolationResponse violationResponse = violationResponses.get(position);

        if (violationResponse != null) {
            viewHolder.txtViolationTitle.setText(violationResponse.getTitle());
            viewHolder.txtViolationType.setText(violationResponse.getViolationGroupName());
            viewHolder.txtViolationLocation.setText(violationResponse.getAddress());
            viewHolder.txtViolationDate.setText(" / " + DateFormatter.dateFormatToString(violationResponse.getViolationDate()));
            viewHolder.txtViolationLikeNumber.setText(violationResponse.getUserLikeCount() + "");
            viewHolder.txtViolationCommentNumber.setText(violationResponse.getCommentCount() + "");
            viewHolder.txtViolationFollowerNumber.setText(violationResponse.getUserWatchCount() + "");

            if (violationResponse.getMedias().size() > 0) {
                Picasso.with(NightsWatchApplication.context)
                        .load(violationResponse.getMedias().get(0).getUrl()).resize(240, 240).transform(new CircleTransformation()).into(viewHolder.imgViolationThumbnail);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        public ImageView imgViolationThumbnail;
        public TextView txtViolationTitle;
        public TextView txtViolationType;
        public TextView txtViolationLocation;
        public TextView txtViolationDate;
        public TextView txtViolationLikeNumber;
        public TextView txtViolationCommentNumber;
        public TextView txtViolationFollowerNumber;
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ViolationResponse> results = new ArrayList<>();
                if (orig == null)
                    orig = violationResponses;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ViolationResponse g : orig) {
                            if (g.getTitle().toLowerCase().contains(constraint.toString()) || g.getAddress().toLowerCase().contains(constraint.toString()) || g.getViolationGroupName().toLowerCase().contains(constraint.toString()) )
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,Filter.FilterResults results) {
                violationResponses = (List<ViolationResponse>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return violationResponses.size();
    }

    @Override
    public ViolationResponse getItem(int position) {
        return violationResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}

