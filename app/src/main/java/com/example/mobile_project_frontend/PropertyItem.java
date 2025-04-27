package com.example.mobile_project_frontend;

import java.io.Serializable;
import java.util.Date;

public class PropertyItem implements Serializable {

    private int estateId, beds, baths, views, ownerId;
    private String type, city, street, imageLink, description;
    private double price, area;
    private String dateBuild, postDate;
    private boolean isFavorite;


    public PropertyItem(int estateId, String type, int beds, int baths,
                        double price, String city, String street, String imageLink,
                        int views, double area, String description, String dateBuild,
                        int ownerId, String postDate, boolean isFavorite) {
        this.estateId = estateId;
        this.type = type;
        this.beds = beds;
        this.baths = baths;
        this.price = price;
        this.city = city;
        this.imageLink = imageLink;
        this.isFavorite = isFavorite;
        this.street = street;
        this.views = views;
        this.area = area;
        this.description = description;
        this.dateBuild = dateBuild;
        this.ownerId = ownerId;
        this.postDate = postDate;
    }
    public int getEstateId() {
        return estateId;
    }

    public void setEstateId(int estateId) {
        this.estateId = estateId;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getDateBuild() {
        return dateBuild;
    }

    public void setDateBuild(String dateBuild) {
        this.dateBuild = dateBuild;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
