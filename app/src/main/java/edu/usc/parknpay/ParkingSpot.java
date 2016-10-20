// To be used by Activities when building up a query to the DatabaseTalker

package edu.usc.parknpay;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkingSpot {
    private String location;
    private String startTime, endTime;
    private String size;
    private String maxPrice;

    public ParkingSpot(@JsonProperty("location") String location, @JsonProperty("startTime") String startTime,
                       @JsonProperty("endTime") String endTime, @JsonProperty("size") String size, @JsonProperty("maxPrice") String maxPrice) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.size = size;
        this.maxPrice = maxPrice;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSize() { return size; }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return getLocation() + " " + getSize() + " " + getStartTime() + " " + getEndTime() + " " + getSize() + " "  + getMaxPrice();
    }
}
