// To be used by Activities when building up a query to the DatabaseTalker

package edu.usc.parknpay.database;

import android.net.Uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;

public class ParkingSpot implements Serializable{
    private String ownerUserId;
    private String ownerFullName;
    private String ownerPhoneNumber;
    private String address;
    private double latitude, longitude;
    private String size;
    private double rating;
    private boolean isHandicap;
    private String description;
    private String parkingId;
    private String photoURL;
    private int numRatings;

    public enum Size {
        Compact(0), Normal(1), Suv(2), Truck(3);
        private final int value;
        private Size(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    };

    public ParkingSpot() {
    }

    public ParkingSpot(@JsonProperty("ownerUserId") String ownerUserId, @JsonProperty("ownerFullName") String ownerFullName, @JsonProperty("ownerPhoneNumber") String ownerPhoneNumber, @JsonProperty("address") String address, @JsonProperty("size") String size, @JsonProperty("rating") double rating,
                       @JsonProperty("isHandicap") boolean isHandicap, @JsonProperty("description") String description, @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude,  @JsonProperty("numRatings") int numRatings) {
        this.ownerUserId = ownerUserId;
        this.ownerFullName = ownerFullName;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.address = address;
        this.size = size;
        this.rating = rating;
        this.isHandicap = isHandicap;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numRatings = numRatings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHandicap() {
        return isHandicap;
    }

    public void setHandicap(boolean handicapped) {
        isHandicap = handicapped;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
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

    @Override
    public String toString() {
        return "Address: " + getAddress();
    }

    public String getParkingId() {return parkingId;}

    public void setParkingId(String PID) {
        parkingId = PID;

        FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("Spots").child(PID)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoURL = uri.toString();
            }
        });
    }

    public void setPhotoURL(String url) {
        photoURL = url;
    }

    public String getPhotoURL() {
        return photoURL;
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

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public void updateRating(int rating) {
        double tempRating = this.rating * numRatings;
        numRatings++;
        this.rating = (tempRating + ((double)rating)) / numRatings;
        FirebaseDatabase.getInstance().getReference().child("Parking-Spots").child(parkingId).setValue(this);
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
}
