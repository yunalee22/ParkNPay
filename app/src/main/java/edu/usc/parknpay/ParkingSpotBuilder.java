package edu.usc.parknpay;
// Builds up Parking Spot Queries neatly, so we can build like so:
// HOW TO USE:
// ParkingSpot s2 = new ParkngSpotBuilder()
//        .isHandicap(true)
//        .size(1)
//        .buildQuery();

import java.util.Date;

public class ParkingSpotBuilder {
    private Location location;
    private Date startTime;
    private Date endTime;
    private boolean isHandicap;
    private ParkingSpot.Size size;
    private double maxPrice;

    public ParkingSpotBuilder() {}

    public ParkingSpot buildQuery() {
        return new ParkingSpot(location, startTime, endTime, size, maxPrice);
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

    public ParkingSpotBuilder size(ParkingSpot.Size size) {
        this.size = size;
        return this;
    }

    public ParkingSpotBuilder maxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }
}
