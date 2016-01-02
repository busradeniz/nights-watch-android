package com.busradeniz.nightswatch.ui.violation;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Created by busradeniz on 02/01/16.
 */
public class ViolationGroupRecyclerAdapter extends RecyclerView.Adapter<ViolationGroupRecyclerAdapter.CustomViewHolder> {

    private List<ViolationGroup> violationGroups;
    private WeakReference<OnViolationGroupButtonsListener> listenerWeakReference;


    public ViolationGroupRecyclerAdapter() {
        violationGroups = Collections.emptyList();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_violation_group, parent, false);
        CustomViewHolder addressViewHolder = new CustomViewHolder(itemView);
        return addressViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        final ViolationGroup item = violationGroups.get(position);
        viewHolder.violationGroupName.setText(item.getName());
        viewHolder.violationGroupDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().onDetailButtonClicked(item);
                }
            }
        });
        viewHolder.violationGroupDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().onDeleteButtonClicked(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return violationGroups.size();
    }

    public void setOnViolationGroupButtonsListener(final OnViolationGroupButtonsListener onViolationGroupButtonsListener) {
        listenerWeakReference = new WeakReference<OnViolationGroupButtonsListener>(onViolationGroupButtonsListener);
    }

    public void addAll(List<ViolationGroup> violationGroups) {
        this.violationGroups = violationGroups;
        this.notifyDataSetChanged();
    }


    public interface OnViolationGroupButtonsListener {
        void onDetailButtonClicked(final ViolationGroup violationGroup);

        void onDeleteButtonClicked(final ViolationGroup violationGroup);
    }

    static final class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView violationGroupName;
        final ImageView violationGroupDetail;
        final ImageView violationGroupDelete;

        public CustomViewHolder(View itemView) {
            super(itemView);
            violationGroupName = (TextView) itemView.findViewById(R.id.violation_group_name);
            violationGroupDetail = (ImageView) itemView.findViewById(R.id.violation_group_detail);
            violationGroupDetail.setColorFilter(new LightingColorFilter(Color.WHITE, itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            violationGroupDelete = (ImageView) itemView.findViewById(R.id.violation_group_delete);
            violationGroupDelete.setColorFilter(new LightingColorFilter(Color.WHITE, Color.RED));
        }
    }
}
