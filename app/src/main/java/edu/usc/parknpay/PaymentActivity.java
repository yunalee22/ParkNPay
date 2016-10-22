package edu.usc.parknpay;

import android.os.Bundle;
import android.view.MenuItem;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

/**
 * Created by Bobo on 10/14/2016.
 */

public class PaymentActivity extends MenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_info_layout);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }


    public static class User {
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
}
