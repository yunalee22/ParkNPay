package edu.usc.parknpay.database;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String ownerId;
    private String seekerId;
    private String photoUrl;
    private String ownerName;
    private String seekerName;
    private String startTime;
    private String endTime;
    private String ownerPhoneNumber;
    private String parkingId;
    private String address;
    private String transactionId;
    double price;
    boolean rated;

    public Transaction() {
    }

    public Transaction(String ownerId, String seekerId, String photoUrl, String ownerName, String seekerName, String startTime, String endTime, String ownerPhoneNumber, String parkingId, String address, String transactionId, double price, boolean rated) {
        this.ownerId = ownerId;
        this.seekerId = seekerId;
        this.photoUrl = photoUrl;
        this.ownerName = ownerName;
        this.seekerName = seekerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.parkingId = parkingId;
        this.address = address;
        this.transactionId = transactionId;
        this.price = price;
        this.rated = rated;
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

}
