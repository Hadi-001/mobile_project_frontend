package com.example.mobile_project_frontend;

public class HorizontalItem {
    private int imageResId;
    private String title;
    private String propertyTitle;
    private String location;
    private int bedroomCount;
    private int bathroomCount;
    private String price;

    public HorizontalItem(int imageResId, String title, String propertyTitle, String location, int bedroomCount, int bathroomCount, String price) {
        this.imageResId = imageResId;
        this.title = title;
        this.propertyTitle = propertyTitle;
        this.location = location;
        this.bedroomCount = bedroomCount;
        this.bathroomCount = bathroomCount;
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public String getLocation() {
        return location;
    }

    public int getBedroomCount() {
        return bedroomCount;
    }

    public int getBathroomCount() {
        return bathroomCount;
    }

    public String getPrice() {
        return price;
    }
}
