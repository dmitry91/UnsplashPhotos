package com.dmitry.unsplashphotos.entities;

import java.io.Serializable;

public class ImageItem implements Serializable {

    private String id;
    private String description;
    private String regularPhotoUrl;
    private String thumbPhotoUrl;

    public ImageItem(String id, String description, String regularPhotoUrl, String thumbPhotoUrl) {
        this.id = id;
        this.description = description;
        this.regularPhotoUrl = regularPhotoUrl;
        this.thumbPhotoUrl = thumbPhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getRegularPhotoUrl() {
        return regularPhotoUrl;
    }

    public String getRegularPhotoName() {
        String[] arr = regularPhotoUrl.split("/");
        return arr[arr.length-1];
    }

    public String getThumbPhotoUrl() {
        return thumbPhotoUrl;
    }

    public String getThumbPhotoName() {
        String[] arr =thumbPhotoUrl.split("/");
        return arr[arr.length-1];
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", regularPhotoUrl='" + regularPhotoUrl + '\'' +
                ", thumbPhotoUrl='" + thumbPhotoUrl + '\'' +
                '}';
    }
}