package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.Transaction;

public class RateActivity extends AppCompatActivity {
    ImageView spotPhoto;
    TextView address, date, time;
    RatingBar spotRatingBar, ownerRatingBar;
    Button doneButton;
    EditText additionalNotesField;
    Transaction t;
    DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_rate);
        toolbarSetup();
        initializeEdits();
        addListeners();

        //update the components: address, date, time, photo
        t = (Transaction) getIntent().getSerializableExtra("transaction");
        address.setText(t.getAddress());
        date.setText(t.getStartTime() + " - " + t.getEndTime());
        Picasso.with(this)
                .load(t.getPhotoUrl())
                .resize(350, 350)
                .centerCrop()
                .into(spotPhoto);
        Ref = FirebaseDatabase.getInstance().getReference();
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
        spotRatingBar = (RatingBar) findViewById(R.id.ratingBarOwner);
        ownerRatingBar = (RatingBar) findViewById(R.id.ratingBarSpot);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        doneButton = (Button) findViewById(R.id.button);
        additionalNotesField = (EditText) findViewById(R.id.additionalNotesField);
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
