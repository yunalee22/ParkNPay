package edu.usc.parknpay.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;

public class ViewSpotActivity extends TemplateActivity {
    ImageView spotPhoto, addButton;
    TextView address, spotType, additionalNotes, handicapped, cancellationPolicy;
    ListView availabilities;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_view_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        addListeners();

        ParkingSpot parkingSpot = (ParkingSpot) getIntent().getSerializableExtra("parkingSpot");

        // Set values from passed in parking spot
        address.setText(parkingSpot.getAddress());
        additionalNotes.setText(parkingSpot.getDescription());
        spotType.setText(parkingSpot.getSize());
        Picasso.with(this)
                .load(parkingSpot.getPhotoURL())
                .resize(450, 450)
                .centerCrop()
                .into(spotPhoto);
        ratingBar.setNumStars((int) parkingSpot.getRating()); // not working?
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Spot");
    }

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.imageView);
        addButton = (ImageView) findViewById(R.id.addAvail);
        address = (TextView) findViewById(R.id.address);
        spotType = (TextView) findViewById(R.id.spotType);
        additionalNotes = (TextView) findViewById(R.id.notes);
        availabilities = (ListView) findViewById(R.id.availabilities);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        handicapped = (TextView) findViewById(R.id.time);
        cancellationPolicy = (TextView) findViewById(R.id.date);
    }

    protected void addListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });
    }

}


