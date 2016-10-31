package edu.usc.parknpay.seeker;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

/**
 * Created by Bobo on 10/27/2016.
 */

public class ViewSpotActivity extends TemplateActivity{
    ImageView spotPhoto, addButton, ownerPhoto;
    TextView address, spotType, additionalNotes, handicapped, cancellationPolicy, ownerName;
    RatingBar spotRatingBar, ownerRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_view_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
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

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.imageView);
        ownerPhoto = (ImageView) findViewById(R.id.pic);
        addButton = (ImageView) findViewById(R.id.addAvail);
        address = (TextView) findViewById(R.id.address);
        spotType = (TextView) findViewById(R.id.spotType);
        additionalNotes = (TextView) findViewById(R.id.notes);
        spotRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        ownerRatingBar = (RatingBar) findViewById(R.id.ratingBar2);
        handicapped = (TextView) findViewById(R.id.time);
        cancellationPolicy = (TextView) findViewById(R.id.date);
        ownerName = (TextView) findViewById(R.id.owner);
    }

    protected void addListeners() {

    }

}