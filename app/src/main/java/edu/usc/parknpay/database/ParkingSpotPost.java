package edu.usc.parknpay.database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by tiffanyhuang on 10/23/16.
 */


public class ParkingSpotPost implements Serializable{
    private String ownerUserId, parkingSpotId;
    private String ownerFullName;
    private String ownerPhoneNumber;
    private String startTime, endTime;
    private double latitude, longitude;
    private double price;
    private double rating;
    private String size;
    private String cancellationPolicy;
    private boolean isHandicap;
    private double ownerRating;
    private String address;
    private String photoUrl;
    private String postId;
    private String description;
    private boolean reserved;

    public ParkingSpotPost() {
    }

    public ParkingSpotPost(String ownerUserId, String ownerFullName, String ownerPhoneNumber, String parkingSpotId, String address, String startTime, String endTime,
                           double latitude, double longitude, double price, double rating, String size, String cancellationPolicy,
                           boolean isHandicap, double ownerRating, String photoUrl, String postId, String description, boolean reserved)
    {
        this.ownerUserId = ownerUserId;
        this.parkingSpotId = parkingSpotId;
        this.ownerFullName = ownerFullName;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.rating = rating;
        this.size = size;
        this.cancellationPolicy = cancellationPolicy;
        this.isHandicap = isHandicap;
        this.ownerRating = ownerRating;
        this.address = address;
        this.photoUrl = photoUrl;
        this.postId = postId;
        this.description = description;
        this.reserved = reserved;
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

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
