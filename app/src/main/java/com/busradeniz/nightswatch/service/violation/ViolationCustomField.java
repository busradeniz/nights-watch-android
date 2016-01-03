package com.busradeniz.nightswatch.service.violation;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ViolationCustomField {

    private int id;
    private String property;
    private String constraintTypeDto;
    private String constraintValue;
    private String description;
    private int violationGroupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue(String constraintValue) {
        this.constraintValue = constraintValue;
    }

    public String getConstraintTypeDto() {
        return constraintTypeDto;
    }

    public void setConstraintTypeDto(String constraintTypeDto) {
        this.constraintTypeDto = constraintTypeDto;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getViolationGroupId() {
        return violationGroupId;
    }

    public void setViolationGroupId(int violationGroupId) {
        this.violationGroupId = violationGroupId;
    }
}
