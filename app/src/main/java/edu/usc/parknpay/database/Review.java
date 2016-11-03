package edu.usc.parknpay.database;

import java.io.Serializable;

public class Review implements Serializable{
    private String seekerName;
    private String comments;
    private double rating;
    private String date;
    private String seekerProfilePhotoURL;

    public Review(String seekerName, String comments, double rating, String date, String seekerProfilePhotoURL) {
        this.seekerName = seekerName;
        this.comments = comments;
        this.rating = rating;
        this.date = date;
        this.seekerProfilePhotoURL = seekerProfilePhotoURL;
    }

    public Review() {}

    public String getSeekerName() {
        return seekerName;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeekerProfilePhotoURL() {
        return seekerProfilePhotoURL;
    }

    public void setSeekerProfilePhotoURL(String seekerProfilePhotoURL) {
        this.seekerProfilePhotoURL = seekerProfilePhotoURL;
    }
}
