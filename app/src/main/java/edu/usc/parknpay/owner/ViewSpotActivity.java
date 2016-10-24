package edu.usc.parknpay.owner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

/**
 * Created by Bobo on 10/23/2016.
 */

public class ViewSpotActivity extends TemplateActivity {
    ImageView spotPhoto, addButton;
    TextView address, spotType, additionalNotes;
    ListView availabilities;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        addListeners();
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a Space");
    }

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.imageView);
        addButton = (ImageView) findViewById(R.id.addAvail);
        address = (TextView) findViewById(R.id.address);
        spotType = (TextView) findViewById(R.id.spotType);
        additionalNotes = (TextView) findViewById(R.id.notes);
        availabilities = (ListView) findViewById(R.id.availabilities);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
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

