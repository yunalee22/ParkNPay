// To be used by Activities when building up a query to the DatabaseTalker

package edu.usc.parknpay.database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ParkingSpot {
    private String ownerUserId;
    private String address;
    private String startDate, endDate;
    private String startTime, endTime;
    private String size;
    private double price;
    private double rating;
    private boolean isHandicapped;
    private String description;
    private String cancellationPolicy;
    // TODO: images

    public ParkingSpot(@JsonProperty("ownerUserId") String ownerUserId, @JsonProperty("address") String address, @JsonProperty("startDate") String startDate,
                       @JsonProperty("endDate") String endDate, @JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime, @JsonProperty("size") String size, @JsonProperty("price") double price, @JsonProperty("rating") double rating,
                       @JsonProperty("isHandicapped") boolean isHandicapped, @JsonProperty("description") String description, @JsonProperty("cancellationPolicy") String cancellationPolicy) {
        this.ownerUserId = ownerUserId;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.size = size;
        this.price = price;
        this.rating = rating;
        this.isHandicapped = isHandicapped;
        this.description = description;
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isHandicapped() {
        return isHandicapped;
    }

    public void setHandicapped(boolean handicapped) {
        isHandicapped = handicapped;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Address: " + getAddress() + " Size: " + getSize() + " startTime: " + getStartDate() + " endDate" + getEndDate() +
               " price: "  + getPrice();
    }
}
