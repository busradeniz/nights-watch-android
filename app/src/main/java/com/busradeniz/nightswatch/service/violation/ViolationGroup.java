package com.busradeniz.nightswatch.service.violation;

import java.util.List;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ViolationGroup {

    private int id;
    private String name;
    private List<ViolationCustomField> violationPropertyDtos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ViolationCustomField> getViolationPropertyDtos() {
        return violationPropertyDtos;
    }

    public void setViolationPropertyDtos(List<ViolationCustomField> violationPropertyDtos) {
        this.violationPropertyDtos = violationPropertyDtos;
    }
}
