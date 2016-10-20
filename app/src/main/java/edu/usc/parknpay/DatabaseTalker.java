package edu.usc.parknpay;

import android.app.Application;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseTalker extends Application {

    private static DatabaseTalker instance = null;

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
        Firebase.setAndroidContext(this);
        getParkingSpots(); // temporary!
    }

    public ArrayList<ParkingSpot> getParkingSpots(/*query*/) {
        ArrayList<ParkingSpot> spots = new ArrayList<ParkingSpot>();
        Query myTopPostsQuery = new Firebase("https://parknpay-4c06e.firebaseio.com/Browse/").orderByChild("maxPrice");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("VALUE: " + dataSnapshot.getValue());
                ParkingSpot spot = dataSnapshot.getValue(ParkingSpot.class);
                System.out.println(spot.toString());

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });


        System.out.println("GET PARKING SPOTS");
        Query ref = new Firebase("https://parknpay-4c06e.firebaseio.com/Browse").limitToLast(10);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>)  messageSnapshot.getValue();
                    System.out.println("SPOT: " + messageSnapshot.getKey());
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        System.out.println("key: " + key + " value: " + value);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
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
