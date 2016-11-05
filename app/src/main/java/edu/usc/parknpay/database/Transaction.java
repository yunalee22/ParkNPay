package edu.usc.parknpay.database;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String ownerId;
    private String seekerId;
    private String parkingSpotPostId;
    private String photoUrl;
    private String ownerName;
    private String seekerName;
    private String startTime;
    private String endTime;
    private String ownerPhoneNumber;
    private String seekerPhoneNumber;
    private String parkingId;
    private String address;
    private String transactionId;
    double price;
    boolean rated;
    boolean cancelled;

    public Transaction() {
    }

    public Transaction(String transactionId, String ownerId, String seekerId, String parkingSpotPostId, String photoUrl, String ownerName, String seekerName, String startTime, String endTime, String ownerPhoneNumber, String seekerPhoneNumber, String parkingId, String address, double price, boolean rated, boolean cancelled) {
        this.transactionId = transactionId;
        this.ownerId = ownerId;
        this.seekerId = seekerId;
        this.parkingSpotPostId = parkingSpotPostId;
        this.photoUrl = photoUrl;
        this.ownerName = ownerName;
        this.seekerName = seekerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.seekerPhoneNumber = seekerPhoneNumber;
        this.parkingId = parkingId;
        this.address = address;
        this.price = price;
        this.rated = rated;
        this.cancelled = cancelled;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSeekerName() {
        return seekerName;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public String getSeekerPhoneNumber() {
        return seekerPhoneNumber;
    }

    public void setSeekerPhoneNumber(String seekerPhoneNumber) {
        this.seekerPhoneNumber = seekerPhoneNumber;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getParkingSpotPostId() {
        return parkingSpotPostId;
    }

    public void setParkingSpotPostId(String parkingSpotPostId) {
        this.parkingSpotPostId = parkingSpotPostId;
    }

}
