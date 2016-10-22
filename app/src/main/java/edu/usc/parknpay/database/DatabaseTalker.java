package edu.usc.parknpay.database;

import android.app.Application;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DatabaseTalker extends Application {
    private static DatabaseTalker instance = null;

    Firebase mRef, mUsersRef, mBrowseRef;

    // Firebase Authentication variable
    private FirebaseAuth firebaseAuth;

    public DatabaseTalker() {
    }

    public synchronized static DatabaseTalker getInstance() {
        if(instance == null) {
            instance = new DatabaseTalker();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //TODO: might have to be in onStart()
        Firebase.setAndroidContext(this);

        mRef = new Firebase("https://parknpay-4c06e.firebaseio.com/");
        mUsersRef = mRef.child("Users");
        mBrowseRef = mRef.child("Browse");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object _data = dataSnapshot.getValue();
                System.out.println("DATA");
                System.out.println(_data.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Error in fetching data");
            }
        });

    }

    public ArrayList<ParkingSpot> getParkingSpots(ParkingSpot query) {
        ArrayList<ParkingSpot> spots = new ArrayList<ParkingSpot>();

        return spots;
    }

    public boolean addParkingSpots(ParkingSpot spot) {
        return false;
    }

    public boolean addParkingSpots(ArrayList<ParkingSpot> list) {
        return false;
    }

    public boolean reserveParkingSpot(ParkingSpot spot) {
        return false;
    }

}
