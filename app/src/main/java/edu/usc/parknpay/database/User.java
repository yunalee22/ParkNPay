package edu.usc.parknpay.database;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String id;
    private int rawRating;
    private int numRatings;
    private String phoneNumber;
    private String licenseNumber;

    // Constructor
    public User(String name,
         String lastName,
         String email,
         String id,
         String phoneNumber,
         String licenseNumber,
         int rawRating,
         int numRatings
    ) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rawRating = rawRating;
        this.numRatings = numRatings;
    }

    // Alternate constructor
    public User(String name,
         String lastName,
         String email,
         String phoneNumber,
         String licenseNumber,
         int rawRating,
         int numRatings
    ) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rawRating = rawRating;
        this.numRatings = numRatings;
    }
    public void setId(String id) { this.id = id; }

    public String getName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
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

    private int getNumRating() {
        return numRatings;
    }

    public void updateRating(int rating) {
        rawRating += rating;
        numRatings++;
        // update these values in firebase
    }

}
