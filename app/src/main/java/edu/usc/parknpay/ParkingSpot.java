// To be used by Activities when building up a query to the DatabaseTalker

package edu.usc.parknpay;

import java.util.Date;

public class ParkingSpot {
    private Location location;
    private Date startTime, endTime;
    private Size size;
    private double maxPrice;
    public enum Size { HANDICAP, COMPACT, TRUCK}

    public ParkingSpot(Location location, Date startTime, Date endTime, ParkingSpot.Size size, double maxPrice) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.size = size;
        this.maxPrice = maxPrice;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
