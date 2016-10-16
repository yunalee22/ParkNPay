package edu.usc.parknpay;
// Builds up Parking Spot Queries neatly, so we can build like so:
// HOW TO USE:
// ParkingSpotQuery s2 = new ParkngSpotBuilder()
//        .isHandicap(true)
//        .size(1);

import java.util.Date;

public class ParkingSpotBuilder {
    private Location location;
    private Date startTime;
    private Date endTime;
    private boolean isHandicap;
    private int size; // TODO: make an enum in ParkingSpot

    public ParkingSpotBuilder() {}

    public ParkingSpotQuery buildQuery() {
        return new ParkingSpotQuery(location, startTime, endTime, isHandicap, size);
    }

    public ParkingSpotBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public ParkingSpotBuilder startTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public ParkingSpotBuilder endTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public ParkingSpotBuilder isHandicap(boolean isHandicap) {
        this.isHandicap = isHandicap;
        return this;
    }

    public ParkingSpotBuilder size(int size) {
        this.size = size;
        return this;
    }
}
