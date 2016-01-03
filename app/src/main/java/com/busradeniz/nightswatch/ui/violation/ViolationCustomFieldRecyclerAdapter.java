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
import com.busradeniz.nightswatch.service.violation.ViolationCustomField;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Created by busradeniz on 02/01/16.
 */
public class ViolationCustomFieldRecyclerAdapter extends RecyclerView.Adapter<ViolationCustomFieldRecyclerAdapter.CustomViewHolder> {

    private List<ViolationCustomField> violationCustomFields;
    private WeakReference<OnViolationCustomFieldButtonsListener> listenerWeakReference;


    public ViolationCustomFieldRecyclerAdapter() {
        violationCustomFields = Collections.emptyList();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_violation_custom_field, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(itemView);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        final ViolationCustomField item = violationCustomFields.get(position);
        viewHolder.customFieldName.setText(item.getProperty());
        viewHolder.customFieldDescription.setText(item.getDescription());
        viewHolder.customFieldExpectedValue.setText(item.getConstraintValue());
        viewHolder.customFieldType.setText(item.getConstraintTypeDto());
        viewHolder.customFieldEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().onDetailButtonClicked(item);
                }
            }
        });
        viewHolder.customFieldDelete.setOnClickListener(new View.OnClickListener() {
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
        return violationCustomFields.size();
    }

    public ViolationCustomField getItem(int position) {
        return this.violationCustomFields.get(position);
    }

    public void setOnViolationGroupButtonsListener(final OnViolationCustomFieldButtonsListener onViolationCustomFieldButtonsListener) {
        listenerWeakReference = new WeakReference<OnViolationCustomFieldButtonsListener>(onViolationCustomFieldButtonsListener);
    }

    public void addAll(List<ViolationCustomField> violationCustomFields) {
        this.violationCustomFields = violationCustomFields;
        this.notifyDataSetChanged();
    }


    public interface OnViolationCustomFieldButtonsListener {
        void onDetailButtonClicked(final ViolationCustomField violationCustomField);

        void onDeleteButtonClicked(final ViolationCustomField violationCustomField);
    }

    static final class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView customFieldName;
        final TextView customFieldDescription;
        final TextView customFieldExpectedValue;
        final TextView customFieldType;
        final ImageView customFieldEdit;
        final ImageView customFieldDelete;

        public CustomViewHolder(View itemView) {
            super(itemView);
            customFieldName = (TextView) itemView.findViewById(R.id.custom_field_name);
            customFieldDescription = (TextView) itemView.findViewById(R.id.custom_field_description);
            customFieldExpectedValue = (TextView) itemView.findViewById(R.id.custom_field_expected_value);
            customFieldType = (TextView) itemView.findViewById(R.id.custom_field_type);
            customFieldEdit = (ImageView) itemView.findViewById(R.id.custom_field_edit);
            customFieldEdit.setColorFilter(new LightingColorFilter(Color.WHITE, itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            customFieldDelete = (ImageView) itemView.findViewById(R.id.custom_field_delete);
            customFieldDelete.setColorFilter(new LightingColorFilter(Color.WHITE, Color.RED));
        }
    }
}
