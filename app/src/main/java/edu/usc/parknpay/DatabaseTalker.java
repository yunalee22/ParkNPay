package edu.usc.parknpay;

import android.app.Application;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DatabaseTalker extends Application {

    private static DatabaseTalker instance = null;
    private String test;

    Firebase mRef, mUsersRef, mBrowseRef;

    private DatabaseTalker() {
        test = "Hi";
    }

    public synchronized static DatabaseTalker getInstance() {
        if(instance == null)
            instance = new DatabaseTalker();
        return instance;
    }

    public String getString() {
        return this.test;
    }

    public void setString(String value) {
        test = value;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //TODO: might have to be in onStart()
        Firebase.setAndroidContext(this);

        mRef = new Firebase("https://parknpay-4c06e.firebaseio.com/Users/47fsSEGu3WOzQ1UkkzBkEj8jGHD3");
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
}
