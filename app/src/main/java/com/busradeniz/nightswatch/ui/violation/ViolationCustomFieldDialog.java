package com.busradeniz.nightswatch.ui.violation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.violation.ViolationCustomField;

public class ViolationCustomFieldDialog extends LinearLayout {

    @Bind(R.id.custom_field_name)
    EditText customFieldName;

    @Bind(R.id.custom_field_description)
    EditText customFieldDescription;

    @Bind(R.id.custom_field_type)
    EditText customFieldType;

    @Bind(R.id.custom_field_expected_value)
    EditText customFieldExpectedValue;

    private ViolationCustomField violationCustomField;

    public ViolationCustomFieldDialog(Context context) {
        super(context);
        initializeLayoutBasics(context);
    }

    private void initializeLayoutBasics(Context context) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.dialog_violation_custom_field, this);
        ButterKnife.bind(this, view);
    }

    public ViolationCustomField getViolationCustomField() {
        if (violationCustomField == null) {
            violationCustomField = new ViolationCustomField();
        }
        violationCustomField.setProperty(customFieldName.getText().toString());
        violationCustomField.setDescription(customFieldDescription.getText().toString());
        violationCustomField.setConstraintTypeDto(customFieldType.getText().toString());
        violationCustomField.setConstraintValue(customFieldExpectedValue.getText().toString());
        return violationCustomField;
    }

    public void setViolationCustomField(ViolationCustomField violationCustomField) {
        this.violationCustomField = violationCustomField;
        customFieldName.setText(violationCustomField.getProperty());
        customFieldDescription.setText(violationCustomField.getDescription());
        customFieldType.setText(violationCustomField.getConstraintTypeDto());
        customFieldExpectedValue.setText(violationCustomField.getConstraintValue());
    }
}
