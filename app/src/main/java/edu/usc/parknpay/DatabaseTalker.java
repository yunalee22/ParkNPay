package edu.usc.parknpay;

import android.app.Application;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
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
        //getParkingSpots(); // temporary!
    }

    public ArrayList<ParkingSpot> getParkingSpots(/*query*/) {
        ArrayList<ParkingSpot> spots = new ArrayList<ParkingSpot>();

        // Example of filtering by price - insert corresponding filter
        Query myTopPostsQuery = new Firebase("https://parknpay-4c06e.firebaseio.com/Browse/").orderByChild("maxPrice");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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

        // Example of acquiring parking spots
        Query ref = new Firebase("https://parknpay-4c06e.firebaseio.com/Browse").limitToLast(10);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>)  messageSnapshot.getValue();
                    System.out.println("Spot: " + messageSnapshot.getKey());
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
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
