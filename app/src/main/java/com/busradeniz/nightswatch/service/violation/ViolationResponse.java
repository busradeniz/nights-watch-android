package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.fileupload.Media;

import java.util.List;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ViolationResponse {

    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String address;
    private String violationStatus;
    private String dangerLevel;
    private String frequencyLevel;
    private String violationGroupName;
    private int id;
    private long violationDate;
    private String owner;
    private int commentCount;
    private int userLikeCount;
    private int userWatchCount;
    private List<String> tags;
    private List<Media> medias;
    private String customProperties;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserWatchCount() {
        return userWatchCount;
    }

    public void setUserWatchCount(int userWatchCount) {
        this.userWatchCount = userWatchCount;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getFrequencyLevel() {
        return frequencyLevel;
    }

    public void setFrequencyLevel(String frequencyLevel) {
        this.frequencyLevel = frequencyLevel;
    }

    public String getViolationGroupName() {
        return violationGroupName;
    }

    public void setViolationGroupName(String violationGroupName) {
        this.violationGroupName = violationGroupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(long violationDate) {
        this.violationDate = violationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getUserLikeCount() {
        return userLikeCount;
    }

    public void setUserLikeCount(int userLikeCount) {
        this.userLikeCount = userLikeCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getViolationStatus() {
        return violationStatus;
    }

    public void setViolationStatus(String violationStatus) {
        this.violationStatus = violationStatus;
    }


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
}
