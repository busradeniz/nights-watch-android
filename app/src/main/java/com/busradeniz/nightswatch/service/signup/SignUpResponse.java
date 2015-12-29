package com.busradeniz.nightswatch.service.signup;

import com.busradeniz.nightswatch.service.fileupload.Media;

/**
 * Created by busradeniz on 27/12/15.
 */
public class SignUpResponse {

    private String bio;
    private String birthday;
    private String email;
    private String fullName;
    private String genderTypeDto;
    private int id;
    private String username;
    private Media media;


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGenderTypeDto() {
        return genderTypeDto;
    }

    public void setGenderTypeDto(String genderTypeDto) {
        this.genderTypeDto = genderTypeDto;
    }

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

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
