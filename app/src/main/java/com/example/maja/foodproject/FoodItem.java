package com.example.maja.foodproject;

import android.net.Uri;

class FoodItem {
    private String title;
    private String description;
    private Uri imageUrl;

    public FoodItem(String title, String description, Uri imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }
}