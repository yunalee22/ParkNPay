package edu.usc.parknpay;
// Builds up Parking Spot Queries neatly, so we can build like so:
// HOW TO USE:
// ParkingSpot s2 = new ParkngSpotBuilder()
//        .isHandicap(true)
//        .size(1)
//        .buildQuery();

import java.util.Date;

public class ParkingSpotBuilder {
    private String location;
    private String startTime;
    private String endTime;
    private String size;
    private String maxPrice;

    public ParkingSpotBuilder() {}

    public ParkingSpot buildQuery() {
        return new ParkingSpot(location, startTime, endTime, size, maxPrice);
    }

    public ParkingSpotBuilder location(String location) {
        this.location = location;
        return this;
    }

    public ParkingSpotBuilder startTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public ParkingSpotBuilder endTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public ParkingSpotBuilder size(String size) {
        this.size = size;
        return this;
    }

    public ParkingSpotBuilder maxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }
}
