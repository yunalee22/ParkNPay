package edu.usc.parknpay.owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.Review;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.seeker.ReviewAdapter;
import edu.usc.parknpay.utility.TemplateActivity;

public class ViewSpotActivity extends TemplateActivity {
    ImageView spotPhoto, addButton;
    TextView address, spotType, additionalNotes, handicapped;
    ListView availabilities, reviewsListView;
    private ArrayList<ParkingSpotPost> availabilitiesList;
    private AddAvailabilityAdapter availabilityListAdapter;
    private ArrayList<Review> reviews;
    private ReviewAdapter reviewsListAdapter;
    RatingBar ratingBar;
    ParkingSpot parkingSpot;
    Button deleteButton;

    @Override
    protected void onNewIntent(Intent intent) {
        parkingSpot = (ParkingSpot) intent.getSerializableExtra("parkingSpot");
        Picasso.with(this)
                .load(parkingSpot.getPhotoURL())
                .placeholder(R.drawable.progress_animation)
                .resize(450, 450)
                .centerCrop()
                .into(spotPhoto);
        additionalNotes.setText(parkingSpot.getDescription());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_view_spot);
        initializeEdits();
        addListeners();

        setUpToolbar("View Spot");

        parkingSpot = (ParkingSpot) getIntent().getSerializableExtra("parkingSpot");

        //availabilites
        availabilitiesList = new ArrayList<ParkingSpotPost>();
        //This function allows for listviews within scrollviews to be scrolled
        availabilities.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        availabilityListAdapter = new AddAvailabilityAdapter(ViewSpotActivity.this, availabilitiesList);
        availabilities.setAdapter(availabilityListAdapter);

        //reviews
        reviews = new ArrayList<Review>();
        reviewsListAdapter = new ReviewAdapter(ViewSpotActivity.this, reviews);
        reviewsListView.setAdapter(reviewsListAdapter);
        //This function allows for listviews within scrollviews to be scrolled
        reviewsListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Parking-Spot-Reviews").child(parkingSpot.getParkingId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Review t = snapshot.getValue(Review.class);
                    reviews.add(t);
                    reviewsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Set values from passed in parking spot
        address.setText(parkingSpot.getAddress());
        additionalNotes.setText(parkingSpot.getDescription());
        spotType.setText(parkingSpot.getSize());
        handicapped.setText(parkingSpot.isHandicap() ? "Handicapped Spot" : "Not A Handicapped Spot");
        Picasso.with(this)
                .load(parkingSpot.getPhotoURL())
                .placeholder(R.drawable.progress_animation)
                .resize(450, 450)
                .centerCrop()
                .into(spotPhoto);
        ratingBar.setRating((float) parkingSpot.getRating());


        String userId = User.getInstance().getId();
        Ref.child("Browse").orderByChild("ownerUserId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ParkingSpotPost t = snapshot.getValue(ParkingSpotPost.class);
                    if (t.getParkingSpotId().equals(parkingSpot.getParkingId())) {
                        processParkingSpots(t);
                        availabilityListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String userId = User.getInstance().getId();
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Browse").orderByChild("ownerUserId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ParkingSpotPost t = snapshot.getValue(ParkingSpotPost.class);
                    if (t.getParkingSpotId().equals(parkingSpot.getParkingId())) {
                        processParkingSpots(t);
                        availabilityListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void processParkingSpots(ParkingSpotPost t) {
        for (int i = 0; i < availabilitiesList.size(); ++i) {
            // If item exists, replace it
            if (availabilitiesList.get(i).getParkingSpotPostId().equals(t.getParkingSpotPostId()))
            {
                //availabilitiesList.set(i, t);
                return;
            }
        }
        // transaction was not part of array
        availabilitiesList.add(t);
    }


    // Call this function to update the availabilites view
    private void loadReservations(ArrayList<ParkingSpotPost> avails) {

        availabilityListAdapter.clear();
        availabilityListAdapter.addAll(avails);
        availabilityListAdapter.notifyDataSetChanged();
    }

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.parkingSpotImage);
        addButton = (ImageView) findViewById(R.id.addAvail);
        address = (TextView) findViewById(R.id.address);
        spotType = (TextView) findViewById(R.id.spotType);
        additionalNotes = (TextView) findViewById(R.id.notes);
        availabilities = (ListView) findViewById(R.id.availabilities);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        handicapped = (TextView) findViewById(R.id.handicap);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        reviewsListView = (ListView) findViewById(R.id.reviews);
    }

    protected void addListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
                intent.putExtra("parkingSpot", parkingSpot);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //send data: parkingspot
                Intent intent = new Intent(getApplicationContext(), EditSpotActivity.class);
                intent.putExtra("parkingSpot", parkingSpot);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }


}


