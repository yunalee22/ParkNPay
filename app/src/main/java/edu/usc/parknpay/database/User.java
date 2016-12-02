package edu.usc.parknpay.database;

import com.google.firebase.database.FirebaseDatabase;

public class User {
    private static User instance = null;
    private String firstName, lastName, fullName;
    private String email;
    private String id;
    private int rawRating;
    private int numRatings;
    private String phoneNumber;
    private String licenseNumber;
    private boolean isSeeker;
    private boolean isCurrentlySeeker;
    private String profilePhotoURL;
    private double balance;

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    public synchronized static User getInstance() {
        return instance;
    }

    public synchronized static void createUser(User user)
    {
        instance = new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId(),
                user.getPhoneNumber(),
                user.getLicenseNumber(),
                user.getRawRating(),
                user.getNumRatings(),
                user.isSeeker(),
                user.getBalance(),
                user.getProfilePhotoURL()
        );
    }

    // On signing out
    public void removeUser()
    {
        instance = null;
    }

    // Constructors

    public User(){}

    public User(String name,
         String lastName,
         String email,
         String id,
         String phoneNumber,
         String licenseNumber,
         int rawRating,
         int numRatings,
         boolean isSeeker,
         double balance,
         String photoURL
    ) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rawRating = rawRating;
        this.numRatings = numRatings;
        this.isSeeker = isSeeker;
        this.balance = balance;
        this.profilePhotoURL = photoURL;
    }

    // Alternate constructor
    public User(String name,
         String lastName,
         String email,
         String phoneNumber,
         String licenseNumber,
         int rawRating,
         int numRatings,
         boolean isSeeker,
         double balance
    ) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.rawRating = rawRating;
        this.numRatings = numRatings;
        this.balance = balance;
    }
    public void setId(String id) { this.id = id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public int Rating() {
        if(numRatings == 0)
            return 0;
        return rawRating / numRatings;
    }

    public int getRawRating() {
        return rawRating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public boolean isSeeker() {
        return isSeeker;
    }

    public void updateRating(int rating) {
        rawRating += rating;
        numRatings++;
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(this);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRawRating(int rawRating) {
        this.rawRating = rawRating;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setSeeker(boolean seeker) {
        isSeeker = seeker;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void changeBalance(double change)
    {
        this.balance+=change;

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(this);
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public void setIsCurrentlySeeker(boolean b)
    {
        this.isCurrentlySeeker = b;
    }

    public boolean getIsCurrentlySeeker()
    {
        return isCurrentlySeeker;
    }

    public String getFullName() { return firstName + " " + lastName;}

    private void setFullName(String fullName) { this.fullName = fullName;}
}
