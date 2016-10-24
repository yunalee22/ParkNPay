package edu.usc.parknpay.database;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tiffanyhuang on 10/23/16.
 */


public class ParkingSpotPost {
    private String ownerId, parkingSpotId;
    private String startTime, endTime;
    private double latitude, longitude;
    private double price;
    private int cancellationPolicy;

    public ParkingSpotPost() {
    }

    public ParkingSpotPost(@JsonProperty("ownerId") String ownerId, @JsonProperty("parkingId") String parkingSpotId, @JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime,
                           @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude, @JsonProperty("price") double price, @JsonProperty("cancellationPolicy") int cancellationPolicy)
    {
        this.ownerId = ownerId;
        this.parkingSpotId = parkingSpotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.cancellationPolicy = cancellationPolicy;
    }


    public int getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(int cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
