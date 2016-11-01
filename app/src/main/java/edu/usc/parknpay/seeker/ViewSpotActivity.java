package edu.usc.parknpay.seeker;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.Serializable;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpotPost;

/**
 * Created by Bobo on 10/27/2016.
 */

public class ViewSpotActivity extends TemplateActivity{

    private ImageView parkingSpotImage;
    private TextView address;
    private RatingBar spotRatingBar;
    private TextView size, handicap;
    private TextView cancellationPolicy;
    private TextView additionalNotes;
    private ImageView ownerImage;
    private TextView ownerName;
    private RatingBar ownerRatingBar;
    private Button reserveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_view_spot);
        super.onCreateDrawer();
        toolbarSetup();

        // Get references to UI views
        parkingSpotImage = (ImageView) findViewById(R.id.parkingSpotImage);
        address = (TextView) findViewById(R.id.address);
        spotRatingBar = (RatingBar) findViewById(R.id.spotRatingBar);
        size = (TextView) findViewById(R.id.size);
        handicap = (TextView) findViewById(R.id.handicap);
        cancellationPolicy = (TextView) findViewById(R.id.cancellationPolicy);
        additionalNotes = (TextView) findViewById(R.id.additionalNotes);
        ownerImage = (ImageView) findViewById(R.id.ownerImage);
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
        reserveButton = (Button) findViewById(R.id.reserveButton);

        // Get parking spot post
        Serializable object = getIntent().getSerializableExtra("Parking spot post");
        ParkingSpotPost parkingSpotPost = (ParkingSpotPost) object;

        // TO DO: Update all the view information using the parkingSpotPost object.




        addListeners();
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

    protected void addListeners() {

        // Called when reserve button is clicked
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Add spot reservation to database

            }
        });

    }
}