package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.fileupload.Media;

import java.util.List;

/**
 * Created by busradeniz on 28/12/15.
 */
public class CreateViolationRequest {

    private String address;
    private String customProperties;
    private String dangerLevel;
    private String description;
    private String frequencyLevel;
    private double latitude;
    private double longitude;
    private List<Media> medias;
    private String[] tags;
    private String title;
    private String violationGroupName;
    private String violationStatus;


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

    public String getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(String customProperties) {
        this.customProperties = customProperties;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequencyLevel() {
        return frequencyLevel;
    }

    public void setFrequencyLevel(String frequencyLevel) {
        this.frequencyLevel = frequencyLevel;
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

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViolationGroupName() {
        return violationGroupName;
    }

    public void setViolationGroupName(String violationGroupName) {
        this.violationGroupName = violationGroupName;
    }
}
