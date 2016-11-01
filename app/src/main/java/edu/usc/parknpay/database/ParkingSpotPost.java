package edu.usc.parknpay.database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by tiffanyhuang on 10/23/16.
 */


public class ParkingSpotPost implements Serializable{
    private String ownerUserId, parkingSpotId;
    private String startTime, endTime;
    private double latitude, longitude;
    private double price;
    private String size;
    private String cancellationPolicy;
    private boolean isHandicap;
    private double ownerRating;
    private String address;
    private String photoUrl;
    private String postId;

    private String description;

    public ParkingSpotPost() {
    }

    public ParkingSpotPost(@JsonProperty("ownerUserId") String ownerUserId, @JsonProperty("parkingSpotId") String parkingSpotId, @JsonProperty("address") String address, @JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime,
                           @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude, @JsonProperty("price") double price, @JsonProperty("size") String size, @JsonProperty("cancellationPolicy") String cancellationPolicy,
                           @JsonProperty("isHandicap") boolean isHandicap, @JsonProperty("ownerRating") double ownerRating, @JsonProperty("photoUrl") String photoUrl, @JsonProperty("postId") String postId, @JsonProperty("description") String description)
    {
        this.ownerUserId = ownerUserId;
        this.parkingSpotId = parkingSpotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.size = size;
        this.cancellationPolicy = cancellationPolicy;
        this.isHandicap = isHandicap;
        this.ownerRating = ownerRating;
        this.address = address;
        this.photoUrl = photoUrl;
        this.postId = postId;
        this.description = description;
    }


    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isHandicap() {
        return isHandicap;
    }

    public void setHandicap(boolean handicap) {
        isHandicap = handicap;
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

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(String parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(double ownerRating) {
        this.ownerRating = ownerRating;
    }

    @Override
    public String toString() {
        return "ParkingSpotPost: OwnerUserId: " + ownerUserId + " ParkingSpotId: " + parkingSpotId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
