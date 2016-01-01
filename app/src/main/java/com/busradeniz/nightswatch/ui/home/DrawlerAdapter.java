package com.busradeniz.nightswatch.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.DateFormatter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

/**
 * Created by busradeniz on 01/01/16.
 */
public class DrawlerAdapter extends ArrayAdapter<String> {


    Context context;
    String data[] = null;

    public DrawlerAdapter(Context mContext, int layoutResourceId, String[] data) {
        super(mContext, layoutResourceId, data);
        this.context = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgDrawlerList = (ImageView) convertView.findViewById(R.id.imgDrawlerList);
            viewHolder.txtDrawlerList = (TextView) convertView.findViewById(R.id.txtDrawlerList);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String title = data[position];
        viewHolder.txtDrawlerList.setText(title);
        if (position == 0){
            viewHolder.imgDrawlerList.setBackground(context.getDrawable(R.drawable.home));
        }else if(position == 1){
            viewHolder.imgDrawlerList.setBackground(context.getDrawable(R.drawable.profile));
        }else if(position == 2){
            viewHolder.imgDrawlerList.setBackground(context.getDrawable(R.drawable.history));
        }else if(position == 3){
            viewHolder.imgDrawlerList.setBackground(context.getDrawable(R.drawable.follow));
        }else if(position == 4){
            viewHolder.imgDrawlerList.setBackground(context.getDrawable(R.drawable.statistics));
        }

        return convertView;
    }

    static class ViewHolder {
        public ImageView imgDrawlerList;
        public TextView txtDrawlerList;
    }

}
