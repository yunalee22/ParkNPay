package edu.usc.parknpay.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.User;

public class ViewSpotActivity extends TemplateActivity {
    ImageView spotPhoto, addButton;
    TextView address, spotType, additionalNotes, handicapped, cancellationPolicy;
    ListView availabilities;
    private ArrayList<ParkingSpotPost> availabilitiesList;
    private AddAvailabilityAdapter availabilityListAdapter;
    RatingBar ratingBar;
    ParkingSpot parkingSpot;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_view_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        addListeners();

        parkingSpot = (ParkingSpot) getIntent().getSerializableExtra("parkingSpot");

        //availabilites
        availabilitiesList = new ArrayList<ParkingSpotPost>();
        availabilityListAdapter = new AddAvailabilityAdapter(ViewSpotActivity.this, availabilitiesList);
        availabilities.setAdapter(availabilityListAdapter);

        // Set values from passed in parking spot
        address.setText(parkingSpot.getAddress());
        additionalNotes.setText(parkingSpot.getDescription());
        spotType.setText(parkingSpot.getSize());
        handicapped.setText(parkingSpot.isHandicap() ? "Handicapped Spot" : "Not A Handicapped Spot");
        Picasso.with(this)
                .load(parkingSpot.getPhotoURL())
                .resize(450, 450)
                .centerCrop()
                .into(spotPhoto);
        ratingBar.setRating((float) parkingSpot.getRating());

        availabilitiesList = new ArrayList<ParkingSpotPost>();
        availabilityListAdapter = new AddAvailabilityAdapter(ViewSpotActivity.this, availabilitiesList);
        availabilities.setAdapter(availabilityListAdapter);

        String userId = User.getInstance().getId();
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Browse").orderByChild("ownerUserId").equalTo(userId).addValueEventListener(new ValueEventListener() {
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
            if (availabilitiesList.get(i).getPostId().equals(t.getPostId()))
            {
                availabilitiesList.set(i, t);
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

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Spot");
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

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.parkingSpotImage);
        addButton = (ImageView) findViewById(R.id.addAvail);
        address = (TextView) findViewById(R.id.address);
        spotType = (TextView) findViewById(R.id.spotType);
        additionalNotes = (TextView) findViewById(R.id.additionalNotes);
        availabilities = (ListView) findViewById(R.id.availabilities);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        handicapped = (TextView) findViewById(R.id.handicap);
        cancellationPolicy = (TextView) findViewById(R.id.cancellationPolicy);
        deleteButton = (Button) findViewById(R.id.deleteButton);
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
    }

}


