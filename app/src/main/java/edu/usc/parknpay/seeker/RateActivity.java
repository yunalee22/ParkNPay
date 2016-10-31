package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.usc.parknpay.R;

/**
 * Created by Bobo on 10/27/2016.
 */

public class RateActivity extends AppCompatActivity {
    ImageView spotPhoto;
    TextView address, date, time;
    RatingBar spotRatingBar, ownerRatingBar;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_rate);
        toolbarSetup();
        initializeEdits();
        addListeners();

        //update the components: address, date, time, photo

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
        address = (TextView) findViewById(R.id.address);
        spotRatingBar = (RatingBar) findViewById(R.id.ratingBarSpot);
        ownerRatingBar = (RatingBar) findViewById(R.id.ratingBarOwner);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        doneButton = (Button) findViewById(R.id.button);
    }

    protected void addListeners() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //upload the ratings to the database
                double spotRating = spotRatingBar.getRating();
                double ownerRating = ownerRatingBar.getRating();

                Intent intent = new Intent(getApplicationContext(), SeekerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });

    }


}
