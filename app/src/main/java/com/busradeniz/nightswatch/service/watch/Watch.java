package com.busradeniz.nightswatch.service.watch;

/**
 * Created by busradeniz on 29/12/15.
 */
public class Watch {

    private int id;
    private String username;
    private int violationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }
}
