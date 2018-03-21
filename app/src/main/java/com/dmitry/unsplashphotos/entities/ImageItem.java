package com.dmitry.unsplashphotos.entities;

import java.io.Serializable;

public class ImageItem implements Serializable {

    private String id;
    private String description;
    private Urls urls;

    public ImageItem(String id, String description, Urls urls) {
        this.id = id;
        this.description = description;
        this.urls = urls;
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
        return urls.getRegular();
    }

    public String getRegularPhotoName() {
        String[] arr = urls.getRegular().split("/");
        arr = arr[arr.length-1].split("\\?");
        return arr[0]+"-Regular";
    }

    public String getThumbPhotoUrl() {
        return urls.getThumb();
    }

    public String getThumbPhotoName() {
        String[] arr = urls.getThumb().split("/");
        arr = arr[arr.length-1].split("\\?");
        return arr[0]+"-Thumb";
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", regularPhotoUrl='" + urls.getRegular() + '\'' +
                ", thumbPhotoUrl='" + urls.getThumb() + '\'' +
                '}';
    }

    public static class Urls implements Serializable {

        private String raw;
        private String full;
        private String regular;
        private String small;
        private String thumb;

        public Urls(String raw, String full, String regular, String small, String thumb) {
            this.raw = raw;
            this.full = full;
            this.regular = regular;
            this.small = small;
            this.thumb = thumb;
        }

        String getThumb() {
            return thumb;
        }

        String getRegular() {
            return regular;
        }

        @Override
        public String toString() {
            return "Urls{" +
                    "raw='" + raw + '\'' +
                    ", full='" + full + '\'' +
                    ", regular='" + regular + '\'' +
                    ", small='" + small + '\'' +
                    ", thumb='" + thumb + '\'' +
                    '}';
        }
    }
}