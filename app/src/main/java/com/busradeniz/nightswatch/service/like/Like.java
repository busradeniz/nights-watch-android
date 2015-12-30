package com.busradeniz.nightswatch.service.like;

/**
 * Created by busradeniz on 29/12/15.
 */
public class Like {

    private int id;
    private long likeDate;
    private String username;
    private int violationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(long likeDate) {
        this.likeDate = likeDate;
    }
}
