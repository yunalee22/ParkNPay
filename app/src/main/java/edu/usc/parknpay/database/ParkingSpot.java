// To be used by Activities when building up a query to the DatabaseTalker

package edu.usc.parknpay.database;

import android.net.Uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class ParkingSpot {
    private String ownerUserId;
    private String address;
    private double latitude, longitude;
    private String size;
    private double rating;
    private boolean isHandicapped;
    private String description;
    private String cancellationPolicy;
    private String parkingId;
    private String photoURL;

    public ParkingSpot() {
    }

    public ParkingSpot(@JsonProperty("ownerUserId") String ownerUserId, @JsonProperty("address") String address, @JsonProperty("size") String size, @JsonProperty("rating") double rating,
                       @JsonProperty("isHandicapped") boolean isHandicapped, @JsonProperty("description") String description, @JsonProperty("cancellationPolicy") String cancellationPolicy) {
        this.ownerUserId = ownerUserId;
        this.address = address;
        this.size = size;
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
}
