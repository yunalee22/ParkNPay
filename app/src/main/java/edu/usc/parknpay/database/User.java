package edu.usc.parknpay.database;

public class User {
    private String name;
    private String lastName;
    private String email;
    private String id;
    private int rawRating;
    private int numRatings;
    private String phoneNumber;
    private String licenseNumber;

    // Constructor
    User(String name,
         String lastName,
         String email,
         String id,
         String phoneNumber,
         String licenseNumber,
         int rawRating,
         int numRatings
    ) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rawRating = rawRating;
        this.numRatings = numRatings;

    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public int getRating() {
        return rawRating / numRatings;
    }

    public void updateRating(int rating) {
        rawRating += rating;
        numRatings++;
        // update these values in firebase
    }

}
