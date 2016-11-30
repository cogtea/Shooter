package com.gardencoder.shooter.models;

/**
 * Created by Ramy on 11/12/16.
 */

public class ShooterModel {
    private String photo;
    private String path;
    private String device_name;
    private String device_model;
    private String activity_name;
    private String photo_extension;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getPhoto_extension() {
        return photo_extension;
    }

    public void setPhoto_extension(String photo_extension) {
        this.photo_extension = photo_extension;
    }
}
