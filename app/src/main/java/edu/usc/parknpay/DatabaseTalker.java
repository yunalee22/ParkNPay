package edu.usc.parknpay;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DatabaseTalker extends Application {

    private static DatabaseTalker instance = null;
    private DatabaseReference mDatabase;

    DatabaseReference mRef;
    DatabaseReference mUsersRef;
    DatabaseReference mBrowseRef;

    public DatabaseTalker() {
    }

    public synchronized static DatabaseTalker getInstance() {
        if(instance == null)
            instance = new DatabaseTalker();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //TODO: might have to be in onStart()
        Firebase.setAndroidContext(this);

        Query ref = new Firebase("https://parknpay-4c06e.firebaseio.com/Users/test").limitToLast(10);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    System.out.println(messageSnapshot.toString());
                    String name = (String) messageSnapshot.getValue();
                    System.out.println("NAME: " + name);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
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
