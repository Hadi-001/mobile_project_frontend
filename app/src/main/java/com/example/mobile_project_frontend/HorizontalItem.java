package com.example.mobile_project_frontend;

import java.io.Serializable;

public class HorizontalItem implements Serializable {
    private int itemId;
    private String imageURL;
    private String title;
    private String propertyTitle;
    private String location;
    private int bedroomCount;
    private int bathroomCount;
    private String price;
    private boolean isLiked;

    public HorizontalItem(int id,String imageURL, String title, String propertyTitle, String location, int bedroomCount, int bathroomCount, String price, boolean isLiked) {
        this.itemId = id;
        this.imageURL = imageURL;
        this.title = title;
        this.propertyTitle = propertyTitle;
        this.location = location;
        this.bedroomCount = bedroomCount;
        this.bathroomCount = bathroomCount;
        this.price = price;
        this.isLiked = isLiked;
    }

    public int getItemId(){return itemId;}

    public String getImageURL() {
        return imageURL;
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

    public boolean isLiked(){return isLiked;}

    public void setLiked(boolean isLiked){this.isLiked = isLiked;}
}
