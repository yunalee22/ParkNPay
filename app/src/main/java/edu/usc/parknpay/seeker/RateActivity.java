package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class RateActivity extends TemplateActivity {

    private ImageView parkingSpotImage;
    private TextView address;
    private TextView dateTimeRange;
    private RatingBar spotRatingBar;
    private RatingBar ownerRatingBar;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_rate);

        // Get references to UI views
        parkingSpotImage = (ImageView) findViewById(R.id.parking_spot_image);
        address = (TextView) findViewById(R.id.address);
        dateTimeRange = (TextView) findViewById(R.id.date_time_range);
        spotRatingBar = (RatingBar) findViewById(R.id.spot_rating_bar);
        ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
        submitButton = (Button) findViewById(R.id.submit_button);

        // Add view listeners
        addListeners();
    }

    protected void addListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // Add the ratings to the database
                float spotRating = spotRatingBar.getRating();
                float ownerRating = ownerRatingBar.getRating();

                Intent intent = new Intent(getApplicationContext(), SeekerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });
    }
}
